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
        return productRepository.findAll()
                .stream()
                .map(ProductMapper::toSimpleDTO)
                .toList();
    }

    @Override
    public ProductDetailDTO getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        return ProductMapper.toDetailDTO(product);
    }

    @Override
    public List<ProductListDTO> searchProductsByName(String name) {
        return productRepository.findByNameContainingIgnoreCase(name)
                .stream()
                .map(ProductMapper::toSimpleDTO)
                .toList();
    }

    @Override
    public List<ProductListDTO> getProductsByCategory(Long categoryId) {
        return productRepository.findByCategoryId(categoryId)
                .stream()
                .map(ProductMapper::toSimpleDTO)
                .toList();
    }

    @Override
    public ProductDetailDTO createProduct(ProductCreateDTO dto) {
        Product product = new Product();
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setStock(dto.getStock());
        product.setDiscountPercentage(dto.getDiscountPercentage());


        User seller = userRepository.findById(dto.getSellerId())
                .orElseThrow(() -> new RuntimeException("Seller not found"));

        boolean isAlreadySeller = seller.getRoles().stream()
                                        .anyMatch(role -> "SELLER".equals(role.getName()));

        if (!isAlreadySeller) {
            Role sellerRole = roleRepository.findByName("SELLER")
                    .orElseThrow(() -> new RuntimeException("Error: Role 'SELLER' not found in database."));
            
            seller.getRoles().add(sellerRole);
            userRepository.save(seller); 
        }
        
        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));
        product.setCategory(category);
        product.setSeller(seller);

        Product savedProduct = productRepository.save(product);

        if (dto.getImages() != null) {
            for (MultipartFile file : dto.getImages()) {
                try {
                    ProductImage img = new ProductImage();
                    img.setImageData(file.getBytes());
                    img.setProduct(savedProduct);
                    productImageRepository.save(img);
                } catch (Exception e) {
                    throw new RuntimeException("Error saving image", e);
                }
            }
        }

        savedProduct = productRepository.findById(savedProduct.getId())
                .orElseThrow(() -> new RuntimeException("Error fetching saved product"));

        return ProductMapper.toDetailDTO(savedProduct);
    }

    @Override
    public List<ProductListDTO> getProductsBySeller() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName();
        User seller = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new RuntimeException("Seller not found"));

        return productRepository.findBySellerId(seller.getId())
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

        return productRepository.findByNameContainingIgnoreCaseAndSeller(name, seller)
                .stream()
                .map(ProductMapper::toSimpleDTO)
                .toList();
    }
}

