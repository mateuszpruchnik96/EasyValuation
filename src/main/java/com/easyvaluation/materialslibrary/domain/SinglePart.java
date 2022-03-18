package com.easyvaluation.materialslibrary.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@NoArgsConstructor
@DiscriminatorValue("SINGLEPART")
public class SinglePart extends Item{
    float mass;
    String producer;
    String symbol;
}
