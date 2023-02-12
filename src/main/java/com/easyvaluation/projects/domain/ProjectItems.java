package com.easyvaluation.projects.domain;

import com.easyvaluation.foundations.domain.BaseEntity;
import com.easyvaluation.materialslibrary.domain.item.Item;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;

@Entity
@Table(name = "PROJECT_ITEMS")
@Getter
@Setter

public class ProjectItems extends BaseEntity {


    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;

    @Column(name = "quantity")
    private float quantity;

    @Column(name = "added")
    private LocalDateTime added;

}
