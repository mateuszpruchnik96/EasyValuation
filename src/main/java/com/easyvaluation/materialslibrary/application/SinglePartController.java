package com.easyvaluation.materialslibrary.application;

import com.easyvaluation.materialslibrary.domain.SinglePart;
import com.easyvaluation.materialslibrary.domain.SinglePartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class SinglePartController {

    @Autowired
    SinglePartRepository singlePartRepository;

    @CrossOrigin
    @GetMapping("/materials/singlepart/{id}")
    public ResponseEntity<SinglePart> getOne(@PathVariable("id") Long id){
        Optional<SinglePart> service = singlePartRepository.findById(id);
        if (service.isPresent()){
            return ResponseEntity.ok(service.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @CrossOrigin
    @GetMapping("/materials/singlepart")
    public ResponseEntity<List<SinglePart>> getMany(){
            return ResponseEntity.ok(singlePartRepository.findAll());
    }

    @CrossOrigin
    @PostMapping("/materials/singlepart")
    public ResponseEntity<SinglePart> addNew(@RequestBody SinglePart singlePart){
           return ResponseEntity.created(null).body(singlePartRepository.save(singlePart));
    }

}
