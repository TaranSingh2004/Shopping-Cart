package com.ecom.Shopping_Cart.services.impl;

import com.ecom.Shopping_Cart.model.UserDtls;
import com.ecom.Shopping_Cart.repository.UserRepository;
import com.ecom.Shopping_Cart.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDtls saveUser(UserDtls user) {
        UserDtls saveUser = userRepository.save(user);
        return saveUser;
    }
}
