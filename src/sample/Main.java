package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import jodd.json.JsonParser;
import jodd.json.JsonSerializer;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class Main extends Application{

    Stroke dataContainer;

    final double DEFAULT_SCENE_WIDTH = 800;
    final double DEFAULT_SCENE_HEIGHT = 800;
    double strokeSize = 1;
    boolean isDrawingFlag = true;
    double x;
    double y;
    StrokeList myList = new StrokeList();

    boolean isSharing = false;
    Stroke currentStroke;
    String sendingString;

    double colorR;
    double colorG;
    double colorB;
//     Server Port 8005

    static GraphicsContext gc;
    GraphicsContext gc2 = null;

    public void run() {
        main(null);
    }

//    public void start(Main myMain) {
//        myMain.run();
//        System.out.println("thread");
//    }

    public void start() {
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Hello Darkness, My Old Friend...");

        // we're using a grid layout
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        grid.setGridLinesVisible(true);

        // add buttons and canvas to the grid
        Text sceneTitle = new Text("Welcome to Paint application");
        sceneTitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(sceneTitle, 0, 0);

        Button button = new Button("Sample paint button");
        Button button1 = new Button("KEEL MEH NAO (Server)");
        Button button3 = new Button("DU ET! (Server Stop)");
        Button button2 = new Button("GIT TUU DI CHOPPA'! NAO! (Client)");
        Button button4 = new Button("THA GAGGLES DUU NATHING!");
        HBox hbButton = new HBox(10);
        hbButton.setAlignment(Pos.TOP_LEFT);
        hbButton.getChildren().add(button);
        hbButton.getChildren().add(button1);
        hbButton.getChildren().add(button3);
        hbButton.getChildren().add(button2);
        hbButton.getChildren().add(button4);

        grid.add(hbButton, 0, 1);

        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                System.out.println("I can switch to another scene here ...");
//                primaryStage.setScene(loginScene);
                startSecondStage();
            }
        });

        button1.setOnAction((new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                isDrawingFlag = false;
                serverStart();

            }
        }));

//        button3.setOnAction((new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent event) {
//               isSharing = false;
//                stopServer();
//            }
//        }));

        button2.setOnAction((new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                isDrawingFlag = true;
                startClient();
                isSharing = true;
            }
        }));

        button4.setOnAction((new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // redraw all strokes
                readFromFile();
                for (Stroke currentStroke : myList.getStrokeArrayList()) {
                    gc.setStroke(Color.color(Math.random(), Math.random(), Math.random()));
                    gc.strokeOval(currentStroke.strokeX, currentStroke.strokeY, currentStroke.strokeSize, currentStroke.strokeSize);
                }
            }
        }));

        // add canvas
        Canvas canvas = new Canvas(DEFAULT_SCENE_WIDTH, DEFAULT_SCENE_HEIGHT - 100);
//        canvas.setFocusTraversable(true);

        gc = canvas.getGraphicsContext2D();

//        gc.setFill(Color.GREEN);
//        gc.setStroke(Color.BLUE);
        Color color = Color.color(Math.random(), Math.random(), Math.random());
        gc.setLineWidth(2);

        grid.setOnKeyPressed(new EventHandler<KeyEvent>() {

            @Override
            public void handle(KeyEvent e) {

                if (e.getText().equalsIgnoreCase("d")) {
                    isDrawingFlag = !isDrawingFlag;
                }
                if (e.getText().equalsIgnoreCase("c")) {
                    gc.clearRect(0, 0, DEFAULT_SCENE_WIDTH, DEFAULT_SCENE_HEIGHT);
                }
                if (e.getCode() == KeyCode.UP) {
                    strokeSize += 1;
                    if (strokeSize == 20) {
                        strokeSize -= 1;
                    }
                }
                if (e.getCode() == KeyCode.DOWN) {
                    strokeSize -= 1;
                    if (strokeSize == 2) {
                        strokeSize += 1;
                    }
                }
            }
        });

        canvas.setOnMouseMoved(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent e) {
                if (isDrawingFlag) {
                    if (e.isDragDetect()) {
//                        System.out.println("x: " + e.getX() + ", y: " + e.getY());

                        colorR = randomColorGenerator();
                        colorG = randomColorGenerator();
                        colorB = randomColorGenerator();

                        gc.setStroke(Color.color(colorR, colorG, colorB));
//                gc.strokeOval(e.getX(), e.getY(), 10, 10); // default mouse tracking
                        gc.strokeOval(e.getX() - 5, e.getY() - 5, strokeSize, strokeSize);
                        x = e.getX();
                        y = e.getY();

                        currentStroke = new Stroke(e.getX(), e.getY(), strokeSize, colorR, colorG, colorB);

                        myList.addToStrokeArrayList(currentStroke);

                        String jsonString = jsonStringGeneratorStroke(currentStroke);
//                        System.out.println(jsonStringGenerator(currentStroke));

                        if (isSharing) {
                            sendingString = jsonString;
                            startClient();
                        }

                        if (gc2 != null) {
                            gc2.strokeOval(x - 5, y - 5, strokeSize, strokeSize);
                        }
//                        addStroke(e.getX(), e.getY(), strokeSize); // Class for Strokes to store stroke coordinates. use a List to serialize to JSON.
                    }
                } else {
                }
            }
        });

//            RunnableGC myRunner = new RunnableGC(gc, currentStroke);

        grid.add(canvas, 0, 2);
//        grid.add(canvas, 0, 2, 2, 1); // sets colspan and rowspan to be bigger

        // set our grid layout on the scene
        Scene defaultScene = new Scene(grid, DEFAULT_SCENE_WIDTH, DEFAULT_SCENE_HEIGHT);

        primaryStage.setScene(defaultScene);
        primaryStage.show();
    }

    public void startSecondStage() {
        Stage secondaryStage = new Stage();
        secondaryStage.setTitle("Welcome to JavaFX");

        // we're using a grid layout
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        grid.setGridLinesVisible(true);
//        grid.setPrefSize(primaryStage.getMaxWidth(), primaryStage.getMaxHeight());

        // add buttons and canvas to the grid
        Text sceneTitle = new Text("Welcome to Paint application");
        sceneTitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(sceneTitle, 0, 0);

        Button button = new Button("Sample paint button");
        HBox hbButton = new HBox(10);
        hbButton.setAlignment(Pos.TOP_LEFT);
        hbButton.getChildren().add(button);
        grid.add(hbButton, 0, 1);

        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                System.out.println("I can switch to another scene here ...");
            }
        });

        // add canvas
        Canvas canvas = new Canvas(DEFAULT_SCENE_WIDTH, DEFAULT_SCENE_HEIGHT - 100);

        gc2 = canvas.getGraphicsContext2D();
            gc2.setStroke(Color.color(Math.random(), Math.random(), Math.random()));

        grid.add(canvas, 0, 2);

        // set our grid layout on the scene
        Scene defaultScene = new Scene(grid, DEFAULT_SCENE_WIDTH, DEFAULT_SCENE_HEIGHT);

        secondaryStage.setScene(defaultScene);
        System.out.println("About to show the second stage");

        secondaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);}

    public void serverStart() {
//        Server myServer = new Server(gc);
        Server myServer = new Server(this);
        Thread handlingThread = new Thread(myServer);
        handlingThread.start();
    }

    public String jsonStringGeneratorStroke(Stroke currentStroke) {
        JsonSerializer jsonSerializer = new JsonSerializer().deep(true);
        String jsonString = jsonSerializer.serialize(currentStroke);

        return jsonString;
    }

    public String jsonStringGeneratorList(StrokeList currentList) {
        JsonSerializer jsonSerializer = new JsonSerializer().deep(true);
        String jsonString = jsonSerializer.serialize(currentList);

        return jsonString;
    }

    public String jsonStringGeneratorGC(GraphicsContext gc2) {
        JsonSerializer jsonSerializer = new JsonSerializer().deep(true);
        String jsonString = jsonSerializer.serialize(gc2);

        return jsonString;
    }

    public Stroke jsonRestoreStroke(String jsonTD) {
        JsonParser toDoItemParser = new JsonParser();
        Stroke item = toDoItemParser.parse(jsonTD, Stroke.class);
        currentStroke = item;
        return item;
    }

    public GraphicsContext jsonRestoreGC(String jsonTD) {
        JsonParser toDoItemParser = new JsonParser();
        GraphicsContext item = toDoItemParser.parse(jsonTD, GraphicsContext.class);
        gc = item;
        return item;
    }

    public StrokeList jsonRestoreList(String jsonTD) {
        JsonParser toDoItemParser = new JsonParser();
        StrokeList item = toDoItemParser.parse(jsonTD, StrokeList.class);
        return item;
    }

    public void startClient() {
        Scanner inputScanner = new Scanner(System.in);

        try {
            Socket clientSocket = new Socket("localhost", 8005);
//            Socket clientSocket = new Socket("10.0.0.141", 8005); // Dom
//            Socket clientSocket = new Socket("10.0.0.126", 8024); // Rebecca
//            Socket clientSocket = new Socket ("10.0.0.136", 8080); // Jessica
//            Socket clientSocket = new Socket ("10.0.0.138", 8080); // Yehia
//            Socket clientSocket = new Socket ("10.0.0.132", 8585); // Donald
//            Socket clientSocket = new Socket ("10.0.0.134", 8005); // Brett


            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                out.println(jsonStringGeneratorStroke(currentStroke));
                String serverRespone = in.readLine();
//
            clientSocket.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void stopServer() {
        Scanner inputScanner = new Scanner(System.in);
        try{
            Socket clientSocket = new Socket("localhost", 8005);
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            out.println("serverStop");

            clientSocket.close();

        } catch (Exception ex) {

        }
    }


    public void Main () {
    }

    public void writeUserFile() {
        FileWriter writeToFile = null;
        try {
            File userFile = new File("List.json");
            writeToFile = new FileWriter(userFile);

            writeToFile.write(jsonStringGeneratorList(myList));

            writeToFile.close();

        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            if (writeToFile != null) {
                try {
                    writeToFile.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    public void readFromFile () {
        try {
            File userFile = new File("List.json");
            if (userFile.exists()) {
            Scanner fileScanner = new Scanner(userFile);

            while (fileScanner.hasNextLine()) {
                String scanString = fileScanner.nextLine();
                myList = jsonRestoreList(scanString);
                }
            }
        } catch (Exception exception) {
        }
    }

    public double randomColorGenerator() {
        double RNG = Math.random();
        return RNG;
    }
}