package ru.job4j.testtask;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.net.URL;
import java.util.Locale;

import ru.job4j.R;

/**
 * Фрагмент отрисовывает профиль работника.
 *
 * @author Шавва Максим.
 * @version 1.1
 * @since 19.05.2019г.
 */
public class EmployeeFragment extends Fragment {

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
        ImageView picture = view.findViewById(R.id.photos);
        TextView occupy = view.findViewById(R.id.prof);
        TextView id = view.findViewById(R.id.code);
        firstName.setText(bundle.getString("name"));
        lastName.setText(bundle.getString("last"));
        birth.setText(bundle.getString("birthday"));
        loadImage(bundle.getString("photo"), picture);
        occupy.setText(bundle.getString("profession"));
        id.setText(String.format(Locale.FRANCE, "%d", bundle.getInt("code")));
    }

    /**
     * Загружаем фото по ссылке.
     */
    private void loadImage(String link, ImageView image) {
        Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    final Bitmap bitmap = BitmapFactory
                            .decodeStream(new URL(link).openStream());
                    image.post(() -> image.setImageBitmap(bitmap));
                } catch (Exception e) {
                    image.post(() -> image.setImageResource(R.drawable.not_found));
                }
            }
        };
        t.start();
    }
}