package com.easyvaluation.projects.domain;

import com.easyvaluation.foundations.domain.BaseEntity;
import com.easyvaluation.materialslibrary.domain.item.Item;
import com.easyvaluation.security.domain.UserAccount;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

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

    @Convert(converter = HashMapConverter.class)
    private Map<Long, Integer> items;

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

}
