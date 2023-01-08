package com.easyvaluation.setup;

import com.easyvaluation.materialslibrary.domain.SinglePart;
import com.easyvaluation.materialslibrary.domain.item.Item;
import com.easyvaluation.materialslibrary.domain.item.ItemRepository;
import com.easyvaluation.projects.domain.Operation;
import com.easyvaluation.projects.domain.Project;
import com.easyvaluation.projects.domain.ProjectRepository;
import com.easyvaluation.security.domain.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Component
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    boolean alreadySetup = false;

    @Autowired
    UserAccountRepository userAccountRepository;

    @Autowired
    UserAccountService userAccountService;

    @Autowired
    UserRoleRepository userRoleRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    ProjectRepository projectRepository;

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if(alreadySetup) return;

        createRoleIfNotFound("ROLE_ADMIN");
        createRoleIfNotFound("ROLE_USER");

        UserRole userRole = userRoleRepository.findByName("ROLE_USER");
        UserRole adminRole = userRoleRepository.findByName("ROLE_ADMIN");

        UserAccount user = new UserAccount("jan", "lokiloki");
        UserAccount admin = new UserAccount("admin", "admin");

        user.setUserRoles(userRole);
        admin.setUserRoles(userRole, adminRole);

        if(userAccountRepository.findByLogin(user.getLogin()) == null) {
            userAccountService.save(user);
        }
        if(userAccountRepository.findByLogin(admin.getLogin()) == null) {
            userAccountService.save(admin);
        }

        Item screw = new SinglePart("Elesa-Ganter", "G12543", 0.1F);
        screw.setItemName("Screw");
//        itemRepository.save(screw);


        Item bolt = new SinglePart("Kipp", "XXXX", 0.12F);
        bolt.setItemName("Bolt");
//        itemRepository.save(bolt);

        Operation operation1 = new Operation();
        operation1.setDescription("Operation 1");
        operation1.setHourPrice(100);
        operation1.setHours(10);

        Operation operation2 = new Operation();
        operation2.setDescription("Operation 2");
        operation2.setHourPrice(200);
        operation2.setHours(20);

        Project project = new Project();
        project.setUserAccount(userAccountRepository.findByLogin(user.getLogin()));
        try {
            project.addItem(itemRepository.findByItemNameStartsWithIgnoreCase("Screw").get(0).getId(), 2);
            project.addItem(itemRepository.findByItemNameStartsWithIgnoreCase("Bolt").get(0).getId(), 10);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        project.addOperation(operation1);
        project.addOperation(operation2);
        projectRepository.save(project);

        alreadySetup = true;
    }

    @Transactional
    UserRole createRoleIfNotFound(String name) {

        UserRole userRole = userRoleRepository.findByName(name);
        if (userRole == null) {
            userRole = new UserRole(name);
            userRoleRepository.save(userRole);
        }
        return userRole;
    }
}
