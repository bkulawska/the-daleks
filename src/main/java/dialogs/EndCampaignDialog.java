package dialogs;

import javafx.scene.control.Alert;

public class EndCampaignDialog {
    private final Alert alert = new Alert(Alert.AlertType.INFORMATION);

    public EndCampaignDialog() {
        alert.setTitle("Congratulations!");
        alert.setHeaderText("You've won the campaign!");
    }

    public void showAndExit() {
        alert.setOnCloseRequest((e) -> System.exit(0));
        alert.showAndWait();
    }
}
