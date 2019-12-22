package model;


import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

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
