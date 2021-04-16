package org.example.mongo;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.example.collection.Task;

import java.util.Date;
import java.util.List;

public class MongoImpl implements Mongo{
    //database name: Reminder
    //database collection: myReminders

    //vytvorenie mongo klinta
    MongoClient mongo = new MongoClient( "localhost" , 27017 );
    //vytvorenie connection
    MongoDatabase database = mongo.getDatabase("Reminder");

    //skusobny main
    public static void main(String[] args) {
        Date date = new Date();
        String date1 = date.toString();
        //new MongoImpl().insertIntoReminder(date1, "Opravit bicykel",2, 15);
        Task task = new Task(date, "Umyt okno", 3, 0, false);
        //new MongoImpl().insertTask(task);

        String hexString = "6079505597dde44e325ff1a1";
        System.out.println(new ObjectId(hexString));
        new MongoImpl().setTaskToDone(new ObjectId(hexString));
    }

    //////
    //metoda na vlozenie do databazy mongo
    /*
    public void insertIntoReminder(String date, String name, int priorita, double price){
        //vytvorenie kolekcie iba raz
        //database.createCollection("myReminders");
        Document document = new Document();
        document.append("Date", date);
        document.append("Name", name);
        document.append("Done", false);
        document.append("Priorita", priorita);
        document.append("Price", price);
        //vkladanie dokumentu do kolekcie
        database.getCollection("myReminders").insertOne(document);
        System.out.println("Document inserted successfully");
    }

     */


    @Override
    public void insertTask(Task task) {
        //database.createCollection("myReminders");
        Document document = new Document();
        document.append("Date", task.getDate().toString());
        document.append("Name", task.getName());
        document.append("Done", task.isDone());
        document.append("Priority", task.getPriority());
        document.append("Price", task.getPrice());
        //vkladanie dokumentu do kolekcie
        database.getCollection("myReminders").insertOne(document);
        System.out.println("Document inserted successfully");
    }

    @Override
    public void setTaskToDone(ObjectId id) {
        //prejdeme celu kolekciu
        MongoCollection<Document> collection = database.getCollection("myReminders");
        //Updating a document
        UpdateResult updateResult = collection.updateOne(Filters.eq("_id", id), Updates.set("Done", true));
        System.out.println("Document update successfully...");
        System.out.println(updateResult);
        long i = updateResult.getModifiedCount();
        /*if(i == 0){
            return false;
        }
        return true;

         */
    }

    @Override
    public List<Task> getAllTasks() {
        return null;
    }

    @Override
    public List<Task> getAllTasks(boolean done) {
        return null;
    }

    @Override
    public List<Task> getAllTasksByPriority(int priority) {
        return null;
    }

    @Override
    public List<Task> getAllTasksByName() {
        return null;
    }

    @Override
    public void deleteDoneTasks() {

    }
}
