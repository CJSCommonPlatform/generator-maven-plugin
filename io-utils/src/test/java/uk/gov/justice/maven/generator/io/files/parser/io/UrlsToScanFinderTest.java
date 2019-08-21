package uk.gov.justice.maven.generator.io.files.parser.io;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.junit.Assert.*;

import org.junit.Rule;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;
import static org.reflections.util.ClasspathHelper.forClassLoader;

import java.io.File;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class UrlsToScanFinderTest {

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @InjectMocks
    private UrlsToScanFinder urlsToScanFinder;

    @Test
    public void shouldReturnJustTheBaseDirectoryIfTheBaseDirectoryIsNotNullAndNotCLASSPATH() throws Exception {

        final File baseDir = temporaryFolder.getRoot();

        assertThat(baseDir.exists(), is(true));

        final List<URL> urlsToScan = urlsToScanFinder.urlsToScan(baseDir.toPath());

        assertThat(urlsToScan.size(), is(1));
        assertThat(urlsToScan.get(0).toString(), is(baseDir.toURI().toString()));
    }

    @Test
    public void shouldReturnAllUrlsOnTheClasspathIfTheBaseDirectoryIsNull() throws Exception {

        final Path baseDir = null;
        final List<URL> urlsToScan = urlsToScanFinder.urlsToScan(baseDir);

        final Collection<URL> urlsFromClasspath = forClassLoader();

        assertThat(urlsToScan.size(), is(urlsFromClasspath.size()));
        assertThat(urlsToScan, hasItems(urlsFromClasspath.toArray(new URL[0])));
    }

    @Test
    public void shouldReturnAllUrlsOnTheClasspathIfTheBaseDirectoryIsCLASSPATH() throws Exception {

        final Path baseDir = Paths.get("CLASSPATH");
        final List<URL> urlsToScan = urlsToScanFinder.urlsToScan(baseDir);

        final Collection<URL> urlsFromClasspath = forClassLoader();

        assertThat(urlsToScan.size(), is(urlsFromClasspath.size()));
        assertThat(urlsToScan, hasItems(urlsFromClasspath.toArray(new URL[0])));
    }
}
