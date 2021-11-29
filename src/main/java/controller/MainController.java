package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import model.Engine;
import renderer.Renderer;

public class MainController {

    private Engine engine;
    private Renderer renderer;

    @FXML
    public Canvas canvas;
    @FXML
    public Button stepButton;

    public MainController() {

    }

    @FXML
    public void initialize() {

    }

    public void initialSetup(Engine engine) {
        this.engine = engine;
        this.renderer = new Renderer(this.canvas, engine.grid.getWidth(), engine.grid.getHeight());
        renderer.updateCanvas(engine.getEntitiesList());
    }

    public void onStepAction(ActionEvent actionEvent) {
        engine.step();
        renderer.updateCanvas(engine.getEntitiesList());
    }
}

