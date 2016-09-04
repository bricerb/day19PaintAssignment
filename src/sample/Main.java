package sample;

import javafx.application.Application;
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Main extends Application {

    Stroke dataContainer;

    final double DEFAULT_SCENE_WIDTH = 800;
    final double DEFAULT_SCENE_HEIGHT = 600;
    double strokeSize = 1;
    boolean isDrawingFlag = true;
    double x;
    double y;

    boolean isSharing = false;
    Stroke currentStroke;
    String sendingString;
    // Server Port 8005

    GraphicsContext gc2 = null;

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
        Button button1 = new Button("KEEL MEH NAO");
        Button button2 = new Button("GIT TUU DI CHOPPA'! NAO!");
        HBox hbButton = new HBox(10);
        hbButton.setAlignment(Pos.TOP_LEFT);
        hbButton.getChildren().add(button);
        hbButton.getChildren().add(button1);
        hbButton.getChildren().add(button2);
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
                serverStart();
            }
        }));

        button2.setOnAction((new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                startClient();
                isSharing = true;
            }
        }));

        // add canvas
        Canvas canvas = new Canvas(DEFAULT_SCENE_WIDTH, DEFAULT_SCENE_HEIGHT - 100);
//        canvas.setFocusTraversable(true);

        GraphicsContext gc = canvas.getGraphicsContext2D();

//        gc.setFill(Color.GREEN);
//        gc.setStroke(Color.BLUE);
        gc.setStroke(Color.color(Math.random(), Math.random(), Math.random()));
        gc.setLineWidth(2);

        grid.setOnKeyPressed(new EventHandler<KeyEvent>() {

            @Override
            public void handle(KeyEvent e) {

                if (e.getText().equalsIgnoreCase("d")) {
                    isDrawingFlag = !isDrawingFlag;
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
                        gc.setStroke(Color.color(Math.random(), Math.random(), Math.random()));
//                gc.strokeOval(e.getX(), e.getY(), 10, 10); // default mouse tracking
                        gc.strokeOval(e.getX() - 5, e.getY() - 5, strokeSize, strokeSize);
                        x = e.getX();
                        y = e.getY();

                        currentStroke = new Stroke(e.getX(), e.getY(), strokeSize);

                        String jsonString = jsonStringGeneratorStroke(currentStroke);
//                        System.out.println(jsonStringGenerator(currentStroke));

                        if (isSharing) {
                            sendingString = jsonString;
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

    public void startThirdStage(GraphicsContext gc3) {
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

        gc3.setStroke(Color.color(Math.random(), Math.random(), Math.random()));

        grid.add(canvas, 0, 2);

        // set our grid layout on the scene
        Scene defaultScene = new Scene(grid, DEFAULT_SCENE_WIDTH, DEFAULT_SCENE_HEIGHT);

        secondaryStage.setScene(defaultScene);
        System.out.println("About to show the second stage");

        secondaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);}

    public static void serverStart() {
        System.out.println("Running");

        Server myServer = new Server();
        myServer.startServer();
    }

    public String jsonStringGeneratorStroke(Stroke currentStroke) {
        JsonSerializer jsonSerializer = new JsonSerializer().deep(true);
        String jsonString = jsonSerializer.serialize(currentStroke);

        return jsonString;
    }

    public String jsonStringGeneratorGC(GraphicsContext gc2) {
        JsonSerializer jsonSerializer = new JsonSerializer().deep(true);
        String jsonString = jsonSerializer.serialize(gc2);

        return jsonString;
    }




    public void startClient() {
        Scanner inputScanner = new Scanner(System.in);

        System.out.println("Running");

        try {
            Socket clientSocket = new Socket("localhost", 8005);
//            Socket clientSocket = new Socket("10.0.0.126", 8024); // Rebecca
//            Socket clientSocket = new Socket ("10.0.0.136", 8080); // Jessica
//            Socket clientSocket = new Socket ("10.0.0.138", 8080); // Yehia
//            Socket clientSocket = new Socket ("10.0.0.132", 8585); // Donald
//            Socket clientSocket = new Socket ("10.0.0.134", 8005); // Brett
            System.out.println("Connected to Server");


            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            out.println("GCsender=" + jsonStringGeneratorGC(gc2));

            while (isSharing){
                out.println();
                out.println("gcSender=" + jsonStringGeneratorGC(gc2));
                out.println("strokeSender=" + jsonStringGeneratorStroke(currentStroke));
            }

                            String serverRespone = in.readLine();


//            System.out.println("Type exit to exit.");
//            System.out.println("Please enter your User Name");
//            String nameInput = inputScanner.nextLine();
//            out.println("name=" + nameInput);

//            String clientMessage = ("name=" + inputScanner.nextLine());

//            while(true) {


//                out.println();

//                String clientMessage = inputScanner.nextLine();
//                if(clientMessage.equals("exit")) {
//                    break;
//                }
//                out.println(clientMessage);
//                System.out.println(in.readLine());
//                out.println(in.readLine());
//
//            out.println("I regret nothing... IN BRICE WE TRUST");
//
//                String serverRespone = in.readLine();
//            }

            clientSocket.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}