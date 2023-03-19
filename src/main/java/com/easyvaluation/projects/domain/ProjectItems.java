package com.easyvaluation.projects.domain;

import com.easyvaluation.foundations.domain.BaseEntity;
import com.easyvaluation.materialslibrary.domain.item.Item;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "PROJECT_ITEMS")
@Getter
@Setter
@NoArgsConstructor
public class ProjectItems extends BaseEntity {

    public ProjectItems(Item item, float quantity){
        this.item = item;
        this.quantity = quantity;
    }

    @ManyToOne
    @JoinColumn(name = "project_id")
    @JsonIgnoreProperties("projectItems")
    private Project project;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;

    @Column(name = "quantity")
    private float quantity;

    @Column(name = "added")
    private LocalDateTime added;

}
