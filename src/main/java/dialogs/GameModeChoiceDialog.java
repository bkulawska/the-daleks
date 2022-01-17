package dialogs;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import model.level_loaders.CampaignLevelLoader;
import model.level_loaders.LevelLoader;
import model.level_loaders.RandomLevelLoader;

import java.util.Optional;

/**
 * Dialog responsible for asking user about Game Mode and setting
 * the LevelLoader respectively.
 */
public class GameModeChoiceDialog {

    private final Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    ButtonType random = new ButtonType("Random");
    ButtonType campaign = new ButtonType("Campaign");

    public GameModeChoiceDialog() {
        alert.setTitle("IT-TOADS: DALEKS!");
        alert.setHeaderText("Choose game mode");

        alert.getButtonTypes().setAll(random, campaign);
    }

    public LevelLoader showAndWaitForChoice() {
        Optional<ButtonType> choice = alert.showAndWait();
        if (choice.isPresent() && choice.get() == this.random) {
            return new RandomLevelLoader();
        }
        return new CampaignLevelLoader();
    }

}
