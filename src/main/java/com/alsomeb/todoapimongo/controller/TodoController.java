package com.alsomeb.todoapimongo.controller;

import com.alsomeb.todoapimongo.model.Todo;
import com.alsomeb.todoapimongo.service.SearchService;
import com.alsomeb.todoapimongo.service.TodoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin // Open for all, can lock endpoints later with Spring Security perhaps (https://www.baeldung.com/spring-cors)
@RequestMapping("/api/todo") // eg. localhost:8081/api/todo/.. root url
public class TodoController {

    // Controller pratar med Service Layer, DI Interfaces
    private final TodoService todoService;
    private final SearchService searchService;

    // Logging
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    public TodoController(TodoService todoService, SearchService searchService) {
        this.todoService = todoService;
        this.searchService = searchService;
    }

    // Swagger UI available at .../swagger
    // @ApiIgnore removes all the default predefined HTTP requests, WE WILL DEFINE THEM BY OURSELVES (IN HERE, SWAGGER AUTO DETECT?)
    @ApiIgnore
    @RequestMapping(value = "/swagger")
    public void redirect(HttpServletResponse response) throws IOException {
        response.sendRedirect("/swagger-ui/");
    }

    // Fetch All Todos in DB
    @GetMapping()
    public List<Todo> getAllTodos() {
        logger.debug("Showing {} todos", todoService.all().size());
        return todoService.all();
    }

    // @PathVariable == Whatever category is incl inside the param is put in this String
    @GetMapping("{category}")
    public List<Todo> searchByCategory(@PathVariable String category) {
        logger.debug("Showing todos by category: {}", category);
        return searchService.searchByCategory(category);
    }

    // @RequestBody, This annotation indicates that Spring should deserialize a request body into an object
    @PostMapping()
    public Todo save(@RequestBody Todo todo) {
        return todoService.save(todo);
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable String id) {
        logger.debug("Deleted Todo with ID: {}", id);
        todoService.delete(id);
    }

    // Förbättring att göra: Return Long Id ist för hela Todo Object eftersom det kan en GET req göra
    // Denna kommer Update befintlig Todo eller skapa en ny, 2 flugor 1 smäll :)
    @PutMapping
    public Todo update(@RequestBody Todo todo) {
        return todoService.saveOrUpdate(todo);
    }



}
