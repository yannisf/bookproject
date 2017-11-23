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

}
