package com.easyvaluation.materialslibrary.domain.item;

import com.easyvaluation.foundations.domain.BaseEntity;
import com.easyvaluation.projects.domain.Project;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "item")
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TYPE", discriminatorType = DiscriminatorType.STRING)
public abstract class Item extends BaseEntity {

    @Column(name = "TYPE", unique = true, nullable = false, insertable = false,updatable = false)
    @Enumerated(EnumType.STRING)
    ItemType type;

//    @ManyToOne(fetch = FetchType.EAGER)
//    @JoinColumn(name = "project_id")
//    Project project;

    @Column(name = "item_name")
    String itemName;

    float price;

    String description;


    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

}
