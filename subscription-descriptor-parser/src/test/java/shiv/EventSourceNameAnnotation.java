package shiv;

import javax.enterprise.util.AnnotationLiteral;

public class EventSourceNameAnnotation extends AnnotationLiteral<EventSourceName>
        implements EventSourceName {

    final String name;

    public EventSourceNameAnnotation(String name) {
        this.name = name;
    }

    @Override
    public String value() {
        return name;
    }
}


