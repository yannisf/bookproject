package bookproject.scrapper;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.apache.commons.lang3.time.StopWatch;
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
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class ScrapperTest {

    private static final XPath X_PATH = XPathFactory.newInstance().newXPath();
    private static final String BOOK_LINK_FROM_RESULT_EXPRESSION = "string(//a[@class=\"booklink\"][1]/@href)";
    private static final String TITLE_EXPRESSION = "string(//h1[@class=\"book_title\"])";
    private static final String AUTHOR_EXPRESSION = "string(//a[@class=\"booklink\" and starts-with(@href,\"/author/\")][1])";
    private static final String PUBLISHER_EXPRESSION = "string(//a[@class=\"booklink\" and starts-with(@href,\"/com/\")][1])";
    private static final String BASE_URL = "http://www.biblionet.gr";
    private static final String SEARCH_FORMAT = BASE_URL + "/main.asp?page=results&isbn=%s";

    @Test
    public void tidyFindTest() throws IOException, XPathExpressionException, ParserConfigurationException {
//        String isbn = "9603291528";
//        String isbn = "9789600316698";
//        String isbn = "9789608965409";
        String isbn = "9789600403886";

        StopWatch stopWatch = StopWatch.createStarted();
        String spec1 = String.format(SEARCH_FORMAT, isbn);
        URL searchUrl = new URL(spec1);

        Tidy tidy = createTidy();
        Document searchDocument = tidy.parseDOM(searchUrl.openStream(), null);
        String link = getResult(searchDocument, BOOK_LINK_FROM_RESULT_EXPRESSION);

        String spec2 = BASE_URL + link;
        URL url = new URL(spec2);

        Document document = tidy.parseDOM(url.openStream(), null);
        String title = getResult(document, TITLE_EXPRESSION);
        String author = getResult(document, AUTHOR_EXPRESSION);
        String publisher = getResult(document, PUBLISHER_EXPRESSION);
        System.out.println("XPATH finished in " + stopWatch.getTime(TimeUnit.MILLISECONDS));

        System.out.println(String.format("spec1: %s", spec1));
        System.out.println(String.format("spec2: %s", spec2));
        System.out.println(String.format("%s: %s, %s, %s", isbn, title, author, publisher));

        //        String expectedTitle = "Το λάθος";
//        String expectedAuthor = "Αντώνης Σαμαράκης";
//        String expectedPublisher = "Εκδόσεις Καστανιώτη";
//        assertThat(title).isEqualTo(expectedTitle);
//        assertThat(author).isEqualTo(expectedAuthor);
//        assertThat(publisher).isEqualTo(expectedPublisher);
    }

    private Tidy createTidy() throws IOException {
        Tidy tidy = new Tidy();
        tidy.setConfigurationFromProps(getTidyConfigurationProperties());
        return tidy;
    }

    private Properties getTidyConfigurationProperties() throws IOException {
        Properties configurationProperties = new Properties();
        configurationProperties.load(ScrapperTest.class.getResourceAsStream("/tidy.properties"));
        return configurationProperties;
    }

    private String getResult(Document document, String expression) throws XPathExpressionException {
        return (String) X_PATH.compile(expression).evaluate(document, XPathConstants.STRING);
    }

    @Test
    public void htmlUnitFindTest() throws IOException {
        String isbn = "9789600403886";

        StopWatch stopWatch = StopWatch.createStarted();
        WebClient client = new WebClient();
        client.getOptions().setCssEnabled(false);
        client.getOptions().setJavaScriptEnabled(false);

        String spec1 = String.format(SEARCH_FORMAT, isbn);
        HtmlPage page1 = client.getPage(spec1);
        String link = page1.getFirstByXPath(BOOK_LINK_FROM_RESULT_EXPRESSION);

        String spec2 = BASE_URL + link;
        HtmlPage page2 = client.getPage(spec2);
        String title = page2.getFirstByXPath(TITLE_EXPRESSION);
        String author = page2.getFirstByXPath(AUTHOR_EXPRESSION);
        String publisher = page2.getFirstByXPath(PUBLISHER_EXPRESSION);
        System.out.println("XPATH finished in " + stopWatch.getTime(TimeUnit.MILLISECONDS));

        System.out.println(String.format("spec1: %s", spec1));
        System.out.println(String.format("spec2: %s", spec2));
        System.out.println(String.format("%s: %s, %s, %s", isbn, title, author, publisher));
    }

}
