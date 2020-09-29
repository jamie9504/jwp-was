package jwp.was.webapplicationserver.controller;

import jwp.was.webserver.HttpStatusCode;
import jwp.was.webserver.dto.HttpRequest;
import jwp.was.webserver.dto.HttpResponse;

public class GlobalExceptionHandler {

    public HttpResponse handleCauseException(HttpRequest httpRequest, Exception exception) {
        if (exception.getCause() instanceof IllegalArgumentException
            || exception.getCause() instanceof NullPointerException) {
            return handleHttpStatusCode(httpRequest, HttpStatusCode.BAD_REQUEST);
        }
        return handleHttpStatusCode(httpRequest, HttpStatusCode.INTERNAL_SERVER_ERROR);
    }

    public HttpResponse handleHttpStatusCode(HttpRequest httpRequest,
        HttpStatusCode httpStatusCode) {
        return HttpResponse.of(
            httpRequest.getProtocol(),
            httpStatusCode,
            httpStatusCode.getMessage()
        );
    }
}
