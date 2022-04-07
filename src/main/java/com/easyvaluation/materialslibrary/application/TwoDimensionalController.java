package com.easyvaluation.materialslibrary.application;

import com.easyvaluation.materialslibrary.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class TwoDimensionalController {

    @Autowired
    TwoDimensionalRepository twoDimensionalRepository;

    @Autowired
    TwoDimensionalService twoDimensionalService;

    @GetMapping("/materials/twodimensional/{id}")
    public ResponseEntity<TwoDimensional> getOne(@PathVariable("id") Long id){
        Optional<TwoDimensional> service = twoDimensionalRepository.findById(id);
        if (service.isPresent()){
            return ResponseEntity.ok(service.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/materials/twodimensional")
    public ResponseEntity<TwoDimensional> addNew(@RequestBody TwoDimensional material){
        return ResponseEntity.created(null).body(twoDimensionalService.save(material));
    }

  /*
  *@GetMapping("/materials")
  *  public ResponseEntity<List<TwoDimensional>> getMany(){
  *     return ResponseEntity.ok(twoDimensionalRepository.findAll());
  *}
  */

}
