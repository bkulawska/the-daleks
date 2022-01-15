package controller;

import com.google.inject.Inject;
import javafx.beans.Observable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DialogEvent;
import model.Engine;
import model.entity.DoctorMoveEvent;
import view.EndGameAlert;
import view.Renderer;
import utils.GameStatus;
import utils.events.EventBus;

public class MainController {

    private final Engine engine;
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
        renderer.updateCanvas(engine.getEntitiesList());

        this.engine.gameStatusProperty().addListener(this::handleGameStatusChange);
    }

    private void handleDoctorMove(DoctorMoveEvent event) {
        engine.step(event.direction());
        updateTeleportButton();
        updateTimeTurnerButton();
        renderer.updateCanvas(engine.getEntitiesList());
    }

    private void handleGameStatusChange(Observable observable, GameStatus oldStatus, GameStatus newStatus) {
        if (oldStatus != GameStatus.GAME_IN_PROGRESS && newStatus== GameStatus.GAME_IN_PROGRESS) return;

        new EndGameAlert(newStatus, this::resetEngine).show();
    }

    private void resetEngine(DialogEvent dialogEvent) {
        engine.reset();
        updateTeleportButton();
        updateTimeTurnerButton();
        renderer.updateCanvas(engine.getEntitiesList());
    }

    private void updateTeleportButton(){
        int teleportsAvailable = engine.getGrid().getDoctor().getNumberOfTeleportsAvailable();
        teleportButton.setText("Teleports: " + teleportsAvailable);
    }

    private void updateTimeTurnerButton(){
        int timeTurnersAvailable = engine.getGrid().getDoctor().getNumberOfTimeTurnersAvailable();
        timeTurnerButton.setText("Time Turners: " + timeTurnersAvailable);
    }

    @FXML
    public void handleUseTeleport(ActionEvent event) {
        event.consume();
        engine.useTeleport();
        updateTeleportButton();
        renderer.updateCanvas(engine.getEntitiesList());
    }

    @FXML
    public void handleUseTimeTurner(ActionEvent event) {
        event.consume();
        engine.useTimeTurner();
        updateTimeTurnerButton();
        renderer.updateCanvas(engine.getEntitiesList());
    }
}

