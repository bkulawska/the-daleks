package controller;

import com.google.inject.Inject;
import javafx.beans.Observable;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Alert;
import javafx.scene.control.DialogEvent;
import model.Engine;
import model.entity.DoctorMoveEvent;
import renderer.Renderer;
import utils.GameStatus;
import utils.events.EventBus;


public class MainController {

    private final Engine engine;
    private Renderer renderer;

    @FXML
    public Canvas canvas;

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
        renderer.updateCanvas(engine.getEntitiesList());
    }

    private void handleGameStatusChange(Observable observable, GameStatus oldStatus, GameStatus newStatus) {
        if (oldStatus != GameStatus.GAME_IN_PROGRESS && newStatus== GameStatus.GAME_IN_PROGRESS) return;

        System.out.println(newStatus.toString());

        String alertText = switch (newStatus) {
            case GAME_IN_PROGRESS -> "Gra jeszcze trwa wtf";
            case DOCTOR_WON -> "Doktor wygrał";
            case DOCTOR_LOST -> "Doktor przegrał";
            case EVERYBODY_DEAD -> "Wszyscy nie żyją xDD";
        };

        var alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(alertText);
        alert.setOnCloseRequest(this::resetEngine);
        alert.show();
    }

    private void resetEngine(DialogEvent dialogEvent) {
        engine.reset();

        renderer.updateCanvas(engine.getEntitiesList());
    }
}

