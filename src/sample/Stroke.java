package sample;

import javafx.scene.paint.Color;

/**
 * Created by Brice on 9/1/16.
 */
public class Stroke {
    double strokeX;
    double strokeY;
    double strokeSize;
    double colorR;
    double colorG;
    double colorB;

    public Stroke(double strokeX, double strokeY, double strokeSize, double colorR, double colorG, double colorB) {
        this.strokeX = strokeX;
        this.strokeY = strokeY;
        this.strokeSize = strokeSize;
        this.colorR = colorR;
        this.colorG = colorG;
        this.colorB = colorB;
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

    public double getColorR() {
        return colorR;
    }

    public void setColorR(double colorR) {
        this.colorR = colorR;
    }

    public double getColorG() {
        return colorG;
    }

    public void setColorG(double colorG) {
        this.colorG = colorG;
    }

    public double getColorB() {
        return colorB;
    }

    public void setColorB(double colorB) {
        this.colorB = colorB;
    }
}
