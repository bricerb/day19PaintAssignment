package sample;

import java.util.ArrayList;

/**
 * Created by Brice on 9/6/16.
 */
public class StrokeList {

    ArrayList<Stroke> strokeArrayList = new ArrayList<Stroke>();

    public ArrayList<Stroke> getStrokeArrayList() {
        return strokeArrayList;
    }

    public void addToStrokeArrayList(Stroke currentStroke) {
        strokeArrayList.add(currentStroke);
    }

    public void setStrokeArrayList(ArrayList<Stroke> strokeArrayList) {
        this.strokeArrayList = strokeArrayList;
    }
}
