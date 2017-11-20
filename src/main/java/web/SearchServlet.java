package web;

import bookproject.scrapper.api.BookInfo;
import bookproject.scrapper.api.Scraper;
import bookproject.scrapper.api.ScraperException;
import bookproject.scrapper.impl.TidyScraper;
import bookproject.scrapper.provider.Politeianet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "search", urlPatterns = "/search")
public class SearchServlet extends HttpServlet {

    private static final Scraper scraper = new TidyScraper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String isbn = req.getParameter("isbn");
            BookInfo bookInfo = scraper.scrape(new Politeianet(), isbn);
            resp.getWriter().println(bookInfo);
        } catch (ScraperException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
