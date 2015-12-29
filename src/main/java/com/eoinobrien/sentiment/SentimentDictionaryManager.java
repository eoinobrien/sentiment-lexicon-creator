package com.eoinobrien.sentiment;

import com.eoinobrien.sentiment.lexiconsHandlers.BingLiuHandler;
import com.eoinobrien.sentiment.lexiconsHandlers.MPQAHandler;
import com.eoinobrien.sentiment.lexiconsHandlers.SentiWordNetHandler;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;

import java.io.IOException;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;


/**
 * @author Eoin O'Brien on 21/12/15.
 */

public class SentimentDictionaryManager {

    public static void main(String[] args) {
        SentiWordNetHandler sentiWordNetHandler = new SentiWordNetHandler("/home/eoin/Dropbox/College/Forth_Year/CA400 - FYP/Project/Sentiment/SentiWordNet_3.0.0/swn/www/admin/dump/SentiWordNet_3.0.0_20130122.txt");
        sentiWordNetHandler.getSentimentDictionary();

        Map<String, DictionaryEntry> dictionary = new HashMap<>();
        mapToDictionary(sentiWordNetHandler.getSentimentDictionary(), dictionary);


        MPQAHandler mpqaHandler = new MPQAHandler("/home/eoin/Dropbox/College/Forth_Year/CA400 - FYP/Project/Sentiment/subjectivity_clues_hltemnlp05/subjclueslen1-HLTEMNLP05.tff");
        mapToDictionary(mpqaHandler.getSentimentDictionary(), dictionary);

        BingLiuHandler bingLiuHandler = new BingLiuHandler("/home/eoin/Dropbox/College/Forth_Year/CA400 - FYP/Project/Sentiment/opinion-lexicon-English/positive-words.txt", true);
        mapToDictionary(bingLiuHandler.getSentimentDictionary(), dictionary);
        bingLiuHandler = new BingLiuHandler("/home/eoin/Dropbox/College/Forth_Year/CA400 - FYP/Project/Sentiment/opinion-lexicon-English/negative-words.txt", false);
        mapToDictionary(bingLiuHandler.getSentimentDictionary(), dictionary);

        try {
            Client client = TransportClient.builder().build().addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("46.101.74.48"), 9300));
//            Node node = nodeBuilder().node();
//            Client client = node.client();

            BulkRequestBuilder bulkRequest = client.prepareBulk();
            int count = 0;
            System.out.println("\nSize: " + dictionary.size());
            for (Map.Entry<String, DictionaryEntry> entry : dictionary.entrySet()) {
                bulkRequest.add(client.prepareIndex("lexicon", "words", entry.getKey())
                        .setSource(entry.getValue().getESJsonObject()));

                count++;
                if(count % 5000 == 0 || dictionary.size() == count) {
                    //Splitting requests to prevent running out of memory
                    BulkResponse bulkResponse = bulkRequest.get();
                    if (bulkResponse.hasFailures()) {
                        bulkResponse.buildFailureMessage();
                    }
                    bulkRequest = client.prepareBulk();
                    System.out.println("Completed: " + count);
                }
            }

            client.close();
//            node.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void mapToDictionary(Map<String, Double> inputMap, Map<String, DictionaryEntry> dictionary) {
        for (Map.Entry<String, Double> entry : inputMap.entrySet()) {
            String key = entry.getKey();
            if (!dictionary.containsKey(key)) {
                dictionary.put(key, new DictionaryEntry(key, entry.getValue()));
            } else {
                DictionaryEntry dictionaryEntry = dictionary.get(key);
                dictionaryEntry.addSentimentValue(entry.getValue());
                dictionary.put(key, dictionaryEntry);
            }
        }
    }
}
