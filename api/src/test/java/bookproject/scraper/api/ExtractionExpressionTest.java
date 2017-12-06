package bookproject.scraper.api;

import bookproject.scraper.api.ExtractionExpression;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ExtractionExpressionTest {

    @Test
    public void buildTest() {
        ExtractionExpression extractionExpression = ExtractionExpression.builder()
                .xpath("xpath")
                .java(s -> s)
                .build();

        assertThat(extractionExpression.getXpath()).isEqualTo("xpath");
        assertThat(extractionExpression.getJava().apply("s")).isEqualTo("s");
    }

}