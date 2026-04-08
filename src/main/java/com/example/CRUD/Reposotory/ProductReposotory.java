package com.example.CRUD.Reposotory;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.CRUD.Model.Product;

public interface ProductReposotory extends JpaRepository<Product, Long> {
    
}
