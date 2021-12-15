package controller;

import com.google.inject.Inject;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import model.entity.DoctorMoveEvent;
import utils.Direction;
import utils.events.EventBus;

public class KeyboardController {

    @Inject
    private EventBus eventBus;

    public void attachToScene(Scene scene) {
        scene.setOnKeyPressed(this::handleKeyPressed);
    }

    private void handleKeyPressed(KeyEvent keyEvent) {
        DoctorMoveEvent event = switch (keyEvent.getCode()) {
            case W -> new DoctorMoveEvent(Direction.NORTH);
            case A -> new DoctorMoveEvent(Direction.WEST);
            case S -> new DoctorMoveEvent(Direction.SOUTH);
            case D -> new DoctorMoveEvent(Direction.EAST);
            case Q -> new DoctorMoveEvent(Direction.NORTH_WEST);
            case E -> new DoctorMoveEvent(Direction.NORTH_EAST);
            case Z -> new DoctorMoveEvent(Direction.SOUTH_WEST);
            case C -> new DoctorMoveEvent(Direction.SOUTH_EAST);
            default -> null;
        };

        if (event != null) {
            eventBus.emit(event);
        }
    }

}
