package sample;

import javafx.application.Platform;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import jodd.json.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by Brice on 8/25/16.
 */
public class Server implements Runnable {

//    public static void main(String[] args) {
//        Server myServer = new Server();
//        myServer.startServer();
//    }

    Socket connection = null;
    GraphicsContext gc;
    boolean isConnected = true;
    Main main = null;

//    public Server (GraphicsContext myGC) {
    public Server (Main myMain) {
//        this.gc = myGC;
        this.main = myMain;
    }

    @Override
    public void run() {
        startServer();
    }

    public Server (Socket connection) {this.connection = connection;}

    public void startServer() {
        try {
            System.out.println("Starting Server");
            ServerSocket listener = new ServerSocket(8005);

            while(true) {
                Socket incConnection = listener.accept();
                ConnectionHandler handler = new ConnectionHandler(incConnection, gc);
//                ConnectionHandler handler = new ConnectionHandler(incConnection, main);
                Thread handlingThread = new Thread(handler);
                handlingThread.start();
                if (isConnected == false) {
                    break;
                }
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public class ConnectionHandler implements Runnable {

        GraphicsContext gc;

        Socket connection = null;

        public ConnectionHandler() {
        }

        public ConnectionHandler(Socket incConnection, GraphicsContext myGC) {
//    public ConnectionHandler(Socket incConnection, Main myMain) {
            this.gc = myGC;
//        this.main = myMain;
            this.connection = incConnection;
        }

        public void run() {

            try {
                handleIncomingConnection(connection);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        public void IncomingMessageDisplay() {

        }

        public void handleIncomingConnection(Socket incClient) throws IOException {

            try {
                BufferedReader clientInput = new BufferedReader(new InputStreamReader(incClient.getInputStream()));
                PrintWriter chatDisplay = new PrintWriter(incClient.getOutputStream(), true);

                String inputLine;

                inputLine = clientInput.readLine();

                Stroke jsonRestoredStroke = jsonRestoreStroke(inputLine);

                chatDisplay.println("received");

                serverList(jsonRestoredStroke);

                Platform.runLater(new RunnableGC(main.gc, jsonRestoredStroke));
            } catch (Exception ex) {

            }
        }

        public Stroke jsonRestoreStroke(String jsonTD) {
            JsonParser toDoItemParser = new JsonParser();
            Stroke item = toDoItemParser.parse(jsonTD, Stroke.class);
            return item;
        }

        public class RunnableGC implements Runnable {

            private GraphicsContext gc = null;
            private Stroke stroke = null;

            public RunnableGC(GraphicsContext gc, Stroke stroke) {
                this.gc = gc;
                this.stroke = stroke;
            }

            public void run() {


                gc.setStroke(Color.color(Math.random(), Math.random(), Math.random()));
                gc.strokeOval(stroke.strokeX, stroke.strokeY, stroke.strokeSize, stroke.strokeSize); // <---- this is the actual work we need to do
            }
        }

        public void serverList (Stroke currentStroke) {
            main.myList.addToStrokeArrayList(currentStroke);
        }
    }

}
