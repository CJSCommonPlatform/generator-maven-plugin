package uk.gov.justice.maven.generator.io.files.parser.subscriptiondescriptor;


import static java.nio.file.Paths.get;

import uk.gov.justice.domain.subscriptiondescriptor.SubscriptionDescriptor;
import uk.gov.justice.maven.generator.io.files.parser.FileParserFactory;
import uk.gov.justice.maven.generator.io.files.parser.YamlFileToJsonObjectConverter;

public class SubscriptionDescriptorFileParserFactory implements FileParserFactory<SubscriptionDescriptor> {


    @SuppressWarnings("squid:S1075")
    private static final String YAML_SCHEMA_FILE = "/json/schema/subscription-schema.json";

    @Override
    public SubscriptionDescriptorFileParser create() {
        return new SubscriptionDescriptorFileParser(
                new SubscriptionDescriptorYamlFileValidator(get(YAML_SCHEMA_FILE), new YamlFileToJsonObjectConverter()));
    }
}
