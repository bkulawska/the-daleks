package model.entity;

import utils.Direction;
import utils.events.Event;

public record DoctorMoveEvent(Direction direction) implements Event {
}
