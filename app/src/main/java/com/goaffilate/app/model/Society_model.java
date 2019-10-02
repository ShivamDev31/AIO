package com.goaffilate.app.model;

public class Society_model {
    String society_name;
    String society_id;

    public Society_model(){
        this.society_name ="";
        this.society_id = "";
    }

    public String getSociety_name() {
        return society_name;
    }

    public void setSociety_name(String society_name) {
        this.society_name = society_name;
    }

    public String getSociety_id() {
        return society_id;
    }

    public void setSociety_id(String society_id) {
        this.society_id = society_id;
    }
}
