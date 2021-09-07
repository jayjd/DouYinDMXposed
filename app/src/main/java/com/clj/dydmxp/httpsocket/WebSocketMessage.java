package com.clj.dydmxp.httpsocket;

import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import androidx.annotation.RequiresApi;

import org.java_websocket.WebSocket;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URI;
import java.nio.charset.StandardCharsets;

public class WebSocketMessage extends Thread {
    public Handler handler;

    public WebSocketMessage(Handler handler) {
        this.handler = handler;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void run() {
        super.run();
        try {
            ServerSocket serverSocket = new ServerSocket(8888);
            String line = null;
            while (true) {
//				Thread.sleep(500);
                Socket accept = serverSocket.accept();
                BufferedReader bff = new BufferedReader(new InputStreamReader(accept.getInputStream()));
                while ((line = bff.readLine()) != null) {
                    try {
                        String s = new String(line.getBytes(), StandardCharsets.UTF_8);
                        System.out.println(s);
                        JSONObject jsonObject = new JSONObject(s);
                        if (jsonObject.getString("method").equals("WebcastChatMessage")) {
                            Message message = handler.obtainMessage();
                            message.obj = s;
                            handler.sendMessage(message);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.i("TAG", "执行了" + e.getMessage());
        }
    }
}
