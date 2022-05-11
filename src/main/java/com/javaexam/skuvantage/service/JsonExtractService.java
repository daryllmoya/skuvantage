package com.javaexam.skuvantage.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.gson.Gson;
import com.javaexam.skuvantage.model.FilteredResult;
import com.javaexam.skuvantage.model.KeyValues;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

@AllArgsConstructor
@RequiredArgsConstructor
@Service
public class JsonExtractService {

    private FilteredResult filteredResult = new FilteredResult(null);
    private List<KeyValues> listKeyValues = new ArrayList<KeyValues>();
    private KeyValues keyValues = new KeyValues(null, null);
    private List<String> tempValues = new ArrayList<String>();

    public FilteredResult getKeyValues(String[] keyNames) {
        // Make sure to clear the objects first
        filteredResult.setKeyValues(null);
        listKeyValues.clear();
        // Retrieve the object response from test api and convert it to JSONObject
        RestTemplate restTemplate = new RestTemplate();
        Object object = restTemplate.getForObject("https://test-api.skulibrary.dev/getTestProductData", Object.class);
        String jsonInString = new Gson().toJson(object);
        JSONObject jsonObject = new JSONObject(jsonInString);
        // For every key name provided
        for (String key : keyNames) {
            // Clear the key values object and temp values per iteration
            keyValues = new KeyValues(null, null);
            tempValues = new ArrayList<String>();
            // Get the actual key value
            getKey(jsonObject, key);
            keyValues.setKey(key);
            keyValues.setValues(tempValues);
            listKeyValues.add(keyValues);
        }
        filteredResult.setKeyValues(listKeyValues);
        return filteredResult;
    }

    private void getKey(JSONObject json, String key) {
        boolean exists = json.has(key);
        Iterator<?> keys;
        String nextKeys;
        if (!exists) {
            keys = json.keys();
            while (keys.hasNext()) {
                nextKeys = (String) keys.next();
                try {
                    if (json.get(nextKeys) instanceof JSONObject) {
                        if (exists == false) {
                            // Recursive call to check inner json object types
                            getKey(json.getJSONObject(nextKeys), key);
                        }
                    } else if (json.get(nextKeys) instanceof JSONArray) {
                        JSONArray jsonarray = json.getJSONArray(nextKeys);
                        for (int i = 0; i < jsonarray.length(); i++) {
                            String jsonarrayString = jsonarray.get(i).toString();
                            JSONObject innerJSOn = new JSONObject(jsonarrayString);
                            if (exists == false) {
                                // Recursive call to check inner json object types
                                getKey(innerJSOn, key);
                            }
                        }
                    }
                } catch (Exception e) {
                }
            }
        } else {
            tempValues.add(json.get(key).toString());
        }
    }

}
