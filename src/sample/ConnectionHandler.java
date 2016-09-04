package sample;

import javafx.application.Platform;
import javafx.scene.canvas.GraphicsContext;
import jodd.json.JsonParser;

import java.io.*;
import java.net.Socket;

/**
 * Created by Brice on 8/25/16.
 */
public class ConnectionHandler implements Runnable{

    private GraphicsContext gc = null;
    private Stroke stroke = null;

    Socket connection = null;

    public ConnectionHandler() {}

    public ConnectionHandler(Socket incConnection) {this.connection = incConnection;}

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

        Main myMain = new Main();
        Server myServer = new Server();
        BufferedReader clientInput = new BufferedReader(new InputStreamReader(incClient.getInputStream()));
        PrintWriter chatDisplay = new PrintWriter(incClient.getOutputStream(), true);

        System.out.println(clientInput);
        System.out.println(chatDisplay);
//        System.out.println(myServer.connection.getInetAddress().getHostAddress());

        String inputLine;
        System.out.println(clientInput.readLine());
        System.out.println(clientInput.readLine());

        while ((inputLine = clientInput.readLine()) != null) {
            System.out.println("test");
            int counter = 2;
            while(counter != 0) {
                if ((inputLine.split("=")[0]).equals("gcSender=")) {
                    gc = jsonRestoreGC((inputLine.split("=")[1]));
                    counter--;
                } else if ((inputLine.split("=")[0]).equals("strokeSender=")) {
                    gc = jsonRestoreGC((inputLine.split("=")[1]));
                    counter--;
                }
            }
            myMain.startThirdStage(gc);
            Platform.runLater(new RunnableGC(gc, stroke));
            counter = 0;
        }





/*        while ((inputLine = clientInput.readLine()) != null) {
            if (message.getUserName() == null) {
                if ((inputLine.split("=")[0]).equals("name")) {
                    message.setUserName(inputLine.split("=")[1]);
                    chatDisplay.println(message.getUserName());
                    System.out.println(message.getUserName() + " has connected.");
                    } else {
                        chatDisplay.write("Invalid Input.");
                    }
                } else if (inputLine.equalsIgnoreCase("histoty")) {
                    for (int counter = 0; counter < myServer.getMessages().size(); counter++)
                        chatDisplay.println(myServer.getMessages().get(counter));
                } else {

                    System.out.println(message.getUserName() + " says: " + inputLine);
                    chatDisplay.println(inputLine);
                    messageHistoryToArray(inputLine);
                }
            } */
        }

    public GraphicsContext jsonRestoreGC(String jsonTD) {
        JsonParser toDoItemParser = new JsonParser();
        GraphicsContext item = toDoItemParser.parse(jsonTD, GraphicsContext.class);

        return item;
    }

    public Stroke jsonRestoreStroke(String jsonTD) {
        JsonParser toDoItemParser = new JsonParser();
        Stroke item = toDoItemParser.parse(jsonTD, Stroke.class);

        return item;
    }


    }
