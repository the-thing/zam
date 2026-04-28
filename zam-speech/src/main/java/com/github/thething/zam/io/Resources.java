package com.github.thething.zam.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public final class Resources {

    private static final Charset DEFAULT_CHARSET = Charset.defaultCharset();

    private Resources() {
    }

    public static InputStream getResourceAsStream(String name) throws IOException {
        InputStream in = Resources.class.getClassLoader().getResourceAsStream(name);

        if (in == null) {
            throw new IOException("Unable to find resource: " + name);
        }

        return in;
    }

    public static void forEachLine(String name, LineProcessor processor) throws IOException {
        forEachLine(name, DEFAULT_CHARSET, processor);
    }

    public static void forEachLine(String name, Charset charset, LineProcessor processor) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(getResourceAsStream(name), charset))) {
            forEachLine(reader, processor);
        }
    }

    public static void forEachLine(Reader reader, LineProcessor processor) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(reader);

        String line;
        int lineNumber = 1;

        while ((line = bufferedReader.readLine()) != null) {
            processor.accept(lineNumber, line);
            lineNumber++;
        }
    }

    public static List<String> readLines(String name) throws IOException {
        return readLines(name, DEFAULT_CHARSET);
    }

    public static List<String> readLines(String name, Charset charset) throws IOException {
        List<String> lines = new ArrayList<>();
        readLines(name, charset, lines);
        return lines;
    }

    public static void readLines(String name, Charset charset, List<String> out) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(Resources.getResourceAsStream(name), charset))) {
            String line;

            while ((line = reader.readLine()) != null) {
                out.add(line);
            }
        }
    }

    public interface LineProcessor {

        void accept(int lineNumber, String line) throws IOException;
    }
}
