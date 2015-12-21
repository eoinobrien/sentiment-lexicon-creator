package com.eoinobrien.sentiment;

import org.elasticsearch.common.xcontent.XContentBuilder;

import java.io.IOException;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

/**
 * @author Eoin O'Brien on 21/12/15.
 */
public class DictionaryEntry {
    private String word;
    private double sentimentValue;
    private int numLexicons;

    public DictionaryEntry(String word) {
        this.word = word;
        sentimentValue = 0;
        numLexicons = 0;
    }

    public DictionaryEntry(String word, double sentimentValue) {
        this(word);
        addSentimentValue(sentimentValue);
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public double getSentimentValue() {
        return sentimentValue;
    }

    public void setSentimentValue(double sentimentValue) {
        this.sentimentValue = sentimentValue;
    }

    public void addSentimentValue(double newSentimentValue) {
        this.sentimentValue += newSentimentValue;
        numLexicons++;
    }

    @Override
    public String toString() {
        return "DictionaryEntry {word=" + word + ", sentimentValue=" + sentimentValue + ", numLexicons=" + numLexicons + "}";
    }

    public XContentBuilder getESJsonObject() throws IOException {
        return jsonBuilder()
                .startObject()
                .field("word", word)
                .field("score", sentimentValue)
                .field("numLexicons", numLexicons)
                .endObject();
    }
}
