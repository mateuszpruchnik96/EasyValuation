package com.easyvaluation.materialslibrary.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@DiscriminatorValue("SINGLEPART")
public class SinglePart extends Item{
    String producer;
    String symbol;
    float mass;

    public SinglePart(String producer, String symbol, float mass){
        this.producer = producer;
        this.symbol = symbol;
        this.mass = mass;
    }
}
