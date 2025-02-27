package com.ecom.Shopping_Cart.util;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;

@Component
public class CommonUtil {

    @Autowired
    private JavaMailSender mailSender;

    public Boolean sendMail(String url, String reciepentEmail) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper= new MimeMessageHelper(message);
        helper.setFrom("ecommercetesttaran@gmail.com", "Shopping Cart");
        helper.setTo(reciepentEmail);
        String content = "<p>Hello, </p>"
                +"<p>You have requested to reset your password</p>"
                +"<p>click the link below to change the password: </p>"
                +"<p><a href=\""
                +url
                + "\">Change my password</a></p>";
        helper.setSubject("Password reset");
        helper.setText(content, true);
        mailSender.send(message);
        return true;
    }

    public static String generateUrl(HttpServletRequest request) {
        String siteUrl = request.getRequestURL().toString();
        return siteUrl.replace(request.getServletPath(), "");
    }
}
