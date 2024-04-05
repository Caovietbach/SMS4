package org.example.sms4.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Student {

    @Id
    @GeneratedValue
    private long id;

    private String name;

    private String email;

    public Student(){

    }

    public Student(String name, String email){
        this.name = name;
        this.email = email;
    }

    public long getId(){
        return id;
    }

    public void setId(long id){
        this.id = id;
    }
    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getEmail(){
        return email;
    }

    public void setEmail(String email){
        this.email = email;
    }

}
