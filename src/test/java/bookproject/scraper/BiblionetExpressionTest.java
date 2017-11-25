package bookproject.scraper;

import bookproject.scraper.provider.Biblionet;
import org.junit.Test;

public class BiblionetExpressionTest {

    @Test
    public void isbnExtractionTest() {
        String xpathResult = "ISBN 960-03-4268-7, ISBN-13 978-960-03-4268-0, [Κυκλοφορεί]";
        Biblionet biblionet = new Biblionet();
        String s = biblionet.getIsbnExpression().getJava().apply(xpathResult);
        System.out.println(s);
    }
}
