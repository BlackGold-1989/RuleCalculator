package com.shindev.rulecalculator.model;

import org.json.JSONException;
import org.json.JSONObject;

public class ProjectItem {

    public String id = "";
    public String name = "";
    public String classname = "";
    public String regdate = "";

    public boolean isSelected = false;

    public void initialWithJson (JSONObject object) {
        try {
            id = object.getString("id");
            name = object.getString("name");
            classname = object.getString("classname");
            regdate = object.getString("regdate");

            isSelected = false;
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
