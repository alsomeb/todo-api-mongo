package com.alsomeb.todoapimongo.service;

// @Service bara på klasserna i service inte Interfaces

import com.alsomeb.todoapimongo.model.Todo;
import com.alsomeb.todoapimongo.repository.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class TodoServiceImpl implements TodoService {
    // talks to db layer
    private final TodoRepository todoRepository;

    @Autowired
    public TodoServiceImpl(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    // POST
    @Override
    public Todo save(Todo todo) {
        return todoRepository.save(todo);
    }

    // GET, ALL
    @Override
    public List<Todo> all() {
        return todoRepository.findAll();
    }

    @Override
    public void delete(String id) {
        todoRepository.deleteById(id);
    }


    // either creates a new one or updates existing
    // Notera att desc/category fält i JSON måste fyllas i / upd annars bli något null
    // date sköter vi i backend
    @Override
    public Todo saveOrUpdate(Todo todo) {
        todo.setLatestUpdated(LocalDate.now());
        return todoRepository.save(todo);
    }
}
