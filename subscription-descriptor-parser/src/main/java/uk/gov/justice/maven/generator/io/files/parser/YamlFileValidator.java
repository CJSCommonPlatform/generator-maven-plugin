package uk.gov.justice.maven.generator.io.files.parser;

import static java.lang.String.format;

import uk.gov.justice.maven.generator.io.files.parser.subscriptiondescriptor.SubscriptionDescriptorException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

import org.everit.json.schema.Schema;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import org.json.JSONTokener;

public abstract class YamlFileValidator {

    protected final YamlFileToJsonObjectConverter yamlFileToJsonObjectConverter;

    private final Path yamlSchemaFileName;

    public YamlFileValidator(
            final Path yamlSchemaFileName,
            final YamlFileToJsonObjectConverter yamlFileToJsonObjectConverter) {
        this.yamlFileToJsonObjectConverter = yamlFileToJsonObjectConverter;
        this.yamlSchemaFileName = yamlSchemaFileName;
    }

    public abstract void validate(final Path path);


    protected  Schema schema() {
        try (final InputStream schemaFileStream = this.getClass().getResourceAsStream(yamlSchemaFileName.toString())) {
            return SchemaLoader.builder()
                    .schemaJson(new JSONObject(new JSONTokener(schemaFileStream)))
                    .build().load().build();
        } catch (final IOException ex) {
            throw new SubscriptionDescriptorException(format("Unable to load JSON schema %s from classpath", yamlSchemaFileName), ex);
        }
    }
}
