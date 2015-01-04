package org.ingrahamrobotics.robottables.network;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.ingrahamrobotics.robottables.network.desktop.Socket;

public class IO {

    private static final String addr = "255.255.255.255";
    private static final String DRIVER_RECV = "DriverRecv";
    private static final String ROBOT_SEND = DRIVER_RECV;
    private static final int DRIVER_RECV_PORT = 1130;
    private static final String ROBOT_RECV = "RobotRecv";
    private static final String DRIVER_SEND = ROBOT_RECV;
    private static final int ROBOT_RECV_PORT = 1140;

    private ListenEvents eventClass;
    Map<String, Socket> sockets = new HashMap<String, Socket>();

    public IO() throws IOException {
        sockets.put(ROBOT_RECV, new Socket(addr, ROBOT_RECV_PORT));
        sockets.put(DRIVER_RECV, new Socket(addr, DRIVER_RECV_PORT));
    }

    public void send(String data) throws IOException {
        // Push on all sockets, so other driver stations can recieve as well as the robot.
        for (Socket socket : sockets.values()) {
            if (socket.isOpen()) {
                socket.send(data);
            }
        }
    }

    public void listen(ListenEvents eventClass) {
        this.eventClass = eventClass;
        for (Socket socket : sockets.values()) {
            (new Thread(new RecvThread(socket))).start();
        }
    }

    public void close() {
        this.eventClass = null;
        for (Socket socket : sockets.values()) {
            socket.close();
        }
    }

    private class RecvThread implements Runnable {

        private final Socket socket;

        public RecvThread(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            boolean done = false;
            while (!done) {
                try {
                    final String message = socket.receive();
                    if (eventClass != null) {
                        eventClass.recv(message);
                    } else {
                        done = true;
                    }
                } catch (IOException ex) {
                    done = true;
                    if (eventClass != null) {
                        eventClass.error(ex.toString());
                    }
                }
            }
        }
    }

    public interface ListenEvents {

        public void recv(String message);

        public void error(String error);
    }
}
