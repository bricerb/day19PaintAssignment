package sample;

import javafx.application.Platform;
import javafx.scene.canvas.GraphicsContext;

/**
 * Created by Brice on 9/5/16.
 */
public class ServerGC implements Runnable{

    GraphicsContext gc;

    public ServerGC(GraphicsContext gc) {
        this.gc = gc;
    }

    @Override
    public void run() {
//        Platform.runLater(new RunnableGC(this.gc, jsonRestoredStroke));
    }


}
