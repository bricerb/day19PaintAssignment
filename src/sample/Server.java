package sample;

import javafx.scene.canvas.GraphicsContext;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by Brice on 8/25/16.
 */
public class Server{

//    public static void main(String[] args) {
//        Server myServer = new Server();
//        myServer.startServer();
//    }

    Socket connection = null;
    GraphicsContext gc;

    public Server (GraphicsContext myGC) {
        this.gc = myGC;
    }

    public Server (Socket connection) {this.connection = connection;}

    public void startServer() {
        try {
            System.out.println("Starting Server");
            ServerSocket listener = new ServerSocket(8005);

            while(true) {
                Socket incConnection = listener.accept();
                ConnectionHandler handler = new ConnectionHandler(incConnection, gc);
                Thread handlingThread = new Thread(handler);
                handlingThread.start();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}
