package com.ecom.Shopping_Cart.config;

import com.ecom.Shopping_Cart.model.UserDtls;
import com.ecom.Shopping_Cart.repository.UserRepository;
import com.ecom.Shopping_Cart.services.UserService;
import com.ecom.Shopping_Cart.util.AppConstant;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class AuthFailureHandlerImpl extends SimpleUrlAuthenticationFailureHandler {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        String email = request.getParameter("username");
        UserDtls userDtls = userRepository.findByEmail(email);
        if( userDtls.getIsEnable()){
            if(userDtls.getAccountNonLocked()){
                if(userDtls.getFailedAttempt() < AppConstant.ATTEMPT_TIME){
                    userService.increaseFailedAttempt(userDtls);
                }else{
                    userService.userAccountLock(userDtls);
                    exception = new LockedException("Your account is locked !! Failed Attempt 3");
                }
            }else {
                if(userService.unlockAccountTimeExpired(userDtls)){
                    exception = new LockedException("Your account is unlocked");
                } else {
                    exception=new LockedException("Your account is locked !! Failed Attempt 3");
                }
            }
        }else {
            exception=new LockedException("Your account is inActive");
        }
        super.setDefaultFailureUrl("/signin?error");
        super.onAuthenticationFailure(request, response, exception);

    }
}
