package com.project.ordersystemapi.service;

import com.project.ordersystemapi.dto.CreateProductDTO;
import com.project.ordersystemapi.dto.ProductResponseDTO;
import com.project.ordersystemapi.dto.UpdateProductDTO;
import com.project.ordersystemapi.exception.ResourceNotFoundException;
import com.project.ordersystemapi.model.Product;
import com.project.ordersystemapi.repository.ProductRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    protected ProductResponseDTO convertToDTO(Product product) {
        if (product == null) {
            return null;
        }
        String categoryName = (product.getCategory() != null) ? product.getCategory().getName() : "Category not defined";
        return new ProductResponseDTO(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStock(),
                categoryName
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponseDTO> getAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponseDTO getProductById(Long id) {
        return productRepository.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Product not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponseDTO> findByCategoryId(Long categoryId) {
        return productRepository.findByCategoryId(categoryId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }


    @Override
    @Transactional
    public ProductResponseDTO createProduct(CreateProductDTO createProductDTO) {
        Product product = new Product();
        product.setName(createProductDTO.getName());

        if (productRepository.existsByName(createProductDTO.getName())) {
            throw new DataIntegrityViolationException("Product already exists with name: " + createProductDTO.getName());
        }

        product.setDescription(createProductDTO.getDescription());
        product.setPrice(createProductDTO.getPrice());
        product.setStock(createProductDTO.getStock());

        Product savedProduct = productRepository.save(product);

        return convertToDTO(savedProduct);
    }


    @Override
    @Transactional
    public ProductResponseDTO updateProduct(Long id, UpdateProductDTO updateProductDTO) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Product not found with id: " + id));

        boolean updated = false;

        if (updateProductDTO.getName() != null && !updateProductDTO.getName().equals(existingProduct.getName())) {
            if (productRepository.existsByName(updateProductDTO.getName())) {
                throw new DataIntegrityViolationException("Product already exists with name: " + updateProductDTO.getName());
            }
            existingProduct.setName(updateProductDTO.getName());
            updated = true;
        }

        if (updateProductDTO.getDescription() != null && !updateProductDTO.getDescription().equals(existingProduct.getDescription())) {
            existingProduct.setDescription(updateProductDTO.getDescription());
            updated = true;
        }

        if (updateProductDTO.getPrice() != null && updateProductDTO.getPrice().compareTo(existingProduct.getPrice()) != 0) {
            existingProduct.setPrice(updateProductDTO.getPrice());
            updated = true;
        }

        if (updateProductDTO.getStock() != null && !updateProductDTO.getStock().equals(existingProduct.getStock())) {
            existingProduct.setStock(updateProductDTO.getStock());
            updated = true;
        }

        if (updated) {
            Product updatedProduct = productRepository.save(existingProduct);
            return convertToDTO(updatedProduct);
        }

        return convertToDTO(existingProduct);
    }


    @Override
    @Transactional
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Product not found with id: " + id);
        }

        productRepository.deleteById(id);
    }
}
