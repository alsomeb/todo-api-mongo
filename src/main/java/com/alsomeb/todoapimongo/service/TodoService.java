package com.alsomeb.todoapimongo.service;

import com.alsomeb.todoapimongo.model.Todo;

import java.util.List;

// Implementeras av TodoServiceImpl

public interface TodoService {
    Todo save(Todo todo);

    List<Todo> all();

    void delete(String id);
    Todo saveOrUpdate(Todo todo);
}
