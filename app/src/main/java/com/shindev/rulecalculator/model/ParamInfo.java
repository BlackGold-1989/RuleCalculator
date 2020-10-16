package com.shindev.rulecalculator.model;

import org.json.JSONException;
import org.json.JSONObject;

public class ParamInfo {
    public String id = "";
    public String name = "";
    public String description = "";
    public String regdate = "";

    public void initialWithJson (JSONObject object) {
        try {
            id = object.getString("id");
            name = object.getString("name");
            description = object.getString("description");
            regdate = object.getString("regdate");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
