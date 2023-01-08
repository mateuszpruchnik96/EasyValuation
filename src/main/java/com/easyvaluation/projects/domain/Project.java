package com.easyvaluation.projects.domain;

import com.easyvaluation.foundations.domain.BaseEntity;
import com.easyvaluation.materialslibrary.domain.item.Item;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    // CHANGE CONVERTER!!!!
    private Map<Long, Integer> items;

//    @ManyToOne
//    @JoinColumn(name="operation_id", referencedColumnName = "id")
//    @Embedded
    @ElementCollection
    @OrderColumn(name="INDEX")
    private List<Operation> operationList = new ArrayList<Operation>();

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

    public float totalPrice(){
        return 0F;
    }

    public Operation addOperation(Operation operation){
        this.operationList.add(operation);
        return operation;
    }


    public Map<String, String> generateItemsAsMapOfStrings(){
        Map<String, String> itemsMap = new HashMap<>();

        for (Map.Entry<Long, Integer> entry : this.items.entrySet()) {
            String keyString = String.valueOf(entry.getKey());
            String valueString = String.valueOf(entry.getValue());
            itemsMap.put(keyString, valueString);
        }

        return itemsMap;
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
