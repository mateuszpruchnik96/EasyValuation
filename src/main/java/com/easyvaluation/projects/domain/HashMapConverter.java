package com.easyvaluation.projects.domain;

import com.easyvaluation.materialslibrary.domain.Item;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.AttributeConverter;
import java.io.IOException;
import java.util.Map;

public class HashMapConverter implements AttributeConverter<Map<Item,Integer>, String> {

    ObjectMapper objectMapper;

    @Override
    public String convertToDatabaseColumn(Map<Item, Integer> items) {

        String itemsJSON =null;
        try {
            itemsJSON=objectMapper.writeValueAsString(items);
        } catch (final JsonProcessingException e){
            System.out.println("JSON writting error: " + e);
        }
        return itemsJSON;
    }

    @Override
    public Map<Item, Integer> convertToEntityAttribute(String itemsJSON) {

        Map<Item, Integer> items = null;
        try {
            items=objectMapper.readValue(itemsJSON, Map.class);
        } catch(final IOException e){
            System.out.println("JSON writting error: " + e);
        }
        return items;
    }
}