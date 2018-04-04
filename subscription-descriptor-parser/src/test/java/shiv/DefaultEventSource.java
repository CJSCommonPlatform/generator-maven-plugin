package shiv;

import javax.enterprise.inject.Vetoed;
import javax.inject.Inject;
import javax.inject.Named;

@Vetoed
public class DefaultEventSource implements EventSource {

    @Inject
    public Child child;

    public String name;

    @Override
    public String eventSourceName() {
        return name;
    }

    @Override
    public Child getChild() {
        return child;
    }
}
