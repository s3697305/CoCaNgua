package controller;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import model.*;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

public class GameViewController implements Initializable {

    private static final int CIRCLE_RADIUS = 16;
    private static final Paint[] COLORS = {Color.RED, Color.BLUE, Color.GOLD, Color.GREEN};

    @FXML
    AnchorPane parentView;
    @FXML
    Button rollDicesBtn;
    @FXML
    Button chooseDice1;
    @FXML
    Button chooseDice2;
    @FXML Text playerText;

    private int circleNumber = 1;
    private HashMap<Integer, CirclePosition> circleHashMap = new HashMap<>();
    private ArrayList<Player> players = new ArrayList<>();
    private int currentPlayerNumber;
    private Dice dice1;
    private Dice dice2;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Draw Moving Path
        drawCirclePath();

        // Add player with their horses
        addHorses();

        // Set current player to first one
        currentPlayerNumber = 0;
        playerText.setText("Current Turn: " + players.get(currentPlayerNumber).getColor());

        rollDicesBtn.setOnMouseClicked(onRollDices());
    }

    private boolean horseCanClick = false;
    private int numberOfStep;

    private EventHandler onRollDices() {
        return event -> {
            // Hide roll btn until moving process done
            rollDicesBtn.setVisible(false);

            // Random and display
            dice1 = new Dice();
            chooseDice1.setText(dice1.getValue() + "");
            dice2 = new Dice();
            chooseDice2.setText(dice2.getValue() + "");

            startTurn();
        };
    }

    private void startTurn() {

        chooseDice1.setOnMouseClicked(onSelectDice(dice1, dice2));
        chooseDice2.setOnMouseClicked(onSelectDice(dice2, dice1));

        checkDices();

        if (!dice1.canUse() && !dice2.canUse()) { // If there is no way to move, end moving, switch player
            endTurn();
        }
    }

    private void checkDices() {
        dice1.setCanUse(haveMovingPossibility(dice1.getValue()));
        dice2.setCanUse(haveMovingPossibility(dice2.getValue()));

        if (dice1.canUse() && !dice1.isUsed()) {
            chooseDice1.setDisable(false);
        } else if (!dice1.canUse()) {
            chooseDice1.setDisable(true);
        }
        if (dice2.canUse() && !dice2.isUsed()) {
            chooseDice2.setDisable(false);
        } else if (!dice2.canUse()) {
            chooseDice2.setDisable(true);
        }
    }

    private EventHandler onSelectDice(Dice dice1, Dice dice2) {
        return mouseEvent -> {
            if (!horseCanClick) {
                horseCanClick = true;
                numberOfStep = dice1.getValue();
                dice1.setUsed(true);
                ((Button) mouseEvent.getSource()).setDisable(true);
            }
        };
    }

    private void endTurn() {
        horseCanClick = false;

        checkDices();

        if (dice1.canUse() && !dice1.isUsed()) return;
        if (dice2.canUse() && !dice2.isUsed()) return;

        if (dice1.getValue() != dice2.getValue()) {
            String mess = "End " + players.get(currentPlayerNumber).getColor() + " turn";
            if (currentPlayerNumber == 3) {
                currentPlayerNumber = 0;
            } else {
                currentPlayerNumber++;
            }
            mess += "\nCurrent Turn: " + players.get(currentPlayerNumber).getColor();
            playerText.setText(mess);
        }
        rollDicesBtn.setVisible(true);

    }



    private boolean haveMovingPossibility(int random) {
        for (Horse horse: players.get(currentPlayerNumber).getHorses()) {
            if (!horse.isInNest())  { // If horse is in nest, check each next move, if it can move to the end, return true
                return canMove(horse, random);
            } else if (random == 6 && circleHashMap.get(currentPlayerNumber * 12 + 1).getHorse() == null){
                // If horse is not in nest but 2 dices value are the same, return true
                return true;
            }
        }
        return false;
    }

    private boolean canMove(Horse horse, int numberOfStep) {
        int startPosition = horse.getCirclePosition().getPosition();
        // Step through each position, if one of them has horse, return false. Else, return true
        for (int i = 1; i < numberOfStep; i++) {
            int position = (startPosition + i > 48 ? (startPosition + i) - 48: startPosition + i);
            System.out.println(position);
            CirclePosition circlePosition = circleHashMap.get(position);
            if (circlePosition.getHorse() != null) return false;
        }
        return true;
    }

    private void addHorses() {
        drawHorses(0.2, 2, "red");
        drawHorses(0.2, 23, "blue");
        drawHorses(10.2, 23, "yellow");
        drawHorses(10.2, 2, "green");
    }

    private void drawHorses(double startX, double startY, String color) {

        Player player = new Player();
        player.setColor(color);
        players.add(player);

        Image image = new Image(getClass().getResource("../resources/horse_" + color +".png").toExternalForm());
        for (int j = 0; j < 2; j++) {
            for (int i = 1; i <= 3; i+=2) {
                ImageView imageView = new ImageView(image);
                imageView.setFitHeight(45);
                imageView.setFitWidth(45);
                imageView.setLayoutX(2*(startX + i)*CIRCLE_RADIUS);
                imageView.setLayoutY(startY*CIRCLE_RADIUS);
                Horse horse = new Horse(imageView, color);
                player.getHorses().add(horse);

                horse.getImageView().setOnMouseClicked(onHorseClick(horse));

                parentView.getChildren().add(imageView);
            }
            startY += 3;
        }
    }

    private EventHandler onHorseClick(Horse horse) {
        return mouseEvent -> {
            if (horseCanClick && players.get(currentPlayerNumber).getHorses().contains(horse)) {
                System.out.println("-----------------------");
                System.out.println("A dice has been selected and horse belong to current player");

                if (horse.isInNest() && numberOfStep == 6) {
                    moveAndKickHorse(horse, currentPlayerNumber*12 + 1);
                } else if (!horse.isInNest()) {
                    if (canMove(horse, numberOfStep)) {
                        int newPosition = horse.getCirclePosition().getPosition() + numberOfStep;
                        if (newPosition > 48) {
                            newPosition = newPosition - 48;
                        }
                        moveAndKickHorse(horse, newPosition);
                    }
                }
            }
        };
    }

    private void moveAndKickHorse(Horse horse, int position) {
        CirclePosition circlePosition = circleHashMap.get(position);
        if (circlePosition.getHorse() != null) {
            if (players.get(currentPlayerNumber).getHorses().contains(circlePosition.getHorse())) { // If horse is belong to current user
                System.out.println("horse is belong to current user");
                return;
            }
            System.out.println("Horse belong to component");
            // Horse belong to component
            circlePosition.getHorse().returnToNest();
        }
        horse.moveHorse(circlePosition);
        endTurn();
    }

    private void drawCirclePath() {
        for (int i = 1; i <= 15; i++) {
            if (i == 8) {
                drawSymmetricCircle(1, i);
            } else if (i == 6) {
                for (int j = 6; j >= 1; j--) {
                    drawSymmetricCircle(j, i);
                }
            } else if (i == 10) {
                for (int j = 1; j <= 6; j++) {
                    drawSymmetricCircle(j, i);
                }
            }
            else if (i != 7 && i != 9) {
                drawSymmetricCircle(6, i);
                if (i == 1) {
                    setupCircle(16*CIRCLE_RADIUS, 2*i*CIRCLE_RADIUS, 48);
                } else if (i == 15) {
                    setupCircle(16*CIRCLE_RADIUS, 2*i*CIRCLE_RADIUS, 24);
                }
            }

        }
    }

    private void setupCircle(int x, int y, int number) {
        CirclePosition circle = new CirclePosition(CIRCLE_RADIUS, x, y, number);
        circle.setStroke(COLORS[number/12 != 4 ? number/12 : 0]);

        circle.setOnMouseClicked(mouseEvent -> {
            System.out.println(number);
        });
        parentView.getChildren().add(circle);

        circleHashMap.put(number, circle);
    }

    private void drawSymmetricCircle(int xPosition, int yPosition) {
        setupCircle(2*xPosition*CIRCLE_RADIUS, 2*yPosition*CIRCLE_RADIUS, circleNumber);
        setupCircle(2*(16-xPosition)*CIRCLE_RADIUS, 2*yPosition*CIRCLE_RADIUS, 48 - circleNumber);
        circleNumber++;
    }

}