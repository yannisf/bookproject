package bookproject.scraper.provider;

import bookproject.scraper.api.BookInformationProvider;
import bookproject.scraper.api.ExtractionExpression;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Book information provider implementation for <i>biblionet</i>.
 */
@Component("biblionet")
public class Biblionet implements BookInformationProvider {

    private static final String NAME = "biblionet";

    private static final String BASE_URL = "http://www.biblionet.gr";

    private static final String SEARCH_FORMAT = BASE_URL + "/main.asp?page=results&isbn=%s";

    private static final Pattern ISBN_EXTRACTION_PATTERN = Pattern.compile(".*?(?:ISBN|ISBN-13)\\s+([0-9-]*).*");

    private static final ExtractionExpression ISBN_EXPRESSION = ExtractionExpression.builder()
            .xpath("string(//span[@class=\"book_details\"]/text()[3])")
            .java(s -> {
                Matcher matcher = ISBN_EXTRACTION_PATTERN.matcher(s);
                if (matcher.matches()) {
                    return matcher.group(1);
                } else {
                    return "N/A";
                }
            })
            .build();

    private static final ExtractionExpression BOOK_LINK_FROM_RESULT_EXPRESSION = ExtractionExpression.builder()
            .xpath("string(//a[@class=\"booklink\"][1]/@href)")
            .build();

    private static final ExtractionExpression TITLE_EXPRESSION = ExtractionExpression.builder()
            .xpath("string(//h1[@class=\"book_title\"])")
            .build();

    private static final ExtractionExpression AUTHOR_EXPRESSION = ExtractionExpression.builder()
            .xpath("string(//a[@class=\"booklink\" and starts-with(@href,\"/author/\")][1])")
            .build();

    private static final ExtractionExpression PUBLISHER_EXPRESSION = ExtractionExpression.builder()
            .xpath("string(//a[@class=\"booklink\" and starts-with(@href,\"/com/\")][1])")
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
        return true;
    }
}
