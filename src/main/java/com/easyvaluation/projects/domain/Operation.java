package com.easyvaluation.projects.domain;

import com.easyvaluation.foundations.domain.BaseEntity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Embeddable;
import javax.persistence.Entity;

@Embeddable
@Setter
@Getter
@NoArgsConstructor
public class Operation {

// added in Product entity
//    private int index;

    private String description;

    private float hours;

    private float hourPrice;

    public float getCost(){
        return this.hourPrice*this.hours;
    }
}
