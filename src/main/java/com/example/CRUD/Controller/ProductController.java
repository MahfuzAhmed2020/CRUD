package com.example.CRUD.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.CRUD.Model.Product;
import com.example.CRUD.Reposotory.ProductReposotory;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api")
public class ProductController {

    private final ProductReposotory productReposotory;
    public ProductController(ProductReposotory productReposotory) {
        this.productReposotory = productReposotory;
    }   

//http://localhost:8080/api/hello
@GetMapping("/hello")
public String sayHello(){ 
    return "Hello, World! And welcome to Spring Boot CRUD API.";
}


//http://localhost:8080/api/hello/details?name=mahfuz&product=pasta&id=5
@GetMapping("/hello/details")
public Map<String, Object> getDetails(
    @RequestParam(value = "name", required = false, defaultValue = "") String name,
    @RequestParam(value = "product", required = false, defaultValue = "") String product,
    @RequestParam(value = "id", required = false) Long id
) {
    Map<String, Object> response = new HashMap<>();

    response.put("message", "hello there");

    if (!name.isEmpty()) {
        response.put("name", name.toUpperCase());
    }

    if (id != null) {
        response.put("id", id);
    }

    if (!product.isEmpty()) {
        response.put("product", product.toUpperCase());
    }

    return response;
}


//http://localhost:8080/api/user/browser?name=mahfuz&product=pasta&id=500 
@GetMapping("/user/browser") 
public String get( @RequestParam(value = "name", required = false, defaultValue = "") String name, 
@RequestParam(value = "product", required = false, defaultValue = "") String product,
 @RequestParam(value = "id", required = false) Long id )  { 
    StringBuilder response = new StringBuilder("hello there");
     if (!name.isEmpty()) { response.append(" ").append(name.toUpperCase()); } 
     if (id != null) { response.append(" (ID: ").append(id).append(")"); } 
     if (!product.isEmpty()) { response.append(". You are looking for: ").append(product.toUpperCase()); }
      return response.toString(); 
    }

//===================================================================
// //http://localhost:8080/api/products 
// @PostMapping("/products")
// public ResponseEntity<Product> createProduct(@RequestBody Product product) {
//    Product savedProduct =  productReposotory.save(product);
  
//         return ResponseEntity.status(201).body(savedProduct);
// }

//http://localhost:8080/api/products 
@PostMapping("/products")
public ResponseEntity<Product> createProduct(@RequestBody Product product) {

    Product savedProduct = productReposotory.save(product);

    URI location = URI.create("/api/products/" + savedProduct.getId());

//     Map<String, Object> response = new HashMap<>();
//    response.put("message", "Product created successfully");
//    response.put("data", savedProduct);

//     return ResponseEntity
//             .created(location)
//             .body(response);

return ResponseEntity
        .created(location)
        .body(savedProduct);
}

//http://localhost:8080/api/products 
@GetMapping("/products")
public ResponseEntity<List<Product>> getAllProducts() {
    return ResponseEntity.ok(productReposotory.findAll());
}

//http://localhost:8080/api/products/search/1 
//GET http://localhost:8080/api/products/search
//GET http://localhost:8080/api/products/search?id=1&name=pasta&price=10
//GET http://localhost:8080/api/products/search?name=pasta

@GetMapping("/products/search")
public ResponseEntity<List<Product>> searchProducts(
        @RequestParam(value = "id", required = false) Long id,
        @RequestParam(value = "name", required = false) String name,
        @RequestParam(value = "price", required = false) Double price) {

    // Start with all products
    List<Product> products = productReposotory.findAll();

    // Filter by ID
    if (id != null) {
        products.removeIf(p -> !p.getId().equals(id));
    }

    // Filter by name
    if (name != null && !name.isEmpty()) {
        products.removeIf(p -> !p.getName().equalsIgnoreCase(name));
    }

    // Filter by price
    if (price != null) {
        products.removeIf(p -> p.getPrice() != price);
    }

    if (products.isEmpty()) {
        return ResponseEntity.notFound().build();
    }

    return ResponseEntity.ok(products);
}
//http://localhost:8080/api/products/4
@PutMapping("/products/{id}")
public ResponseEntity<Product> updateProduct(
        @PathVariable Long id,
        @RequestBody Product updatedProduct) {

    return productReposotory.findById(id)
            .map(product -> {
                product.setName(updatedProduct.getName());
                product.setPrice(updatedProduct.getPrice());
                Product saved = productReposotory.save(product);
                return ResponseEntity.ok(saved); // 200 OK
            })
            .orElse(ResponseEntity.notFound().build());
}

// http://localhost:8080/api/products/1
@PatchMapping("/products/{id}")
public ResponseEntity<Map<String, Object>> patchProductWithOldNew(
        @PathVariable Long id,
        @RequestBody Map<String, Object> updates) {

    // Find the product by ID
    return productReposotory.findById(id)
            .map(product -> {

                // Map to store response (old and new values)
                Map<String, Object> response = new HashMap<>();

                // Store old values
                Map<String, Object> oldValues = new HashMap<>();
                oldValues.put("name", product.getName());
                oldValues.put("price", product.getPrice());

                // Prepare new values map
                Map<String, Object> newValues = new HashMap<>();

                // Update name if present
                if (updates.containsKey("name")) {
                    String newName = (String) updates.get("name");
                    product.setName(newName);
                    newValues.put("name", newName);
                } else {
                    newValues.put("name", product.getName()); // unchanged
                }

                // Update price if present
                if (updates.containsKey("price")) {
                    Object priceObj = updates.get("price");
                    if (priceObj instanceof Number) {
                        double newPrice = ((Number) priceObj).doubleValue();
                        product.setPrice(newPrice);
                        newValues.put("price", newPrice);
                    }
                } else {
                    newValues.put("price", product.getPrice()); // unchanged
                }

                // Save updated product
                productReposotory.save(product);

                // Prepare final response
                response.put("message", "Product updated successfully");
                response.put("oldValues", oldValues);
                response.put("newValues", newValues);

                // Return response with HTTP 200 OK
                return ResponseEntity.ok(response);

            })
            .orElse(ResponseEntity.notFound().build()); // 404 if product not found
}

@DeleteMapping("/products/{id}")
public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {

    if (!productReposotory.existsById(id)) {
        return ResponseEntity.notFound().build(); // 404 if not exists
    }

    productReposotory.deleteById(id);
    return ResponseEntity.noContent().build(); // 204 No Content
}

//allow deleting by query parameters, e.g., name
@DeleteMapping("/products")
public ResponseEntity<Void> deleteByName(@RequestParam(value = "name") String name) {

    List<Product> products = productReposotory.findAll();
    products.removeIf(p -> !p.getName().equalsIgnoreCase(name));

    if (products.isEmpty()) {
        return ResponseEntity.notFound().build();
    }

    productReposotory.deleteAll(products);
    return ResponseEntity.noContent().build();
}


}

