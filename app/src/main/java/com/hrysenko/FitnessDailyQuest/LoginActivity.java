package com.hrysenko.FitnessDailyQuest;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LoginActivity extends AppCompatActivity {


    com.google.android.material.textfield.TextInputEditText loginName;
    com.google.android.material.textfield.TextInputEditText loginAge;
    com.google.android.material.textfield.TextInputEditText loginHeight;
    com.google.android.material.textfield.TextInputEditText loginWeight;
    Button loginBtn;


    PersonDatabase personDB;
    List<Person> personList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        personDB = Room.databaseBuilder(getApplicationContext(), PersonDatabase.class, "PersonDB").build();

        checkIfUserExists();
    }

    private void checkIfUserExists() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executorService.execute(new Runnable() {
            @Override
            public void run() {

                List<Person> personList = personDB.getPersonDAO().getAllPerson();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (!personList.isEmpty()) {
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();
                        } else {
                            setContentView(R.layout.activity_login);

                            setupLoginScreen();
                        }
                    }
                });
            }
        });
    }

    private void setupLoginScreen() {

        loginName = findViewById(R.id.loginName);
        loginAge = findViewById(R.id.loginAge);
        loginHeight = findViewById(R.id.loginHeight);
        loginWeight = findViewById(R.id.loginWeight);
        loginBtn = findViewById(R.id.loginBtn);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = loginName.getText().toString();
                String ageStr = loginAge.getText().toString();
                String heightStr = loginHeight.getText().toString();
                String weightStr = loginWeight.getText().toString();

                if (name.isEmpty() || ageStr.isEmpty() || heightStr.isEmpty() || weightStr.isEmpty()) {
                    Toast.makeText(LoginActivity.this, getString(R.string.error_fill_all_fields), Toast.LENGTH_SHORT).show();
                    return;
                }

                int age = Integer.parseInt(ageStr);
                int height = Integer.parseInt(heightStr);
                int weight = Integer.parseInt(weightStr);

                if (age > 125) {
                    Toast.makeText(LoginActivity.this, getString(R.string.error_age_limit), Toast.LENGTH_SHORT).show();
                    return;
                }

                if (height > 250) {
                    Toast.makeText(LoginActivity.this, getString(R.string.error_height_limit), Toast.LENGTH_SHORT).show();
                    return;
                }

                if (weight > 650) {
                    Toast.makeText(LoginActivity.this, getString(R.string.error_weight_limit), Toast.LENGTH_SHORT).show();
                    return;
                }

                Person p1 = new Person(name, ageStr, heightStr, weightStr);

                addPersonInBackground(p1);

                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            }
        });
    }

    public void addPersonInBackground(Person person){
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        Handler handler = new Handler(Looper.getMainLooper());
        executorService.execute(new Runnable() {
            @Override
            public void run() {

                //background task
                personDB.getPersonDAO().addPerson(person);
                //on finishing task
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(LoginActivity.this, "Successfully", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }


}