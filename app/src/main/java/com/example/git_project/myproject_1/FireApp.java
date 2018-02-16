package com.example.git_project.myproject_1;

import android.app.Application;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Alpa on 07-02-2018.
 */

public class FireApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        if (!FirebaseApp.getApps(this).isEmpty())

        {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        }
    }
}
