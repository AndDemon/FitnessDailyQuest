package com.hrysenko.FitnessDailyQuest;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.room.Room;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class MainMenuFragment extends Fragment {

    private TextView userNameText;
    private PersonDatabase personDB;
    private List<Person> personList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Інфлювання макету фрагменту
        View view = inflater.inflate(R.layout.fragment_main_menu, container, false);

        // Отримання доступу до TextView
        userNameText = view.findViewById(R.id.user_name_text);

        // Підключення до бази даних
        personDB = Room.databaseBuilder(getContext(), PersonDatabase.class, "PersonDB").build();

        // Викликаємо метод для отримання списку користувачів
        getPersonListInBackground();

        return view;
    }

    private void getPersonListInBackground() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executorService.execute(() -> {

            personList = personDB.getPersonDAO().getAllPerson();

            handler.post(() -> {
                if (personList != null && !personList.isEmpty()) {
                    StringBuilder sb = new StringBuilder();
                    for (Person p : personList) {
                        sb.append(p.getName());
                    }

                    userNameText.setText(sb.toString());
                } else {
                    userNameText.setText("Немає користувачів");
                }
            });
        });
    }
}
