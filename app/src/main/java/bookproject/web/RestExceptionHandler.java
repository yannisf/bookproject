package bookproject.web;

import bookproject.scraper.api.ErrorCode;
import bookproject.scraper.api.ScraperException;
import lombok.Builder;
import lombok.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static org.springframework.http.HttpStatus.*;

/**
 * Handles exceptions in the REST layer, and translates them accordingly.
 */
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(RestExceptionHandler.class);

    /**
     * Exception handler for {@link ScraperException}.
     *
     * @param request   the request object
     * @param throwable the exception, can be cast to {@link ScraperException}
     * @return an HTTP entity with human readable message
     */
    @ExceptionHandler(ScraperException.class)
    ResponseEntity<ApiError> handleControllerException(HttpServletRequest request, Throwable throwable) {
        ScraperException exception = (ScraperException) throwable;

        ApiError apiError;
        ErrorCode errorCode = exception.getErrorCode();
        switch (errorCode) {
            case BOOK_NOT_FOUND:
                apiError = createApiError(NOT_FOUND, "Το βιβλίο δεν βρέθηκε");
                break;
            case BOOK_NOT_EXTRACTED_RELIABLY:
                apiError = createApiError(NOT_FOUND, "Οι πληροφορίες βιβλίου δεν μπόρεσαν να ανακτηθούν αξιόπιστα");
                break;
            case INVALID_TOOL:
                apiError = createApiError(BAD_REQUEST, "Άκυρο εργαλείο ανάκτησης πληροφοριών βιβλίου");
                break;
            case INVALID_PROVIDER:
                apiError = createApiError(BAD_REQUEST, "Άκυρος πάροχος πληροφοριών βιβλίου");
                break;
            case INVALID_ISBN:
                apiError = createApiError(BAD_REQUEST, "Άκυρο ISBN");
                break;
            default:
                apiError = createApiError(INTERNAL_SERVER_ERROR, "Γενικό σφάλμα");
                LOG.info(throwable.getMessage());
        }

        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    private ApiError createApiError(HttpStatus status, String messsage) {
        return ApiError.builder()
                .status(status)
                .message(messsage)
                .build();
    }

    @Value
    @Builder
    public static class ApiError {
        private HttpStatus status;
        private String message;
        private List<String> errors;
    }

}