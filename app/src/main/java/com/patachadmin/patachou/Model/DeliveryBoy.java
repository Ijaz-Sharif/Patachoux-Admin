package com.patachadmin.patachou.Model;

public class DeliveryBoy {
    String lastName,email,number,firstName,id;

    public DeliveryBoy(String lastName, String email, String firstName , String number, String id) {
        this.email = email;
        this.lastName=lastName;
        this.firstName=firstName;
        this.number=number;
        this.id=id;
    }
    public DeliveryBoy(String name, String email, String id) {
        this.email = email;
        this.lastName=name;
        this.id=id;
    }

    public String getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getNumber() {
        return number;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }
}
