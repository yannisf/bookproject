package bookproject.scrapper;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.tidy.Tidy;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.IOException;
import java.net.URL;

import static org.assertj.core.api.Assertions.assertThat;

public class ScrapperTest {

    @Test
    public void find() throws IOException, XPathExpressionException, ParserConfigurationException {
        String location = "http://www.biblionet.gr/book/13945/%CE%A3%CE%B1%CE%BC%CE%B1%CF%81%CE%AC%CE%BA%CE%B7%CF%82,_%CE%91%CE%BD%CF%84%CF%8E%CE%BD%CE%B7%CF%82,_1919-2003/%CE%A4%CE%BF_%CE%BB%CE%AC%CE%B8%CE%BF%CF%82";
        String expression = "string(//a[3])";
        String expected = "Υπηρεσίες και Προϊόντα";

        URL url = new URL(location);
        Tidy tidy = createTidy();
        Document document = tidy.parseDOM(url.openStream(), null);
        XPath xPath = XPathFactory.newInstance().newXPath();
        String s = (String) xPath.compile(expression).evaluate(document, XPathConstants.STRING);
        assertThat(s).isEqualTo(expected);
    }

    private Tidy createTidy() {
        Tidy tidy = new Tidy();
        tidy.setXmlOut(true);
        tidy.setForceOutput(true);
        tidy.setDocType("omit");
        tidy.setInputEncoding("UTF-8");
        tidy.setOutputEncoding("UTF-8");
        tidy.setQuiet(true);
        tidy.setShowWarnings(false);
        return tidy;
    }
}
