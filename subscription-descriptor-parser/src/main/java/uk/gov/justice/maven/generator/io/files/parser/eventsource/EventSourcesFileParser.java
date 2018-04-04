package uk.gov.justice.maven.generator.io.files.parser.eventsource;

import static com.fasterxml.jackson.annotation.JsonCreator.Mode.PROPERTIES;
import static java.lang.String.format;

import uk.gov.justice.domain.eventsource.EventSource;
import uk.gov.justice.domain.eventsource.EventSources;
import uk.gov.justice.maven.generator.io.files.parser.FileParser;
import uk.gov.justice.maven.generator.io.files.parser.YamlFileValidator;

import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.everit.json.schema.ValidationException;

public class EventSourcesFileParser implements FileParser<List<EventSource>> {

    private YamlFileValidator yamlFileValidator;

    public EventSourcesFileParser(final YamlFileValidator yamlFileValidator) {
        this.yamlFileValidator = yamlFileValidator;
    }

    @Override
    public Collection<List<EventSource>> parse(final Path baseDir, final Collection<Path> paths) {
        return paths.stream()
                .map(path -> read(baseDir.resolve(path).toAbsolutePath()))
                .collect(Collectors.toList());
    }

    private List<EventSource> read(final Path filePath) {
        try {
            yamlFileValidator.validate(filePath);

            final ObjectMapper mapper = new ObjectMapper(new YAMLFactory())
                    .registerModule(new Jdk8Module())
                    .registerModule(new ParameterNamesModule(PROPERTIES));

            mapper.setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES);

            return mapper.readValue(filePath.toFile(), EventSources.class).getEventSources();
        } catch (final NoSuchFileException e) {
            throw new EventSourcesException(format("No such event sources YAML file %s ", filePath), e);
        } catch (final ValidationException e) {
            throw new EventSourcesException(format("Failed to validate event sources yaml file %s ", filePath), e);
        } catch (final IOException e) {
            throw new EventSourcesException(format("Failed to read event sources yaml file %s ", filePath), e);
        }
    }
}
