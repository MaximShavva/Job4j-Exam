package ru.job4j.dialogtest;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import java.util.Calendar;

/**
 * Класс - содержит фрагмент с диалоговым окном "Время"
 *
 * @author Шавва Максим.
 * @version 1.
 * @since 10.05.2019г.
 */

public class DateDialog extends DialogFragment {

    /**
     * Ссылка на слушателя (контекст фрагмента).
     */
    private DateDialogListener callback;

    /**
     * Интерфейс слушателя.
     */
    public interface DateDialogListener {
        void onDateClick(int year, int month, int day);
        void openTimeDialog();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker.
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        return new DatePickerDialog(getActivity(), this::onDateSet, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        callback.onDateClick(year, month, day);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            callback = (DateDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(
                    String.format("%s must implement DateDialogListener",
                            context.toString()));
        }
    }

    /**
     * При отсоединении фрагмента вызываем следующий диалог.
     */
    @Override
    public void onDetach() {
        super.onDetach();
        callback.openTimeDialog();
        callback = null;
    }
}