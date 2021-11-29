package controller;

import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import model.Engine;
import renderer.Renderer;


public class MainController {

    private final Engine engine;
    private Renderer renderer;

    @FXML
    public Canvas canvas;
    @FXML
    public Button stepButton;

    @Inject
    public MainController(Engine engine) {
        this.engine = engine;
    }

    @FXML
    public void initialize() {
        this.renderer = new Renderer(this.canvas, engine.getGrid().getWidth(), engine.getGrid().getHeight());
        renderer.updateCanvas(engine.getEntitiesList());
    }

    public void onStepAction() {
        engine.step();
        renderer.updateCanvas(engine.getEntitiesList());
    }
}

