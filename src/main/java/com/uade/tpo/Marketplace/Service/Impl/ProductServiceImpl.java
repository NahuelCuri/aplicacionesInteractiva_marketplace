package com.uade.tpo.Marketplace.Service.Impl;



import com.uade.tpo.Marketplace.DTOs.ProductCreateDTO;
import com.uade.tpo.Marketplace.DTOs.ProductDetailDTO;
import com.uade.tpo.Marketplace.DTOs.ProductListDTO;
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
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private  ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductImageRepository productImageRepository;

    @Autowired
    private RoleRepository roleRepository;

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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName();
        User seller = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new RuntimeException("Seller not found"));

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
        product.setImages(new java.util.ArrayList<>());

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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName();
        User seller = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new RuntimeException("Seller not found"));

        return productRepository.findBySellerIdAndDeletedFalse(seller.getId())
                .stream()
                .map(ProductMapper::toSimpleDTO)
                .toList();
    }

    @Override
    public List<ProductListDTO> searchProductsByNameAndSeller(String name) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName();
        User seller = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new RuntimeException("Seller not found"));

        return productRepository.findByNameContainingIgnoreCaseAndSellerAndDeletedFalse(name, seller)
                .stream()
                .map(ProductMapper::toSimpleDTO)
                .toList();
    }

    @Override
    public ProductDetailDTO updateProduct(Long id, com.uade.tpo.Marketplace.DTOs.ProductUpdateDTO productUpdateDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName();
        User seller = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new RuntimeException("Seller not found"));

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
                productImageRepository.deleteById(Long.parseLong(imageId));
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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName();
        User seller = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new RuntimeException("Seller not found"));

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (!product.getSeller().getId().equals(seller.getId())) {
            throw new RuntimeException("You are not authorized to delete this product");
        }

        product.setDeleted(true);
        productRepository.save(product);
    }
}
