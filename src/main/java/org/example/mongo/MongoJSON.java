package org.example.mongo;

import org.json.simple.JSONObject;

public interface MongoJSON {

    public void insertTaskJSON(JSONObject tasks);

    public JSONObject getAllTasksJSON();



}
