package com.jojolapa.cafemanagement.restcontroller;

import com.jojolapa.cafemanagement.model.Product;
import com.jojolapa.cafemanagement.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "http://localhost:5173") // React frontend
public class ProductController {

    @Autowired
    private ProductRepository repo;

    @PostMapping
    public Product addProduct(@RequestParam("name") String name,
                              @RequestParam("price") Double price,
                              @RequestParam("description") String description,
                              @RequestParam("image") MultipartFile image) throws IOException {

        // Create uploads directory if it doesn't exist
        Path uploadsDir = Paths.get("uploads");
        if (!Files.exists(uploadsDir)) {
            Files.createDirectories(uploadsDir);
        }

        // Generate unique filename
        String fileName = UUID.randomUUID() + "_" + image.getOriginalFilename();
        Path filePath = uploadsDir.resolve(fileName);

        // Save file to "uploads" folder
        Files.copy(image.getInputStream(), filePath);

        // Create product
        Product p = new Product();
        p.setName(name);
        p.setPrice(price);
        p.setDescription(description);
        p.setImageUrl("http://localhost:8080/uploads/" + fileName);

        return repo.save(p);
    }

    @GetMapping
    public List<Product> getAll() {
        return repo.findAll();
    }

    // Add this endpoint to get a product by ID
    @GetMapping("/{id}")
    public Product getProductById(@PathVariable Long id) {
        return repo.findById(id).orElse(null);
    }

    // Add this endpoint to update a product
    @PutMapping("/{id}")
    public Product updateProduct(@PathVariable Long id,
                                 @RequestParam(value = "name", required = false) String name,
                                 @RequestParam(value = "price", required = false) Double price,
                                 @RequestParam(value = "description", required = false) String description,
                                 @RequestParam(value = "image", required = false) MultipartFile image) throws IOException {

        Product existingProduct = repo.findById(id).orElse(null);
        if (existingProduct == null) {
            return null;
        }

        if (name != null) existingProduct.setName(name);
        if (price != null) existingProduct.setPrice(price);
        if (description != null) existingProduct.setDescription(description);

        if (image != null && !image.isEmpty()) {
            // Create uploads directory if it doesn't exist
            Path uploadsDir = Paths.get("uploads");
            if (!Files.exists(uploadsDir)) {
                Files.createDirectories(uploadsDir);
            }

            // Generate unique filename
            String fileName = UUID.randomUUID() + "_" + image.getOriginalFilename();
            Path filePath = uploadsDir.resolve(fileName);

            // Save file to "uploads" folder
            Files.copy(image.getInputStream(), filePath);
            existingProduct.setImageUrl("http://localhost:8080/uploads/" + fileName);
        }

        return repo.save(existingProduct);
    }

    // Add this endpoint to delete a product
    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id) {
        repo.deleteById(id);
    }
}