package com.patachadmin.patachou.Model;

public class User {

    String name ,pic,id,email;

    public User(String name, String pic, String id, String email) {
        this.name = name;
        this.pic = pic;
        this.id = id;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public String getPic() {
        return pic;
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }
}
