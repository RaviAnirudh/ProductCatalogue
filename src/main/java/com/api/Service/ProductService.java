package com.api.Service;

import com.api.Entity.Product;
import com.api.Helper.ProductHelper;
import com.api.Respository.ProductRepository;
import com.api.error.ProductNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLConnection;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    public void saveProductsToDb(MultipartFile file){
        try{
            List<Product> products = ProductHelper.convertExcelToListOfProduct(file.getInputStream());
            productRepository.saveAll(products);

        }catch (IOException e){
            throw new RuntimeException("fail to store csv data: " + e.getMessage());
        }
    }
    public void saveProductsCSV(MultipartFile file) {
        try {
            List<Product> data = ProductHelper.csvToProducts(file.getInputStream());
            productRepository.saveAll(data);

        } catch (IOException e) {
            throw new RuntimeException("fail to store csv data: " + e.getMessage());
        }
    }
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
    public Page<Product> getProductsWithPagination(int offset, int pageSize){
        Page<Product> products = productRepository.findAll(PageRequest.of(offset, pageSize));
        return products;
    }

    public Optional<Product> getProductById(String id) throws ProductNotFoundException {
        Optional<Product> product = productRepository.findByProductId(id);
        if (!product.isPresent())
                throw new ProductNotFoundException("ProductId");
        return product;
    }
    public List<Product> getProductDetailsByName(String name) throws ProductNotFoundException {
        List<Product> products = productRepository.findByProductName(name);
        if (products.isEmpty()){
            throw new ProductNotFoundException("ProductName");
        }
        return products;
    }

    public List<Product> getProductDetailsByMerchantId(Long id) throws ProductNotFoundException {
        List<Product> products = productRepository.findByMerchantId(id);
        if (products.isEmpty()){
            throw new ProductNotFoundException("MerchantID");
        }
        return products;

    }
    public List<Product> getProductDetailsByProductCategory(String productCategory) throws ProductNotFoundException {
        List<Product> products = productRepository.findByProductCategory(productCategory);
        if(products.isEmpty()){
            throw new ProductNotFoundException("Category");
        }
        return products;
    }

    public Product saveProductQuantity(Product product) {
        return productRepository.save(product);
    }
    public List<Product> sortProductsByPriceDesc() {
        return productRepository.findAll(Sort.by(Sort.Direction.DESC,"productPrice"));
    }
    public List<Product> sortProductsByPriceAsc() {
        return productRepository.findAll(Sort.by(Sort.Direction.ASC,"productPrice"));
    }

    public void updateStatus(Boolean value, String productId) throws ProductNotFoundException {
        Optional<Product> product = getProductById(productId);
        if(product.isPresent()){
            product.get().setActive(value);
            productRepository.save(product.get());
        }
        else
            throw new ProductNotFoundException("ProductId");
    }
    public Page<Product> findProductsWithPaginationAndSorting(int offset,int pageSize,String field,Boolean flag){
        Page<Product> products = null;
        if (flag){
            products = productRepository.findAll(PageRequest.of(offset, pageSize).withSort(Sort.by(field)));
        }
        else{
            products = productRepository.findAll(PageRequest.of(offset, pageSize).withSort(Sort.by(field).descending()));
        }
        return  products;
    }
    public Page<Product> findPaginatedByProductCategory(Pageable pageable, String category){
        Page<Product> products = productRepository.findByProductCategory(category,pageable);
        return products;
    }
    public Page<Product> findPaginatedByProductName(Pageable pageable, String name){
        Page<Product> products = productRepository.findByProductName(name,pageable);
        return products;
    }

    public Page<Product> findPaginatedByMerchantId(Pageable pageable, Long id) {
        Page<Product> products = productRepository.findByMerchantId(id,pageable);
        return products;
    }
}
