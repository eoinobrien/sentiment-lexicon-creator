package com.eoinobrien.sentiment.lexiconsHandlers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Eoin O'Brien on 21/12/15.
 */
abstract public class Handler {
    private String fileName;
    private HashMap<String, Double> sentimentValues;

    public Handler(String fileName) {
        this.fileName = fileName;
        this.sentimentValues = new HashMap<>();
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public File getFile() throws IOException {
        return new File(fileName);
    }

    public Map<String, Double> getSentimentDictionary() {
        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new FileReader(getFile()));
            String line = null;

            while ((line = reader.readLine()) != null) {
                Object wordsObject = getValues(line);
                if(wordsObject != null) {
                    if(wordsObject instanceof Map) {
                        sentimentValues.putAll((Map<String, Double>) wordsObject);
                    } else if (wordsObject instanceof AbstractMap.SimpleEntry){
                        AbstractMap.SimpleEntry wordsFromLine = (AbstractMap.SimpleEntry) wordsObject;
                        sentimentValues.put((String) wordsFromLine.getKey(), (Double) wordsFromLine.getValue());
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return sentimentValues;
    }

    abstract Object getValues(String line);
}
