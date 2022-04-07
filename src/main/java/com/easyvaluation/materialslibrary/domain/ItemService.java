package com.easyvaluation.materialslibrary.domain;

import com.easyvaluation.foundations.domain.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ItemService implements AbstractService<Item> {
    @Autowired
    ItemRepository itemRepository;


    @Override
    public Item save(Item entity) {
        entity = itemRepository.save(entity);
        return entity;
    }

    public List<Item> findItemsById(List<Long> ids) throws NoSuchFieldException {
        List<Item> items = itemRepository.findAllById(ids);
        if(!items.isEmpty()) {
            return items;
        } else {
            throw new NoSuchFieldException("No match");
        }
    }

    public List<String> findItemNamesByIds(List<Long> ids) throws NoSuchFieldException {
        List<String> names = new ArrayList<>();
        try {
            List<Item> items = findItemsById(ids);
            items.stream().forEach(item -> names.add(item.getItemName()));
        } catch (NoSuchFieldException e){
            throw e;
        }
        return names;
    }

    public List<AbstractMap.SimpleEntry<Long,String>> findItemsByFirstLetters(String letters){

        List<Item> list = itemRepository.findByItemNameStartsWithIgnoreCase(letters);
        List<AbstractMap.SimpleEntry<Long,String>> idNameList = new ArrayList<>();
        list.stream().forEach(item -> idNameList
                .add(new AbstractMap.SimpleEntry<>(Long.valueOf(item.getId()),item.getItemName())));
        return idNameList;
    }

    public List<AbstractMap.SimpleEntry<Long, String>> getItemsByType(String type) throws NoSuchFieldException {
        List<Item> service = itemRepository.findAllByType(type);

        if(!service.isEmpty()) {
            List<AbstractMap.SimpleEntry<Long, String>> itemIdNameList = new ArrayList<>();
            service.stream()
                    .forEach(item -> itemIdNameList.add(new AbstractMap.SimpleEntry(item.getId(), item.getItemName())));
            return itemIdNameList;
        } else {
            throw new NoSuchFieldException("No match");
        }
    }
}