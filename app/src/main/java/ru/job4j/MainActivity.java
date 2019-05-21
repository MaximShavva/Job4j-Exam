package ru.job4j;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
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
 * Класс - Активность - тест с вопросами.
 *
 * @author Шавва Максим.
 * @version 1.1
 * @since 17.05.2019г.
 */
public class MainActivity
        extends AppCompatActivity
        implements ConfirmHintDialogFragment.ConfirmHintDialogListener {

    /**
     * Используется для фильтрации вывода в LogCat.
     */
    private static final String TAG = "ExamActivity";

    /**
     * Содержит счётчик числа поворотов экрана.
     */
    private int spins;

    /**
     * Константа будет идентификатором в интенте.
     */
    public static final String HINT_FOR = "hint_for";
    public static final String HINT_QUESTION = "hint_question";
    public static final String RIGHT = "right";
    public static final String ALL = "all";

    /**
     * Счётчик правильных ответов.
     */
    private int correct = 0;

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
        if (savedInstanceState != null) {
            this.spins = savedInstanceState.getInt("spins", -1);
            this.correct = savedInstanceState.getInt("correct", 0);
            this.position = savedInstanceState.getInt("position", 0);
            int checked = savedInstanceState.getInt("checked", -1);
            ((RadioGroup) findViewById(R.id.variants)).check(checked);
        }
        setQuestions();
        this.fillForm();
        this.setListeners();
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
        next.setOnClickListener(this::nextBtn);
        Button previous = findViewById(R.id.previous);
        previous.setOnClickListener(this::backBtn);
        Button hint = findViewById(R.id.hint);
        hint.setOnClickListener(this::hintBtn);
    }

    /**
     * Передаём этот метод по ссылке в OnclickListener::onClick для кнопки next.
     *
     * @param view ссылка на кнопку next.
     */
    private void nextBtn(View view) {
        RadioGroup variants = findViewById(R.id.variants);
        checkAnswer(variants);
        if (position == questions.size() - 1) {
            Intent intent = new Intent(MainActivity.this, ResultActivity.class);
            intent.putExtra(ALL, questions.size());
            intent.putExtra(RIGHT, correct);
            intent.putExtra("id", getIntent().getIntExtra("id", -1));
            startActivity(intent);
            return;
        }
        if (variants.getCheckedRadioButtonId() == -1) {
            Toast.makeText(MainActivity.this,
                    getString(R.string.fill_in),
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

    /**
     * Проверка правильности ответа.
     */
    private void checkAnswer(RadioGroup variants) {
        if (variants.getCheckedRadioButtonId() == questions.get(position).getAnswer()) {
            correct++;
        }
    }

    /**
     * Передаём этот метод по ссылке в OnclickListener::onClick для кнопки previous.
     *
     * @param view ссылка на кнопку back.
     */
    private void backBtn(View view) {
        RadioGroup variants = findViewById(R.id.variants);
        questions.get(position)
                .setGiven(variants.getCheckedRadioButtonId());
        position--;
        fillForm();
    }

    /**
     * Передаём этот метод по ссылке в OnclickListener::onClick для кнопки hint.
     *
     * @param view ссылка на кнопку hint.
     */
    private void hintBtn(View view) {
        DialogFragment dialog = new ConfirmHintDialogFragment();
        dialog.show(getSupportFragmentManager(), "dialog_tag");
    }

    /**
     * Метод будет брать текущую позицию и заполнять вопрос и варианты ответов.
     */
    private void fillForm() {
        findViewById(R.id.previous).setEnabled(position != 0);
        findViewById(R.id.next).setEnabled(true);
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
                        getString(R.string.your_answer),
                        id, question.getAnswer()),
                Toast.LENGTH_SHORT)
                .show();
    }

    /**
     * @param outState Увеличиваем переменную spins и сохраняем её в bundle.
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        RadioGroup variants = findViewById(R.id.variants);
        outState.putInt("spins", ++spins);
        outState.putInt("correct", correct);
        outState.putInt("position", position);
        outState.putInt("checked", variants.getCheckedRadioButtonId());
    }

    /**
     * ExamsActivity запускается в режиме singleInstance
     * чтобы стек не копился.
     */
    public void toExamList(View view) {
        startActivity(new Intent(this, ExamsActivity.class));
    }

    /**
     * Вызывается из диалога ConfirmHintDialogFragment.
     */
    @Override
    public void onPositiveDialogClick(DialogFragment dialog) {
        Intent intent = new Intent(MainActivity.this, HintActivity.class);
        intent.putExtra(HINT_FOR, position);
        intent.putExtra(HINT_QUESTION, questions.get(position).getText());
        startActivity(intent);
    }

    /**
     * Вызывается из диалога ConfirmHintDialogFragment.
     */
    @Override
    public void onNegativeDialogClick(DialogFragment dialog) {
        Toast.makeText(this, "Молодец!!!", Toast.LENGTH_SHORT).show();
    }
}