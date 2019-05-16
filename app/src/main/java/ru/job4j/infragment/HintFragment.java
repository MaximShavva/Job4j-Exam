package ru.job4j.infragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import ru.job4j.R;

/**
 * Класс - Фрагмент - подсказка к вопросу.
 *
 * @author Шавва Максим.
 * @version 1.
 * @since 13.05.2019г.
 */
public class HintFragment extends Fragment {

    /**
     * Храним здесь подсказки для всех вопросов.
     */
    private final Map<Integer, String> answers = new HashMap<>();

    /**
     * Ссылка на основную активность с этим фрагментом.
     */
    private HintFragmentListener callback;

    /**
     * Слушатель кнопки "Назад".
     */
    public interface HintFragmentListener {
        void onRevertToQuiz();
    }

    public HintFragment() {
        this.answers.put(0, "Eight");
        this.answers.put(1, "Command processor to perform Java-programs.");
        this.answers.put(2, "We getting NullPointerException");
    }

    /**
     * Назначаем активность при присоединении фрагмента к активити.
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        callback = (HintFragmentListener) context;
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
        View view = inflater.inflate(R.layout.activity_hint, container, false);
        Button back = view.findViewById(R.id.back);
        back.setOnClickListener(v -> callback.onRevertToQuiz());
        fillViews(view);
        return view;
    }

    /**
     * Заполняем текстовые поля активности.
     */
    private void fillViews(View layer) {
        TextView text = layer.findViewById(R.id.hint_question);
        TextView answer = layer.findViewById(R.id.hint_text);
        Bundle bundle = getArguments();
        if (bundle != null) {
            int question = bundle.getInt(BaseActivity.HINT_FOR, 0);
            answer.setText(this.answers.get(question));
            text.setText(bundle.getString(BaseActivity.HINT_QUESTION));
        }
    }
}
