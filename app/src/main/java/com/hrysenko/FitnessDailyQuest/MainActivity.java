package com.hrysenko.FitnessDailyQuest;

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
import androidx.fragment.app.Fragment;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import com.hrysenko.FitnessDailyQuest.R;

public class MainActivity extends AppCompatActivity {


    private static Integer indexbottom = null;

    PersonDatabase personDB;
    List<Person> personList;
    private BottomNavigationView bottom_navigation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        personDB = Room.databaseBuilder(getApplicationContext(), PersonDatabase.class, "PersonDB").build();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);




            RoomDatabase.Callback myCallBack = new RoomDatabase.Callback() {
                @Override
                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                    super.onCreate(db);
                }

                @Override
                public void onOpen(@NonNull SupportSQLiteDatabase db) {
                    super.onOpen(db);
                }
            };

            return insets;
        });
        bottom_navigation = findViewById(R.id.bottom_navigation);

        if(indexbottom == null) {

            bottom_navigation.setSelectedItemId(R.id.main);


            indexbottom = R.id.main;
        }else {
            bottom_navigation.setSelectedItemId(R.id.main);

        }
        if (indexbottom == R.id.main) {
            showFragment(new MainMenuFragment());
        } else if (indexbottom == R.id.dailyQuest) {
            showFragment(new DailyQuestFragment());
        } else if (indexbottom == R.id.profile) {
            showFragment(new ProfileFragment());
        } else {
            showFragment(new MainMenuFragment()); // Default case
        }


        // Set item selected listener
        bottom_navigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.main) {
                indexbottom = R.id.main;
                showFragment(new MainMenuFragment());
            } else if (itemId == R.id.dailyQuest) {
                indexbottom = R.id.dailyQuest;
                showFragment(new DailyQuestFragment());
            } else if (itemId == R.id.profile) {
                indexbottom = R.id.profile;
                showFragment(new ProfileFragment());
            }
            return true;
        });

    }

    private void showFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_fragments, fragment)
                .commit();
    }

    public void getPersonListInBackground(){
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        Handler handler = new Handler(Looper.getMainLooper());
        executorService.execute(new Runnable() {
            @Override
            public void run() {

                //background task
                personList = personDB.getPersonDAO().getAllPerson();
                //on finishing task
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        StringBuilder sb = new StringBuilder();
                        for(Person p : personList){
                            sb.append(p.getName()+" : "+p.getAge()+" : "+p.getHeight()+" : "+p.getWeight());
                            sb.append("\n");
                        }
                        String finalData = sb.toString();
                        Toast.makeText(MainActivity.this,""+finalData,Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}