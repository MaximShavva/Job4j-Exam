package ru.job4j.infragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import ru.job4j.R;

/**
 * Класс - контейнер для фрагментов приложения - экзамена.
 *
 * @author Шавва Максим.
 * @version 1.
 * @since 13.05.2019г.
 */
public class BaseActivity extends AppCompatActivity
        implements ExamsFragment.OnItemClickListener,
        QuizFragment.QuizButtonListener,
        HintFragment.HintFragmentListener,
        ResultFragment.ResultFragmentListener {

    /**
     * Константы будут идентификатором для Bundle.
     */
    public static final String HINT_FOR = "hint_for";
    public static final String HINT_QUESTION = "hint_question";
    public static final String RIGHT = "right";
    public static final String ALL = "all";

    private FragmentManager fm;
    private Fragment examsFragment;
    private QuizFragment quizFragment;
    private HintFragment hintFragment;
    private ResultFragment resultFragment;

    /**
     * ID первой транзакции фрагмента. Используется для возврата
     * из фрагмента - результата.
     */
    int id;

    /**
     * Добавляем первый фрагмент в активность.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        fm = getSupportFragmentManager();
        fm.findFragmentById(R.id.base_fragment);
        if (null == fm.findFragmentById(R.id.base_fragment)) {
            examsFragment = new ExamsFragment();
            id = fm.beginTransaction()
                    .add(R.id.base_fragment, examsFragment)
                    .addToBackStack(null)
                    .commit();
        }
    }

    /**
     * При клике на элемент списка экзаменов.
     */
    @Override
    public void onItemClicked() {
        if (quizFragment == null) {
            quizFragment = new QuizFragment();
        }
        fm.beginTransaction()
                .replace(R.id.base_fragment, quizFragment)
                .addToBackStack(null)
                .commit();
    }

    /**
     * При клике на кнопку "К списку элементов"
     * (Возврат к списку экзаменов).
     */
    public void toExamList(View view) {
        fm.popBackStack();
        quizFragment = null;
    }

    /**
     * При вызове фрагмента - подсказки к вопросу.
     */
    @Override
    public void onHintButtonClick(int question, String answer) {
        if (hintFragment == null) {
            hintFragment = new HintFragment();
        }
        Bundle bundle = new Bundle();
        bundle.putInt(HINT_FOR, question);
        bundle.putString(HINT_QUESTION, answer);
        hintFragment.setArguments(bundle);
        fm.beginTransaction()
                .replace(R.id.base_fragment, hintFragment)
                .addToBackStack(null)
                .commit();
    }

    /**
     * При вызове фрагмента с результатами экзамена.
     */
    @Override
    public void onResultButtonClick(int all, int right) {
        if (resultFragment == null) {
            resultFragment = new ResultFragment();
        }
        Bundle bundle = new Bundle();
        bundle.putInt(ALL, all);
        bundle.putInt(RIGHT, right);
        resultFragment.setArguments(bundle);
        fm.beginTransaction()
                .replace(R.id.base_fragment, resultFragment)
                .addToBackStack(null)
                .commit();
        quizFragment = null;
    }

    /**
     * При возврате из фрагмента - подсказки.
     */
    @Override
    public void onRevertToQuiz() {
        fm.popBackStack();
    }

    /**
     * При возврате из фрагмента - результата в список экзаменов.
     */
    @Override
    public void onRevertToExams() {
        fm.popBackStack(id, 0);
        resultFragment = null;
    }
}