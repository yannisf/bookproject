package bookproject.scrapper.api;

/**
 * Abstraction of an entity that provides book information.
 */
public interface BookInfoProvider {

    String getBaseUrl();

    String getSearchFormat();

    String getBookLinkFromResultExpression();

    String getTitleExpression();

    String getAuthorExpression();

    String getPublisherExpression();

}
