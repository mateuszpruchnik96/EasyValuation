package com.easyvaluation.setup;

import com.easyvaluation.materialslibrary.domain.SinglePart;
import com.easyvaluation.materialslibrary.domain.item.Item;
import com.easyvaluation.materialslibrary.domain.item.ItemRepository;
import com.easyvaluation.security.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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

        itemRepository.save(screw);

//\"id\":1,\"version\":0,\"itemName\":\"Screw\"," +
//                "\"producer\":\"Elesa-Ganter\",\"symbol\":\"G12543\",\"mass\":0.1}"
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
