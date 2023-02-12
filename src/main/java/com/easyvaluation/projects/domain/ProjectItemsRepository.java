package com.easyvaluation.projects.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectItemsRepository extends JpaRepository<Project,Long> {

    List<Project> findByUserAccountId(Long userAccountId);

    Project findByUserAccountIdAndId(Long userAccountId, Long Id);


}
