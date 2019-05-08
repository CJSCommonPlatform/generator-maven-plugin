package uk.gov.justice.maven.generator.io.files.parser.unifiedsearch;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.*;

import org.junit.Test;

public class UnifiedSearchExceptionTest {
    @Test
    public void shouldCreateInstanceOfUnifiedSearchExceptionWithMessage() throws Exception {
        final UnifiedSearchException exception = new UnifiedSearchException("Test message", new Exception());
        assertThat(exception.getMessage(), is("Test message"));
        assertThat(exception, instanceOf(Exception.class));
    }

}