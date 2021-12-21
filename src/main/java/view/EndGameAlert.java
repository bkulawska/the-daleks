package view;

import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.DialogEvent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import utils.GameStatus;

public class EndGameAlert {
    private final GameStatus gameStatus;
    private final Alert alert = new Alert(Alert.AlertType.INFORMATION);

    public EndGameAlert(GameStatus gameStatus, EventHandler<DialogEvent> closeHandler) {
        this.gameStatus = gameStatus;
        setupAlert(closeHandler);
    }

    public void show() {
        alert.show();
    }

    private void setupAlert(EventHandler<DialogEvent> closeHandler) {
        var alertText = getAlertText();

        String imagePath = String.valueOf(getClass().getResource(getAlertImagePath()));
        Image image = new Image(imagePath);
        ImageView imageView = new ImageView(image);

        alert.setGraphic(imageView);
        alert.setContentText("");
        alert.setHeaderText("");
        alert.setTitle(alertText);
        alert.setOnCloseRequest(closeHandler);
    }

    private String getAlertText() {
        return switch (gameStatus) {
            case GAME_IN_PROGRESS -> "Game in still in progress, something went wrong";
            case DOCTOR_WON -> "You won!";
            case DOCTOR_LOST -> "You've lost :(";
            case EVERYBODY_DEAD -> "Everybody dead, how did it happen?";
        };
    }

    private String getAlertImagePath() {
        return switch (gameStatus) {
            case GAME_IN_PROGRESS -> "/image/doctor_daleks.png";
            case DOCTOR_WON -> "/image/happy_yoda.png";
            case DOCTOR_LOST, EVERYBODY_DEAD -> "/image/sad_yoda.jpeg";
        };
    }


}