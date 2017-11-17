package bookproject.scrapper.impl;

import bookproject.scrapper.api.BookInfo;
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
    public BookInfo scrape(String isbn) throws ScraperException {
        BookInfo bookInfo;

        try {
            WebClient client = getWebClient();
            String link = getBookLink(isbn, client);
            HtmlPage page = getBookHtmlPage(client, link);
            String title = page.getFirstByXPath(TITLE_EXPRESSION);
            String author = page.getFirstByXPath(AUTHOR_EXPRESSION);
            String publisher = page.getFirstByXPath(PUBLISHER_EXPRESSION);
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

    private String getBookLink(String isbn, WebClient client) throws IOException {
        String spec = String.format(SEARCH_FORMAT, isbn);
        HtmlPage page = client.getPage(spec);
        return page.getFirstByXPath(BOOK_LINK_FROM_RESULT_EXPRESSION);
    }

    private HtmlPage getBookHtmlPage(WebClient client, String link) throws IOException {
        String spec = BASE_URL + link;
        return client.getPage(spec);
    }

    private WebClient getWebClient() {
        WebClient client = new WebClient();
        client.getOptions().setCssEnabled(false);
        client.getOptions().setJavaScriptEnabled(false);
        return client;
    }

}
