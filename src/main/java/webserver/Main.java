package webserver;

import static spark.Spark.*;

public class Main {
    public static void main(String[] args) {

        webSocket("/echo", EchoWebSocket.class);
        port(8080);
        init();
    }
}