package com.alsomeb.todoapimongo.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

// Man måste säga vilken Collection i DB som vi skall jobba från
// Behöver ej skriva Collections pga value är samma i annotering!

// Behöver ej ange Id pga MongoDB genererar en ObjectId('.....') automagiskt
@Document("TodoList")
public class Todo {

    @Id
    private String id;
    private String desc;
    private LocalDate dateAdded;
    private String category;
    private LocalDate latestUpdated;

    public Todo() {
        dateAdded = LocalDate.now();
        latestUpdated = LocalDate.now();
    }

    public Todo(String desc, String category) {
        this.desc = desc;
        this.category = category;
        dateAdded = LocalDate.now();
        latestUpdated = LocalDate.now();
    }

    public String getId() {
        return id;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public LocalDate getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(LocalDate dateAdded) {
        this.dateAdded = dateAdded;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public LocalDate getLatestUpdated() {
        return latestUpdated;
    }

    public void setLatestUpdated(LocalDate latestUpdated) {
        this.latestUpdated = latestUpdated;
    }

    @Override
    public String toString() {
        return "Todo{" +
                "id=" + id +
                ", desc='" + desc + '\'' +
                ", dateAdded=" + dateAdded +
                ", category='" + category + '\'' +
                ", latestUpdated=" + latestUpdated +
                '}';
    }
}
