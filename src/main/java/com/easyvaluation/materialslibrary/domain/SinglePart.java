package com.easyvaluation.materialslibrary.domain;

import com.easyvaluation.materialslibrary.domain.item.Item;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.checkerframework.common.aliasing.qual.Unique;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.UniqueConstraint;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@DiscriminatorValue("SINGLEPART")
public class SinglePart extends Item {

    String producer;

    String symbol;

    float mass;

    public SinglePart(String producer, String symbol, float mass){
        this.producer = producer;
        this.symbol = symbol;
        this.mass = mass;
    }
}
