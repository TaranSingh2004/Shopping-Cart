package com.ecom.Shopping_Cart.services.impl;

import com.ecom.Shopping_Cart.model.UserDtls;
import com.ecom.Shopping_Cart.repository.UserRepository;
import com.ecom.Shopping_Cart.services.UserService;
import org.aspectj.apache.bcel.classfile.Module;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;
import java.util.SplittableRandom;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDtls saveUser(UserDtls user) {
        user.setRole("ROLE_USER");
        user.setIsEnable(true);
        String encodePassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodePassword);
        return userRepository.save(user);
    }

    @Override
    public UserDtls getUserByEmail(String email) {

        return userRepository.findByEmail(email);
    }

    @Override
    public List<UserDtls> getUsers(String role) {
        return userRepository.findByRole(role);
    }

    @Override
    public Boolean updateAccountStatus(Integer id, Boolean status) {
        Optional<UserDtls> findByuser = userRepository.findById(id);
        if(findByuser.isPresent()){
            UserDtls userDtls=findByuser.get();
            userDtls.setIsEnable(status);
            userRepository.save(userDtls);
            return true;
        }
        return false;
    }
}
