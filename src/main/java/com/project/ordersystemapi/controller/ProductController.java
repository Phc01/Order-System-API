package com.project.ordersystemapi.controller;

import com.project.ordersystemapi.dto.CreateProductDTO;
import com.project.ordersystemapi.dto.ProductResponseDTO;
import com.project.ordersystemapi.dto.UpdateProductDTO;
import com.project.ordersystemapi.model.Category;
import com.project.ordersystemapi.model.Product;
import com.project.ordersystemapi.repository.CategoryRepository;
import com.project.ordersystemapi.repository.ProductRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductController(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    // metodo helper para converter Product para ProductResponseDTO
    private ProductResponseDTO convertToDTO(Product product) {
        String categoryName = (product.getCategory() != null) ? product.getCategory().getName() : null;
        return new ProductResponseDTO(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStock(),
                categoryName
        );
    }

    @GetMapping
    public ResponseEntity<List<ProductResponseDTO>> getAllProducts() {
        List<ProductResponseDTO> productDTOs = productRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(productDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> getProductById(@PathVariable Long id) {
        return productRepository.findById(id)
                .map(this::convertToDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<ProductResponseDTO>> getProductsByCategory(@PathVariable Long categoryId) {
        if (!categoryRepository.existsById(categoryId)) { // Verifica se a categoria existe
            return ResponseEntity.notFound().build();
        }
        List<ProductResponseDTO> productDTOs = productRepository.findByCategoryId(categoryId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(productDTOs);
    }

    @PostMapping
    public ResponseEntity<ProductResponseDTO> createProduct(@RequestBody CreateProductDTO createProductDTO) {
        Category category = categoryRepository.findById(createProductDTO.getCategoryId()) // Encontra a categoria
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + createProductDTO.getCategoryId()));
        Product product = new Product();
        product.setName(createProductDTO.getName());
        product.setDescription(createProductDTO.getDescription());
        product.setPrice(createProductDTO.getPrice());
        product.setStock(createProductDTO.getStock());
        product.setCategory(category);

        Product savedProduct = productRepository.save(product);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedProduct.getId())
                .toUri();
        return ResponseEntity.created(location).body(convertToDTO(savedProduct));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> updateProduct(@PathVariable Long id, @RequestBody UpdateProductDTO updateProductDTO) {
        return productRepository.findById(id)
                .map(product -> {
                    if (updateProductDTO.getName() != null) {
                        product.setName(updateProductDTO.getName());
                    }
                    if (updateProductDTO.getDescription() != null) {
                        product.setDescription(updateProductDTO.getDescription());
                    }
                    if (updateProductDTO.getPrice() != null) {
                        product.setPrice(updateProductDTO.getPrice());
                    }
                    if (updateProductDTO.getStock() != null) {
                        product.setStock(updateProductDTO.getStock());
                    }
                    if (updateProductDTO.getCategoryId() != null) {
                        Category category = categoryRepository.findById(updateProductDTO.getCategoryId())
                                .orElseThrow(() -> new RuntimeException("Category not found with id: " + updateProductDTO.getCategoryId()));
                        product.setCategory(category);
                    }
                    Product updatedProduct = productRepository.save(product);
                    return ResponseEntity.ok(convertToDTO(updatedProduct));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
