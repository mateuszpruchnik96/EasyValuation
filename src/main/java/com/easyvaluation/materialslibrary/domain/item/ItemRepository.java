package com.easyvaluation.materialslibrary.domain.item;

import com.easyvaluation.materialslibrary.domain.item.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    @Query(value="select i from Item i where i.type=:type")
    List<Item> findAllByType(@Param("type") String type);

//    @Query(value = "SELECT i FROM Item i WHERE i.item_name LIKE ':letters%'")
//    List<AbstractMap.SimpleEntry<Long,String>> findByFirstLetters(@Param("letters") String letters);

    List<Item> findByItemNameStartsWithIgnoreCase(String letters);
}
