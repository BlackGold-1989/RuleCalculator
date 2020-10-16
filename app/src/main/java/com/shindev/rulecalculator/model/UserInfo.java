package com.shindev.rulecalculator.model;

import org.json.JSONException;
import org.json.JSONObject;

public class UserInfo {
    public String id;
    public String openid;
    public String email;
    public String nickname;
    public String deviceID;
    public String headurl;
    public String classname;
    public int product, functions, payed;
    public String regDate, other;

    public void initialWithJson (JSONObject object) {
        try {
            id = object.getString("id");
            openid = object.getString("openid");
            email = object.getString("email");
            deviceID = object.getString("deviceid");
            headurl = object.getString("headurl");
            nickname = object.getString("nickname");
            classname = object.getString("classname");
            product = object.getInt("product");
            functions = object.getInt("func");
            payed = object.getInt("payed");
            regDate = object.getString("regdate");
            other = object.getString("other");

            if (nickname.equals("")) {
                nickname = email;
            }
            if (headurl.equals("")) {
                headurl = "url";
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void init() {
        id = "";
        openid = "";
        email = "";
        deviceID = "";
        nickname = "";
        headurl = "";
        classname = "";
        product = 0;
        functions = 0;
        payed = 0;
        regDate = "";
        other = "";
    }
}
