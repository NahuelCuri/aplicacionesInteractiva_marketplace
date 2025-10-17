package com.uade.tpo.Marketplace.Controller;


import com.uade.tpo.Marketplace.DTOs.ProductCreateDTO;
import com.uade.tpo.Marketplace.DTOs.ProductDetailDTO;
import com.uade.tpo.Marketplace.DTOs.ProductListDTO;
import com.uade.tpo.Marketplace.Service.ProductService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private  ProductService productService;

    @GetMapping
    public ResponseEntity<List<ProductListDTO>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDetailDTO> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @GetMapping("/search")
    public ResponseEntity<List<ProductListDTO>> searchProductsByName(@RequestParam String name) {
        return ResponseEntity.ok(productService.searchProductsByName(name));
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<ProductListDTO>> getProductsByCategory(@PathVariable Long categoryId) {
        return ResponseEntity.ok(productService.getProductsByCategory(categoryId));
    }

    @PostMapping(consumes = {"multipart/form-data"})
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<ProductDetailDTO> createProduct(@ModelAttribute ProductCreateDTO productCreateDTO) {
        System.out.println("Received productCreateDTO: " + productCreateDTO);
        ProductDetailDTO created = productService.createProduct(productCreateDTO);
        return ResponseEntity.ok(created);
    }

    @GetMapping("/my-products")
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<List<ProductListDTO>> getProductsBySeller() {
        return ResponseEntity.ok(productService.getProductsBySeller());
    }

    @GetMapping("/my-products/search")
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<List<ProductListDTO>> searchProductsByNameAndSeller(@RequestParam String name) {
        return ResponseEntity.ok(productService.searchProductsByNameAndSeller(name));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<ProductDetailDTO> updateProduct(@PathVariable Long id, @ModelAttribute com.uade.tpo.Marketplace.DTOs.ProductUpdateDTO productUpdateDTO) {
        return ResponseEntity.ok(productService.updateProduct(id, productUpdateDTO));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}

