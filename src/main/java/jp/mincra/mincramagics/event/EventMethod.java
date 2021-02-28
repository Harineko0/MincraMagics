package jp.mincra.mincramagics.event;

import org.bukkit.event.Listener;

import java.lang.reflect.Method;

public class EventMethod {
    private Method method;
    private Listener listener;

    public EventMethod(Method method, Listener listener) {
        this.method = method;
        this.listener = listener;
    }

    public Method getMethod() {
        return method;
    }

    public Listener getListener() {
        return listener;
    }
}
