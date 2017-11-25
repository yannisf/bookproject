package bookproject.scraper.impl;

import bookproject.scraper.api.Scraper;
import bookproject.scraper.api.ScraperQualifier;
import bookproject.scraper.api.Tool;
import bookproject.scraper.api.UnknownScraperException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Resolves scraping tool implementation from its id.
 *
 * @see Tool
 */
@Component
public class ScraperResolver {

    @Autowired
    @ScraperQualifier(Tool.TIDY)
    private Scraper tidy;

    @Autowired
    @ScraperQualifier(Tool.HTML_UNIT)
    private Scraper htmlUnit;

    public Scraper resolve(String scraperId) throws UnknownScraperException {
        if (scraperId.equals(Tool.HTML_UNIT.id)) {
            return htmlUnit;
        } else if (scraperId.equals(Tool.TIDY.id)) {
            return tidy;
        } else {
            throw new UnknownScraperException(String.format("Scraper with id [%s] could not be found", scraperId));
        }
    }

}
