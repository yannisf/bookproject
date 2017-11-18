package bookproject.scrapper.impl;

import bookproject.scrapper.api.BookInfo;
import bookproject.scrapper.api.BookInfoProvider;
import bookproject.scrapper.api.Scraper;
import bookproject.scrapper.api.ScraperException;
import org.w3c.dom.Document;
import org.w3c.tidy.Tidy;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;

/**
 * Scraper implementation using <i>HtmlTidy (JTidy)</i>.
 */
public class TidyScraper implements Scraper {

    private static final XPath X_PATH = XPathFactory.newInstance().newXPath();
    private static final String TIDY_PROPERTIES = "/tidy.properties";

    /**
     * {@inheritDoc}
     */
    @Override
    public BookInfo scrape(BookInfoProvider provider, String isbn) throws ScraperException {
        BookInfo bookInfo;
        try {
            String link = getBookLink(provider, isbn);
            Document document = getBookDocument(link);
            String title = getResult(document, provider.getTitleExpression());
            String author = getResult(document, provider.getAuthorExpression());
            String publisher = getResult(document, provider.getPublisherExpression());
            bookInfo = BookInfo.builder()
                    .isbn(isbn)
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
        configurationProperties.load(TidyScraper.class.getResourceAsStream(TIDY_PROPERTIES));
        return configurationProperties;
    }

    private String getResult(Document document, String expression) throws XPathExpressionException {
        return (String) X_PATH.compile(expression).evaluate(document, XPathConstants.STRING);
    }

}
