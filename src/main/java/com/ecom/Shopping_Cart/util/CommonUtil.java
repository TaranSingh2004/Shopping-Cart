package com.ecom.Shopping_Cart.util;

import com.ecom.Shopping_Cart.model.ProductOrder;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.weaver.ast.Or;
import org.hibernate.query.Order;
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

    String msg=null;

    public Boolean sendMailForProductOrder(ProductOrder order, String status) throws MessagingException, UnsupportedEncodingException {
        msg = "<p> Hello [[name]],</p>" +
                "<p>Thanku your order <b>[[orderStatus]]</b>.</p>" +
                "<p>Product Details : </p>" +
                "<p>Name : [[productName]]</p>" +
                "<p>Category : [[category]]</p>" +
                "<p>Quantity : [[qauntity]]</p>" +
                "<p>Price : [[price]]</p>" +
                "<p>Payment Type : [[paymentType]]</p>";
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper= new MimeMessageHelper(message);
        helper.setFrom("ecommercetesttaran@gmail.com", "Shopping Cart");
        helper.setTo(order.getOrderAddress().getEmail());

//        OrderStatus[] values = OrderStatus.values();
//        for(OrderStatus status: values){
//            if(status.getId().equals(status)){
//
//            }
//        }

        msg = msg.replace("[[name]]", order.getOrderAddress().getFirstName());
        msg = msg.replace("[[orderStatus]]", status);
        msg=msg.replace("[[productName]]", order.getProduct().getTitle());
        msg=msg.replace("[[category]]", order.getProduct().getCategory());
        msg=msg.replace("[[qauntity]]", order.getQuantity().toString());
        msg=msg.replace("[[price]]", order.getPrice().toString());
        msg=msg.replace("[[paymentType]]", order.getPaymentType());

        helper.setSubject("Password order status");
        helper.setText(msg, true);
        mailSender.send(message);
        return true;
    }
}
