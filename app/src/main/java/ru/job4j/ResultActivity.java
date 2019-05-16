package ru.job4j;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;

/**
 * Класс - Активность - результат теста.
 *
 * @author Шавва Максим.
 * @version 1.
 * @since 6.05.2019г.
 */
public class ResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        Button begin = findViewById(R.id.begin);
        begin.setOnClickListener(this::onBegin);
        fillViews();
    }

    /**
     * Заполняем текстовые элементы.
     */
    private void fillViews() {
        Intent intent = getIntent();
        int all = intent.getIntExtra(MainActivity.ALL, 0);
        int right = intent.getIntExtra(MainActivity.RIGHT, 0);
        TextView result = findViewById(R.id.result);
        result.setText(String.format(Locale.ENGLISH,
                getString(R.string.results), right, all));
    }

    /**
     * Определяет реакцию на щелчёк по кнопке begin/заново.
     * Открываем MainActivity поновой для прохождения теста.
     */
    private void onBegin(View view) {
        startActivity(new Intent(ResultActivity.this, MainActivity.class));
        this.finish();
    }
}