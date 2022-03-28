package com.easyvaluation.projects.application;

import com.easyvaluation.materialslibrary.domain.SinglePart;
import com.easyvaluation.projects.domain.Project;
import com.easyvaluation.projects.domain.ProjectRepository;
import com.easyvaluation.projects.domain.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class ProjectController {
    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    ProjectService projectService;

    @GetMapping("/projects/{id}")
    public ResponseEntity<Project> getOne(@PathVariable("id") Long id){
        Optional<Project> service = projectRepository.findById(id);
        if (service.isPresent()){
            return ResponseEntity.ok(service.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("projects")
    public ResponseEntity<Project> save(@RequestBody Project project){
        Project savedProject = projectService.save(project);

        return ResponseEntity.ok(savedProject);
    }
}
