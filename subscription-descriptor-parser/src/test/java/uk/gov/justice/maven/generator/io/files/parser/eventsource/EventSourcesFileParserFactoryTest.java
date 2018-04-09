package uk.gov.justice.maven.generator.io.files.parser.eventsource;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import uk.gov.justice.domain.eventsource.EventSource;
import uk.gov.justice.domain.eventsource.EventSources;
import uk.gov.justice.maven.generator.io.files.parser.FileParser;

import java.util.List;

import org.junit.Test;

public class EventSourcesFileParserFactoryTest {

    @Test
    public void shouldCreateEvenSourcesJsonSchemaFileParser() throws Exception {

        final FileParser<List<EventSource>> eventSourcesFileParser = new EventSourcesFileParserFactory().create();

        assertThat(eventSourcesFileParser, is(instanceOf(EventSourcesFileParser.class)));
    }

}