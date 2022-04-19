package com.easyvaluation.projects.domain;

import com.easyvaluation.foundations.domain.BaseEntity;
import com.easyvaluation.security.domain.UserAccount;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import lombok.*;

import javax.persistence.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name="project")
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class Project extends BaseEntity {

    private String name;

    private String description;

    private LocalDateTime openingProjectTime;


    private String itemsJSON;

    @Convert(converter = HashMapConverter.class)
//    @JsonIgnore
    private Map<Long, Integer> items;

    @ManyToOne
    @JoinColumn(name="user_account_id", referencedColumnName = "id")
    @JsonIgnore
    UserAccount userAccount;

    public Project(){
        this.items = Maps.newHashMap();
        this.openingProjectTime = LocalDateTime.now();
    }

    public void addItem(Long itemId, Integer integer) throws JsonProcessingException {
        this.items.put(itemId, integer);
//        this.serializeProductItems();
    }

//    @JsonIgnore
//    @JsonProperty(value = "items")
    public String getItems() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(items);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }


//    public Map<String, String> getItems(){
//        Map<String, String> itemsString = new HashMap<String, String>();
//        this.items.forEach((aLong, integer) -> itemsString.put(String.valueOf(aLong), String.valueOf(integer)));
//        return itemsString;
//    }

    public void serializeProductItems() throws JsonProcessingException{
        ObjectMapper objectMapper = new ObjectMapper();
        this.itemsJSON = objectMapper.writeValueAsString(items);
    }

    public void deserializeProductItems() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        this.items = objectMapper.readValue(itemsJSON, HashMap.class);
    }

}
