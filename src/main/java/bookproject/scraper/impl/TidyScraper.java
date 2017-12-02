package bookproject.scraper.impl;

import bookproject.scraper.api.*;
import bookproject.service.IsbnService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.ISBNValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.tidy.Tidy;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

/**
 * Scraper implementation using <i>HtmlTidy (JTidy)</i>.
 */
@Component("tidyScraper")
@ScraperQualifier(Tool.TIDY)
public class TidyScraper implements Scraper {

    private static final Logger LOG = LoggerFactory.getLogger(TidyScraper.class);

    private static final XPathFactory X_PATH_FACTORY = XPathFactory.newInstance();
    private static final String TIDY_PROPERTIES = "/tidy.properties";

    @Autowired
    private IsbnService isbnService;

    /**
     * {@inheritDoc}
     */
    @Override
    public BookInfoValue scrape(BookInfoProvider provider, String submittedIsbn) throws ScraperException {
        LOG.info("Scraping using Tidy");
        BookInfoValue bookInfoValue;
        try {
            String link = getBookLink(provider, submittedIsbn);
            if (provider.usesNoHostLinks()) {
                link = provider.getBaseUrl() + link;
            }
            LOG.debug("Fetching [{}]", link);
            Document document = getBookDocument(link);
            String isbn = ISBNValidator.getInstance(false).validate(getResult(document, provider.getIsbnExpression()));

            if (!isbn.equals(submittedIsbn)) {
                throw new ScraperException("Book information could not be extracted reliably.");
            }

            String title = getResult(document, provider.getTitleExpression());
            String author = getResult(document, provider.getAuthorExpression());
            String publisher = getResult(document, provider.getPublisherExpression());
            bookInfoValue = BookInfoValue.builder()
                    .isbn(submittedIsbn)
                    .title(title)
                    .author(author)
                    .publisher(publisher)
                    .sourceUrl(link)
                    .build();
        } catch (IOException | XPathExpressionException e) {
            throw new ScraperException(e);
        }

        return bookInfoValue;
    }

    private String getBookLink(BookInfoProvider provider, String isbn) throws IOException, XPathExpressionException, ScraperException {
        String spec = String.format(provider.getSearchFormat(), isbn);
        URL searchUrl = new URL(spec);
        Tidy tidy = createTidy();
        Document searchDocument = tidy.parseDOM(searchUrl.openStream(), null);
        String link = getResult(searchDocument, provider.getBookLinkFromResultExpression());

        if (StringUtils.isBlank(link) && isbnService.isIsbn10(isbn)) {
            isbn = isbnService.convertToIsbn13(isbn);
            spec = String.format(provider.getSearchFormat(), isbn);
            searchUrl = new URL(spec);
            searchDocument = tidy.parseDOM(searchUrl.openStream(), null);
            link = getResult(searchDocument, provider.getBookLinkFromResultExpression());
        }

        if (StringUtils.isBlank(link)) {
            throw new ScraperException("Book not found");
        }

        if (provider.usesNoHostLinks()) {
            link = provider.getBaseUrl() + link;
        }

        return link;
    }

    private Document getBookDocument(String link) throws IOException {
        URL url = new URL(link);
        Tidy tidy = createTidy();
        return tidy.parseDOM(url.openStream(), null);
    }

    private Tidy createTidy() throws IOException {
        Tidy tidy = new Tidy();
        tidy.setConfigurationFromProps(getTidyConfigurationProperties());
        return tidy;
    }

    private Properties getTidyConfigurationProperties() throws IOException {
        Properties configurationProperties = new Properties();
        try (InputStream resourceAsStream = TidyScraper.class.getResourceAsStream(TIDY_PROPERTIES)) {
            configurationProperties.load(resourceAsStream);
        }
        return configurationProperties;
    }

    private String getResult(Document document, ExtractionExpression expression) throws XPathExpressionException {
        String result = (String) X_PATH_FACTORY.newXPath().compile(expression.getXpath()).evaluate(document, XPathConstants.STRING);
        if (expression.getJava() != null) {
            result = expression.getJava().apply(result);
        }

        return result;
    }

}
