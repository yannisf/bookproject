package bookproject.scraper.provider;

import org.junit.Test;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class BiblionetExpressionTest {

    @Test
    public void isbnExtractionTest() {
        String xpathResult = "ISBN 960-03-4268-7, ISBN-13 978-960-03-4268-0, [Κυκλοφορεί]";
        Biblionet biblionet = new Biblionet();
        String s = biblionet.getIsbnExpression().getJava().apply(xpathResult);
        assertThat(s).isEqualTo("960-03-4268-7");
    }
}
