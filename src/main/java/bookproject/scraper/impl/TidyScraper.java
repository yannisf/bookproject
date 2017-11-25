package bookproject.scraper.impl;

import bookproject.scraper.api.*;
import org.apache.commons.validator.routines.ISBNValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    /**
     * {@inheritDoc}
     */
    @Override
    public BookInfo scrape(BookInfoProvider provider, String submittedIsbn) throws ScraperException {
        LOG.info("Scraping using Tidy");
        BookInfo bookInfo;
        try {
            String link = getBookLink(provider, submittedIsbn);
            if (provider.usesNoHostLinks()) {
                link = provider.getBaseUrl() + link;
            }
            Document document = getBookDocument(link);
            String isbn = ISBNValidator.getInstance().validate(getResult(document, provider.getIsbnExpression()));

            if (!isbn.equals(submittedIsbn)) {
                throw new ScraperException("Book information could not be extracted reliably.");
            }

            String title = getResult(document, provider.getTitleExpression());
            String author = getResult(document, provider.getAuthorExpression());
            String publisher = getResult(document, provider.getPublisherExpression());
            bookInfo = BookInfo.builder()
                    .isbn(submittedIsbn)
                    .title(title)
                    .author(author)
                    .publisher(publisher)
                    .build();
        } catch (IOException | XPathExpressionException e) {
            throw new ScraperException(e);
        }

        return bookInfo;
    }

    private String getBookLink(BookInfoProvider provider, String isbn) throws IOException, XPathExpressionException {
        String spec = String.format(provider.getSearchFormat(), isbn);
        URL searchUrl = new URL(spec);
        Tidy tidy = createTidy();
        Document searchDocument = tidy.parseDOM(searchUrl.openStream(), null);
        return getResult(searchDocument, provider.getBookLinkFromResultExpression());
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
