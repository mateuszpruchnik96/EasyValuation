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
        return null;
    }

    public List<Item> getItemsById(List<Long> ids){

        return null;
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