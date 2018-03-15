package fr.poivey.tache3;

import org.apache.commons.io.FileUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Utility {

  public static List<Double[]> readCentroids(String filename) throws IOException {
    FileInputStream fileInputStream = new FileInputStream(filename);
    BufferedReader reader = new BufferedReader(new InputStreamReader(fileInputStream));
    return readData(reader);
  }

  private static List<Double[]> readData(BufferedReader reader) throws IOException {
    List<Double[]> centroids = new ArrayList<>();
    String line;
    try {
      while ((line = reader.readLine()) != null) {
        String[] values = line.split("\t");
        String[] temp = values[1].split(" ");
        Double[] centroid = new Double[2];
        centroid[0] = Double.parseDouble(temp[0]);
        centroid[1] = Double.parseDouble(temp[1]);
        centroids.add(centroid);
      }
    }
    finally {
      reader.close();
    }
    return centroids;
  }

  public static void replaceFileContent(String target, String fileWithContent) throws IOException {
    File centroidFile = new File(target);
    FileWriter fileWriter = new FileWriter(centroidFile, false);

    FileInputStream fileInputStream = new FileInputStream(fileWithContent);
    BufferedReader reader = new BufferedReader(new InputStreamReader(fileInputStream));
    String line;

    while ((line = reader.readLine()) != null) {
      fileWriter.write(line + "\n");
    }

    reader.close();
    fileWriter.close();
  }

  public static boolean checkContentEquality(String file1, String file2) throws IOException {
    return FileUtils.contentEquals(new File(file1), new File(file2));
  }

}
