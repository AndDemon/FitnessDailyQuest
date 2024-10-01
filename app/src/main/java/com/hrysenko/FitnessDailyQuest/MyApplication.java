package com.hrysenko.FitnessDailyQuest;

import com.google.android.material.color.DynamicColors;

public class MyApplication extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();
        DynamicColors.applyToActivitiesIfAvailable(this);

    }
}