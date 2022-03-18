package com.easyvaluation.materialslibrary.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TwoDimensionalRepository extends JpaRepository<TwoDimensional, Long> {
    List<TwoDimensional> findAll();
}