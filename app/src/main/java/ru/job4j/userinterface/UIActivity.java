package ru.job4j.userinterface;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import ru.job4j.R;

public class UIActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ui);
    }
}
