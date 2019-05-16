package ru.job4j;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

/**
 * Класс - Активность - подсказка.
 *
 * @author Шавва Максим.
 * @version 1.
 * @since 6.05.2019г.
 */
public class HintActivity extends AppCompatActivity {

    /**
     * Храним здесь подсказки для всех вопросов.
     */
    private final Map<Integer, String> answers = new HashMap<>();

    public HintActivity() {
        this.answers.put(0, "Eight");
        this.answers.put(1, "Command processor to perform Java-programs.");
        this.answers.put(2, "We getting NullPointerException");
    }

    /**
     * Заполняем текстовые поля активности.
     */
    private void fillViews() {
        TextView text = findViewById(R.id.hint_question);
        TextView answer = findViewById(R.id.hint_text);
        Intent intent = getIntent();
        int question = intent.getIntExtra(MainActivity.HINT_FOR, 0);
        answer.setText(this.answers.get(question));
        text.setText(intent.getStringExtra(MainActivity.HINT_QUESTION));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hint);
        Button back = findViewById(R.id.back);
        back.setOnClickListener(this::onBack);
        fillViews();
    }

    /**
     * Присваиваем кнопке back слушателя с вызовом метода onBackPressed().
     */
    private void onBack(View view) {
        onBackPressed();
    }
}