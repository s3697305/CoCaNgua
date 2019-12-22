package sample.model;


import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import sample.Main;

import java.util.ArrayList;

public class CirclePosition extends Circle {
    private boolean isContainHorse;
    private int position;
    private Horse horse;


    public CirclePosition(double radius, int x, int y, int position) {
        setRadius(radius);
        setLayoutX(x);
        setLayoutY(y);
        setFill(Color.WHITE);
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public Horse getHorse() {
        return horse;
    }

    public void setHorse(Horse horse) {
        this.horse = horse;
    }
}
