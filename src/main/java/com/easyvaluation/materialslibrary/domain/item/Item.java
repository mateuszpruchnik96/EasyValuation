package com.easyvaluation.materialslibrary.domain.item;

import com.easyvaluation.foundations.domain.BaseEntity;
import com.easyvaluation.projects.domain.Project;
import com.easyvaluation.projects.domain.ProjectItems;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.AbstractMap;
import java.util.Map;
import java.util.Set;

@Entity
@Table(name = "item"
        , uniqueConstraints = { @UniqueConstraint(columnNames = { "producer", "symbol" }) }
)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TYPE", discriminatorType = DiscriminatorType.STRING)
public abstract class Item extends BaseEntity {

    @Column(name = "TYPE", unique = true, nullable = false, insertable = false,updatable = false)
    @Enumerated(EnumType.STRING)
    ItemType type;

    Boolean custom;

//    @ManyToOne(fetch = FetchType.EAGER)
//    @JoinColumn(name = "project_id")
//    Project project;

//    @OneToMany(mappedBy = "item")
//    private Set<ProjectItems> projectItems;

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
