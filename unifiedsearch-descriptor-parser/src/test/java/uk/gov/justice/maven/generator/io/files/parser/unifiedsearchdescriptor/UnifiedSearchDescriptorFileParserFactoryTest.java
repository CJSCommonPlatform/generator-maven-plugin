package uk.gov.justice.maven.generator.io.files.parser.unifiedsearchdescriptor;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import uk.gov.justice.domain.unifiedsearchdescriptor.UnifiedSearchDescriptor;
import uk.gov.justice.maven.generator.io.files.parser.FileParser;
import uk.gov.justice.maven.generator.io.files.parser.unifiedsearch.UnifiedSearchDescriptorFileParserFactory;
import uk.gov.justice.maven.generator.io.files.parser.unifiedsearch.UnifiedSearchDescriptorParser;

import org.junit.Test;

public class UnifiedSearchDescriptorFileParserFactoryTest {

    @Test
    public void shouldCreateUninfiedSearchDescriptorParser() throws Exception {

        final FileParser<UnifiedSearchDescriptor> unifiedSearchDescriptorFileParser = new UnifiedSearchDescriptorFileParserFactory().create();

        assertThat(unifiedSearchDescriptorFileParser, is(instanceOf(UnifiedSearchDescriptorParser.class)));
    }

}