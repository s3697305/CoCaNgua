package model;


import javafx.scene.image.ImageView;

public class Horse {
    private static final int CIRCLE_RADIUS = 16;
    private CirclePosition circlePosition;
    private ImageView imageView;
    private double defaultX;
    private double defaultY;
    private String color;

    public Horse(ImageView imageView, String color) {
        this.imageView = imageView;
        this.defaultX = imageView.getLayoutX();
        this.defaultY = imageView.getLayoutY();
        this.color = color;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public boolean isInNest() {
        return (imageView.getLayoutX() == defaultX && imageView.getLayoutY() == defaultY);
    }

    public void returnToNest() {
        imageView.setLayoutX(defaultX);
        imageView.setLayoutY(defaultY);
        if (this.circlePosition != null) {circlePosition.setHorse(null);}
    }

    public void moveHorse(CirclePosition position) {
        if (this.circlePosition != null) {
            this.circlePosition.setHorse(null);
        }
        imageView.setLayoutX(position.getLayoutX() - 1.5 * CIRCLE_RADIUS);
        imageView.setLayoutY(position.getLayoutY() - 2 * CIRCLE_RADIUS);
        this.circlePosition = position;
        this.circlePosition.setHorse(this);
    }

    public CirclePosition getCirclePosition() {
        return circlePosition;
    }

    public void setCirclePosition(CirclePosition circlePosition) {
        this.circlePosition = circlePosition;
    }
}
