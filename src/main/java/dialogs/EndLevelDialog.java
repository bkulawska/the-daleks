package dialogs;

import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import utils.GameStatus;

public class EndLevelDialog {
    private final GameStatus gameStatus;
    private final Alert alert = new Alert(Alert.AlertType.INFORMATION);

    public static final String GAME_IN_PROGRESS_IMAGE = "/image/doctor_daleks.png";
    public static final String DOCTOR_WON_IMAGE = "/image/happy_yoda.png";
    public static final String DOCTOR_LOST_IMAGE = "/image/sad_yoda.jpeg";

    public EndLevelDialog(GameStatus gameStatus, Runnable closeHandler) {
        this.gameStatus = gameStatus;
        setupAlert(closeHandler);
    }

    public void show() {
        alert.show();
    }

    private void setupAlert(Runnable closeHandler) {
        var alertText = getAlertText();

        String imagePath = String.valueOf(getClass().getResource(getAlertImagePath()));
        Image image = new Image(imagePath);
        ImageView imageView = new ImageView(image);

        alert.setGraphic(imageView);
        alert.setContentText("");
        alert.setHeaderText("");
        alert.setTitle(alertText);
        alert.setOnCloseRequest((e) -> closeHandler.run());
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
            case GAME_IN_PROGRESS -> GAME_IN_PROGRESS_IMAGE;
            case DOCTOR_WON -> DOCTOR_WON_IMAGE;
            case DOCTOR_LOST, EVERYBODY_DEAD -> DOCTOR_LOST_IMAGE;
        };
    }


}
