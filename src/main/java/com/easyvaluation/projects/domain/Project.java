package com.easyvaluation.projects.domain;

import com.easyvaluation.foundations.domain.BaseEntity;
import com.easyvaluation.materialslibrary.domain.item.Item;
import com.easyvaluation.security.domain.UserAccount;
import com.easyvaluation.security.domain.UserRole;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.collect.Maps;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

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

    @OneToMany(mappedBy = "project", orphanRemoval = true, cascade = CascadeType.ALL)
    @JsonIgnoreProperties("project")
    private List<ProjectItems> projectItems = new ArrayList<>();

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
        this.projectItems= new ArrayList<>();
    }

    public void addItem(Long itemId, Integer integer) throws JsonProcessingException {
        this.items.put(itemId, integer);
    }

    public void addProjectItem(ProjectItems projectItem) {
        projectItems.add(projectItem);
        projectItem.setProject(this);
    }

    public void removeProjectItem(ProjectItems projectItem) {
        projectItems.remove(projectItem);
        projectItem.setProject(null);
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
