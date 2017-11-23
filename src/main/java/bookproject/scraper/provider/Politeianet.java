package bookproject.scraper.provider;

import bookproject.scraper.api.BookInfoProvider;

public class Politeianet implements BookInfoProvider {

    private static final String BASE_URL = "https://www.politeianet.gr";
    private static final String SEARCH_FORMAT = "https://www.politeianet.gr/index.php?option=com_virtuemart&Itemid=89&advanced=1&isbn=%s";
    private static final String BOOK_LINK_FROM_RESULT_EXPRESSION = "string(//a[@class=\"browse-product-title\"][1]/@href)";
    private static final String ISBN_EXPRESSION = "string(//div[@class=\"product-type\"]/descendant::*/td[text()=\"ISBN13\"]/following-sibling::td)";
    private static final String TITLE_EXPRESSION = "string(//div[@class=\"details-right-column\"]/h1)";
    private static final String AUTHOR_EXPRESSION = "string(//div[@class=\"details-right-column\"]/b)";
    private static final String PUBLISHER_EXPRESSION =
            "string(//div[@class=\"product-type\"]/descendant::*/td[text()=\"Εκδότης\"]/following-sibling::td)";
    private static final String POLITEIANET_GR = "politeianet.gr";

    @Override
    public String getName() {
        return POLITEIANET_GR;
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
    public String getBookLinkFromResultExpression() {
        return BOOK_LINK_FROM_RESULT_EXPRESSION;
    }

    @Override
    public String getIsbnExpression() {
        return ISBN_EXPRESSION;
    }

    @Override
    public String getTitleExpression() {
        return TITLE_EXPRESSION;
    }

    @Override
    public String getAuthorExpression() {
        return AUTHOR_EXPRESSION;
    }

    @Override
    public String getPublisherExpression() {
        return PUBLISHER_EXPRESSION;
    }

}
