package com.easyvaluation.materialslibrary.application;

import com.easyvaluation.materialslibrary.domain.Item;
import com.easyvaluation.materialslibrary.domain.ItemRepository;
import com.easyvaluation.materialslibrary.domain.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class ItemController{
    @Autowired
    ItemService itemService;

    @Autowired
    ItemRepository itemRepository;

    @GetMapping("/items/{id}")
    public ResponseEntity<Item> getOne(@PathVariable("id") Long id){
        Optional<Item> service= itemRepository.findById(id);
        if(service.isPresent()){
            return ResponseEntity.ok(service.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
