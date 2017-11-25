package bookproject.scraper.provider;

import bookproject.scraper.api.BookInfoProvider;
import bookproject.scraper.api.ExtractionExpression;
import org.springframework.stereotype.Component;

/**
 * Book information provider implementation for <i>politeianet</i>.
 */
@Component("politeianet")
public class Politeianet implements BookInfoProvider {

    private static final String NAME = "politeianet";

    private static final String BASE_URL = "https://www.politeianet.gr";

    private static final String SEARCH_FORMAT =
            "https://www.politeianet.gr/index.php?option=com_virtuemart&Itemid=89&advanced=1&isbn=%s";

    private static final ExtractionExpression BOOK_LINK_FROM_RESULT_EXPRESSION = ExtractionExpression.builder()
            .xpath("string(//a[@class=\"browse-product-title\"][1]/@href)")
            .build();

    private static final ExtractionExpression ISBN_EXPRESSION = ExtractionExpression.builder()
            .xpath("string(//div[@class=\"product-type\"]/descendant::*/td[text()=\"ISBN13\"]/following-sibling::td)")
            .build();

    private static final ExtractionExpression TITLE_EXPRESSION = ExtractionExpression.builder()
            .xpath("string(//div[@class=\"details-right-column\"]/h1)")
            .build();

    private static final ExtractionExpression AUTHOR_EXPRESSION = ExtractionExpression.builder()
            .xpath("string(//div[@class=\"details-right-column\"]/b)")
            .build();

    private static final ExtractionExpression PUBLISHER_EXPRESSION = ExtractionExpression.builder()
            .xpath("string(//div[@class=\"product-type\"]/descendant::*/td[text()=\"Εκδότης\"]/following-sibling::td)")
            .build();

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getBaseUrl() {
        return BASE_URL;
    }

    @Override
    public String getSearchFormat() {
        return SEARCH_FORMAT;
    }

    @Override
    public ExtractionExpression getBookLinkFromResultExpression() {
        return BOOK_LINK_FROM_RESULT_EXPRESSION;
    }

    @Override
    public ExtractionExpression getIsbnExpression() {
        return ISBN_EXPRESSION;
    }

    @Override
    public ExtractionExpression getTitleExpression() {
        return TITLE_EXPRESSION;
    }

    @Override
    public ExtractionExpression getAuthorExpression() {
        return AUTHOR_EXPRESSION;
    }

    @Override
    public ExtractionExpression getPublisherExpression() {
        return PUBLISHER_EXPRESSION;
    }

    @Override
    public boolean usesNoHostLinks() {
        return false;
    }

}
