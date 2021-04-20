package org.example.mongo;

import com.mongodb.MongoClient;
import com.mongodb.ReadConcern;
import com.mongodb.ReadPreference;
import com.mongodb.WriteConcern;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.example.collection.Task;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MongoImpl implements Mongo, MongoJSON{
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
        Task task = new Task(date, "Vymenit koleso", 2,2, false);
        //new MongoImpl().insertTask(task);
        //new MongoImpl().getAllTasks();
        //String hexString = "6079505597dde44e325ff1a1";
        //System.out.println(new ObjectId(hexString));
        //new MongoImpl().setTaskToDone(new ObjectId(hexString));

        System.out.println("---");
        List<Task> list = new MongoImpl().getAllTasksByName("zamok");

        for(Task t : list){
            System.out.println(t.toString());
        }
        System.out.println("JSON");
        System.out.println(new MongoImpl().getAllTasksJSON());
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

        if(task == null){
            System.out.println("Task is null!!!");
            return;
        }

        Document document = new Document();
        document.append("Date", task.getDate());
        document.append("Name", task.getName());
        document.append("Done", task.isDone());
        document.append("Priority", task.getPriority());
        //document.append("Price", task.getPrice());
        if(task.getPrice() >= 0.0){
            document.append("Price", task.getPrice());
        }
        if(task.getId() != null){
            document.append("_id", task.getId());
        }

        try {
            //vkladanie dokumentu do kolekcie
            database.getCollection("myReminders").insertOne(document);
            System.out.println("Document inserted successfully");
        }catch (Exception e){
            e.printStackTrace();
        }

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
        //vytiahnut kolekciu users z databazy mongo -> allUsers
        MongoCollection<Document> collection = database.getCollection("myReminders")
                .withReadPreference(ReadPreference.primary())
                .withReadConcern(ReadConcern.MAJORITY)
                .withWriteConcern(WriteConcern.MAJORITY);
        List<Task> list = new ArrayList<>();
        for(Document document : collection.find()){
            ObjectId id = document.getObjectId("_id");
            if(document.containsKey("Price")){
                Task task = new Task(document.getDate("Date"), document.getString("Name"), document.getInteger("Priority"), document.getDouble("Price"), document.getBoolean("Done"));
                task.setId(id);
                list.add(task);
                System.out.println(task.toString());
            }else{
                Task task = new Task(document.getDate("Date"), document.getString("Name"), document.getInteger("Priority"), document.getBoolean("Done"));
                task.setId(id);
                list.add(task);
                task.toString();
                System.out.println(task.toString());
            }

            //System.out.println(document.get("name"));
            //System.out.println(task.getDate() + " " + task.getName() + " " + task.getPriority() + " " + task.getPrice() + " " + task.isDone());
            //System.out.println(document.getDate("Date"));
            //System.out.println(document.getString("Name"));
            //System.out.println(document.getDate("Priority"));
            //System.out.println(document.getDate("Price"));
            //System.out.println(document.getDate("Done"));
        }
        return list;
    }

    @Override
    public List<Task> getAllTasks(boolean done) {
        //vytiahnut kolekciu users z databazy mongo -> allUsers
        MongoCollection<Document> collection = database.getCollection("myReminders")
                .withReadPreference(ReadPreference.primary())
                .withReadConcern(ReadConcern.MAJORITY)
                .withWriteConcern(WriteConcern.MAJORITY);
        List<Task> list = new ArrayList<>();
        for(Document document : collection.find()){
            ObjectId id = document.getObjectId("_id");
            if(document.containsKey("Price")){
                Task task = new Task(document.getDate("Date"), document.getString("Name"), document.getInteger("Priority"), document.getDouble("Price"), document.getBoolean("Done"));
                if(task.isDone() == done){
                    task.setId(id);
                    list.add(task);
                }
                //System.out.println(task.toString());
            }else{
                Task task = new Task(document.getDate("Date"), document.getString("Name"), document.getInteger("Priority"), document.getBoolean("Done"));
                if(task.isDone() == done){
                    task.setId(id);
                    list.add(task);
                }
                //System.out.println(task.toString());
            }

        }
        return list;
    }



    public List<Task> getAllTasksByPriority() {
        //vytiahnut kolekciu users z databazy mongo -> allUsers
        MongoCollection<Document> collection = database.getCollection("myReminders")
                .withReadPreference(ReadPreference.primary())
                .withReadConcern(ReadConcern.MAJORITY)
                .withWriteConcern(WriteConcern.MAJORITY);
        List<Task> list = new ArrayList<>();
        for(Document document : collection.find()){
            ObjectId id = document.getObjectId("_id");
            if(document.containsKey("Price")){
                Task task = new Task(document.getDate("Date"), document.getString("Name"), document.getInteger("Priority"), document.getDouble("Price"), document.getBoolean("Done"));
                task.setId(id);
                list.add(task);
                System.out.println(task.toString());
            }else{
                Task task = new Task(document.getDate("Date"), document.getString("Name"), document.getInteger("Priority"), document.getBoolean("Done"));
                task.setId(id);
                list.add(task);
                task.toString();
                System.out.println(task.toString());
            }
        }
        //prekopirovanie arraya do pola
        Task[] tasks = new Task[list.size()];
        for(int i = 0; i < list.size(); i++){
            tasks[i] = list.get(i);
        }

        //buble sort na zoradenie od najensieho cisla po najvacsie
        for(int i = 0; i < tasks.length; i++){
            for(int j = 0; j < tasks.length; j++){
                if(tasks[i].getPriority()>tasks[j].getPriority()){
                    Task helpTask = tasks[i];
                    tasks[i] = tasks[j];
                    tasks[j] = helpTask;
                }
            }
        }

        List<Task> listok = new ArrayList<>();

        //z pola na arraylist

        for(int i = 0; i < tasks.length; i++){
            listok.add(i,tasks[i]);
        }
        return listok;
    }


    @Override
    public List<Task> getAllTasksByPriority(int priority){
        //vytiahnut kolekciu users z databazy mongo -> allUsers
        MongoCollection<Document> collection = database.getCollection("myReminders")
                .withReadPreference(ReadPreference.primary())
                .withReadConcern(ReadConcern.MAJORITY)
                .withWriteConcern(WriteConcern.MAJORITY);
        List<Task> list = new ArrayList<>();
        for(Document document : collection.find()){
            ObjectId id = document.getObjectId("_id");
            if(document.containsKey("Price")){
                Task task = new Task(document.getDate("Date"), document.getString("Name"), document.getInteger("Priority"), document.getDouble("Price"), document.getBoolean("Done"));
                task.setId(id);
                if(task.getPriority() == priority){
                    list.add(task);
                }
            }else{
                Task task = new Task(document.getDate("Date"), document.getString("Name"), document.getInteger("Priority"), document.getBoolean("Done"));
                task.setId(id);
                if(task.getPriority() == priority){
                    list.add(task);
                }
            }
        }
        return list;
    }

    @Override
    public List<Task> getAllTasksByName(String name) {
        //vytiahnut kolekciu users z databazy mongo -> allUsers
        MongoCollection<Document> collection = database.getCollection("myReminders")
                .withReadPreference(ReadPreference.primary())
                .withReadConcern(ReadConcern.MAJORITY)
                .withWriteConcern(WriteConcern.MAJORITY);
        List<Task> list = new ArrayList<>();
        for(Document document : collection.find()){
            ObjectId id = document.getObjectId("_id");

            if(document.containsKey("Price")){
                Task task = new Task(document.getDate("Date"), document.getString("Name"), document.getInteger("Priority"), document.getDouble("Price"), document.getBoolean("Done"));
                task.setId(id);
                    //if(task.getName().equals(name)){
                    if(task.getName().contains(name)){
                        list.add(task);
                    }
            }else{
                Task task = new Task(document.getDate("Date"), document.getString("Name"), document.getInteger("Priority"), document.getBoolean("Done"));
                task.setId(id);
                    //if(task.getName().equals(name)){
                    if(task.getName().contains(name)){
                        list.add(task);
                    }
            }
        }
        return list;

    }

    @Override
    public void deleteDoneTasks() {

    }




    @Override
    public void insertTaskJSON(JSONObject tasks) {
        String name = (String)tasks.get("Name");
        ObjectId id = (ObjectId) tasks.get("_id");
        int priority = ((Long)tasks.get("Priority")).intValue();
        boolean done = (boolean)tasks.get("Done");
        Date date = (Date)tasks.get("Date");
        if(tasks.containsKey("Price")){
            double price = (double)tasks.get("Pice");
            Document document = new Document();
            document.append("Date", date);
            document.append("Name", name);
            document.append("Done", done);
            document.append("Priority", priority);
            document.append("Price", price);
            database.getCollection("myReminders").insertOne(document);
            return;
        }
        Document document = new Document();
        document.append("Date", date);
        document.append("Name", name);
        document.append("Done", done);
        document.append("Priority", priority);
        database.getCollection("myReminders").insertOne(document);
    }

    @Override
    public JSONObject getAllTasksJSON() {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();

        List<Task> list = getAllTasks();
        for(int i = 0; i < list.size(); i++){
            Task task = list.get(i);
            if(task.getPrice() == -1){
                JSONObject js = new JSONObject();
                js.put("_id", task.getId());
                js.put("Name", task.getName());
                js.put("Priority", task.getPriority());
                js.put("Date", task.getDate());
                js.put("Done", task.isDone());
                jsonArray.add(js);
            }else{
                JSONObject js = new JSONObject();
                js.put("_id", task.getId());
                js.put("Name", task.getName());
                js.put("Priority", task.getPriority());
                js.put("Date", task.getDate());
                js.put("Done", task.isDone());
                js.put("Price", task.getPrice());
                jsonArray.add(js);
            }
        }
        jsonObject.put("AllReminders", jsonArray);
        return jsonObject;
    }
}
