package controller.handler;

import static webserver.HttpStatusCode.METHOD_NOT_ALLOW;
import static webserver.HttpStatusCode.NOT_FOUND;

import controller.GlobalExceptionHandler;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.dto.HttpRequest;
import webserver.dto.HttpResponse;
import webserver.utils.ResponseUtils;

public class ControllerHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ControllerHandler.class);

    private final ControllerMethodMappers controllerMethodMappers;
    private final GlobalExceptionHandler globalExceptionHandler;

    public ControllerHandler() {
        this.controllerMethodMappers = new ControllerMethodMappers();
        this.globalExceptionHandler = new GlobalExceptionHandler();
    }

    public void handleAPI(OutputStream out, HttpRequest httpRequest) throws IOException {
        MatchInfo matchInfo = controllerMethodMappers.getMatchInstanceMethod(httpRequest);

        try (DataOutputStream dos = new DataOutputStream(out)) {
            HttpResponse httpResponse = makeHttpResponse(httpRequest, matchInfo);
            response(dos, httpResponse);
        }
    }

    private HttpResponse makeHttpResponse(HttpRequest httpRequest, MatchInfo matchInfo) {
        if (matchInfo.isMatch()) {
            return executeMatchedMethod(httpRequest, matchInfo);
        }

        if (matchInfo.isNotMatch() && matchInfo.anyMatchUrlPath()) {
            return globalExceptionHandler.handleHttpStatusCode(httpRequest, METHOD_NOT_ALLOW);
        }

        return globalExceptionHandler.handleHttpStatusCode(httpRequest, NOT_FOUND);
    }

    private HttpResponse executeMatchedMethod(HttpRequest httpRequest, MatchInfo matchInfo) {
        try {
            return matchInfo.executeMethod(httpRequest);
        } catch (IllegalAccessException | InvocationTargetException e) {
            return globalExceptionHandler.handleCauseException(httpRequest, e);
        }
    }

    private void response(DataOutputStream dos, HttpResponse httpResponse) throws IOException {
        ResponseUtils.response(dos, httpResponse);
        LOGGER.info("httpResponse: {}", httpResponse);
    }
}
