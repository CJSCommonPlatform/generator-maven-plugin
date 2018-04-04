package uk.gov.justice.maven.generator.io.files.parser.eventsource;

import static java.nio.file.Paths.get;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.fail;

import uk.gov.justice.maven.generator.io.files.parser.YamlFileToJsonObjectConverter;
import uk.gov.justice.maven.generator.io.files.parser.YamlFileValidator;
import uk.gov.justice.maven.generator.io.files.parser.subscriptiondescriptor.SubscriptionDescriptorYamlFileValidator;

import java.io.IOException;
import java.nio.file.Path;

import org.everit.json.schema.ValidationException;
import org.junit.Test;

public class EventSourcesFileValidatorTest {

    @Test
    public void shouldNotThrowExceptionOnCorrectEventSourcesYaml() throws IOException {

        try {
            final Path yamlSchemaFile = get("/json/schema/event-source-schema.json");
            final Path yamlFile = get("src/test/resources/event-sources.yaml");

            final YamlFileValidator subscriptionDescriptorFileValidator
                    = new SubscriptionDescriptorYamlFileValidator(yamlSchemaFile, new YamlFileToJsonObjectConverter());

            subscriptionDescriptorFileValidator.validate(yamlFile);
        } catch (ValidationException e) {
            fail("Unexpected validation exception");
        }

    }

    @Test
    public void shouldThrowExceptionOnInCorrectEventSourcesYaml() {
        final Path yamlSchemaFile = get("/json/schema/event-source-schema.json");
        final Path yamlFile = get("src/test/resources/incorrect-event-sources.yaml");

        try {
            final YamlFileValidator subscriptionDescriptorFileValidator
                    = new EventSourceFileYamlFileValidator(yamlSchemaFile, new YamlFileToJsonObjectConverter());

            subscriptionDescriptorFileValidator.validate(yamlFile);
            fail();
        } catch (ValidationException e) {
            assertThat(e, is(instanceOf(ValidationException.class)));
            assertThat(e.getMessage(), is("#/event_sources/0: required key [name] not found"));
        }
    }
}