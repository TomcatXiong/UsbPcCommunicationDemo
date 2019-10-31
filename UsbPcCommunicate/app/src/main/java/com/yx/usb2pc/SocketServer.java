package com.yx.usb2pc;

import android.util.Log;

import com.yx.usb2pc.utils.ThreadPoolManager;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * SocketServer
 * 服务端接收socket信息，回复设备端的时间戳信息
 *
 * @author yx
 * @date 2019/10/30 20:39
 */
public class SocketServer {
    private static final String TAG = "SocketServer";
    private static final int PORT = 12345;
    private ServerSocket mServerSocket = null;
    private boolean mRunning = false;

    private static SocketServer sSocketServer;

    public static SocketServer getSocketServerInstant() {
        if (sSocketServer == null) {
            sSocketServer = new SocketServer();
        }
        return sSocketServer;
    }

    private SocketServer() {
    }

    /**
     * 启动服务
     */
    public void startServer() {
        stopServer();
        ThreadPoolManager.getInstance().startTaskThread(new ServerThread(), "server-thread");
    }

    /**
     * 关闭服务
     */
    public void stopServer() {
        if (mServerSocket != null) {
            mRunning = false;
            try {
                mServerSocket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            mServerSocket = null;
        }
    }

    class ServerThread extends Thread {

        @Override
        public void run() {
            mRunning = true;
            try {
                mServerSocket = new ServerSocket(PORT);
                while (mRunning) {
                    Socket socket = mServerSocket.accept();
                    Log.d(TAG, "accept ");
                    ThreadPoolManager.getInstance()
                            .startTaskThread(new ReceiveThread(socket), "receive-thread");
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (mServerSocket != null) {
                    try {
                        mServerSocket.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    class ReceiveThread extends Thread {
        private Socket socket;

        public ReceiveThread(Socket socket) {
            this.socket = socket;
            Log.d(TAG, " socket:" + socket.toString());
        }

        @Override
        public void run() {
            try {
                DataInputStream dis = new DataInputStream(socket.getInputStream());
                DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

                while (true) {
                    String data = dis.readUTF();
                    Log.d(TAG, "receive:" + data);
                    String s = "device:" + System.currentTimeMillis();
                    dos.writeUTF(s);
                    dos.flush();
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    socket.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
