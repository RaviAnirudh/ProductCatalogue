package com.api.controller;

import com.api.Entity.Product;
import com.api.Entity.Response;
import com.api.Helper.ProductHelper;
import com.api.Service.ProductService;
import com.api.error.ProductNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@RestController
public class ProductController {
    @Autowired
    private ProductService productService;

    @PostMapping("/product/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        String message = "";
        if (ProductHelper.checkDocumentFormat(file)) {
            try {
                productService.saveProductsToDb(file);
                HashMap<String, String> msg = new HashMap<>();
                msg.put("message", "File Uploaded Successfully");
                return ResponseEntity.ok(msg);
            } catch (Exception e) {
                message = "Could not upload the file: " + file.getOriginalFilename() + "!";
                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body((message));
            }
        }
        if (ProductHelper.hasCSVFormat(file)) {
            try {
                productService.saveProductsCSV(file);
                HashMap<String, String> msg = new HashMap<>();
                msg.put("message", "File Uploaded Successfully");
                return ResponseEntity.status(HttpStatus.OK).body(msg);
            } catch (Exception e) {
                message = "Could not upload the file: " + file.getOriginalFilename() + "!";
                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body((message));
            }
        }
        message = "Please upload a csv or xlsx file!";
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
    }

    @GetMapping("/product")
    public ResponseEntity<Response> getProductCatalogue(@RequestParam int offset, @RequestParam int pageSize) {
        Page<Product> products = productService.getProductsWithPagination(offset, pageSize);
        Response api = new Response();
        api.setResponseCode(HttpStatus.OK.value());
        api.setResponseMessage(" Products fetched Successfully ");
        api.setPayloads(products.getContent());
        return new ResponseEntity<Response>(api, HttpStatus.OK);
    }

    @GetMapping("/product/{id}")
    public ResponseEntity<?> getProductById(@PathVariable("id") String id) throws ProductNotFoundException {
        Optional<Product> product = null;
        product = productService.getProductById(id);
        List<Product> products = new ArrayList<>();
        products.add(product.get());
        Response api = new Response();
        api.setResponseCode(HttpStatus.OK.value());
        api.setResponseMessage(" Product fetched Successfully ");
        api.setPayloads(products);
        return new ResponseEntity<Response>(api, HttpStatus.OK);
    }

    @PostMapping("/product/quantity/{productId}")
    public ResponseEntity<?> saveProductQuantity(@PathVariable("productId") String productId, @RequestParam("quantity") Long quantity) throws ProductNotFoundException {
        Optional<Product> product = productService.getProductById(productId);
        Product product1 = productService.saveProductQuantity(product.get());
        List<Product> products = new ArrayList<>();
        products.add(product1);
        Response api = new Response();
        api.setResponseCode(HttpStatus.CREATED.value());
        api.setResponseMessage(" Product Quantity Updated Successfully ");
        api.setPayloads(products);
        product.get().setProductQuantity(quantity);
        return new ResponseEntity<Response>(api, HttpStatus.OK);

    }

    @GetMapping("/product/merchantId/{productId}")
    public Long getMerchantID(@PathVariable("productId") String productId) throws ProductNotFoundException {
        Optional<Product> product = productService.getProductById(productId);
        Long id = product.get().getMerchantId();
        return id;
    }

    @GetMapping("/product/filter")
    public ResponseEntity<Response> switchFilters
            (@RequestParam(required = false) String category,
             @RequestParam(required = false) String name,
             @RequestParam(required = false) String sort,
             @RequestParam(required = false) Long id,
             @RequestParam(defaultValue = "0") int page,
             @RequestParam(defaultValue = "3") int size) throws ProductNotFoundException {
        List<Product> products = new ArrayList<>();
        Pageable paging = PageRequest.of(page, size);
        Page<Product> productsWithPagination = null;
        int switchVar = 0;
        if (category != null) {
            switchVar = 1;
        }
        if (sort != null) {
            switchVar = 2;
        }
        if (name != null) {
            switchVar = 3;
        }
        if (id != null) {
            switchVar = 4;
        }
        if (sort != null && category != null) {
            switchVar = 5;
        }
        if (sort != null && id != null) {
            switchVar = 6;
        }
        if (sort != null && name != null)
            switchVar = 7;
        List<Product> productList = new ArrayList<>();
        switch (switchVar) {
            case 1:
//                products = productService.getProductDetailsByProductCategory(category);
                products = productService.findPaginatedByProductCategory(paging, category).getContent();
                break;
            case 2:
                if (sort.equalsIgnoreCase("Asc")) {
                    productsWithPagination = productService.findProductsWithPaginationAndSorting(page, size, "productPrice", true);
                    products = productsWithPagination.getContent();
                } else {
                    productsWithPagination = productService.findProductsWithPaginationAndSorting(page, size, "productPrice", false);
                    products = productsWithPagination.getContent();
                }
                break;
            case 3:
//                products = productService.getProductDetailsByName(name);
                products = productService.findPaginatedByProductName(paging, name).getContent();
                break;
            case 4:
//                products = productService.getProductDetailsByMerchantId(id);
                products = productService.findPaginatedByMerchantId(paging, id).getContent();
                break;
            case 5:
                products = productService.getProductDetailsByProductCategory(category);
//                products = productService.findPaginatedByProductCategory(paging, category).getContent();
                sorting(products, sort);
                break;
            case 6:
                products = productService.getProductDetailsByMerchantId(id);
//                products = productService.findPaginatedByMerchantId(paging, id).getContent();
                sorting(products, sort);
                break;
            case 7:
                products = productService.getProductDetailsByName(name);
//                products = productService.findPaginatedByProductName(paging, name).getContent();
                sorting(products, sort);
                break;
        }

        Response api = new Response();
        api.setResponseCode(HttpStatus.OK.value());
        api.setResponseMessage("Products Fetched Successfully!");
        api.setPayloads(products);
        return new ResponseEntity<Response>(api, HttpStatus.CREATED);
    }

    @PutMapping("/product/status/{id}")
    public ResponseEntity<String> updateStatus(@RequestParam Boolean value, @PathVariable("id") String productId) throws ProductNotFoundException {
        productService.updateStatus(value, productId);
        return new ResponseEntity<>("Status Changed Successfully", HttpStatus.OK);
    }

    public void sorting(List<Product> arr, String sort) {
        if (sort.equalsIgnoreCase("Asc"))
            Collections.sort(arr, Comparator.comparing(Product::getProductPrice));
        else
            Collections.sort(arr, Comparator.comparing(Product::getProductPrice).reversed());

    }
//    @GetMapping("/product/sorting/{field}")
//    public ResponseEntity<Response> paginationWithSorting(@RequestParam int offset,@RequestParam int pageSize,
//                                                          @PathVariable String field){
//        List<Product> products = new ArrayList<>();
//        Page<Product> productsWithPagination = null;
//        productsWithPagination = productService.findProductsWithPaginationAndSorting(offset, pageSize, field);
//        products = productsWithPagination.getContent();
//        Response api = new Response();
//        api.setResponseCode(HttpStatus.OK.value());
//        api.setResponseMessage("Products Fetched Successfully!");
//        api.setPayloads(products);
//        return new  ResponseEntity<Response>(api, HttpStatus.CREATED);
//    }


}
