package com.project.ordersystemapi.service;

import com.project.ordersystemapi.dto.CreateProductDTO;
import com.project.ordersystemapi.dto.ProductResponseDTO;
import com.project.ordersystemapi.dto.UpdateProductDTO;

import java.util.List;

public interface ProductService {

    List<ProductResponseDTO> getAllProducts();

    ProductResponseDTO getProductById(Long id);

    List<ProductResponseDTO> findByCategoryId(Long categoryId);

    ProductResponseDTO createProduct(CreateProductDTO createProductDTO);

    ProductResponseDTO updateProduct(Long id, UpdateProductDTO updateProductDTO);

    void deleteProduct(Long id);
}