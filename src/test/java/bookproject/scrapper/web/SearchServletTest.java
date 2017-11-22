package bookproject.scrapper.web;

import bookproject.scrapper.api.BookInfo;
import bookproject.scrapper.api.BookInfoProvider;
import bookproject.scrapper.api.Scraper;
import bookproject.scrapper.api.ScraperException;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class SearchServletTest {

    @Test
    public void testInvalidIsbn() throws ServletException, IOException {
        SearchServlet searchServlet = new SearchServlet();
        ArgumentCaptor<Integer> status = ArgumentCaptor.forClass(Integer.class);
        HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
        HttpServletResponse httpServletResponse = mock(HttpServletResponse.class);
        when(httpServletRequest.getParameter(eq("isbn"))).thenReturn("X");
        searchServlet.doGet(httpServletRequest, httpServletResponse);
        verify(httpServletResponse, never()).getWriter();
        verify(httpServletResponse).setStatus(status.capture());
        assertThat(status.getValue(), equalTo(HttpServletResponse.SC_INTERNAL_SERVER_ERROR));
    }

    @Test
    public void testScraping() throws ScraperException, ServletException, IOException {
        String isbn = "9789600316698";

        SearchServlet searchServlet = new SearchServlet();

        HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
        HttpServletResponse httpServletResponse = mock(HttpServletResponse.class);
        when(httpServletRequest.getParameter(eq("isbn"))).thenReturn(isbn);

        StringWriter writer = new StringWriter();
        when(httpServletResponse.getWriter()).thenReturn(new PrintWriter(writer));

        Scraper scraper = mock(Scraper.class);
        BookInfo bookInfo = BookInfo.builder().isbn("i").author("a").title("t").publisher("p").build();
        when(scraper.scrape(any(BookInfoProvider.class), eq(isbn))).thenReturn(bookInfo);

        searchServlet.setScraper(scraper);
        searchServlet.doGet(httpServletRequest, httpServletResponse);
        verify(httpServletResponse, times(1)).setContentType(eq("text/html; charset=UTF-8"));
        assertThat(StringUtils.chomp(writer.toString()), equalTo("i: t, a, p"));

    }

}
