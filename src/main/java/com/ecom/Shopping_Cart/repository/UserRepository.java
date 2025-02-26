package com.ecom.Shopping_Cart.repository;

import com.ecom.Shopping_Cart.model.UserDtls;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<UserDtls, Integer> {

    public UserDtls findByEmail(String email);

    public List<UserDtls> findByRole(String role);
}
