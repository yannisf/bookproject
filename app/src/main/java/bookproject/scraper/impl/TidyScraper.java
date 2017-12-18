package bookproject.scraper.impl;

import bookproject.scraper.api.*;
import bookproject.service.IsbnService;
import org.apache.commons.lang3.StringUtils;
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

    @Autowired
    private ExtractionValidator extractionValidator;

    /**
     * {@inheritDoc}
     */
    @Override
    public BookInformationValue scrape(BookInformationProvider provider, String submittedIsbn) throws ScraperException {
        LOG.info("Scraping using Tidy");
        BookInformationValue bookInformationValue;
        try {
            String link = getBookLink(provider, submittedIsbn);
            LOG.debug("Fetching [{}]", link);
            Document document = getBookDocument(link);
            String extractedIsbn = isbnService.clean(getResult(document, provider.getIsbnExpression()));
            extractionValidator.validate(submittedIsbn, extractedIsbn);
            String title = getResult(document, provider.getTitleExpression());
            String author = getResult(document, provider.getAuthorExpression());
            String publisher = getResult(document, provider.getPublisherExpression());
            bookInformationValue = BookInformationValue.builder()
                    .isbn(submittedIsbn)
                    .title(title)
                    .author(author)
                    .publisher(publisher)
                    .provider(provider.getName())
                    .sourceUrl(link)
                    .build();
        } catch (IOException | XPathExpressionException e) {
            throw new ScraperException(e);
        }

        return bookInformationValue;
    }


    String getBookLink(BookInformationProvider provider, String isbn) throws IOException, XPathExpressionException, ScraperException {
        String spec = String.format(provider.getSearchFormat(), isbn);
        LOG.debug("Searching with ISBN [{}]: [{}]", isbn, spec);
        URL searchUrl = new URL(spec);
        Tidy tidy = createTidy();
        Document searchDocument = tidy.parseDOM(searchUrl.openStream(), null);
        String link = getResult(searchDocument, provider.getBookLinkFromResultExpression());

        if (StringUtils.isBlank(link) && isbnService.isIsbn10(isbn)) {
            isbn = isbnService.convertToIsbn13(isbn);
            spec = String.format(provider.getSearchFormat(), isbn);
            LOG.debug("2nd try with ISBN13 [{}]: [{}]", isbn, spec);
            searchUrl = new URL(spec);
            searchDocument = tidy.parseDOM(searchUrl.openStream(), null);
            link = getResult(searchDocument, provider.getBookLinkFromResultExpression());
        }

        if (StringUtils.isBlank(link)) {
            throw new ScraperException(ErrorCode.BOOK_NOT_FOUND);
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
