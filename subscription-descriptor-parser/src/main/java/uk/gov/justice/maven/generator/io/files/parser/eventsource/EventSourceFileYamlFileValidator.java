package uk.gov.justice.maven.generator.io.files.parser.eventsource;

import static java.lang.String.format;

import uk.gov.justice.maven.generator.io.files.parser.YamlFileToJsonObjectConverter;
import uk.gov.justice.maven.generator.io.files.parser.YamlFileValidator;

import java.io.IOException;
import java.nio.file.Path;

import org.json.JSONObject;

public class EventSourceFileYamlFileValidator extends YamlFileValidator {

    public EventSourceFileYamlFileValidator(final Path schemaFile, final YamlFileToJsonObjectConverter yamlFileToJsonObjectConverter) {
        super(schemaFile, yamlFileToJsonObjectConverter);
    }

    @Override
    public void validate(final Path filePath) {
        try {
            final JSONObject convert = yamlFileToJsonObjectConverter.convert(filePath);
            schema().validate(convert);
        } catch (IOException ex) {
            throw new EventSourcesException(format("Unable to convert to JSON file %s ", filePath.toString()), ex);
        }
    }
}
