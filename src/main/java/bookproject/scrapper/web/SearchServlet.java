package bookproject.scrapper.web;

import bookproject.scrapper.api.BookInfo;
import bookproject.scrapper.api.Scraper;
import bookproject.scrapper.api.ScraperException;
import bookproject.scrapper.impl.TidyScraper;
import bookproject.scrapper.provider.Politeianet;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.ISBNValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "search", urlPatterns = "/search")
public class SearchServlet extends HttpServlet {

    private static final long serialVersionUID = -6938592751343956358L;
    private static final Logger LOG = LoggerFactory.getLogger(SearchServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String isbn = StringUtils.EMPTY;
        try {
            LOG.debug("Received request");
            resp.setContentType("text/html; charset=UTF-8");
            isbn = req.getParameter("isbn");
            String validIsbn = ISBNValidator.getInstance().validate(isbn);

            if (validIsbn != null) {
                LOG.info("Scrapping for ISBN [{}]", validIsbn);
                Politeianet provider = new Politeianet();
                LOG.debug("Using provider [{}]", provider.getName());
                BookInfo bookInfo = new TidyScraper().scrape(provider, isbn);
                resp.getWriter().println(String.format("%s: %s, %s, %s", bookInfo.getIsbn(), bookInfo.getTitle(), bookInfo.getAuthor(), bookInfo.getPublisher()));
            } else {
                throw new ScraperException(String.format("Received invalid ISBN [%s]", isbn));
            }

        } catch (ScraperException e) {
            LOG.warn(String.format("Scrapping error while looking for [%s]", isbn), e);
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } catch (IOException e) {
            LOG.error("Generic error: ", e);
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

}
