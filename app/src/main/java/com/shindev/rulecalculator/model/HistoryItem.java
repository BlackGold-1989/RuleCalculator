package com.shindev.rulecalculator.model;

import org.json.JSONException;
import org.json.JSONObject;

public class HistoryItem {

    public String id = "";
    public String pay = "";
    public String description = "";
    public String regdate = "";

    public void initialWithJson (JSONObject object) {
        try {
            id = object.getString("id");
            pay = object.getString("pay");
            description = object.getString("other");
            regdate = object.getString("regdate");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
