package bookproject.scrapper.impl;

import bookproject.scrapper.api.BookInfo;
import bookproject.scrapper.api.BookInfoProvider;
import bookproject.scrapper.api.Scraper;
import bookproject.scrapper.api.ScraperException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import java.io.IOException;

/**
 * Scraper implementation using <i>HtmlUnit</i>.
 */
public class HtmlUnitScraper implements Scraper {

    /**
     * {@inheritDoc}
     */
    @Override
    public BookInfo scrape(BookInfoProvider provider, String isbn) throws ScraperException {
        BookInfo bookInfo;

        try {
            WebClient client = getWebClient();
            String link = getBookLink(provider, isbn, client);
            HtmlPage page = client.getPage(link);
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
