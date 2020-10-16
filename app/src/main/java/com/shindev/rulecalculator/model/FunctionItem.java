package com.shindev.rulecalculator.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FunctionItem {
    public String id = "";
    public String title = "";
    public String name = "";
    public String regdate = "";
    public String content = "";

    public boolean isSelected = false;

    public List<ParamInfo> params = new ArrayList<>();

    public void initialWithJson (JSONObject object) {
        try {
            id = object.getString("id");
            title = object.getString("description");
            name = object.getString("name");
            content = object.getString("content");
            regdate = object.getString("regdate");
            isSelected = false;

            params.clear();
            JSONArray paramAry = object.getJSONArray("params");
            for (int i = 0; i < paramAry.length(); i++) {
                JSONObject json = paramAry.getJSONObject(i);
                ParamInfo param = new ParamInfo();
                param.initialWithJson(json);
                params.add(param);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getName() {
        return name;
    }

    public int getParamCount () {
        return params.size();
    }

    public String getTitle() {
        return title;
    }

}
