package bookproject.scraper.impl;

import bookproject.scraper.api.*;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.apache.commons.validator.routines.ISBNValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Scraper implementation using <i>HtmlUnit</i>.
 */
@Component("htmlUnitScraper")
@ScraperQualifier(Tool.HTML_UNIT)
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

            if (provider.usesNoHostLinks()) {
                link = provider.getBaseUrl() + link;
            }

            HtmlPage page = client.getPage(link);
            String isbn = ISBNValidator.getInstance(false).validate(getResult(page, provider.getIsbnExpression()));

            if (!isbn.equals(submittedIsbn)) {
                throw new ScraperException("Book information could not be extracted reliably.");
            }

            String title = getResult(page, provider.getTitleExpression());
            String author = getResult(page, provider.getAuthorExpression());
            String publisher = getResult(page, provider.getPublisherExpression());
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
        return getResult(page, provider.getBookLinkFromResultExpression());
    }

    private WebClient getWebClient() {
        WebClient client = new WebClient();
        client.getOptions().setCssEnabled(false);
        client.getOptions().setJavaScriptEnabled(false);
        return client;
    }

    private String getResult(HtmlPage page, ExtractionExpression expression) {
        String result = page.getFirstByXPath(expression.getXpath());
        if (expression.getJava() != null) {
            result = expression.getJava().apply(result);
        }

        return result.trim();
    }


}
