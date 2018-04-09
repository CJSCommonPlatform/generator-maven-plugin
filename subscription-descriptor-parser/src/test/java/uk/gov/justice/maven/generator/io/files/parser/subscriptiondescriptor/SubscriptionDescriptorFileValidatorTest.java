package uk.gov.justice.maven.generator.io.files.parser.subscriptiondescriptor;

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

public class SubscriptionDescriptorFileValidatorTest {

    @Test
    public void shouldNotThrowExceptionOnCorrectSubscriptionYaml() throws IOException {
        try {
            final Path filePath = get("/json/schema/subscription-schema.json");
            final Path yamlPath = get("src/test/resources/subscription.yaml");

            final YamlFileValidator subscriptionDescriptorFileValidator
                    = new SubscriptionDescriptorYamlFileValidator(filePath,new YamlFileToJsonObjectConverter());

            subscriptionDescriptorFileValidator.validate(yamlPath);
        } catch (ValidationException e) {
            fail("Unexpected validation exception");
        }

    }

    @Test
    public void shouldThrowExceptionOnInCorrectSubscriptionYaml() {
        try {
            final Path schemaFilePath = get("/json/schema/subscription-schema.json");
            final Path yamlPath = get("src/test/resources/incorrect-subscription.yaml");

            final YamlFileValidator subscriptionDescriptorFileValidator
                    = new SubscriptionDescriptorYamlFileValidator(schemaFilePath,new YamlFileToJsonObjectConverter());

            subscriptionDescriptorFileValidator.validate(yamlPath);
            fail();
        } catch (ValidationException e) {
            assertThat(e, is(instanceOf(ValidationException.class)));
            assertThat(e.getMessage(), is("#/subscription_descriptor: required key [spec_version] not found"));
        }
    }
}