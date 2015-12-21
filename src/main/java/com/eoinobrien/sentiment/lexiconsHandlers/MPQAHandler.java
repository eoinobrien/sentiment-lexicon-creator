package com.eoinobrien.sentiment.lexiconsHandlers;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Eoin O'Brien on 21/12/15.
 */
public class MPQAHandler extends Handler {

    public MPQAHandler(String fileName){
        super(fileName);
    }

    public AbstractMap.SimpleEntry<String, Double> getValues(String line){
        Map<String, String> wordDetails = convert(line);

        double score = 0;

        if(wordDetails.get("priorpolarity").equals("positive")){
            score = 0.5;
        } else if (wordDetails.get("priorpolarity").equals("negative")){
            score = -0.5;
        }

        if(wordDetails.get("type").equals("strongsubj")){
            score = score * 2;
        }

        return new AbstractMap.SimpleEntry<>(wordDetails.get("word1"), score);
    }

    private static Map<String, String> convert(String str) {
        String[] tokens = str.split(" |=");
        Map<String, String> map = new HashMap<>();
        for (int i=0; i<tokens.length-1; ) {
            map.put(tokens[i++], tokens[i++]);
        }
        return map;
    }
}
