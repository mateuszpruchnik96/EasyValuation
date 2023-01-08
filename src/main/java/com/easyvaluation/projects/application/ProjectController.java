/**
 * RestController for projects
 *
 * @author Mateusz Pruchnik
 */

package com.easyvaluation.projects.application;

import com.easyvaluation.materialslibrary.domain.item.Item;
import com.easyvaluation.projects.domain.ItemWithQuantity;
import com.easyvaluation.projects.domain.Project;
import com.easyvaluation.projects.domain.ProjectRepository;
import com.easyvaluation.projects.domain.ProjectService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.*;

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

    /**
     * Returns user's projects by his token
     *
     * @return - Response with user's projects and status
     */
    @GetMapping("/projects")
    public ResponseEntity<List<Project>> getAllProjectsByUserToken(@RequestHeader("Authorization") String token){
        Optional<List<Project>> service = Optional.ofNullable(projectService.findProjectsByUserId(token));
        if (service.isPresent()){
            return ResponseEntity.ok(service.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Returns project for user by token and projectId
     *
     * @param token - Authorization Bearer token from header
     * @param projectId - ID of project
     * @return - Response with project and status
     */
    @GetMapping("/projects/project")
    public ResponseEntity<Project> getOneByUserTokenAndProjectId(@RequestHeader("Authorization") String token, @RequestParam(value="projectId") String projectId){
        Optional<Project> service = projectService.findProjectByUserIdAndProjectId( Long.valueOf(projectId), token);
        if (service.isPresent()){
            return ResponseEntity.ok(service.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Returns project for user by token and projectId with items map
     *
     * @param token - Authorization Bearer token from header
     * @param projectId - ID of project
     * @return - Response with project and status
     */
    @GetMapping("/projects/project_with_items")
    //ResponseEntity<AbstractMap.SimpleEntry<List<Project>,List<ItemWithQuantity>>>
    public ResponseEntity<Object[]> getOneByUserTokenAndProjectIdWithItems(@RequestHeader("Authorization") String token, @RequestParam(value="projectId") String projectId){

        try {
//            AbstractMap.SimpleEntry<List<Project>, List<ItemWithQuantity>>
                   Object[] service = projectService.getProjectByUserIdAndProjectIdWithItemObjects(Long.valueOf(projectId), token);

            return ResponseEntity.ok(service);
        } catch(EntityNotFoundException e){
            return ResponseEntity.notFound().build();
        } catch (JsonProcessingException e){
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
        }
    }

    /**
     * Update project for user by token and projectId
     *
     * @param token - Authorization Bearer token from header
     * @param project - Project with modified fields
     * @return - Response with saved project and status
     */
    @PostMapping("/projects")
    public ResponseEntity<Project> save(@RequestBody Project project, @RequestHeader("Authorization") String token){
        Project savedProject = projectService.save(project, token);
        return ResponseEntity.ok(savedProject);
    }

    @DeleteMapping("/projects/{id}")
    public ResponseEntity<Project> deleteById(@PathVariable("id") Long id){
        projectRepository.deleteById(id);
        return ResponseEntity.accepted().build();
    }
}
