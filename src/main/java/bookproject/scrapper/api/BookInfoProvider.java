package bookproject.scrapper.api;

/**
 * Abstraction of an entity that provides book information.
 */
public interface BookInfoProvider {

    String getName();

    String getBaseUrl();

    String getSearchFormat();

    String getBookLinkFromResultExpression();

    String getIsbnExpression();

    String getTitleExpression();

    String getAuthorExpression();

    String getPublisherExpression();

}
