package jwp.was.webapplicationserver.configure.controller;

import static jwp.was.util.Constants.CONTENT_TYPE_TEXT_PLAIN;
import static jwp.was.util.Constants.HEADERS_EMPTY;
import static jwp.was.util.Constants.PARAMETERS_EMPTY;
import static jwp.was.util.Constants.PARAMETERS_FOR_CREATE_USER;
import static jwp.was.util.Constants.PROTOCOL;
import static jwp.was.util.Constants.URL_PATH_API_CREATE_USER;
import static jwp.was.util.Constants.URL_PATH_NOT_EXISTS_FILE;
import static jwp.was.webserver.FileNameExtension.API;
import static jwp.was.webserver.HttpMethod.CONNECT;
import static jwp.was.webserver.HttpMethod.POST;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import jwp.was.webapplicationserver.db.DataBaseTest;
import jwp.was.webserver.HttpStatusCode;
import jwp.was.webserver.dto.HttpRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ControllerHandlerTest {

    private final ControllerHandler controllerHandler = new ControllerHandler();

    @AfterEach
    void tearDown() {
        DataBaseTest.clear();
    }

    @DisplayName("유저 생성 성공, 302 반환")
    @Test
    void handleAPI_UserCreate_Return302() throws IOException {
        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            HttpRequest httpRequest = new HttpRequest(
                POST,
                URL_PATH_API_CREATE_USER,
                PARAMETERS_FOR_CREATE_USER,
                PROTOCOL,
                HEADERS_EMPTY,
                API);
            controllerHandler.handleAPI(os, httpRequest);

            assertThat(os.toString()).contains(HttpStatusCode.FOUND.getCodeAndMessage());
            assertThat(os.toString()).contains(CONTENT_TYPE_TEXT_PLAIN);
        }
    }

    @DisplayName("지원하지 않는 메서드, 405 반환")
    @Test
    void handleAPI_NotAllowMethod_Return405() throws IOException {
        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            HttpRequest httpRequest = new HttpRequest(
                CONNECT,
                URL_PATH_API_CREATE_USER,
                PARAMETERS_FOR_CREATE_USER,
                PROTOCOL,
                HEADERS_EMPTY,
                API);
            controllerHandler.handleAPI(os, httpRequest);

            assertThat(os.toString()).contains(HttpStatusCode.METHOD_NOT_ALLOW.getCodeAndMessage());
            assertThat(os.toString()).contains(CONTENT_TYPE_TEXT_PLAIN);
        }
    }

    @DisplayName("없는 API, 404 반환")
    @Test
    void handleAPI_NotExistsAPI_Return404() throws IOException {
        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            HttpRequest httpRequest = new HttpRequest(
                POST,
                URL_PATH_NOT_EXISTS_FILE,
                PARAMETERS_FOR_CREATE_USER,
                PROTOCOL,
                HEADERS_EMPTY,
                API);
            controllerHandler.handleAPI(os, httpRequest);

            assertThat(os.toString()).contains(HttpStatusCode.NOT_FOUND.getCodeAndMessage());
            assertThat(os.toString()).contains(CONTENT_TYPE_TEXT_PLAIN);
        }
    }

    @DisplayName("잘못된 parameter, 400 반환")
    @Test
    void handleAPI_WrongParameters_Return400() throws IOException {
        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            HttpRequest httpRequest = new HttpRequest(
                POST,
                URL_PATH_API_CREATE_USER,
                PARAMETERS_EMPTY,
                PROTOCOL,
                HEADERS_EMPTY,
                API);
            controllerHandler.handleAPI(os, httpRequest);

            assertThat(os.toString()).contains(HttpStatusCode.BAD_REQUEST.getCodeAndMessage());
            assertThat(os.toString()).contains(CONTENT_TYPE_TEXT_PLAIN);
        }
    }
}
