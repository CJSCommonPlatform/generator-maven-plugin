package uk.gov.justice.maven.generator.io.files.parser.io;

import static java.util.Collections.singletonList;
import static org.reflections.util.ClasspathHelper.forClassLoader;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class UrlsToScanFinder {

    public List<URL> urlsToScan(final Path baseDir) throws MalformedURLException {

        if(shouldSearchOnClasspath(baseDir)) {
          return new ArrayList<>(forClassLoader());
        }

        return singletonList(baseDir.toUri().toURL());
    }

    private boolean shouldSearchOnClasspath(final Path baseDir) {
        return baseDir == null || baseDir.toString().contains("CLASSPATH");
    }
}
