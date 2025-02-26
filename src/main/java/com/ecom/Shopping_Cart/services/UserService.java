package com.ecom.Shopping_Cart.services;

import com.ecom.Shopping_Cart.model.UserDtls;

import java.util.List;

public interface UserService {

    public UserDtls saveUser(UserDtls user);

    public UserDtls getUserByEmail(String email);

    public List<UserDtls> getUsers(String role);

    public Boolean updateAccountStatus(Integer id, Boolean status);
}
