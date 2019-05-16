package ru.job4j.testtask;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.time.DateTimeException;
import java.util.Date;
import java.util.Locale;

import ru.job4j.R;

/**
 * Фрагмент отрисовывает профиль работника.
 *
 * @author Шавва Максим.
 * @version 1.
 * @since 16.05.2019г.
 */public class EmployeeFragment extends Fragment {

    public EmployeeFragment() {
        // Required empty public constructor
    }

    /**
     * Создаём объект пользовательского интерфейса из мекета.
     * Вызываем его заполнение.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.employee_full, container, false);
        fill(view);
        return view;
    }

    /**
     * Заполняем наши элементы пользовательского интерфейса данными о работнике.
     */
    private void fill(View view) {
        Bundle bundle = getArguments();
        TextView firstName = view.findViewById(R.id.first);
        TextView lastName = view.findViewById(R.id.last);
        TextView birth = view.findViewById(R.id.birthday);
        TextView picture = view.findViewById(R.id.photos);
        TextView occupy = view.findViewById(R.id.prof);
        TextView id = view.findViewById(R.id.code);
        firstName.setText(bundle.getString("name"));
        lastName.setText(bundle.getString("last"));
        birth.setText(bundle.getString("birthday"));
        picture.setText(bundle.getString("photo"));
        occupy.setText(bundle.getString("profession"));
        id.setText(String.format(Locale.FRANCE, "%d", bundle.getInt("code")));
    }
}