package com.eoinobrien.sentiment.lexiconsHandlers;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Eoin O'Brien on 21/12/15.
 */
public class SentiWordNetHandler extends Handler {

    public SentiWordNetHandler(String fileName) {
        super(fileName);
    }

    public Object getValues(String line) {
        if (line.charAt(0) == '#') {
            return null;
        }

        String[] columns = line.split("\t");

        double posScore = Double.parseDouble(columns[2]);
        double negScore = Double.parseDouble(columns[3]);
        double score = posScore >= negScore ? posScore : 0 - negScore;

        String[] words = columns[4].split(" ");

        if (words.length == 1) {
            return new AbstractMap.SimpleEntry<>(makeWordStandard(words[0]), score);
        }

        Map<String, Double> values = new HashMap<>();
        for (String word : words) {
            values.put(makeWordStandard(word), score);
        }
        return values;
    }

    private String makeWordStandard(String word){
        return word.substring(0, word.indexOf("#")).replaceAll("_", " ");
    }
}
