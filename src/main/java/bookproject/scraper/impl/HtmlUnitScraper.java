package bookproject.scraper.impl;

import bookproject.scraper.api.BookInfo;
import bookproject.scraper.api.BookInfoProvider;
import bookproject.scraper.api.Scraper;
import bookproject.scraper.api.ScraperException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Scraper implementation using <i>HtmlUnit</i>.
 */
@Component("htmlUnitScraper")
public class HtmlUnitScraper implements Scraper {

    private static final Logger LOG = LoggerFactory.getLogger(HtmlUnitScraper.class);

    /**
     * {@inheritDoc}
     */
    @Override
    public BookInfo scrape(BookInfoProvider provider, String submittedIsbn) throws ScraperException {
        LOG.info("Scraping using HtmlUnit");
        BookInfo bookInfo;

        try {
            WebClient client = getWebClient();
            String link = getBookLink(provider, submittedIsbn, client);
            HtmlPage page = client.getPage(link);
            String isbn = page.getFirstByXPath(provider.getIsbnExpression());

            if (!isbn.equals(submittedIsbn)) {
                throw new ScraperException("Book information could not be extracted reliably.");
            }

            String title = page.getFirstByXPath(provider.getTitleExpression());
            String author = page.getFirstByXPath(provider.getAuthorExpression());
            String publisher = page.getFirstByXPath(provider.getPublisherExpression());
            bookInfo = BookInfo.builder()
                    .isbn(isbn)
                    .title(title)
                    .author(author)
                    .publisher(publisher)
                    .build();
        } catch (IOException e) {
            throw new ScraperException(e);
        }

        return bookInfo;
    }

    private String getBookLink(BookInfoProvider provider, String isbn, WebClient client) throws IOException {
        String spec = String.format(provider.getSearchFormat(), isbn);
        HtmlPage page = client.getPage(spec);
        return page.getFirstByXPath(provider.getBookLinkFromResultExpression());
    }

    private WebClient getWebClient() {
        WebClient client = new WebClient();
        client.getOptions().setCssEnabled(false);
        client.getOptions().setJavaScriptEnabled(false);
        return client;
    }

}
