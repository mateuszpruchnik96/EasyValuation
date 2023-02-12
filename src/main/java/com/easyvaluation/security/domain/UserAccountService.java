package com.easyvaluation.security.domain;

import com.easyvaluation.foundations.domain.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.AbstractMap;
import java.util.Optional;

@Service
public class UserAccountService implements AbstractService<UserAccount> {

    @Autowired
    UserAccountRepository userAccountRepository;

    @Autowired
    UserRoleRepository userRoleRepository;

    BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(16);

    @Override
    public UserAccount save(UserAccount entity){

            entity.setPassword(bCryptPasswordEncoder.encode(entity.getPassword()));
            UserRole userRole = userRoleRepository.findByName("ROLE_USER");
            entity.setUserRoles(userRole);
            entity = userAccountRepository.save(entity);

            return entity;
    }

    public UserAccount findByLogin(UserAccount entity) throws EntityNotFoundException {
        try{
            UserAccount userAccount = userAccountRepository.findByLogin(entity.getLogin());
            return userAccount;
        } catch(NullPointerException e){
          throw new EntityNotFoundException();
        }
    }

    public UserAccount findById(Long id) throws EntityNotFoundException {

            Optional<UserAccount> userAccount = userAccountRepository.findById(id);
            if(userAccount != null){
                return userAccount.stream().findFirst().orElse(null);
            } else throw new EntityNotFoundException();
    }

    public AbstractMap.SimpleEntry<Boolean, String> isExist(UserAccount entity){
        try {
            UserAccount userAccount = userAccountRepository.findByLogin(entity.getLogin());

            if(entity!=null && bCryptPasswordEncoder.matches(entity.getPassword(), userAccount.getPassword())){
                return new AbstractMap.SimpleEntry(true, "Succesful login");
            } else {
                return new AbstractMap.SimpleEntry(false, "Wrong login or password");
            }
        } catch(NullPointerException e){
            return new AbstractMap.SimpleEntry(false, "Wrong log or pass");
        }
    }

    public UserAccount addUserAccountRoles(Long userAccountId, String userRoleName) throws EntityNotFoundException{
        Optional<UserAccount> userAccount =
                userAccountRepository.findUserAccountWithUserRolesById(userAccountId);
        if(userAccount.isPresent()) {
            UserAccount entity = userAccount.get();
            entity.addRole(userRoleRepository.findByName(userRoleName));

            for (UserRole userRole:entity.getUserRoles()) {
                if(userRole.getName().equals(userRoleName)) throw new EntityExistsException("This user have that role!");
            }

            userAccountRepository.save(entity);
            return entity;
        } else {
            throw new EntityNotFoundException("There is no user with that ID");
        }
    }

//    public RefreshToken setRefreshToken(UserAccount userAccount, RefreshToken refreshToken){
//        userAccountRepository.setRefreshToken(userAccount.getLogin(), refreshToken);
//        return refreshToken;
//    }

}
