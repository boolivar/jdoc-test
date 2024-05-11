package org.bool.jdoc.gradle.spock;

import org.bool.jdoc.spock.ResourceContainer;

import org.gradle.api.file.FileCollection;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;

public class UrlClassLoaderFactory {

    public ResourceContainer<ClassLoader> createClassLoader(FileCollection files) {
        URL[] urls = files.getFiles().stream()
                .map(File::toURI).map(this::url).toArray(URL[]::new);
        return new ResourceContainer<>(new URLClassLoader(urls, null)); 
    }

    private URL url(URI uri) {
        try {
            return uri.toURL();
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("Invalid URI: " + uri, e);
        }
    }
}
