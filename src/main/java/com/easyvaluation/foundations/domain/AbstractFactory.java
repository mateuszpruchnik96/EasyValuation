package com.easyvaluation.foundations.domain;

public interface AbstractFactory<E extends BaseEntity> {

    E create();

}
