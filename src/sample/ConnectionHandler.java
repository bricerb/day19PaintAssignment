//package sample;
//
//import javafx.application.Application;
//import javafx.application.Platform;
//import javafx.scene.canvas.GraphicsContext;
//import javafx.scene.paint.Color;
//import javafx.stage.Stage;
//import jodd.json.JsonParser;
//
//import java.io.*;
//import java.net.Socket;
//
///**
// * Created by Brice on 8/25/16.
// */
//public class ConnectionHandler implements Runnable {
//
//
//    boolean isConnected = true;
//
//    GraphicsContext gc;
//    Main main;
//
//    Socket connection = null;
//
//    public ConnectionHandler() {
//    }
//
//    public ConnectionHandler(Socket incConnection, GraphicsContext myGC) {
////    public ConnectionHandler(Socket incConnection, Main myMain) {
//        this.gc = myGC;
////        this.main = myMain;
//        this.connection = incConnection;
//    }
//
//    public void run() {
//
//        try {
//            handleIncomingConnection(connection);
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        }
//    }
//
//    public void IncomingMessageDisplay() {
//
//    }
//
//    public void handleIncomingConnection(Socket incClient) throws IOException {
//
//        try {
//            BufferedReader clientInput = new BufferedReader(new InputStreamReader(incClient.getInputStream()));
//            PrintWriter chatDisplay = new PrintWriter(incClient.getOutputStream(), true);
//
//            String inputLine;
//
//            inputLine = clientInput.readLine();
//            if (inputLine.equals("serverStop")) {
//                connection.close();
//            }
//
//            Stroke jsonRestoredStroke = jsonRestoreStroke((inputLine.split("=")[1]));
//
//            chatDisplay.println("received");
//
//            Platform.runLater(new RunnableGC(this.gc, jsonRestoredStroke));
//        } catch (Exception ex) {
//
//        }
//    }
//
//    public Stroke jsonRestoreStroke(String jsonTD) {
//        JsonParser toDoItemParser = new JsonParser();
//        Stroke item = toDoItemParser.parse(jsonTD, Stroke.class);
//        return item;
//    }
//
//    public class RunnableGC implements Runnable {
//
//        private GraphicsContext gc = null;
//        private Stroke stroke = null;
//
//        public RunnableGC(GraphicsContext gc, Stroke stroke) {
//            this.gc = gc;
//            this.stroke = stroke;
//        }
//
//        public void run() {
//            gc.setStroke(Color.color(Math.random(), Math.random(), Math.random()));
//            gc.strokeOval(stroke.strokeX, stroke.strokeY, stroke.strokeSize, stroke.strokeSize); // <---- this is the actual work we need to do
//        }
//    }
//}
