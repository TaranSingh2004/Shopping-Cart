package com.ecom.Shopping_Cart.controller;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;

import com.ecom.Shopping_Cart.model.Product;
import com.ecom.Shopping_Cart.model.ProductOrder;
import com.ecom.Shopping_Cart.model.UserDtls;
import com.ecom.Shopping_Cart.services.*;
import com.ecom.Shopping_Cart.util.CommonUtil;
import com.ecom.Shopping_Cart.util.OrderStatus;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.ecom.Shopping_Cart.model.Category;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    @Autowired
    private CartService cartService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private CommonUtil commonUtil;

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

    @GetMapping("/")
    public String index() {
        return "admin/index";
    }

    @GetMapping("/loadAddProduct")
    public String loadAddProduct(Model m) {
        List<Category> categories = categoryService.getAllCategory();
        m.addAttribute("categories", categories);
        return "admin/add_product";
    }

    @GetMapping("/category")
    public String category(Model m) {
        m.addAttribute("categorys", categoryService.getAllCategory());
        return "admin/category";
    }

    @PostMapping("/saveCategory")
    public String saveCategory(@ModelAttribute Category category, @RequestParam("file") MultipartFile file,
                               HttpSession session) throws IOException {

        String imageName = file != null ? file.getOriginalFilename() : "default.jpg";
        category.setImageName(imageName);

        Boolean existCategory = categoryService.existCategory(category.getName());

        if (existCategory) {
            session.setAttribute("errorMsg", "Category Name already exists");
        } else {
            Category saveCategory = categoryService.saveCategory(category);

            if (ObjectUtils.isEmpty(saveCategory)) {
                session.setAttribute("errorMsg", "Not saved! Internal server error");
            } else {
                // Use absolute path for saving files in development
                String uploadDir = "src/main/resources/static/img/category_img/";

                // Ensure the directory exists
                File directory = new File(uploadDir);
                if (!directory.exists()) {
                    directory.mkdirs();  // Create the folder if it doesn't exist
                }

                // Save the file in the correct location
                Path path = Paths.get(uploadDir + File.separator + imageName);
                Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

                System.out.println("File saved at: " + path.toString());
                session.setAttribute("succMsg", "Saved successfully");
            }
        }

        return "redirect:/admin/category";
    }


    @GetMapping("/deleteCategory/{id}")
    public String deleteCategory(@PathVariable int id, HttpSession session){
        boolean deleteCategory=categoryService.deleteCategory(id);
        if(deleteCategory){
            session.setAttribute("succMsg", "category deleted succesfully");
        } else {
            session.setAttribute("errorMsg", "something wrong on server");
        }
        return "redirect:/admin/category";
    }

    @GetMapping("/loadEditCategory/{id}")
    public String loadEditCategory(@PathVariable int id, Model m) {
        m.addAttribute("category", categoryService.getCategoryById(id));
        return "admin/edit_category";
    }

    @PostMapping("/updateCategory")
    public String updateCategory(@ModelAttribute Category category, @RequestParam("file") MultipartFile file,
                                 HttpSession session) throws IOException {

        Category oldCategory = categoryService.getCategoryById(category.getId());
        String imageName = file.isEmpty() ? oldCategory.getImageName() : file.getOriginalFilename();

        if (!ObjectUtils.isEmpty(category)) {

            oldCategory.setName(category.getName());
            oldCategory.setIsActive(category.getIsActive());
            oldCategory.setImageName(imageName);
        }

        Category updateCategory = categoryService.saveCategory(oldCategory);

        if (!ObjectUtils.isEmpty(updateCategory)) {

            if (!file.isEmpty()) {
                File saveFile = new ClassPathResource("static/img").getFile();

                Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + "category_img" + File.separator
                        + file.getOriginalFilename());

                // System.out.println(path);
                Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            }

            session.setAttribute("succMsg", "Category update success");
        } else {
            session.setAttribute("errorMsg", "something wrong on server");
        }

        return "redirect:/admin/loadEditCategory/" + category.getId();
    }


    @PostMapping("/saveProduct")
    public String saveProduct(@ModelAttribute Product product,@RequestParam("file") MultipartFile image ,HttpSession session) throws IOException {

        String imageName = image.isEmpty()?"default.jpg":image.getOriginalFilename();
        product.setImage(imageName);
        product.setDiscount(0);
        product.setDiscountPrice(product.getPrice());

        Product saveProduct = productService.saveProduct(product);

        if(!ObjectUtils.isEmpty(saveProduct)){
            if (!image.isEmpty()) {
                File saveFile = new ClassPathResource("static/img").getFile();
                File directory = new File(saveFile.getAbsolutePath() + File.separator + "product_img");
                if(!directory.exists()){
                    directory.mkdir();
                }

                Path path = Paths.get(directory.getAbsolutePath() + File.separator + image.getOriginalFilename());

//                 System.out.println(path);
                Files.copy(image.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            }
            session.setAttribute("succMsg", "Product saved successfully");
        } else {
            session.setAttribute("errorMsg", "something wrong on server");
        }
        return "redirect:/admin/loadAddProduct";
    }

    @GetMapping("/products")
    public String loadViewProduct(Model m, @RequestParam(defaultValue = "") String ch){

        List<Product> searchProducts=null;
        if(ch!=null && ch.length()>0){
            searchProducts = productService.searchProduct(ch);
        }else {
            searchProducts = productService.getAllProducts();
        }
        m.addAttribute("products", searchProducts);
        return "admin/products";
    }

    @GetMapping("/deleteProduct/{id}")
    public String deleteProduct(@PathVariable int id, HttpSession session){
        Boolean deleteProduct = productService.deleteProduct(id);
        if(deleteProduct){
            session.setAttribute("succMsg", "product deleted succesfully");
        } else {
            session.setAttribute("errorMsg", "something wrong on server");
        }
        return "redirect:/admin/products";
    }

    @GetMapping("/editProduct/{id}")
    public String editProduct(@PathVariable int id,Model m){
        m.addAttribute("product", productService.getProductById(id));
        m.addAttribute("categories", categoryService.getAllCategory());
        return "admin/edit_product";
    }

    @PostMapping("/updateProduct")
    public String updateProduct(@ModelAttribute Product product,@RequestParam("file") MultipartFile image, HttpSession session, Model m){
        if(product.getDiscount()<0 || product.getDiscount()>100){
            session.setAttribute("errorMsg", "invalid discount");
        } else {
            Product updateProduct = productService.updateProduct(product, image);
            if(!ObjectUtils.isEmpty(updateProduct)){
                session.setAttribute("succMsg", "product updated succesfully");
            } else {
                session.setAttribute("errorMsg", "something wrong on server");
            }
        }
        return "redirect:/admin/editProduct/" + product.getId();
    }

    @GetMapping("/users")
    public String getAllUsers(Model m){
        List<UserDtls> users = userService.getUsers("ROLE_USER");
        m.addAttribute("users", users);
        return "/admin/users";
    }

    @GetMapping("/updateSts")
    public String updateAccountStatus(@RequestParam Integer id, @RequestParam Boolean status, HttpSession session){
        boolean f = userService.updateAccountStatus(id, status);
        if(f){
            session.setAttribute("succMsg", "Account Ststus updated");
        }else {
            session.setAttribute("errorMsg", "Something wrong on server");
        }
        return "redirect:/admin/users";
    }

    @GetMapping("/orders")
    public String getAllOrders(Model m){
        List<ProductOrder> allOrders = orderService.getAllOrders();
        m.addAttribute("orders", allOrders);
        m.addAttribute("srch", false);
        return "/admin/orders";
    }

    @PostMapping("/update-order-status")
    private String updateOrderStatus(@RequestParam Integer id, @RequestParam Integer st, HttpSession session) throws MessagingException, UnsupportedEncodingException {
        OrderStatus[] values = OrderStatus.values();
        String status = null;
        for(OrderStatus orderSt:values){
            if(orderSt.getId().equals(st)){
                status=orderSt.getName();
            }

        }
        ProductOrder updateOrder = orderService.updateOrderStatus(id, status);

        commonUtil.sendMailForProductOrder(updateOrder, status);

        if(!ObjectUtils.isEmpty(updateOrder)){
            session.setAttribute("succMsg", "Status Updated");
        } else {
            session.setAttribute("errorMsg", "Status not updated");
        }
        return "redirect:/admin/orders";
    }


    @GetMapping("/search-order")
    public String searchProduct(@RequestParam String orderId, Model m, HttpSession session){
        if(orderId.length()>0 && orderId!=null) {
            ProductOrder order = orderService.getOrdersByOrderId(orderId.trim());
            if (ObjectUtils.isEmpty(order)) {
                session.setAttribute("errorMsg", "Incorrect orderId");
                m.addAttribute("orderDtls", null);
            } else {
                m.addAttribute("orderDtls", order);
            }
            m.addAttribute("srch", true);
        } else {
            List<ProductOrder> allOrders = orderService.getAllOrders();
            m.addAttribute("orders", allOrders);
            m.addAttribute("srch", false);
        }
        return "/admin/orders";
    }

    @GetMapping("/search")
    public String searchProduct(@RequestParam String ch, Model m){
        List<Product> searchProduct = productService.searchProduct(ch);
        m.addAttribute("products", searchProduct);
        return "product";
    }
}