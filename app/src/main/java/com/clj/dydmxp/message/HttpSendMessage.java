package com.clj.dydmxp.message;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.clj.dydmxp.httpsocket.WebSocketMessage;

import org.java_websocket.WebSocket;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.enums.ReadyState;
import org.java_websocket.handshake.ServerHandshake;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;

public class HttpSendMessage {

    static WSWebSocket wsWebSocket;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static void PostHttpSendMessage(String content) {
        wsWebSocket = getInstance();
        if (!wsWebSocket.isOpen()) {
            if (wsWebSocket.getReadyState().equals(ReadyState.NOT_YET_CONNECTED)) {
                try {
                    wsWebSocket.connect();
                } catch (IllegalStateException e) {
                }
            } else if (wsWebSocket.getReadyState().equals(ReadyState.CLOSING) || wsWebSocket.getReadyState().equals(ReadyState.CLOSED)) {
                wsWebSocket.reconnect();
            }
        }
        if (wsWebSocket.getReadyState().equals(ReadyState.OPEN)) {
            wsWebSocket.send(content.trim());
        }
    }


    public static synchronized WSWebSocket getInstance() {
        if (wsWebSocket == null) {
            URI serverURI = URI.create("ws://192.168.2.7:8080/demo1/websocket");
            wsWebSocket = new WSWebSocket(serverURI);
        }
        return wsWebSocket;
    }

    public static class WSWebSocket extends WebSocketClient {

        public WSWebSocket(URI serverUri) {
            super(serverUri, new Draft_6455());
        }

        @Override
        public void onOpen(ServerHandshake handshakedata) {
            Log.e("SendMessage", "onOpen");
        }

        @Override
        public void onMessage(String message) {
            Log.e("SendMessage", "onMessage..." + message);
        }

        @Override
        public void onClose(int code, String reason, boolean remote) {
            Log.e("SendMessage", "onClose..." + code + "..." + reason);
        }

        @Override
        public void onError(Exception ex) {
            Log.e("SendMessage", "onError..." + ex.getMessage());
        }
    }
}
