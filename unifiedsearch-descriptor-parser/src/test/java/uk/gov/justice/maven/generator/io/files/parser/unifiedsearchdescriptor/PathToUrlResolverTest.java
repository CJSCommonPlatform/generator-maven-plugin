package uk.gov.justice.maven.generator.io.files.parser.unifiedsearchdescriptor;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

import uk.gov.justice.maven.generator.io.files.parser.unifiedsearch.PathToUrlResolver;
import uk.gov.justice.maven.generator.io.files.parser.unifiedsearch.UnifiedSearchException;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Test;


public class PathToUrlResolverTest {

    @Test
    public void shouldResolvePathToUrl() {
        final Path baseDir = Paths.get("/yaml");
        final Path path = Paths.get("unified-search-descriptor.yaml");

        final URL url = new PathToUrlResolver().resolveToUrl(baseDir, path);

        assertThat(url.toString(), is("file:/yaml/unified-search-descriptor.yaml"));
    }

    @Test
    public void shouldThrowUnifiedSearchExceptionWhenResolutionFailsForPathToUrl() {

        try {

            final PathToUrlResolver pathToUrlResolver = spy(PathToUrlResolver.class);
            final Path basedir = mock(Path.class);
            final Path url = mock(Path.class);

            given(basedir.resolve(url)).willAnswer(invocation -> {
                throw new MalformedURLException("oops");
            });

            pathToUrlResolver.resolveToUrl(basedir, url);
            fail();
        } catch (final UnifiedSearchException expected) {
            assertThat(expected.getMessage(), is("Cannot resolve path as URL null"));

        }
    }
}