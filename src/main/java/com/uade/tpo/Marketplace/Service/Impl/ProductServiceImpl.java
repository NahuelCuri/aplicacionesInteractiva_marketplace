package com.uade.tpo.Marketplace.Service.Impl;

import com.uade.tpo.Marketplace.DTOs.ProductCreateDTO;
import com.uade.tpo.Marketplace.DTOs.ProductDetailDTO;
import com.uade.tpo.Marketplace.DTOs.ProductListDTO;
import com.uade.tpo.Marketplace.DTOs.ProductUpdateDTO; // Make sure this import matches your structure
import com.uade.tpo.Marketplace.DTOs.Mapper.ProductMapper;
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
import com.uade.tpo.Marketplace.Service.ProductService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductImageRepository productImageRepository;

    @Autowired
    private RoleRepository roleRepository;

     private User getCurrentSeller() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        String email = null;

        // 1. Extract the email based on what the Principal object actually is
        if (principal instanceof UserDetails) {
            // This works because your User entity implements UserDetails
            email = ((UserDetails) principal).getUsername();
        } else if (principal instanceof String) {
            email = (String) principal;
        }

        if (email == null) {
            throw new RuntimeException("Authentication error: No email found in Security Context");
        }

        // 2. RELOAD the user from the database.
        // This is crucial! It ensures we have a fresh 'Entity' attached to the current
        // transaction, preventing LazyInitializationException when we access user.getRoles().
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Seller not found"));
    }

    @Override
    public List<ProductListDTO> getAllProducts() {
        return productRepository.findAllByDeletedFalse()
                .stream()
                .map(ProductMapper::toSimpleDTO)
                .toList();
    }

    @Override
    public ProductDetailDTO getProductById(Long id) {
        Product product = productRepository.findById(id)
                .filter(p -> !p.isDeleted())
                .orElseThrow(() -> new RuntimeException("Product not found"));
        return ProductMapper.toDetailDTO(product);
    }

    @Override
    public List<ProductListDTO> searchProductsByName(String name) {
        return productRepository.findByNameContainingIgnoreCaseAndDeletedFalse(name)
                .stream()
                .map(ProductMapper::toSimpleDTO)
                .toList();
    }

    @Override
    public List<ProductListDTO> getProductsByCategory(Long categoryId) {
        return productRepository.findByCategoryIdAndDeletedFalse(categoryId)
                .stream()
                .map(ProductMapper::toSimpleDTO)
                .toList();
    }

    @Override
    public ProductDetailDTO createProduct(ProductCreateDTO dto) {
        // Use the helper method instead of casting manually
        User seller = getCurrentSeller();

        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        Product product = new Product();
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setStock(dto.getStock());
        product.setDiscountPercentage(dto.getDiscountPercentage());
        product.setCategory(category);
        product.setSeller(seller);
        product.setImages(new ArrayList<>());

        if (dto.getImages() != null) {
            for (MultipartFile file : dto.getImages()) {
                try {
                    ProductImage img = new ProductImage();
                    img.setImageData(file.getBytes());
                    img.setProduct(product);
                    product.getImages().add(img);
                } catch (Exception e) {
                    throw new RuntimeException("Error saving image", e);
                }
            }
        }
        
        boolean isAlreadySeller = seller.getRoles().stream()
                                        .anyMatch(role -> "SELLER".equals(role.getName()));

        if (!isAlreadySeller) {
            Role sellerRole = roleRepository.findByName("SELLER")
                    .orElseThrow(() -> new RuntimeException("Error: Role 'SELLER' not found in database."));
            
            seller.getRoles().add(sellerRole);
            userRepository.save(seller); 
        }

        Product savedProduct = productRepository.save(product);
        return ProductMapper.toDetailDTO(savedProduct);
    }

    @Override
    public List<ProductListDTO> getProductsBySeller() {
        // Use the helper method
        User seller = getCurrentSeller();

        return productRepository.findBySellerIdAndDeletedFalse(seller.getId())
                .stream()
                .map(ProductMapper::toSimpleDTO)
                .toList();
    }

    @Override
    public List<ProductListDTO> searchProductsByNameAndSeller(String name) {
        // Use the helper method
        User seller = getCurrentSeller();

        return productRepository.findByNameContainingIgnoreCaseAndSellerAndDeletedFalse(name, seller)
                .stream()
                .map(ProductMapper::toSimpleDTO)
                .toList();
    }

    @Override
    public ProductDetailDTO updateProduct(Long id, ProductUpdateDTO productUpdateDTO) {
        // Use the helper method
        User seller = getCurrentSeller();

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (!product.getSeller().getId().equals(seller.getId())) {
            throw new RuntimeException("You are not authorized to update this product");
        }

        product.setName(productUpdateDTO.getName());
        product.setDescription(productUpdateDTO.getDescription());
        product.setPrice(productUpdateDTO.getPrice());
        product.setStock(productUpdateDTO.getStock());
        product.setDiscountPercentage(productUpdateDTO.getDiscountPercentage());

        Category category = categoryRepository.findById(productUpdateDTO.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));
        product.setCategory(category);

        if (productUpdateDTO.getImagesToDelete() != null) {
            for (String imageId : productUpdateDTO.getImagesToDelete()) {
                // Ensure the ID is valid before parsing
                if (imageId != null && !imageId.isEmpty()) {
                   productImageRepository.deleteById(Long.parseLong(imageId));
                }
            }
        }

        if (productUpdateDTO.getNewImages() != null) {
            for (MultipartFile file : productUpdateDTO.getNewImages()) {
                try {
                    ProductImage img = new ProductImage();
                    img.setImageData(file.getBytes());
                    img.setProduct(product);
                    productImageRepository.save(img);
                } catch (Exception e) {
                    throw new RuntimeException("Error saving image", e);
                }
            }
        }

        Product updatedProduct = productRepository.save(product);
        return ProductMapper.toDetailDTO(updatedProduct);
    }

    @Override
    public void deleteProduct(Long id) {
        // Use the helper method
        User seller = getCurrentSeller();

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (!product.getSeller().getId().equals(seller.getId())) {
            throw new RuntimeException("You are not authorized to delete this product");
        }

        product.setDeleted(true);
        productRepository.save(product);
    }
}