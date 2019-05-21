package ru.job4j.dialogtest;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import java.util.Calendar;

/**
 * Класс - содержит фрагмент с диалоговым окном "Время"
 *
 * @author Шавва Максим.
 * @version 1.
 * @since 10.05.2019г.
 */
public class TimeDialog extends DialogFragment {

    /**
     * Ссылка на слушателя (контекст фрагмента).
     */
    private TimeDialogListener callBack;

    /**
     * Интерфейс слушателя.
     */
    public interface TimeDialogListener {
        void onTimeClick(int hour, int minutes);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        return new TimePickerDialog(getActivity(), this::onTimeSet, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        callBack.onTimeClick(hourOfDay, minute);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            callBack = (TimeDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(
                    String.format("%s must implement TimeDialogListener",
                            context.toString()));
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        callBack = null;
    }
}