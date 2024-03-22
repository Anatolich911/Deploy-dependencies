package com.hoopladigital.test;

import org.apache.commons.io.IOUtils;

import java.io.*;
import java.net.URL;

public class FileTestHelper {

    public String getResourceAsString(final String resourceName) {

        final InputStream inputStream = getClassLoader().getResourceAsStream(resourceName);
        try (final ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            IOUtils.copy(inputStream, outputStream);
            return new String(outputStream.toByteArray(), "UTF-8");
        } catch (final Exception e) {
            throw new RuntimeException(e.toString(), e);
        }

    }

    protected File getResourceAsFile(final String resourceName) {
        final URL url = getClassLoader().getResource(resourceName);
        if (null == url) {
            return null;
        }
        return new File(url.getFile());
    }

    private ClassLoader getClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }

}
