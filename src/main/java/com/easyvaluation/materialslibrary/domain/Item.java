package com.easyvaluation.materialslibrary.domain;

import com.easyvaluation.foundations.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TYPE", discriminatorType = DiscriminatorType.STRING)
public abstract class Item extends BaseEntity {

    @Column(name = "TYPE", unique = true, nullable = false, insertable = false,updatable = false)
    @Enumerated(EnumType.STRING)
    ItemType type;

    float price;

    String description;

}
