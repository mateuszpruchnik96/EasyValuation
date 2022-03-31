package com.easyvaluation.materialslibrary.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    @Query(value="select c from Item c where c.type=:type")
    public List<Item> findAllByType(@Param("type") String type);
}
