package com.opp.util;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Created by ctobe on 9/15/16.
 */
public class FileUtil {

    public static boolean writeToFile(String filePath, String contents){
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(filePath))) {
            writer.write(contents);
        } catch (IOException ioEx){
            ioEx.printStackTrace();
            return false;
        }
        return true;
    }

    public static List<String> getFilesFromDirectoryRecusively(String dirPath, String matcher){
        List<String> fileList = new ArrayList<>();
        PathMatcher pathMatcher = FileSystems.getDefault().getPathMatcher(matcher);
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(dirPath))) {
            stream.forEach(pathEntry -> {
                System.out.println(pathEntry.getFileName() + " | " + pathEntry.toFile().isDirectory());
                if(!pathEntry.toFile().isDirectory() && pathMatcher.matches(pathEntry)){
                    fileList.add(pathEntry.toString());
                }
            });
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return fileList;
    }

    public static Stream<String> readGzipFile(Path path) {
        InputStream fileIs = null;
        BufferedInputStream bufferedIs = null;
        GZIPInputStream gzipIs = null;
        try {
            fileIs = Files.newInputStream(path);
            // Even though GZIPInputStream has a buffer it reads individual bytes
            // when processing the header, better add a buffer in-between
            bufferedIs = new BufferedInputStream(fileIs, 65535);
            gzipIs = new GZIPInputStream(bufferedIs);
        } catch (IOException e) {
            closeSafely(gzipIs);
            closeSafely(bufferedIs);
            closeSafely(fileIs);
            throw new UncheckedIOException(e);
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(gzipIs));
        return reader.lines().onClose(() -> closeSafely(reader));
    }

    public static List<List<String>> readCsv(String filename) throws IOException {
        Path path = Paths.get(filename);
        List<List<String>> csvFile = Files.readAllLines(path)
                .stream()
                .map(line -> Arrays.asList(line.split(",")))
                .collect(Collectors.toList());

        return csvFile;
    }

    private static void closeSafely(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                // Ignore
            }
        }
    }

    public static void unGzipFile(String gzipFile, String newFile) {
        try {
            FileInputStream fis = new FileInputStream(gzipFile);
            GZIPInputStream gis = new GZIPInputStream(fis);
            FileOutputStream fos = new FileOutputStream(newFile);
            byte[] buffer = new byte[1024];
            int len;
            while((len = gis.read(buffer)) != -1){
                fos.write(buffer, 0, len);
            }
            //close resources
            fos.close();
            gis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void gzipStream(FileInputStream fis, String outputGzFile){
        try {
            FileOutputStream fos = new FileOutputStream(outputGzFile);
            GZIPOutputStream gzipOS = new GZIPOutputStream(fos);
            byte[] buffer = new byte[1024];
            int len;
            while((len=fis.read(buffer)) != -1){
                gzipOS.write(buffer, 0, len);
            }
            //close resources
            gzipOS.close();
            fos.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void gzipFile(String inputFile, String outputGzFile) {
        try {
            FileInputStream fis = new FileInputStream(inputFile);
            gzipStream(fis, outputGzFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
