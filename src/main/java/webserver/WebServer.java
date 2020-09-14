package webserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.handler.DispenseHandler;
import webserver.handler.RequestHandler;

public class WebServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebServer.class);
    private static final int DEFAULT_PORT = 8080;
    private static DispenseHandler DISPENSE_HANDLER;

    public static void main(String[] args) throws Exception {
        int port = calculatePort(args);

        // 핸들러를 생성한다.
        LOGGER.info("Create Controller Handler");
        DISPENSE_HANDLER = new DispenseHandler();

        // 서버소켓을 생성한다.
        try (ServerSocket listenSocket = new ServerSocket(port)) {
            LOGGER.info("Web Application Server started {} port.", port);
            startConnectionThread(listenSocket);
        }
    }

    private static int calculatePort(String[] args) {
        // 웹서버는 기본적으로 8080번 포트를 사용한다.
        if (args == null || args.length == 0) {
            return DEFAULT_PORT;
        }

        // 포트를 입력받는 경우 입력받은 포트를 사용한다.
        return Integer.parseInt(args[0]);
    }

    private static void startConnectionThread(ServerSocket listenSocket)
        throws IOException, NoSuchFieldException, IllegalAccessException {
        // 클라이언트가 연결될때까지 대기한다.
        Socket connection;
        while ((connection = listenSocket.accept()) != null) {
            Thread thread = new Thread(new RequestHandler(connection, DISPENSE_HANDLER));
            thread.start();
        }
    }
}
