package controller;

import com.google.inject.Inject;
import dialogs.EndCampaignDialog;
import dialogs.GameModeChoiceDialog;
import javafx.beans.Observable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import model.Engine;
import model.level_loaders.LevelLoader;
import model.entity.DoctorMoveEvent;
import dialogs.EndLevelDialog;
import rendering.Renderer;
import utils.GameStatus;
import utils.events.EventBus;

public class MainController {

    private final Engine engine;
    private LevelLoader levelLoader;
    private Renderer renderer;

    @FXML
    public Canvas canvas;

    @FXML
    public Button teleportButton;

    @FXML
    public Button timeTurnerButton;


    @Inject
    public MainController(Engine engine, EventBus eventBus) {
        this.engine = engine;


        eventBus.listen(DoctorMoveEvent.class, this::handleDoctorMove);
    }

    @FXML
    public void initialize() {
        this.renderer = new Renderer(this.canvas, engine.getGrid().getWidth(), engine.getGrid().getHeight());
        this.engine.gameStatusProperty().addListener(this::handleGameStatusChange);

        this.levelLoader = new GameModeChoiceDialog().showAndWaitForChoice();
        setupNextLevel();
    }

    private void handleDoctorMove(DoctorMoveEvent event) {
        engine.step(event.direction());
        updateTeleportButton();
        updateTimeTurnerButton();
        renderer.updateCanvas(engine.getEntitiesList());
    }

    private void handleGameStatusChange(Observable observable, GameStatus oldStatus, GameStatus newStatus) {
        if (oldStatus != GameStatus.GAME_IN_PROGRESS && newStatus== GameStatus.GAME_IN_PROGRESS) return;

        new EndLevelDialog(newStatus, this::setupNextLevel).show();
    }

    private void setupNextLevel() {
        boolean previousLevelWon = engine.gameStatusProperty().getValue().equals(GameStatus.DOCTOR_WON);

        engine.reset();
        boolean finishedGame = !levelLoader.hasNextLevel();
        if (previousLevelWon && finishedGame) {
            new EndCampaignDialog().showAndExit();
        }
        levelLoader.loadLevel(engine.getGrid(), previousLevelWon);

        updateTeleportButton();
        updateTimeTurnerButton();
        renderer.updateCanvas(engine.getEntitiesList());
    }

    private void updateTeleportButton(){
        int teleportsAvailable = engine.getGrid().getDoctor().getOwnedTeleports().size();
        teleportButton.setText("Teleports: " + teleportsAvailable);
    }

    private void updateTimeTurnerButton(){
        int timeTurnersAvailable = engine.getGrid().getDoctor().getOwnedTimeTurners().size();
        timeTurnerButton.setText("Time Turners: " + timeTurnersAvailable);
    }

    @FXML
    public void handleUseTeleport(ActionEvent event) {
        event.consume();
        engine.teleportDoctor();
        updateTeleportButton();
        renderer.updateCanvas(engine.getEntitiesList());
    }

    @FXML
    public void handleUseTimeTurner(ActionEvent event) {
        event.consume();
        engine.turnBackTime();
        updateTimeTurnerButton();
        renderer.updateCanvas(engine.getEntitiesList());
    }
}

