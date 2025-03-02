package com.ecom.Shopping_Cart.controller;

import com.ecom.Shopping_Cart.model.Cart;
import com.ecom.Shopping_Cart.model.Category;
import com.ecom.Shopping_Cart.model.OrderRequest;
import com.ecom.Shopping_Cart.model.UserDtls;
import com.ecom.Shopping_Cart.services.CartService;
import com.ecom.Shopping_Cart.services.CategoryService;
import com.ecom.Shopping_Cart.services.OrderService;
import com.ecom.Shopping_Cart.services.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CartService cartService;

    @Autowired
    private OrderService orderService;

    @GetMapping("/")
    public String home() {
        return "user/home";
    }

    @ModelAttribute
    public void getUserDetails(Principal p, Model m){
        if(p!=null){
            String email= p.getName();
            UserDtls userDtls = userService.getUserByEmail(email);
            m.addAttribute("user", userDtls);
            Integer countCart = cartService.getCountCart(userDtls.getId());
            m.addAttribute("countCart", countCart);
        }
        List<Category> list = categoryService.getAllActiveCategory();
        m.addAttribute("categorys", list);
    }

    @GetMapping("/addCart")
    public String addToCart(@RequestParam Integer pid, @RequestParam Integer uid, HttpSession session){
        Cart saveCart = cartService.saveCart(pid, uid);
        if(ObjectUtils.isEmpty(saveCart)){
            session.setAttribute("errorMsg", "Product add to caart failed");
        }else {
            session.setAttribute("succMsg", "Product added to cart");
        }
        return "redirect:/product/" + pid;
    }

    @GetMapping("/cart")
    public String loadCartPage(Principal p, Model m){
        UserDtls user = getLoggedInUserDetails(p);
        List<Cart> carts = cartService.getCartByUser(user.getId());
        m.addAttribute("carts", carts);
        if(!carts.isEmpty()){
            m.addAttribute("totalOrderPrice", carts.get(carts.size()-1).getTotalOrderPrice());
        }
        return "/user/cart";
    }

    @GetMapping("/cartQauntityUpdate")
    public String updateCartQuantity(@RequestParam String sy, @RequestParam Integer cid){
        cartService.updateQuantity(sy, cid);
        return "redirect:/user/cart";
    }

    private UserDtls getLoggedInUserDetails(Principal p){
        String email = p.getName();
        UserDtls userDtls = userService.getUserByEmail(email);
        return userDtls;
    }

    @GetMapping("/orders")
    public String orderPage(Principal p, Model m){
        UserDtls user = getLoggedInUserDetails(p);
        List<Cart> carts = cartService.getCartByUser(user.getId());
        m.addAttribute("carts", carts);
        if(!carts.isEmpty()){
            Double orderPrice = carts.get(carts.size()-1).getTotalOrderPrice();
            Double totalOrderPrice = carts.get(carts.size()-1).getTotalOrderPrice()+250+100;
            m.addAttribute("orderPrice", orderPrice);
            m.addAttribute("totalOrderPrice", totalOrderPrice);
        }
        return "/user/order";
    }

    @PostMapping("/save-order")
    public String saveOrder(@ModelAttribute OrderRequest request, Principal p){
//        System.out.println(request);
        UserDtls user = getLoggedInUserDetails(p);
        orderService.saveOrder(user.getId(), request);

        return "redirect:/user/success";
    }

    @GetMapping("/success")
    public String loadSuccess(){
        return "/user/success";
    }

}
