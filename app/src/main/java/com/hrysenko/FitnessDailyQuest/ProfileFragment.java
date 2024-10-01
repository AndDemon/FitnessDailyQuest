package com.hrysenko.FitnessDailyQuest;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.room.Room;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class ProfileFragment extends Fragment {

    private TextView userNameText, textHeight, textAge, textWeight;
    private PersonDatabase personDB;
    private List<Person> personList;
    private Button supportButton;
    private Button editprofileButton;
    private Button aboutButton;

    @Override
    public void onResume() {
        super.onResume();
        getPersonListInBackground();  // Перезавантажити дані з бази
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Инфлюирование макета фрагмента
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Получение доступа к элементам TextView
        userNameText = view.findViewById(R.id.user_name_text);
        textHeight = view.findViewById(R.id.text_height);
        textAge = view.findViewById(R.id.text_age);
        textWeight = view.findViewById(R.id.text_weight);
        supportButton = view.findViewById(R.id.support_btn);
        editprofileButton = view.findViewById(R.id.edit_button);
        aboutButton = view.findViewById(R.id.about_btn);

        supportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoUrl("https://t.me/Holdthepump");
            }
        });
        aboutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoUrl("https://github.com/AndDemon/FitnessDailyQuest.git");
            }
        });
        editprofileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), EditProfileActivity.class));
            }
        });
        // Подключение к базе данных
        personDB = Room.databaseBuilder(getContext(), PersonDatabase.class, "PersonDB").build();

        // Вызов метода для получения списка пользователей
        getPersonListInBackground();

        return view;
    }

    private void gotoUrl(String s) {
        Uri uri = Uri.parse(s);
        startActivity(new Intent(Intent.ACTION_VIEW,uri));
    }

    private void getPersonListInBackground() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executorService.execute(() -> {
            // Получаем список пользователей
            personList = personDB.getPersonDAO().getAllPerson();

            handler.post(() -> {
                if (personList != null && !personList.isEmpty()) {
                    Person user = personList.get(0); // Предположим, что есть только один пользователь

                    // Установка данных в TextView с добавлением суффиксов
                    userNameText.setText(user.getName());
                    textHeight.setText(user.getHeight() + getString(R.string.cm)); // добавляем см для роста
                    textAge.setText(user.getAge() + getString(R.string.yo)); // добавляем лет для возраста
                    textWeight.setText(user.getWeight() + getString(R.string.kg)); // добавляем кг для веса
                } else {
                    userNameText.setText("Немає користувачів");
                }
            });
        });
    }

}
