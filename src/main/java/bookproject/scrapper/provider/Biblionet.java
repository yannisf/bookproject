package bookproject.scrapper.provider;

import bookproject.scrapper.api.BookInfoProvider;

public class Biblionet implements BookInfoProvider {

    private static final String BASE_URL = "http://www.biblionet.gr";
    private static final String SEARCH_FORMAT = BASE_URL + "/main.asp?page=results&isbn=%s";
    private static final String BOOK_LINK_FROM_RESULT_EXPRESSION = "string(//a[@class=\"booklink\"][1]/@href)";
    private static final String TITLE_EXPRESSION = "string(//h1[@class=\"book_title\"])";
    private static final String AUTHOR_EXPRESSION = "string(//a[@class=\"booklink\" and starts-with(@href,\"/author/\")][1])";
    private static final String PUBLISHER_EXPRESSION = "string(//a[@class=\"booklink\" and starts-with(@href,\"/com/\")][1])";
    private static final String BIBLIONET_GR = "biblionet.gr";

    @Override
    public String getName() {
        return BIBLIONET_GR;
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
        return BASE_URL + BOOK_LINK_FROM_RESULT_EXPRESSION;
    }

    @Override
    public String getIsbnExpression() {
        throw new UnsupportedOperationException("Not yet supported");
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
