package com.easyvaluation.materialslibrary.application;

import com.easyvaluation.materialslibrary.domain.item.Item;
import com.easyvaluation.materialslibrary.domain.item.ItemRepository;
import com.easyvaluation.materialslibrary.domain.item.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.AbstractMap;
import java.util.List;
import java.util.Optional;

@RestController
public class ItemController{
    @Autowired
    ItemService itemService;

    @Autowired
    ItemRepository itemRepository;

    @GetMapping("/materials/items/{id}")
    public ResponseEntity<Item> getOne(@PathVariable("id") Long id){
        Optional<Item> service= itemRepository.findById(id);
        if(service.isPresent()){
            return ResponseEntity.ok(service.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/materials/items")
    public ResponseEntity<List<AbstractMap.SimpleEntry<Long,String>>> getItemsByName(@RequestParam(value="name") String name){
        Optional<List<AbstractMap.SimpleEntry<Long,String>>> service = Optional.of(itemService.findItemsByFirstLetters(name));
        if(service.isPresent()){
            return ResponseEntity.ok(service.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/materials/items")
    public ResponseEntity<Item> addNew(@RequestBody Item item){
        return ResponseEntity.created(null).body(itemRepository.save(item));
    }

}
