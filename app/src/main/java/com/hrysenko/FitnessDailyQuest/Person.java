package com.hrysenko.FitnessDailyQuest;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "Person")
public class Person {

    @ColumnInfo(name="person_id")
    @PrimaryKey(autoGenerate = true)
    int id;
    @ColumnInfo(name = "name")
    String name;

    @ColumnInfo(name = "age")
    String age;

    @ColumnInfo(name = "height")
    String height;

    @ColumnInfo(name = "weight")
    String weight;

    @Ignore
    public Person(){

    }

    public Person(String name, String age, String height, String weight){
        this.name = name;
        this.age = age;
        this.height = height;
        this.weight = weight;
        this.id = 0;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }
}
