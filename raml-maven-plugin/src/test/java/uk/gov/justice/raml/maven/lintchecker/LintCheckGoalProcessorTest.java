package uk.gov.justice.raml.maven.lintchecker;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import uk.gov.justice.raml.io.FileTreeScanner;
import uk.gov.justice.raml.io.files.parser.RamlFileParser;

import java.io.File;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;

import org.apache.maven.plugin.MojoExecutionException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.raml.model.Raml;

@RunWith(MockitoJUnitRunner.class)
public class LintCheckGoalProcessorTest {

    @Mock
    private RamlFileParser ramlFileParser;

    @Mock
    private FileTreeScanner fileTreeScanner;

    @InjectMocks
    private LintCheckGoalProcessor lintCheckGoalProcessor;

    @Test
    public void shouldExecuteAllRulesOnAllRamls() throws Exception {

        final LintCheckRule lintCheckRule_1 = mock(LintCheckRule.class);
        final LintCheckRule lintCheckRule_2 = mock(LintCheckRule.class);
        final Raml raml_1 = mock(Raml.class);
        final Raml raml_2 = mock(Raml.class);

        final Collection<Raml> ramls = asList(raml_1, raml_2);
        final Collection<Path> paths = singletonList(mock(Path.class));

        final File sourceDirectory = new File(("."));
        final List<LintCheckRule> rules = asList(lintCheckRule_1, lintCheckRule_2);
        final String[] excludes = { "exclude_1" };
        final String[] includes = { "include_1" };

        when(fileTreeScanner.find(sourceDirectory.toPath(), includes, excludes)).thenReturn(paths);
        when(ramlFileParser.ramlOf(sourceDirectory.toPath(), paths)).thenReturn(ramls);

        lintCheckGoalProcessor.execute(sourceDirectory, rules, asList(includes), asList(excludes));

        verify(lintCheckRule_1).execute(eq(raml_1), any(LintCheckConfiguration.class));
        verify(lintCheckRule_1).execute(eq(raml_2), any(LintCheckConfiguration.class));
        verify(lintCheckRule_2).execute(eq(raml_1), any(LintCheckConfiguration.class));
        verify(lintCheckRule_2).execute(eq(raml_2), any(LintCheckConfiguration.class));
    }

    @Test
    public void shouldThrowMojoExecutionExceptionAIfExecutingARuleFails() throws Exception {

        final LintCheckerException lintCheckerException = new LintCheckerException("Ooops");

        final LintCheckRule lintCheckRule_1 = mock(LintCheckRule.class);
        final LintCheckRule lintCheckRule_2 = mock(LintCheckRule.class);
        final Raml raml_1 = mock(Raml.class);
        final Raml raml_2 = mock(Raml.class);

        final Collection<Raml> ramls = asList(raml_1, raml_2);
        final Collection<Path> paths = singletonList(mock(Path.class));

        final File sourceDirectory = new File(("."));
        final List<LintCheckRule> rules = asList(lintCheckRule_1, lintCheckRule_2);
        final String[] excludes = { "exclude_1" };
        final String[] includes = { "include_1" };

        when(fileTreeScanner.find(sourceDirectory.toPath(), includes, excludes)).thenReturn(paths);
        when(ramlFileParser.ramlOf(sourceDirectory.toPath(), paths)).thenReturn(ramls);

        doThrow(lintCheckerException).when(lintCheckRule_1).execute(eq(raml_1), any(LintCheckConfiguration.class));

        try {
            lintCheckGoalProcessor.execute(sourceDirectory, rules, asList(includes), asList(excludes));
            fail();
        } catch (final MojoExecutionException expected) {
            assertThat(expected.getCause(), is(lintCheckerException));
            assertThat(expected.getMessage(), is("Lint checker rule failed for rule " + lintCheckRule_1.getClass().getSimpleName()));
        }

        verify(lintCheckRule_1).execute(eq(raml_1), any(LintCheckConfiguration.class));
    }
}
