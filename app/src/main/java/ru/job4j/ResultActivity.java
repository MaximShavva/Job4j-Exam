package ru.job4j;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.SQLDataException;
import java.sql.SQLException;
import java.util.Date;
import java.util.Locale;

import ru.job4j.store.ExamBaseHelper;
import ru.job4j.store.ExamDbSchema;

/**
 * Класс - Активность - результат теста.
 *
 * @author Шавва Максим.
 * @version 1.1
 * @since 17.05.2019г.
 */
public class ResultActivity extends AppCompatActivity {

    private int all;
    private int right;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        Button begin = findViewById(R.id.begin);
        begin.setOnClickListener(this::onBegin);
        fillViews();
        fillBase();
    }

    /**
     * Заполняем текстовые элементы.
     */
    private void fillViews() {
        Intent intent = getIntent();
        all = intent.getIntExtra(MainActivity.ALL, 0);
        right = intent.getIntExtra(MainActivity.RIGHT, 0);
        TextView result = findViewById(R.id.result);
        result.setText(String.format(Locale.ENGLISH,
                getString(R.string.results), right, all));
    }

    /**
     * Добавляем результаты теста в базу.
     */
    private void fillBase() {
        int id = getIntent().getIntExtra("id", -1);
        Log.d("ExamActivity", String.valueOf(id));
        if (id != -1) {
            try (SQLiteDatabase db = ExamBaseHelper.getInstance(this).getWritableDatabase()) {
                ContentValues value = new ContentValues();
                value.put(ExamDbSchema.ExamTable.Cols.DATE, System.currentTimeMillis());
                value.put(ExamDbSchema.ExamTable.Cols.RESULT, 100 * right / all);
                db.update(ExamDbSchema.ExamTable.NAME, value,
                        "_id = ?", new String[]{Integer.toString(id)});
            } catch (SQLiteException e) {
                Toast.makeText(this, "DataBase unavailable", Toast.LENGTH_SHORT).show();
            }
        }
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