package connection;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;

public class ControllerHelpers {
    public static Map<String, String> stringToMap(String jsonString) throws Exception {
        Map<String, String> result = new HashMap<>();

        // Assume jsonString is a valid JSON object for simplicity
        jsonString = jsonString.trim();
        if (jsonString.startsWith("{") && jsonString.endsWith("}")) {
            // Remove outer curly braces
            jsonString = jsonString.substring(1, jsonString.length() - 1);

            // Split the string into key-value pairs
            String[] keyValuePairs = jsonString.split(",");

            // Process each key-value pair
            for (String pair : keyValuePairs) {
                // Split the pair into key and value
                String[] keyValue = pair.split(":", 2);

                // Trim leading and trailing spaces from key and value
                String key = keyValue[0].trim();
                String value = keyValue[1].trim();

                // Handle values based on their types (string, number, boolean, etc.)
                // For simplicity, assume values are either strings or numbers
                if (value.startsWith("\"") && value.endsWith("\"")) {
                    // Value is a string, remove quotes
                    key = key.substring(1, key.length() - 1);
                    value = value.substring(1, value.length() - 1);
                    result.put(key, value);
                } else {
                    System.err.println("Invalid JSON format. Expected an object.");
                }
            }
        } else {
            System.err.println("Invalid JSON format. Expected an object.");
        }

        return result;
    }

    public static String arrayToString(int[] pixilBuffer) {
        return Arrays.toString(pixilBuffer);
    }

    public static int[] getIntArray(String arrString) {
        return Stream.of(arrString.substring(1, arrString.length() - 1).split(", "))
                     .mapToInt(e -> Integer.parseInt(e))
                     .toArray();
    }

    public static File returnSavedFile(String fileContent, String path, String filename) {
        File file = new File(path + "/" + filename);

        try {
            if (file.createNewFile()) {
                PrintWriter pr = new PrintWriter(new FileWriter(file));
                pr.write(fileContent);
                pr.close();
            } else {
                System.out.println("File already exists");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return file;
    }
    
    public static String mapToString(Map<String, String> map) {
        return "{" +
                map.entrySet().stream()
                    .map(entry -> "\"" + entry.getKey() + "\":\"" + entry.getValue() + "\"")
                    .collect(Collectors.joining(","))
                +
                "}";
    }

    // public static String mapToString(Map<String, String> map) {
    //     return map.toString();
    // }
    
    public static String readFile(String filePath) {
        StringBuilder stringBuilder = new StringBuilder();
        String line;
    
        try (BufferedReader reader = new BufferedReader(new FileReader(new File(filePath)))) {
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
                stringBuilder.append("\n");
            }
            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        String str = stringBuilder.toString();
        
        line = null;
        stringBuilder = null;

        return str;
    }
}
