package uk.gov.justice.domain.eventsource;

import java.util.List;

public class EventSources {

    @SuppressWarnings("squid:S1700")
    private final List<EventSource> eventSources;

    public EventSources(final List<EventSource> eventSources) {
        this.eventSources = eventSources;
    }

    public List<EventSource> getEventSources() {
        return eventSources;
    }
}
