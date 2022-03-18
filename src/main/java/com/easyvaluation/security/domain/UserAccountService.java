package com.easyvaluation.security.domain;

import com.easyvaluation.foundations.domain.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.AbstractMap;

@Service
public class UserAccountService implements AbstractService<UserAccount> {

    @Autowired
    UserAccountRepository userAccountRepository;

    BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(16);

    @Override
    public UserAccount save(UserAccount entity){

            entity.setPassword(bCryptPasswordEncoder.encode(entity.getPassword()));
            entity = userAccountRepository.save(entity);

            return entity;
    }

    public AbstractMap.SimpleEntry<Boolean, String> isExist(UserAccount entity){
        try {
            UserAccount userAccount = userAccountRepository.findByLogin(entity.getLogin());

            if(entity!=null && bCryptPasswordEncoder.matches(entity.getPassword(), userAccount.getPassword())){
                return new AbstractMap.SimpleEntry(true, "Succesful login");
            } else {
                return new AbstractMap.SimpleEntry(false, "Wrong login or password");
            }

        } catch(Error e){
            return new AbstractMap.SimpleEntry(false, "Wrong logorpass");
        }



    }

}
