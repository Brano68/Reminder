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
        this(date, name, priority, done);
        this.price = price;
        this.id = null;
    }


    public Task(Date date, String name, int priority, boolean done) {
        this.date = date;
        this.name = name;
        this.done = done;
        this.priority = priority;
        this.price = -1;
    }

    public void setId(ObjectId id) {
        this.id = id;
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

    @Override
    public String toString() {
        if(price == -1){
            return "Task: " + "id: " + id + " Name: " + name + " Priority: " + priority + " Done: " + done + " Date: " + date;
        }else{
            return "Task: " + "id: " + id + " Name: " + name + " Priority: " + priority + " Done: " + done + " Date: " + date + "Price: " + price;
        }
    }
}
