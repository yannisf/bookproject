package bookproject.scraper.impl;

import bookproject.scraper.api.ErrorCode;
import bookproject.scraper.api.Scraper;
import bookproject.scraper.api.ScraperException;
import bookproject.scraper.api.Tool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Resolves scraping tool implementation from its id.
 *
 * @see Tool
 */
@Component
public class ScraperResolver {

    private static final Logger LOG = LoggerFactory.getLogger(ScraperResolver.class);

    @Autowired
    @ScraperQualifier(Tool.TIDY)
    private Scraper tidy;

    @Autowired
    @ScraperQualifier(Tool.HTML_UNIT)
    private Scraper htmlUnit;

    /**
     * Resolver method.
     *
     * @param scraperId the intended scraper
     * @return the scraper implementation
     * @throws ScraperException when the scraper could not be resolved
     */
    public Scraper resolve(String scraperId) throws ScraperException {
        LOG.debug("Resolving Scraper [{}]", scraperId);
        if (scraperId.equals(Tool.HTML_UNIT.id)) {
            return htmlUnit;
        } else if (scraperId.equals(Tool.TIDY.id)) {
            return tidy;
        } else {
            throw new ScraperException(ErrorCode.INVALID_TOOL);
        }
    }

}
