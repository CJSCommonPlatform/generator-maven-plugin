package uk.gov.justice.maven.generator.io.files.parser.io;

import static com.google.common.base.Predicates.and;
import static com.google.common.base.Predicates.not;
import static com.google.common.base.Predicates.or;
import static java.util.Arrays.asList;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import com.google.common.base.Predicate;
import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

/**
 * Utility class for searching directories.
 */
public class FileTreeScanner {

    private static final String RAML_PATTERN = "**/*.raml";

    /**
     * Finding all files within a directory that fulfil a set of include and exclude patterns, using standard
     * Ant patterns - {@see http://ant.apache.org/manual/dirtasks.html#patterns}.
     *
     * @param urlsToScan  Either the base directory or a list of URLs from the classpath
     * @param includes the path patterns to include
     * @param excludes the path patterns to exclude
     * @return a list of paths to matching files under the specified directory
     */
    public Collection<Path> find(final List<URL> urlsToScan, final String[] includes, final String[] excludes) throws IOException {
        
        final ConfigurationBuilder configuration = new ConfigurationBuilder()
                .filterInputsBy(filterOf(includes, excludes))
                .setUrls(urlsToScan)
                .setScanners(new ResourcesScanner());

        final Reflections reflections = new Reflections(configuration);
        final Set<String> resources = reflections.getResources(Pattern.compile(".*"));

        return resources.stream().map(Paths::get).collect(toList());
    }

    private Predicate<String> filterOf(final String[] includes, final String[] excludes) {
        final List<AntPathMatcher> includesMatcher = stream(includes).map(AntPathMatcher::new).collect(toList());
        final Predicate<String> includesFilter = notEmpty(includes)
                ? or(includesMatcher)
                : new AntPathMatcher(RAML_PATTERN);

        final List<Predicate<String>> excludesMatcher = stream(excludes).map(i -> not(new AntPathMatcher(i))).collect(toList());
        final Predicate<String> excludesFilter = and(excludesMatcher);

        return and(includesFilter, excludesFilter);
    }

    private boolean notEmpty(final String[] includes) {
        return includes != null && includes.length > 0;
    }

}
