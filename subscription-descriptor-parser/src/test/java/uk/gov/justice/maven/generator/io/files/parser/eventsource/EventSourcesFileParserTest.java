package uk.gov.justice.maven.generator.io.files.parser.eventsource;

import static java.nio.file.Paths.get;
import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.junit.Assert.fail;

import uk.gov.justice.domain.eventsource.EventSource;
import uk.gov.justice.domain.eventsource.Location;
import uk.gov.justice.maven.generator.io.files.parser.FileParser;

import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;

import org.everit.json.schema.ValidationException;
import org.junit.Test;

public class EventSourcesFileParserTest {

    final Path baseDir = get("src/test/resources/");

    @Test
    public void shouldThrowFileNotFoundExceptionWhenEventSourcesYamlNotFound() {
        final FileParser<List<EventSource>> eventSourcesFileParser = new EventSourcesFileParserFactory().create();
        final List<Path> paths = asList(get("no-event-source.yaml"));
        try {
            eventSourcesFileParser.parse(baseDir, paths);
            fail();
        } catch (EventSourcesException re) {
            assertThat(re, is(instanceOf(EventSourcesException.class)));
            assertThat(re.getCause(), is(instanceOf(NoSuchFileException.class)));
        }
    }

    @Test
    public void shouldFailOnIncorrectEventSourcesYaml() {
        final FileParser<List<EventSource>> eventSourcesFileParser = new EventSourcesFileParserFactory().create();

        final List<Path> paths = asList(get("incorrect-event-sources.yaml"));
        try {
            eventSourcesFileParser.parse(baseDir, paths);
            fail();
        } catch (EventSourcesException re) {
            assertThat(re, is(instanceOf(EventSourcesException.class)));
            assertThat(re.getCause(), is(instanceOf(ValidationException.class)));
            assertThat(re.getCause().getMessage(), is("#/event_sources/0: required key [name] not found"));
        }
    }

    @Test
    public void shouldParsePathsToYaml() {
        final FileParser<List<EventSource>> eventSourcesFileParser = new EventSourcesFileParserFactory().create();

        final List<Path> paths = asList(get("event-sources.yaml"));
        final Collection<List<EventSource>> eventSourcesCollection = eventSourcesFileParser.parse(baseDir, paths);

        assertThat(eventSourcesCollection.size(), is(1));
        for (final List<EventSource> eventSources : eventSourcesCollection) {
            assertThat(eventSources.size(), is(2));
            for (final EventSource eventSource : eventSources) {
                    if (eventSource.getName().equalsIgnoreCase("people")) {
                        Location location = eventSource.getLocation();
                        assertThat(location.getJmsUri(), is("jms:topic:people.event?timeToLive=1000"));
                        assertThat(location.getRestUri(), is("http://localhost:8080/people/event-source-api/rest"));
                        assertThat(location.getDataSource(), is("java:/app/peoplewarfilename/DS.eventstore"));
                    }
                    if (eventSource.getName().equalsIgnoreCase("example")) {
                        Location location = eventSource.getLocation();
                        assertThat(location.getJmsUri(), is("jms:topic:example.event?timeToLive=1000"));
                        assertThat(location.getRestUri(), is("http://localhost:8080/example/event-source-api/rest"));
                        assertThat(location.getDataSource(), isEmptyOrNullString());
                    }

            }
        }
    }
}