package ru.job4j.infragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import ru.job4j.Option;
import ru.job4j.Question;
import ru.job4j.R;

/**
 * Класс - Фрагмент - тест с вопросами.
 *
 * @author Шавва Максим.
 * @version 1.
 * @since 13.05.2019г.
 */
public class QuizFragment extends Fragment {

    /**
     * Счётчик правильных ответов.
     */
    private int correct = 0;

    /**
     * Содержит список вопросов
     */
    private final List<Question> questions = Arrays.asList(
            new Question(
                    1, "How many primitive variables does Java have?",
                    Arrays.asList(
                            new Option(1, "One"),
                            new Option(2, "One hundred"),
                            new Option(3, "Five"),
                            new Option(4, "Eight")
                    ), 4
            ),
            new Question(
                    2, "What is Java Virtual Machine?",
                    Arrays.asList(
                            new Option(1, "Time-machine."),
                            new Option(2, "Command processor to perform Java-programs."),
                            new Option(3, "Steam engine."),
                            new Option(4, "Machine you do not have yet.")
                    ), 2
            ),
            new Question(
                    3, "What is happen if we try unboxing null?",
                    Arrays.asList(
                            new Option(1, "We watching devil jumps out."),
                            new Option(2, "We getting 0"),
                            new Option(3, "We getting NullPointerException"),
                            new Option(4, "We getting ArithmeticException")
                    ), 3
            )
    );

    /**
     * Указатель на текущий вопрос.
     */
    private int position = 0;

    /**
     * Объект UI фрагмента.
     */
    private View layout;

    /**
     * Ссылка на основную активность с этим фрагментом.
     */
    private QuizButtonListener callback;

    /**
     * Слушатель кнопки "Подсказка", кнопки "К результатам".
     */
    public interface QuizButtonListener {
        void onHintButtonClick(int question, String answer);
        void onResultButtonClick(int all, int right);
    }

    public QuizFragment() {
        // Required empty public constructor
    }

    /**
     * Назначаем активность при присоединении фрагмента к активити.
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        callback = (QuizButtonListener) context;
    }

    /**
     * Обнуляем ссылку при отсоединении фрагмента от активити.
     */
    @Override
    public void onDetach() {
        super.onDetach();
        callback = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        layout = inflater.inflate(R.layout.activity_main, container, false);
        if (savedInstanceState != null) {
            this.correct = savedInstanceState.getInt("correct", 0);
            this.position = savedInstanceState.getInt("position", 0);
            int checked = savedInstanceState.getInt("checked", -1);
            ((RadioGroup) layout.findViewById(R.id.variants)).check(checked);
        }
        this.fillForm();
        this.setListeners();
        return layout;
    }

    /**
     * Метод будет брать текущую позицию и заполнять вопрос и варианты ответов.
     */
    private void fillForm() {
        layout.findViewById(R.id.previous).setEnabled(position != 0);
        layout.findViewById(R.id.next).setEnabled(true);
        final TextView text = layout.findViewById(R.id.question);
        Question question = this.questions.get(this.position);
        text.setText(question.getText());
        RadioGroup variants = layout.findViewById(R.id.variants);
        variants.check(question.getGiven());
        for (int index = 0; index != variants.getChildCount(); index++) {
            RadioButton button = (RadioButton) variants.getChildAt(index);
            Option option = question.getOptions().get(index);
            button.setId(option.getId());
            button.setText(option.getText());
        }
    }

    /**
     * Задаём кнопкам слушателей.
     */
    private void setListeners() {
        Button next = layout.findViewById(R.id.next);
        next.setOnClickListener(this::nextBtn);
        Button previous = layout.findViewById(R.id.previous);
        previous.setOnClickListener(this::backBtn);
        Button hint = layout.findViewById(R.id.hint);
        hint.setOnClickListener(this::hintBtn);
    }

    /**
     * Передаём этот метод по ссылке в OnclickListener::onClick для кнопки next.
     *
     * @param view ссылка на кнопку next.
     */
    private void nextBtn(View view) {
        RadioGroup variants = layout.findViewById(R.id.variants);
        checkAnswer(variants);
        if (position == questions.size() - 1) {
            callback.onResultButtonClick(questions.size(), correct);
            return;
        }
        if (variants.getCheckedRadioButtonId() == -1) {
            Toast.makeText(getContext(),
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
     * Передаём этот метод по ссылке в OnclickListener::onClick для кнопки previous.
     *
     * @param view ссылка на кнопку back.
     */
    private void backBtn(View view) {
        RadioGroup variants = layout.findViewById(R.id.variants);
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
        callback.onHintButtonClick(position, questions.get(position).getText());
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
     * @param outState Увеличиваем переменную spins и сохраняем её в bundle.
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        RadioGroup variants = layout.findViewById(R.id.variants);
        outState.putInt("correct", correct);
        outState.putInt("position", position);
        outState.putInt("checked", variants.getCheckedRadioButtonId());
    }

    /**
     * Показываем правильный вариант ответа.
     */
    private void showAnswer() {
        RadioGroup variants = layout.findViewById(R.id.variants);
        int id = variants.getCheckedRadioButtonId();
        Question question = this.questions.get(this.position);
        Toast.makeText(getContext(),
                String.format(Locale.ENGLISH,
                        getString(R.string.your_answer),
                        id, question.getAnswer()),
                Toast.LENGTH_SHORT)
                .show();
    }
}