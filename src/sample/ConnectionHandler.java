package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;
import jodd.json.JsonParser;

import java.io.*;
import java.net.Socket;

/**
 * Created by Brice on 8/25/16.
 */
public class ConnectionHandler implements Runnable{



    boolean isConnected = true;

    GraphicsContext gc;

    Socket connection = null;

    public ConnectionHandler() {
    }

    public ConnectionHandler(Socket incConnection, GraphicsContext myGC) {
        this.gc = myGC;
        this.connection = incConnection;
    }

    public void run() {

        try{
            handleIncomingConnection(connection);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void IncomingMessageDisplay () {

    }

    public void handleIncomingConnection (Socket incClient) throws IOException{

        try {
            BufferedReader clientInput = new BufferedReader(new InputStreamReader(incClient.getInputStream()));
            PrintWriter chatDisplay = new PrintWriter(incClient.getOutputStream(), true);

            System.out.println(clientInput);
            System.out.println(chatDisplay);
//        System.out.println(myServer.connection.getInetAddress().getHostAddress());

            String inputLine;
//        System.out.println(inputLine);

//            while ((inputLine = clientInput.readLine()) != null) {
            while (isConnected) {
//            Stroke currentStroke = jsonRestoreStroke((inputLine.split("=")[1]));
                inputLine = clientInput.readLine();
                if (inputLine.equalsIgnoreCase("stop")) {
//                    isConnected = false;
                }
                Stroke jsonRestoredStroke = jsonRestoreStroke((inputLine.split("=")[1]));
                System.out.println("test");
//                RunnableGC myRunnableGC = new RunnableGC(myMain.gc, jsonRestoredStroke);
                System.out.println(jsonRestoredStroke.strokeX + " and " + jsonRestoredStroke.strokeY);

                chatDisplay.println("received");
                Platform.runLater(new RunnableGC(this.gc, jsonRestoredStroke));
//            Platform.runLater(new RunnableGC(gc, stroke));
//            }
            }
        } catch (Exception ex) {

        }
    }

    public Stroke jsonRestoreStroke(String jsonTD) {
        JsonParser toDoItemParser = new JsonParser();
        Stroke item = toDoItemParser.parse(jsonTD, Stroke.class);
        return item;
    }


    }
