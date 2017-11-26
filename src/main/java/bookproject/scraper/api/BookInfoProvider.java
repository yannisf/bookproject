package bookproject.scraper.api;

/**
 * Abstraction of an entity that provides book information.
 */
public interface BookInfoProvider {

    /**
     * The provider name. Typically used as identifier.
     *
     * @return the name
     */
    String getName();

    /**
     * The provider base url.
     *
     * @return the url
     */
    String getBaseUrl();

    /**
     * The string format that searches against the provider, using the isbn.
     *
     * @return the search format
     */
    String getSearchFormat();

    /**
     * Expression to extract the book links from the search result page.
     *
     * @return the expression
     */
    ExtractionExpression getBookLinkFromResultExpression();

    /**
     * Expression to extract ISBN information.
     *
     * @return the expression
     */

    ExtractionExpression getIsbnExpression();

    /**
     * Expression to extract title information.
     *
     * @return the expression
     */

    ExtractionExpression getTitleExpression();

    /**
     * Expression to extract author information.
     *
     * @return the expression
     */
    ExtractionExpression getAuthorExpression();

    /**
     * Expression to extract publisher information.
     *
     * @return the expression
     */
    ExtractionExpression getPublisherExpression();

    /**
     * Used to distinguish between providers that use fully qualified URLs and those that do not.
     *
     * @return true when extracted links (href) do not include the protocol and host
     */
    boolean usesNoHostLinks();
}
