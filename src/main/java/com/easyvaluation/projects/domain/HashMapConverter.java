package com.easyvaluation.projects.domain;

import com.easyvaluation.materialslibrary.domain.item.Item;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.io.IOException;
import java.util.Map;

@Converter
public class HashMapConverter implements AttributeConverter<Map<Long,Integer>, String> {
//    @Autowired
    ObjectMapper mapper = new ObjectMapper();

//    @Override
//    public String convertToDatabaseColumn(Map<Item, Integer> items) {
//
//        String itemsJSON = null;
//        try {
//            itemsJSON=mapper.writeValueAsString(items);
//        } catch (final JsonProcessingException e){
//            System.out.println("JSON writting error: " + e);
//        }
//        return itemsJSON;
//    }

    @Override
    public String convertToDatabaseColumn(Map<Long, Integer> items) {

        JSONObject jsonObject = new JSONObject();

        for (Map.Entry<Long, Integer> entry : items.entrySet()){
            String keyString = String.valueOf(entry.getKey());
//            String valueString = String.valueOf(entry.getValue());
            jsonObject.put(keyString, entry.getValue().intValue());
        }
//        String itemsString = jsonObject.toString();
//        itemsString = itemsString.replace("\\", "").substring(1, itemsString.length());
        return jsonObject.toString();
    }


    @Override
    public Map<Long, Integer> convertToEntityAttribute(String itemsJSON) {

        Map<Long, Integer> items = null;
        try {
            items=mapper.readValue(itemsJSON, Map.class);
        } catch(final IOException e){
            System.out.println("JSON reading error: " + e);
        }
        return items;
    }


}