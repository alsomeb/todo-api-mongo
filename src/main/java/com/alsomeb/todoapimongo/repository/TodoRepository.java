package com.alsomeb.todoapimongo.repository;

import com.alsomeb.todoapimongo.model.Todo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TodoRepository extends MongoRepository<Todo, String> {
    // här är det spring magic, MongoRepository tar hand om CRUD operations
    // TodoRepo pratar bara med Service

    // La till några Query Methods, finns mer på dokumentation
    // https://docs.spring.io/spring-data/mongodb/docs/1.2.0.RELEASE/reference/html/mongo.repositories.html
    // Most of the data access operations you usually trigger on a repository result a query being executed against the MongoDB databases.
    // Defining such a query is just a matter of declaring a method on the repository interface
    List<Todo> findTodoByCategory(String category);
    List<Todo> findTodoByDesc(String desc);
}
