package bookproject.scraper.api;

import lombok.Builder;
import lombok.Data;

import java.util.function.Function;

/**
 * Abstraction of expression (XPath and Java) that extract a specific piece of information. The concept is that
 * first using XPath, a string is retrieved. If this string needs further processing to extract the required information
 * Java code can be applied.
 */
@Data
@Builder
public class ExtractionExpression {

    private String xpath;
    private Function<String, String> java;

}
