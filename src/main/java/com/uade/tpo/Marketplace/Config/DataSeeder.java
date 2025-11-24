package com.uade.tpo.Marketplace.Config;

import com.uade.tpo.Marketplace.Entity.Category;
import com.uade.tpo.Marketplace.Entity.Product;
import com.uade.tpo.Marketplace.Entity.Role;
import com.uade.tpo.Marketplace.Entity.User;
import com.uade.tpo.Marketplace.Repository.CategoryRepository;
import com.uade.tpo.Marketplace.Repository.ProductRepository;
import com.uade.tpo.Marketplace.Repository.RoleRepository;
import com.uade.tpo.Marketplace.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Component
public class DataSeeder implements CommandLineRunner {

    @Autowired private UserRepository userRepository;
    @Autowired private RoleRepository roleRepository;
    @Autowired private CategoryRepository categoryRepository;
    @Autowired private ProductRepository productRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        
        if (roleRepository.count() == 0) {
            Role adminRole = new Role();
            adminRole.setName("ADMIN");
            Role buyerRole = new Role();
            buyerRole.setName("BUYER");
            Role sellerRole = new Role();
            sellerRole.setName("SELLER");
            roleRepository.saveAll(Arrays.asList(adminRole, buyerRole, sellerRole));
        }
        
        if (userRepository.count() == 0) {
            createUsers();
        }

        if (categoryRepository.count() == 0) {
            createCategories();
        }

        if (productRepository.count() == 0) {
            createProducts();
        }
    }

    private void createUsers() {
        Role adminRole = roleRepository.findByName("ADMIN").orElseThrow();
        Role buyerRole = roleRepository.findByName("BUYER").orElseThrow();
        Role sellerRole = roleRepository.findByName("SELLER").orElseThrow();

        createUser("admin", "admin@marketplace.com", "adminpass", Arrays.asList(adminRole, buyerRole, sellerRole));
        createUser("user", "user@marketplace.com", "userpass", Collections.singletonList(buyerRole));
        createUser("eze", "eze@marketplace.com", "ezepass", Arrays.asList(sellerRole, buyerRole));
        createUser("karen", "karen@marketplace.com", "karenpass", Arrays.asList(sellerRole, buyerRole));
        createUser("steve", "steve@marketplace.com", "stevepass", Collections.singletonList(buyerRole));
    }
    
    private void createCategories() {
        Map<String, List<ProductData>> categoryProducts = getProductData();
        for (String categoryName : categoryProducts.keySet()) {
            Category newCategory = new Category();
            newCategory.setName(categoryName);
            newCategory.setDescription("A category for " + categoryName);
            categoryRepository.save(newCategory);
        }
    }
    
    private void createProducts() {
        User eze = userRepository.findByUsername("eze").orElseThrow(() -> new RuntimeException("Seeder error: eze user not found"));
        User karen = userRepository.findByUsername("karen").orElseThrow(() -> new RuntimeException("Seeder error: karen user not found"));
        Map<String, List<ProductData>> categoryProducts = getProductData();

        for (Map.Entry<String, List<ProductData>> entry : categoryProducts.entrySet()) {
            Category category = categoryRepository.findByName(entry.getKey());
            if (category == null) {
                throw new RuntimeException("Seeder error: category not found: " + entry.getKey());
            }
            List<ProductData> productDataList = entry.getValue();
            for (int i = 0; i < productDataList.size(); i++) {
                User seller = (i % 2 == 0) ? eze : karen;
                ProductData data = productDataList.get(i);
                
                Product product = new Product();
                product.setName(data.name);
                product.setDescription(data.description);
                product.setPrice(data.price);
                product.setStock(data.stock);
                product.setSeller(seller);
                product.setCategory(category);
                productRepository.save(product);
            }
        }
    }

    private void createUser(String username, String email, String password, List<Role> roles) {
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setEmail(email);
        newUser.setPassword(passwordEncoder.encode(password));
        newUser.setRoles(roles);
        newUser.setEnabled(true);
        userRepository.save(newUser);
    }
    
    private static class ProductData {
        String name;
        String description;
        double price;
        int stock;

        ProductData(String name, String description, double price, int stock) {
            this.name = name;
            this.description = description;
            this.price = price;
            this.stock = stock;
        }
    }

    private Map<String, List<ProductData>> getProductData() {
        return Map.of(
            "Electronics", List.of(
                new ProductData("Laptop Pro X", "High-performance laptop for professionals.", 1499.99, 15),
                new ProductData("Smartphone Z", "Latest generation smartphone with AI camera.", 899.50, 40),
                new ProductData("Noise-Cancelling Headphones", "Immersive sound experience, blocking out all distractions.", 249.99, 30),
                new ProductData("Smartwatch Series 8", "Track your fitness and stay connected.", 399.00, 25),
                new ProductData("4K Ultra HD TV 55-inch", "Stunning picture quality with vibrant colors.", 699.99, 10),
                new ProductData("Next-Gen Gaming Console", "Experience the future of gaming.", 499.99, 12),
                new ProductData("Portable Bluetooth Speaker", "Compact and powerful sound on the go.", 79.95, 50),
                new ProductData("Graphics Tablet", "Unleash your creativity with this professional graphics tablet.", 320.00, 20),
                new ProductData("VR Headset", "Step into virtual reality with this immersive headset.", 450.00, 18),
                new ProductData("Compact Drone with Camera", "Capture breathtaking aerial footage.", 550.00, 8)
            ),
            "Books", List.of(
                new ProductData("The Alchemist", "A philosophical book about following your dreams.", 15.99, 100),
                new ProductData("Sapiens: A Brief History of Humankind", "A captivating look at human history.", 22.50, 80),
                new ProductData("Dune", "A science fiction epic set in a distant future.", 18.00, 120),
                new ProductData("Atomic Habits", "An easy and proven way to build good habits.", 14.99, 150),
                new ProductData("The Midnight Library", "A novel about the choices that go into a life well-lived.", 20.00, 90),
                new ProductData("Project Hail Mary", "A lone astronaut must save the earth from disaster.", 25.50, 70),
                new ProductData("The Lord of the Rings", "The complete trilogy in one edition.", 35.00, 60),
                new ProductData("1984", "A dystopian novel by George Orwell.", 12.99, 200),
                new ProductData("To Kill a Mockingbird", "A classic of modern American literature.", 14.00, 180),
                new ProductData("The Hitchhiker's Guide to the Galaxy", "A comedic science fiction series.", 13.49, 130)
            ),
            "Home & Garden", List.of(
                new ProductData("Robotic Vacuum Cleaner", "Keeps your floors clean automatically.", 350.00, 20),
                new ProductData("Air Fryer XL", "Enjoy your favorite fried foods with less oil.", 120.50, 45),
                new ProductData("Cordless Drill Set", "A versatile tool for all your DIY projects.", 99.99, 35),
                new ProductData("Organic Herb Garden Kit", "Grow your own fresh herbs at home.", 29.99, 70),
                new ProductData("LED Smart Bulbs (4-pack)", "Control your lighting with your voice or an app.", 49.99, 60),
                new ProductData("Memory Foam Mattress", "Experience ultimate comfort and support.", 799.00, 10),
                new ProductData("Weatherproof Outdoor Furniture Set", "Durable and stylish for your patio.", 599.00, 8),
                new ProductData("High-Pressure Shower Head", "Transform your shower into a spa-like experience.", 39.99, 80),
                new ProductData("Electric Lawn Mower", "Quiet and efficient mowing for a perfect lawn.", 250.00, 15),
                new ProductData("Blender for Smoothies", "Powerful blender for smoothies, shakes, and more.", 89.99, 40)
            ),
            "Clothing", List.of(
                new ProductData("Men's Classic Denim Jacket", "A timeless piece for any wardrobe.", 75.00, 50),
                new ProductData("Women's High-Waisted Yoga Pants", "Comfortable and stylish for workouts or casual wear.", 45.50, 80),
                new ProductData("Leather Ankle Boots", "Versatile and durable boots for any season.", 120.00, 40),
                new ProductData("Cashmere Sweater", "Luxuriously soft and warm.", 150.00, 30),
                new ProductData("Summer Floral Dress", "Light and airy dress perfect for warm days.", 60.00, 60),
                new ProductData("Men's Chino Trousers", "A smart-casual staple.", 55.00, 70),
                new ProductData("Waterproof Rain Jacket", "Stay dry in any weather.", 90.00, 45),
                new ProductData("Silk Scarf", "An elegant accessory to elevate any outfit.", 35.00, 100),
                new ProductData("Running Sneakers", "Lightweight and supportive for your daily run.", 110.00, 65),
                new ProductData("Designer Sunglasses", "Protect your eyes in style.", 180.00, 25)
            ),
            "Sports & Outdoors", List.of(
                new ProductData("Yoga Mat with Carrying Strap", "Non-slip and cushioned for your yoga practice.", 30.00, 90),
                new ProductData("2-Person Camping Tent", "Easy to set up and weatherproof.", 150.00, 25),
                new ProductData("Adjustable Dumbbells Set", "Save space with this versatile dumbbell set.", 300.00, 15),
                new ProductData("Hiking Backpack 50L", "Durable and spacious for long treks.", 120.00, 30),
                new ProductData("Insulated Water Bottle", "Keeps your drinks cold for 24 hours or hot for 12.", 25.00, 120),
                new ProductData("Mountain Bike", "Conquer any trail with this rugged mountain bike.", 800.00, 10),
                new ProductData("Fishing Rod and Reel Combo", "Perfect for beginners and experienced anglers.", 75.00, 50),
                new ProductData("Portable Camping Stove", "Cook delicious meals anywhere.", 60.00, 40),
                new ProductData("Professional Football", "Official size and weight for the best performance.", 40.00, 80),
                new ProductData("Binoculars for Bird Watching", "Get a closer look at nature.", 95.00, 35)
            ),
            "Toys & Games", List.of(
                new ProductData("LEGO Starship Model", "A challenging and rewarding build for fans.", 150.00, 20),
                new ProductData("Strategy Board Game: Settlers of Catan", "A classic game of trading and building.", 49.99, 50),
                new ProductData("Remote Control Car", "High-speed and durable for all-terrain fun.", 65.00, 40),
                new ProductData("3D Puzzle of a Famous Landmark", "A fun and educational challenge.", 25.00, 60),
                new ProductData("Art and Craft Kit for Kids", "Unleash creativity with hundreds of pieces.", 35.00, 70),
                new ProductData("Plush Toy Collection", "Soft and cuddly companions for all ages.", 20.00, 100),
                new ProductData("Educational Science Kit", "Conduct fun experiments at home.", 45.00, 45),
                new ProductData("Family-Friendly Card Game", "Easy to learn and hilarious to play.", 15.00, 150),
                new ProductData("Wooden Train Set", "A classic toy for imaginative play.", 80.00, 30),
                new ProductData("Action Figure Set", "A team of heroes ready for adventure.", 50.00, 55)
            ),
            "Health & Beauty", List.of(
                new ProductData("Vitamin C Serum", "Brighten your skin and reduce fine lines.", 25.00, 80),
                new ProductData("Electric Toothbrush", "Achieve a dentist-clean feeling every day.", 70.00, 50),
                new ProductData("Essential Oil Diffuser", "Create a calming atmosphere with aromatherapy.", 40.00, 60),
                new ProductData("Organic Shampoo and Conditioner Set", "Nourish your hair with natural ingredients.", 35.00, 70),
                new ProductData("Sunscreen SPF 50", "Protect your skin from harmful UV rays.", 15.00, 120),
                new ProductData("Beard Grooming Kit", "Everything you need for a perfect beard.", 45.00, 40),
                new ProductData("Makeup Brush Set", "Professional quality brushes for a flawless application.", 30.00, 90),
                new ProductData("Anti-Aging Moisturizer", "Hydrate and firm your skin.", 50.00, 65),
                new ProductData("Hair Dryer with Ionic Technology", "Dry your hair faster with less frizz.", 85.00, 35),
                new ProductData("Manicure and Pedicure Set", "All the tools for a perfect at-home salon experience.", 55.00, 45)
            )
        );
    }
}