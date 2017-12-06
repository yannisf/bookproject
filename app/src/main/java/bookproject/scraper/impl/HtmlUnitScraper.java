package bookproject.scraper.impl;

import bookproject.scraper.api.*;
import bookproject.service.IsbnService;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Scraper implementation using <i>HtmlUnit</i>.
 */
@Component("htmlUnitScraper")
@ScraperQualifier(Tool.HTML_UNIT)
public class HtmlUnitScraper implements Scraper {

    private static final Logger LOG = LoggerFactory.getLogger(HtmlUnitScraper.class);

    @Autowired
    private IsbnService isbnService;

    @Autowired
    private ExtractionValidator extractionValidator;

    /**
     * {@inheritDoc}
     */
    @Override
    public BookInformationValue scrape(BookInfoProvider provider, String submittedIsbn) throws ScraperException {
        LOG.info("Scraping using HtmlUnit");
        BookInformationValue bookInformationValue;

        try {
            WebClient client = getWebClient();
            String link = getBookLink(provider, submittedIsbn, client);
            LOG.debug("Fetching [{}]", link);
            HtmlPage page = client.getPage(link);
            String extractedIsbn = isbnService.clean(getResult(page, provider.getIsbnExpression()));
            extractionValidator.validate(submittedIsbn, extractedIsbn);
            String title = getResult(page, provider.getTitleExpression());
            String author = getResult(page, provider.getAuthorExpression());
            String publisher = getResult(page, provider.getPublisherExpression());
            bookInformationValue = BookInformationValue.builder()
                    .isbn(extractedIsbn)
                    .title(title)
                    .author(author)
                    .publisher(publisher)
                    .provider(provider.getName())
                    .sourceUrl(link)
                    .build();
        } catch (IOException e) {
            throw new ScraperException(e);
        }

        return bookInformationValue;
    }

    private String getBookLink(BookInfoProvider provider, String isbn, WebClient client) throws IOException, ScraperException {
        String spec = String.format(provider.getSearchFormat(), isbn);
        HtmlPage page = client.getPage(spec);
        String link = getResult(page, provider.getBookLinkFromResultExpression());

        if (StringUtils.isBlank(link) && isbnService.isIsbn10(isbn)) {
            isbn = isbnService.convertToIsbn13(isbn);
            spec = String.format(provider.getSearchFormat(), isbn);
            page = client.getPage(spec);
            link = getResult(page, provider.getBookLinkFromResultExpression());
        }

        if (StringUtils.isBlank(link)) {
            throw new ScraperException("Book not found");
        }

        if (provider.usesNoHostLinks()) {
            link = provider.getBaseUrl() + link;
        }

        return link;
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
