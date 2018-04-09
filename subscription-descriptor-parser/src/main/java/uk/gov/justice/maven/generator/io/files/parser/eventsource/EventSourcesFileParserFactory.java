package uk.gov.justice.maven.generator.io.files.parser.eventsource;


import static java.nio.file.Paths.get;

import uk.gov.justice.domain.eventsource.EventSource;
import uk.gov.justice.maven.generator.io.files.parser.FileParser;
import uk.gov.justice.maven.generator.io.files.parser.FileParserFactory;
import uk.gov.justice.maven.generator.io.files.parser.YamlFileToJsonObjectConverter;

import java.util.List;

public class EventSourcesFileParserFactory implements FileParserFactory<List<EventSource>> {

    @SuppressWarnings("squid:S1075")
    private static final String YAML_SCHEMA_FILE = "/json/schema/event-source-schema.json";

    @Override
    public FileParser<List<EventSource>> create() {
        return new EventSourcesFileParser(
                new EventSourceFileYamlFileValidator(get(YAML_SCHEMA_FILE),
                        new YamlFileToJsonObjectConverter()));
    }
}
