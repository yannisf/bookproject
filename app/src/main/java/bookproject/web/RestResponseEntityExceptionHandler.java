package bookproject.web;

import bookproject.scraper.api.InvalidIsbnException;
import bookproject.scraper.api.ScraperException;
import lombok.Builder;
import lombok.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(InvalidIsbnException.class)
    ResponseEntity<ApiError> handleControllerException(HttpServletRequest request, Throwable ex) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return new ResponseEntity<>(
                ApiError.builder()
                        .message("Άκυρο ISBN")
                        .status(status)
                        .build(),
                status);
    }

    @ExceptionHandler(ScraperException.class)
    ResponseEntity<ApiError> handleScraperException(HttpServletRequest request, Throwable ex) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        return new ResponseEntity<>(
                ApiError.builder()
                        .message("Το βιβλίο δεν βρέθηκε!")
                        .status(status)
                        .build(),
                status);
    }

    @Value
    @Builder
    public static class ApiError {
        private HttpStatus status;
        private String message;
        private List<String> errors;
    }

}