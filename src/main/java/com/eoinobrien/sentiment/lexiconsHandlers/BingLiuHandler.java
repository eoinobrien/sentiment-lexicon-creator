package com.eoinobrien.sentiment.lexiconsHandlers;

import java.util.AbstractMap;

/**
 * @author Eoin O'Brien on 21/12/15.
 */
public class BingLiuHandler extends Handler{
    private boolean isPositive;

    public BingLiuHandler(String fileName, boolean isPositive){
        super(fileName);
        this.isPositive = isPositive;
    }

    public AbstractMap.SimpleEntry<String, Double> getValues(String line){
        if(line.length() == 0 || line.charAt(0) == ';') {
            return null;
        }

        double score = -1;

        if(isPositive){
            score = 1;
        }

        return new AbstractMap.SimpleEntry<>(line, score);
    }
}
