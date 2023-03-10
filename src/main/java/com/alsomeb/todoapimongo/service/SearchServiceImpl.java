package com.alsomeb.todoapimongo.service;

// PRATAR MED DB LAYER och MongoClient

import com.alsomeb.todoapimongo.model.Todo;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.convert.MongoConverter;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class SearchServiceImpl implements SearchService {
    private MongoClient client;
    private MongoConverter converter;

    @Autowired // Constructor DI
    public SearchServiceImpl(MongoClient client, MongoConverter converter) {
        this.client = client;
        this.converter = converter;
    }

    // Search
    // Mongo DB Atlas SEARCH! Måste ställa in indexes på MongoDB
    // Gå in på min Collection "Search/Indexes" följ guide
    // Man kan välja själv vilka saker som skall indexeras annars görs allt (DYNAMIC)
    // Sedan kan man prova på MongoDB cloud genom: Search - ... - Edit with visual editor - Search tester
    // Aggregation MongoDB cloud
    // - Man kan skapa sin egna Pipeline på mongos hemsida
    // - Export to Java Language
    // - Får kod snippet som man får tweaka lite i sitt projekt, klar att använda
    @Override
    public List<Todo> searchByCategory(String category) {
        final List<Todo> found = new ArrayList<>();

        MongoDatabase database = client.getDatabase("alsomeb");
        MongoCollection<Document> collection = database.getCollection("TodoList");

        // Använd denna klass ist för den givna FindIterable
        AggregateIterable<Document> result = collection.aggregate(List.of(
                new Document("$match",
                        new Document("category",
                                new Document("$regex", Pattern.compile(category))
                                        .append("$options", "i")))));

        // Result is an iterable, so we can iterate through it and add it to found list
        // MongoConverter will convert this doc to a Todo object
        //  .read(classname.class, document)
        result.forEach(document -> found.add(converter.read(Todo.class, document)));

        return found;
    }
}
