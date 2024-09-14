package com.hrysenko.FitnessDailyQuest;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface PersonDAO {

    @Insert
    public void addPerson(Person person);

    @Update
    public void updatePerson(Person person);

    @Delete
    public void  deletePerson(Person person);

    @Query("select * from person")
    public List<Person> getAllPerson();

    @Query("select * from person where person_id==:person_id")
    public Person getPerson(int person_id);

}
