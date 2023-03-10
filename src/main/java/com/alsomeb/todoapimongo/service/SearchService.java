package com.alsomeb.todoapimongo.service;

import com.alsomeb.todoapimongo.model.Todo;

import java.util.List;

// Impl av SearchServiceImpl

public interface SearchService {
    List<Todo> searchByCategory(String category);
}
