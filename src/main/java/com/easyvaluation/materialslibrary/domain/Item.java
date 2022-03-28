package com.easyvaluation.materialslibrary.domain;

import com.easyvaluation.foundations.domain.BaseEntity;
import com.easyvaluation.projects.domain.Project;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "project_id")
    Project project;

    @Column
    String itemName;

    float price;

    String description;

}
