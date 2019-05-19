package ru.job4j.dialogtest;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import ru.job4j.R;

/**
 * Класс - содержит в себе Кнопку для вызова диалогов установки
 * времени и даты.
 * Результат записывается в TextView с ID = date_and_time.
 *
 * @author Шавва Максим.
 * @version 1.
 * @since 10.05.2019г.
 */
public class DateTime
        extends AppCompatActivity
        implements DateDialog.DateDialogListener,
        TimeDialog.TimeDialogListener {

    /**
     * Получаем ссылку на фрагментменеджер.
     */
    private FragmentManager fm = getSupportFragmentManager();

    /**
     * Создаём форматировщик даты.
     */
    @SuppressLint({"DefaultLocale", "SimpleDateFormat"})
    private SimpleDateFormat df = new SimpleDateFormat("d MMMM yyyy");

    /**
     * Строка содержит введённую пользователем дату.
     */
    private String term;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_time);
    }

    /**
     * При нажатии на кнопку Дата/время вызываем диалоговое окно.
     */
    public void openDateDialog(View view) {
        DialogFragment dialog = new DateDialog();
        dialog.show(fm, "date_dialog");
    }

    /**
     * При уничтожении фрагмента "дата" вызывам следующий диалог - "время".
     */
    public void openTimeDialog() {
        DialogFragment newdialog = new TimeDialog();
        newdialog.show(fm, "time_dialog");
    }

    /**
     * Калбэк из диалога "Дата"
     *
     * @param year  год.
     * @param month день.
     * @param day   месяц.
     */
    @Override
    public void onDateClick(int year, int month, int day) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.DAY_OF_MONTH, day);
        cal.set(Calendar.MONTH, month);
        term = df.format(cal.getTime());
    }

    /**
     * Калбэк из диалога "Время"
     *
     * @param hour    час.
     * @param minutes минуты.
     */
    @SuppressLint("DefaultLocale")
    @Override
    public void onTimeClick(int hour, int minutes) {
        TextView textView = findViewById(R.id.date_and_time);
        textView.setText(String.format(term + "%n%d : %d", hour, minutes));
    }
}