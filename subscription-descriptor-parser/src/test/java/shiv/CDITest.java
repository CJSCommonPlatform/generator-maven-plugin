package shiv;

import uk.gov.justice.services.jdbc.persistence.JdbcDataSourceProvider;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.jglue.cdiunit.AdditionalClasses;
import org.jglue.cdiunit.CdiRunner;
import org.jglue.cdiunit.ProducesAlternative;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(CdiRunner.class)
@AdditionalClasses(value = {DefaultEventSource.class,
        EventSourceProvider.class,
        EventSource.class, EventSourceName.class})
@ProducesAlternative
public class CDITest {

    @Inject
    @Any
    Instance<EventSource> eventSources;

    @Inject
    EventSource defaultEventSource;

    @Inject
    @EventSourceName
    EventSource defaultIfNoName;

    @Inject
    @EventSourceName("people")
    EventSource people;

    @Inject
    @EventSourceName("example")
    EventSource example;


    @Test
    public void test() {
        System.out.println("========");
        System.out.println("Expected people -> Got " + people.eventSourceName());
        System.out.println("Expected example ->Got " + example.eventSourceName());
        System.out.println("Expected defaultIfNoName ->Got " + defaultIfNoName.eventSourceName());
        System.out.println("Expected default ->Got " + defaultEventSource.eventSourceName() + " and child " + defaultEventSource.getChild());

        System.out.println("========");
        List<String> eventSOurceNames = new ArrayList<>();
        eventSOurceNames.add("people");
        eventSOurceNames.add("example");
        eventSOurceNames.add("unknown");
        eventSOurceNames.add("default");
        eventSOurceNames.forEach(name -> {
            EventSource eventSource = eventSources.select(new EventSourceNameAnnotation(name)).get();
            System.out.println("eventSource = " + eventSource + " eventSourceName = " + eventSource.eventSourceName() + " with child " + eventSource.getChild() + " with child Name: " + eventSource.getChild().getChildName());

        });


    }
}
