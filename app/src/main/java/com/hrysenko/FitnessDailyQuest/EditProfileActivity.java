package com.hrysenko.FitnessDailyQuest;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.room.Room;

import com.google.android.material.textfield.TextInputEditText;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EditProfileActivity extends AppCompatActivity {

    private TextInputEditText loginName, loginAge, loginHeight, loginWeight;
    private Button saveBtn;
    private PersonDatabase personDB;
    private Person currentPerson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        setupEdgeToEdge();


        personDB = Room.databaseBuilder(getApplicationContext(), PersonDatabase.class, "PersonDB").build();


        loginName = findViewById(R.id.loginName);
        loginAge = findViewById(R.id.loginAge);
        loginHeight = findViewById(R.id.loginHeight);
        loginWeight = findViewById(R.id.loginWeight);
        saveBtn = findViewById(R.id.loginBtn);


        loadUserData();


        saveBtn.setOnClickListener(view -> {
            saveUserData();
            Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.main);
            if (currentFragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .detach(currentFragment)
                        .attach(currentFragment)
                        .commit();
            }
        });

    }

    private void setupEdgeToEdge() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void loadUserData() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executorService.execute(() -> {
            List<Person> persons = personDB.getPersonDAO().getAllPerson();


            if (persons != null && !persons.isEmpty()) {
                currentPerson = persons.get(0);

                handler.post(() -> {
                    if (currentPerson != null) {

                        loginName.setText(currentPerson.getName());
                        loginAge.setText(currentPerson.getAge());
                        loginHeight.setText(currentPerson.getHeight());
                        loginWeight.setText(currentPerson.getWeight());
                    }
                });
            } else {
                handler.post(() -> {
                });
            }
        });
    }


    private void saveUserData() {
        String name = loginName.getText().toString();
        String ageStr = loginAge.getText().toString();
        String heightStr = loginHeight.getText().toString();
        String weightStr = loginWeight.getText().toString();

        if (name.isEmpty() || ageStr.isEmpty() || heightStr.isEmpty() || weightStr.isEmpty()) {
            Toast.makeText(this, getString(R.string.error_fill_all_fields), Toast.LENGTH_SHORT).show();
            return;
        }

        int age = Integer.parseInt(ageStr);
        int height = Integer.parseInt(heightStr);
        int weight = Integer.parseInt(weightStr);

        if (age > 125) {
            Toast.makeText(EditProfileActivity.this, getString(R.string.error_age_limit), Toast.LENGTH_SHORT).show();
            return;
        }

        if (height > 250) {
            Toast.makeText(EditProfileActivity.this, getString(R.string.error_height_limit), Toast.LENGTH_SHORT).show();
            return;
        }

        if (weight > 650) {
            Toast.makeText(EditProfileActivity.this, getString(R.string.error_weight_limit), Toast.LENGTH_SHORT).show();
            return;
        }


        currentPerson.setName(name);
        currentPerson.setAge(ageStr);
        currentPerson.setHeight(heightStr);
        currentPerson.setWeight(weightStr);


        updatePersonInBackground(currentPerson);
    }


    private void updatePersonInBackground(Person person) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executorService.execute(() -> {
            personDB.getPersonDAO().updatePerson(person);

            handler.post(() -> {
                Toast.makeText(EditProfileActivity.this, getString(R.string.update_successful), Toast.LENGTH_SHORT).show();
                finish();
            });
        });
    }

}
