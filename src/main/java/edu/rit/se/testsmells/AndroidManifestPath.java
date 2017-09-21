package edu.rit.se.testsmells;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.nio.file.Files.walk;

public class AndroidManifestPath {


    public static void getPaths() throws IOException {
        List<Path> projects = getProjectDirectories("C:\\Projects\\Android");
        List<Path> manifestFiles;
        Map<String, List<String>> appManifest = new HashMap<>();

        for (Path path : projects) {
            manifestFiles = getAndroidManifestFile(path.toAbsolutePath().toString());
            appManifest.put(path.toAbsolutePath().toString().split("\\\\")[3],
                    manifestFiles.stream().map(x -> x.toAbsolutePath().toString()).collect(Collectors.toList()));
        }

        FileWriter fileWriter = new FileWriter("Output_AndroidManifestPath.txt", false);
        for (Map.Entry<String, List<String>> entry : appManifest.entrySet()) {
            String key = entry.getKey();
            List<String> value = entry.getValue();
            for (String path : value) {
                fileWriter.write(key + "," + path);
                fileWriter.write(System.getProperty("line.separator"));
            }
        }
        fileWriter.close();

    }


    private static List<Path> getAndroidManifestFile(String root) throws IOException {
        File start = new File(root);

        return walk(start.toPath())
                .filter(Files::isRegularFile)
                .filter(ext -> ext.getFileName().toString().toLowerCase().equals("androidmanifest.xml"))
                .collect(Collectors.toList());

    }

    private static List<Path> getProjectDirectories(String root) throws IOException {
        File x = new File(root);

        List<Path> paths = walk(x.toPath(), 1)
                .filter(Files::isDirectory)
                .collect(Collectors.toList());

        paths.remove(0);

        return paths;
    }
}
