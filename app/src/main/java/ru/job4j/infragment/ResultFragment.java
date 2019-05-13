package ru.job4j.infragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;

import ru.job4j.R;

/**
 * Класс - Фрагмент - результат прохождения экзамена.
 *
 * @author Шавва Максим.
 * @version 1.
 * @since 13.05.2019г.
 */
public class ResultFragment extends Fragment {

    /**
     * Ссылка на основную активность с этим фрагментом.
     */
    private ResultFragmentListener callback;

    /**
     * Слушатель кнопки "Сначала".
     */
    public interface ResultFragmentListener {
        void onRevertToExams();
    }

    public ResultFragment() {
        // Required empty public constructor
    }

    /**
     * Назначаем активность при присоединении фрагмента к активити.
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        callback = (ResultFragmentListener) context;
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
        View view = inflater.inflate(R.layout.activity_result, container, false);
        Button begin = view.findViewById(R.id.begin);
        begin.setOnClickListener(v -> callback.onRevertToExams());
        fillViews(view);
        return view;
    }

    /**
     * Заполняем текстовые элементы.
     */
    private void fillViews(View view) {
        Bundle bundle = getArguments();
        if (bundle != null) {
            int all = bundle.getInt(BaseActivity.ALL, 0);
            int right = bundle.getInt(BaseActivity.RIGHT, 0);
            TextView result = view.findViewById(R.id.result);
            result.setText(String.format(Locale.ENGLISH,
                    getString(R.string.results), right, all));
        }
    }
}