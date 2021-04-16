package org.example.collection;

import org.bson.types.ObjectId;

import java.util.Date;

public class Task {
    private ObjectId id;
    private Date date;
    private String name;
    private boolean done;
    private int priority;
    private double price;


    public Task(Date date, String name, int priority, double price, boolean done){
        this.date = date;
        this.name = name;
        this.priority = priority;
        this.price = price;
        this.done = done;
    }

    public ObjectId getId() {
        return id;
    }

    public Date getDate() {
        return date;
    }

    public String getName() {
        return name;
    }

    public boolean isDone() {
        return done;
    }

    public int getPriority() {
        return priority;
    }

    public double getPrice() {
        return price;
    }
}
