package com.easyvaluation.materialslibrary.domain;

import com.easyvaluation.materialslibrary.domain.item.Item;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@NoArgsConstructor
@DiscriminatorValue("TWODIMENSIONAL")
public class TwoDimensional extends Item {
    float massInKilogramsPerMeter;
    float thickness;
    float density;

}
