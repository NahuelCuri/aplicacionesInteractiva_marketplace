package com.uade.tpo.Marketplace.Config;

import com.github.javafaker.Faker;
import com.uade.tpo.Marketplace.Entity.Category;
import com.uade.tpo.Marketplace.Entity.Product;
import com.uade.tpo.Marketplace.Entity.ProductImage;
import com.uade.tpo.Marketplace.Entity.Role;
import com.uade.tpo.Marketplace.Entity.User;
import com.uade.tpo.Marketplace.Repository.CategoryRepository;
import com.uade.tpo.Marketplace.Repository.ProductImageRepository;
import com.uade.tpo.Marketplace.Repository.ProductRepository;
import com.uade.tpo.Marketplace.Repository.RoleRepository;
import com.uade.tpo.Marketplace.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

@Component
public class DataSeeder implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductImageRepository productImageRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (roleRepository.count() == 0) {
            Role adminRole = new Role();
            adminRole.setName("ADMIN");

            Role userRole = new Role();
            userRole.setName("USER");

            Role buyerRole = new Role();
            buyerRole.setName("BUYER");

            Role sellerRole = new Role();
            sellerRole.setName("SELLER");

            roleRepository.saveAll(Arrays.asList(adminRole, userRole, buyerRole, sellerRole));
        }

        if (userRepository.count() == 0) {
            Role adminRole = roleRepository.findByName("ADMIN").orElseThrow(() -> new RuntimeException("Admin role not found"));
            Role buyerRole = roleRepository.findByName("BUYER").orElseThrow(() -> new RuntimeException("Buyer role not found"));
            Role sellerRole = roleRepository.findByName("SELLER").orElseThrow(() -> new RuntimeException("Seller role not found"));

            User admin = new User();
            admin.setUsername("admin");
                        admin.setEmail("admin@marketplace.com");
                        admin.setPassword(passwordEncoder.encode("admin"));
                        admin.setRoles(Arrays.asList(adminRole, buyerRole, sellerRole));
                        admin.setEnabled(true);
                        userRepository.save(admin);
            
                        User user = new User();
                        user.setUsername("user");
                        user.setEmail("user@marketplace.com");
                        user.setPassword(passwordEncoder.encode("user"));
                        user.setRoles(Collections.singletonList(buyerRole));
                        user.setEnabled(true);
                        userRepository.save(user);
                    }

        if (categoryRepository.count() == 0) {
            List<String> categories = Arrays.asList("Electronics", "Books", "Home & Garden", "Clothing", "Sports & Outdoors", "Toys & Games", "Health & Beauty");
            for (String catName : categories) {
                Category category = new Category();
                category.setName(catName);
                category.setDescription("Category for " + catName);
                categoryRepository.save(category);
            }
        }

        if (productRepository.count() == 0) {
            Faker faker = new Faker();
            Random random = new Random();
            User seller = userRepository.findByUsername("admin").orElseThrow(() -> new RuntimeException("User not found"));
            List<Category> categories = categoryRepository.findAll();

            for (int i = 0; i < 200; i++) {
                try {
                    Product product = new Product();
                    product.setName(faker.commerce().productName());
                    product.setDescription(faker.lorem().sentence());
                    product.setPrice(Double.parseDouble(faker.commerce().price(10.00, 2000.00)));
                    product.setStock(faker.number().numberBetween(1, 100));
                    product.setSeller(seller);
                    product.setCategory(categories.get(random.nextInt(categories.size())));
                    Product savedProduct = productRepository.save(product);

                    // Fetch a new random image for each product
                    byte[] placeholderImage;
                    try (InputStream in = new URL("https://picsum.photos/400/400").openStream()) {
                        ByteArrayOutputStream out = new ByteArrayOutputStream();
                        byte[] buf = new byte[1024];
                        int n;
                        while (-1 != (n = in.read(buf))) {
                            out.write(buf, 0, n);
                        }
                        out.close();
                        placeholderImage = out.toByteArray();
                    }

                    ProductImage productImage = new ProductImage();
                    productImage.setProduct(savedProduct);
                    productImage.setImageData(placeholderImage);
                    productImageRepository.save(productImage);
                } catch (Exception e) {
                    System.err.println("Skipping product creation due to error: " + e.getMessage());
                }
            }
        }
    }
}
