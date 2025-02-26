package com.ecom.Shopping_Cart.services.impl;

import com.ecom.Shopping_Cart.model.UserDtls;
import com.ecom.Shopping_Cart.repository.UserRepository;
import com.ecom.Shopping_Cart.services.UserService;
import com.ecom.Shopping_Cart.util.AppConstant;
import org.aspectj.apache.bcel.classfile.Module;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.Date;
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
        user.setAccountNonLocked(true);
        user.setFailedAttempt(0);

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

    @Override
    public void increaseFailedAttempt(UserDtls user) {
//        int attempt = (user.getFailedAttempt() != null) ? user.getFailedAttempt() + 1 : 1;
        int attempt = user.getFailedAttempt()+1;
        user.setFailedAttempt(attempt);
        userRepository.save(user);
    }


    @Override
    public void userAccountLock(UserDtls user) {
        user.setAccountNonLocked(false);
        user.setLockTime(new Date());
        userRepository.save(user);
    }

    @Override
    public boolean unlockAccountTimeExpired(UserDtls user) {

//        if (user.getLockTime() == null) {
//            return false;  // Or handle it as per your logic
//        }
        long lockTime = user.getLockTime().getTime();

        long unLockTime = lockTime+ AppConstant.UNLOCK_DURATION_TIME;
        long currentTime = System.currentTimeMillis();

        if(unLockTime<currentTime){
            user.setAccountNonLocked(true);
            user.setFailedAttempt(0);
            user.setLockTime(null);
            userRepository.save(user);
            return true;
        }

        return false;
    }

    @Override
    public void resetAttempt(int userId) {

    }
}
