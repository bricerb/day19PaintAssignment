package sample;

import javafx.scene.paint.Color;

/**
 * Created by Brice on 9/1/16.
 */
public class Stroke {
    double strokeX;
    double strokeY;
    double strokeSize;

    public Stroke(double strokeX, double strokeY, double strokeSize) {
        this.strokeX = strokeX;
        this.strokeY = strokeY;
        this.strokeSize = strokeSize;
    }

    public Stroke() {
    }

    public double getStrokeX() {
        return strokeX;
    }

    public void setStrokeX(double strokeX) {
        this.strokeX = strokeX;
    }

    public double getStrokeY() {
        return strokeY;
    }

    public void setStrokeY(double strokeY) {
        this.strokeY = strokeY;
    }

    public double getStrokeSize() {
        return strokeSize;
    }

    public void setStrokeSize(double strokeSize) {
        this.strokeSize = strokeSize;
    }

}
