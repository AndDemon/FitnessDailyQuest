package com.hrysenko.FitnessDailyQuest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.hrysenko.FitnessDailyQuest.databinding.ActivityIntroBinding;

public class IntroActivity extends AppCompatActivity {
    private ActivityIntroBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityIntroBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        View view = binding.getRoot();
        setContentView(view);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);

            binding.getstartedBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(IntroActivity.this, LoginActivity.class));
                    finish();
                }
            });

            SharedPreferences preferences = getSharedPreferences("PREFERENCE", MODE_PRIVATE);
            String FirstTime = preferences.getString("FirstTimeInstall","");

            if(FirstTime.equals("Yes")){
                Intent intent = new Intent(IntroActivity.this, LoginActivity.class);
                startActivity(intent);
            }else{

                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("FirstTimeInstall", "Yes");
                editor.apply();
            }

            return insets;
        });
    }
}