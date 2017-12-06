package bookproject.scraper.api;

/**
 * Scraping tools.
 */
public enum Tool {


    TIDY("tidy"),
    HTML_UNIT("htmlunit");

    /**
     * The scraping tool id.
     */
    public final String id;

    Tool(String id) {
        this.id = id;
    }

}
