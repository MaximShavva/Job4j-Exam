package ru.job4j.testtask;

import android.content.Context;
import android.content.res.Configuration;
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
import java.text.SimpleDateFormat;
import java.util.Locale;

import ru.job4j.R;

/**
 * Фрагмент отрисовывает профиль работника.
 *
 * @author Шавва Максим.
 * @version 1.2
 * @since 2.05.2019г.
 */
public class EmployeeFragment extends Fragment {

    /**
     * Регион устройства.
     */
    private Locale locale;

    /**
     * Контекст (Lister)
     */
    private Context context;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Configuration sysConfig = context.getResources().getConfiguration();
        this.locale = sysConfig.locale;
        this.context = context;
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
        SimpleDateFormat df = new SimpleDateFormat("d MMMM yyyy", locale);
        Employee employee = ((Lister) context).getEmployee();
        TextView firstName = view.findViewById(R.id.first);
        TextView lastName = view.findViewById(R.id.last);
        TextView birth = view.findViewById(R.id.birthday);
        ImageView picture = view.findViewById(R.id.photos);
        TextView occupy = view.findViewById(R.id.prof);
        TextView id = view.findViewById(R.id.code);
        firstName.setText(employee.getName());
        lastName.setText(employee.getLast());
        birth.setText(df.format(employee.getBirthday()));
        loadImage(employee.getPhoto(), picture);
        occupy.setText(employee.getOccupation().getProfession());
        id.setText(String.format(locale, "%d", employee.getOccupation().getCode()));
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

    @Override
    public void onDetach() {
        ((Lister) context).setEmployee(null);
        context = null;
        super.onDetach();
    }
}