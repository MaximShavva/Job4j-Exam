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
 * Класс - первый фрагмент.
 *
 * @author Шавва Максим.
 * @version 1.
 * @since 8.05.2019г.
 */
public class FirstFragment extends Fragment {

    /**
     * Ссылка на кнопку во фрагменте.
     */
    private Button nextFragment;

    private OnNextButtonClickListener callback;

    public interface OnNextButtonClickListener {
        void onNextButtonClicked(String message);
    }

    public void onClick(View view) {
        callback.onNextButtonClicked("Next button clicked");
    }

    public FirstFragment() {
        // Required empty public constructor
    }

    /**
     * Назначаем активность при присоединении фрагмента к активити.
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        callback = (OnNextButtonClickListener) context;
    }

    /**
     * Этот метод определяет представление фрагмента
     * (аналогично методу onCreate в активити).
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_first, container, false);
        nextFragment = view.findViewById(R.id.next_fragment);
        nextFragment.setOnClickListener(this::onClick);
        TextView message = view.findViewById(R.id.message_back);
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