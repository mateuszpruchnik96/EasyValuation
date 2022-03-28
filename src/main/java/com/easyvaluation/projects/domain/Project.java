package com.easyvaluation.projects.domain;

import com.easyvaluation.foundations.domain.BaseEntity;
import com.easyvaluation.materialslibrary.domain.Item;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import lombok.*;

import javax.persistence.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name="project")
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class Project extends BaseEntity {

    private LocalDateTime openingProjectTime;

    private String itemsJSON;

    @Convert(converter = HashMapConverter.class)
    private Map<Long, Integer> items;

    public Project(){
        this.items = Maps.newHashMap();
        this.openingProjectTime = LocalDateTime.now();
    }

    public void setItems(Long itemId, Integer integer) {
        this.items.put(itemId, integer);
    }

    public void serializeProductItems() throws JsonProcessingException{
        ObjectMapper objectMapper = new ObjectMapper();
        this.itemsJSON = objectMapper.writeValueAsString(items);
    }

    public void deserializeProductItems() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        this.items = objectMapper.readValue(itemsJSON, HashMap.class);
    }

}
