package com.easyvaluation.projects.domain;

import com.easyvaluation.materialslibrary.domain.SinglePart;
import com.easyvaluation.materialslibrary.domain.item.Item;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class HashMapConverterTest {

    @Test
    void convertToEntityAttributeAndConvertToDatabaseColumnMethodsShouldGiveSameResultAfterUsingThemTogether(){
        //given
        Item screw = new SinglePart("Elesa-Ganter", "G12543", 0.1F);
        screw.setItemName("Screw");
        Map<Item, Integer> items = new HashMap<>();

        //when

        //then
    }

}