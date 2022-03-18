package com.easyvaluation.foundations.domain;

public interface AbstractService<E extends BaseEntity> {
    E save(E entity);
}
