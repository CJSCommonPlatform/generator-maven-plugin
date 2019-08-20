package uk.gov.justice.maven.generator.io.files.parser.io;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;

import org.junit.Test;

/**
 * Unit tests for the {@link FileTreeScanner} class.
 */
public class FileTreeScannerTest {

    @Test
    @SuppressWarnings("unchecked")
    public void shouldFindMatchingFilesInBaseDir() throws Exception {

        final String[] includes = {"**/*.raml"};
        final String[] excludes = {"**/*ignore.raml"};
        final Path baseDir = Paths.get(getRamlDirectory());
        final List<URL> urlsToScan = new UrlsToScanFinder().urlsToScan(baseDir);

        final FileTreeScanner fileTreeResolver = new FileTreeScanner();

        final Collection<Path> paths = fileTreeResolver.find(urlsToScan, includes, excludes);

        assertThat(paths, hasSize(3));
        assertThat(paths, containsInAnyOrder(
                equalTo(Paths.get("example-1.raml")),
                equalTo(Paths.get("example-2.raml")),
                equalTo(Paths.get("subfolder/example-3.raml"))));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldFindMatchingFilesInClasspathIfBaseDirSetToCLASSPATH() throws Exception {

        final String[] includes = {"**/*.raml"};
        final String[] excludes = {"**/*ignore.raml"};
        final Path baseDir = Paths.get("CLASSPATH");

        final FileTreeScanner fileTreeResolver = new FileTreeScanner();

        final List<URL> urlsToScan = new UrlsToScanFinder().urlsToScan(baseDir);
        final Collection<Path> paths = fileTreeResolver.find(urlsToScan, includes, excludes);

        assertThat(paths, containsInAnyOrder(
                equalTo(Paths.get("raml/external-3.raml")),
                equalTo(Paths.get("raml/external-4.raml")),
                equalTo(Paths.get("raml/example-1.raml")),
                equalTo(Paths.get("raml/example-2.raml")),
                equalTo(Paths.get("raml/subfolder/example-3.raml"))
        ));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldFindMatchingFilesInClasspathIfBaseDirNULL() throws Exception {

        final String[] includes = {"**/*.raml"};
        final String[] excludes = {"**/*ignore.raml"};

        final FileTreeScanner fileTreeResolver = new FileTreeScanner();
        final List<URL> urlsToScan = new UrlsToScanFinder().urlsToScan(null);
        final Collection<Path> paths = fileTreeResolver.find(urlsToScan, includes, excludes);

        assertThat(paths, hasSize(5));
        assertThat(paths, containsInAnyOrder(
                equalTo(Paths.get("raml/external-3.raml")),
                equalTo(Paths.get("raml/external-4.raml")),
                equalTo(Paths.get("raml/example-1.raml")),
                equalTo(Paths.get("raml/example-2.raml")),
                equalTo(Paths.get("raml/subfolder/example-3.raml"))));
    }


    @Test
    @SuppressWarnings("unchecked")
    public void shouldIncludeMultipleFiles() throws Exception {

        final String[] includes = {"**/*example-1.raml", "**/*example-2.raml"};
        final String[] excludes = {};

        final Path baseDir = Paths.get(getRamlDirectory());
        final FileTreeScanner fileTreeResolver = new FileTreeScanner();
        final List<URL> urlsToScan = new UrlsToScanFinder().urlsToScan(baseDir);
        final Collection<Path> paths = fileTreeResolver.find(urlsToScan, includes, excludes);

        assertThat(paths, hasSize(2));
        assertThat(paths, containsInAnyOrder(
                equalTo(Paths.get("example-1.raml")),
                equalTo(Paths.get("example-2.raml"))));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldIncludeRamlFilesByDefaultIfNoIncludesSpecified() throws Exception {

        final String[] includes = {};
        final String[] excludes = {};

        final Path baseDir = Paths.get(getRamlDirectory());

        final FileTreeScanner fileTreeResolver = new FileTreeScanner();

        final List<URL> urlsToScan = new UrlsToScanFinder().urlsToScan(baseDir);
        final Collection<Path> paths = fileTreeResolver.find(urlsToScan, includes, excludes);

        assertThat(paths, hasSize(4));
        assertThat(paths, containsInAnyOrder(
                equalTo(Paths.get("ignore.raml")),
                equalTo(Paths.get("subfolder/example-3.raml")),
                equalTo(Paths.get("example-1.raml")),
                equalTo(Paths.get("example-2.raml"))));

    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldExcludeMultipleFiles() throws Exception {

        final String[] includes = {"**/*raml"};
        final String[] excludes = {"**/*ignore.raml", "**/*example-2.raml"};
        final Path baseDir = Paths.get(getRamlDirectory());

        final FileTreeScanner fileTreeResolver = new FileTreeScanner();

        final List<URL> urlsToScan = new UrlsToScanFinder().urlsToScan(baseDir);
        final Collection<Path> paths = fileTreeResolver.find(urlsToScan, includes, excludes);

        assertThat(paths, hasSize(2));
        assertThat(paths, containsInAnyOrder(
                equalTo(Paths.get("example-1.raml")),
                equalTo(Paths.get("subfolder/example-3.raml"))));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldReturnEmptyCollectionIfBaseDirDoesNotExist() throws Exception {

        final String[] includes = {"**/*.messaging.raml"};
        final String[] excludes = {"**/*external-ignore.raml"};
        final Path baseDir = Paths.get("C:\\workspace-moj\\raml-maven\\raml-maven-plugin-it\\src\\raml");

        final FileTreeScanner fileTreeResolver = new FileTreeScanner();
        final List<URL> urlsToScan = new UrlsToScanFinder().urlsToScan(baseDir);
        final Collection<Path> paths = fileTreeResolver.find(urlsToScan, includes, excludes);

        assertThat(paths, empty());
    }

    private URI getRamlDirectory() throws URISyntaxException {
        return getClass().getClassLoader().getResource("raml").toURI();
    }
}
