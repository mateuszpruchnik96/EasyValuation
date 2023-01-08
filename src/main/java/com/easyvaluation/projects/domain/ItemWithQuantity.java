package com.easyvaluation.projects.domain;

import com.easyvaluation.materialslibrary.domain.item.Item;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItemWithQuantity {
    private Item item;
    private Integer quantity;
}
