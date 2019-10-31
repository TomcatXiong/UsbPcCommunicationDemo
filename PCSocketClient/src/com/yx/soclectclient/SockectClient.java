package com.yx.soclectclient;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * SockectClient
 * <p>
 * PC端每隔3s给监听端口发送PC时间戳信息
 *
 * @author yx
 * @date 2019/5/17 13:57
 */
public class SockectClient {
    private Socket mSocket = null;
    private static final int PORT = 12345;

    private boolean adbCmd() {
        String cmd = "adb forward tcp:54321 tcp:12345";
        try {
            Runtime.getRuntime().exec("adb forward tcp:12580 tcp:10086");
        } catch (IOException e) {
            e.printStackTrace();
        }

        Process process = null;
        try {
            process = Runtime.getRuntime().exec(cmd);
            process.waitFor();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            if (process != null) {
                process.destroy();
            }
        }
        return true;
    }

    private void test() {
        try {
            mSocket = new Socket("127.0.0.1", PORT);
            System.out.println("socket:" + mSocket.toString());

            DataInputStream dis = new DataInputStream(mSocket.getInputStream());
            DataOutputStream dos = new DataOutputStream(mSocket.getOutputStream());

            while (true) {
                String data = "sendTime:" + System.currentTimeMillis();
                dos.writeUTF(data);
                dos.flush();

                String s = dis.readUTF();
                System.out.println("receive:" + s);

                Thread.sleep(5000);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SockectClient client = new SockectClient();
        if (client.adbCmd()) {
            client.test();
        }
    }
}
