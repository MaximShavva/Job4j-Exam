package ru.job4j.twofragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import ru.job4j.R;

/**
 * Класс - второй фрагмент.
 *
 * @author Шавва Максим.
 * @version 1.
 * @since 8.05.2019г.
 */
public class SecondFragment extends Fragment {

    /**
     * Ссылка на кнопку во фрагменте.
     */
    private Button previous;

    private OnBackButtonClickListener callback;

    public interface OnBackButtonClickListener {
        void onBackButtonClicked(String message);
    }

    public void onClick(View view) {
        callback.onBackButtonClicked("Back button clicked");
    }

    public SecondFragment() {
        // Required empty public constructor
    }

    /**
     * Назначаем активность при присоединении фрагмента к активити.
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        callback = (OnBackButtonClickListener) context;
    }

    /**
     * Наполняем фрагмент представлениями, присваиваем слушателя кнопке,
     * Извлекаем переданный текст в TextView message.
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_second, container, false);
        previous = view.findViewById(R.id.back);
        previous.setOnClickListener(this::onClick);
        TextView message = view.findViewById(R.id.message);
        Bundle bundle = getArguments();
        if (bundle != null) {
            message.setText(bundle.getString("message", "default"));
        }
        return view;
    }

    /**
     * Обнуляем ссылку при отсоединении фрагмента от активити.
     */
    @Override
    public void onDetach() {
        super.onDetach();
        callback = null;
    }
}