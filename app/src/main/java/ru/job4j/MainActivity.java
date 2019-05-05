package ru.job4j;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Класс для проверки методов жизненного цикла Activity.
 * Содержит счётчик поворотов экрана и вывод его в ЛОГ.
 *
 * @author Шавва Максим.
 * @version 1.
 * @since 6.05.2019г.
 */
public class MainActivity extends AppCompatActivity {

    /**
     * Используется для фильтрации вывода в LogCat.
     */
    private static final String TAG = "ExamActivity";

    /**
     * Содержит счётчик числа поворотов экрана.
     */
    private int spins;

    /**
     * Содержит список вопросов
     */
    private List<Question> questions = new ArrayList<>();

    /**
     * Указатель на текущий вопрос.
     */
    private int position = 0;

    /**
     * @param savedInstanceState Используем для восстановления значения spins.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setQuestions();
        this.fillForm();
        this.setListeners();
        if (savedInstanceState != null) {
            this.spins = savedInstanceState.getInt("spins", -1);
            Log.d(TAG, String.format("Количество поворотов экрана = %d", spins));
        }
    }

    /**
     * Заполняем список вопросами.
     */
    private void setQuestions() {
        QuestionsSupplier supplier = new QuestionsSupplier();
        try {
            questions = supplier.getQuestions(this, "questions.json");
        } catch (IOException e) {
            Toast.makeText(this,
                    "Reading data from file is fault!",
                    Toast.LENGTH_LONG)
                    .show();
        } catch (JSONException e) {
            Toast.makeText(this,
                    "It Fails to read Questions!",
                    Toast.LENGTH_SHORT)
                    .show();
        }
    }

    /**
     * Задаём кнопкам слушателей.
     */
    private void setListeners() {
        Button next = findViewById(R.id.next);
        next.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        RadioGroup variants = findViewById(R.id.variants);
                        if (variants.getCheckedRadioButtonId() == -1) {
                            Toast.makeText(MainActivity.this,
                                    "Fill in any answer",
                                    Toast.LENGTH_SHORT)
                                    .show();
                        } else {
                            questions.get(position)
                                    .setGiven(variants.getCheckedRadioButtonId());
                            showAnswer();
                            position++;
                            fillForm();
                        }
                    }
                }
        );
        Button previous = findViewById(R.id.previous);
        previous.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        RadioGroup variants = findViewById(R.id.variants);
                        questions.get(position)
                                .setGiven(variants.getCheckedRadioButtonId());
                        position--;
                        fillForm();
                    }
                }
        );
    }

    /**
     * Метод будет брать текущую позицию и заполнять вопрос и варианты ответов.
     */
    private void fillForm() {
        findViewById(R.id.previous).setEnabled(position != 0);
        findViewById(R.id.next).setEnabled(position != questions.size() - 1);
        final TextView text = findViewById(R.id.question);
        Question question = this.questions.get(this.position);
        text.setText(question.getText());
        RadioGroup variants = findViewById(R.id.variants);
        variants.check(question.getGiven());
        for (int index = 0; index != variants.getChildCount(); index++) {
            RadioButton button = (RadioButton) variants.getChildAt(index);
            Option option = question.getOptions().get(index);
            button.setId(option.getId());
            button.setText(option.getText());
        }
    }

    /**
     * Показываем правильный вариант ответа.
     */
    private void showAnswer() {
        RadioGroup variants = findViewById(R.id.variants);
        int id = variants.getCheckedRadioButtonId();
        Question question = this.questions.get(this.position);
        Toast.makeText(this,
                String.format(Locale.ENGLISH,
                        "Your answer is %d, correct is %d",
                        id, question.getAnswer()),
                Toast.LENGTH_SHORT)
                .show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
    }

    /**
     * @param outState Увеличиваем переменную spins и сохраняем её в bundle.
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("spins", ++spins);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }
}