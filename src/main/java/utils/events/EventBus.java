package utils.events;

import com.google.inject.Singleton;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Singleton
public class EventBus {
    private final Map<Class<?>, List<EventListener>> listeners = new HashMap<>();

    public void emit(Event event) {
        var eventClass = event.getClass();
        List<EventListener> eventListeners = listeners.get(eventClass);

        if (eventListeners == null) return;

        for (var listener : eventListeners) {
            listener.handle(event);
        }
    }

    public <T extends Event> void listen(Class<T> eventClass, EventListener<T> listener) {
        listeners.computeIfAbsent(eventClass, evClass -> new LinkedList<>());
        listeners.get(eventClass).add(listener);
    }
}
