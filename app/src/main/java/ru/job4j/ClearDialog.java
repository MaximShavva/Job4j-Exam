package ru.job4j;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

/**
 * Класс - Фрагмент с диалогм подтверждения очистки списка экзаменов.
 *
 * @author Шавва Максим.
 * @version 1.
 * @since 13.05.2019г.
 */
public class ClearDialog extends DialogFragment {

    /**
     * Ссылка на слушателя (контекст фрагмента).
     */
    private ClearDialogListener callback;

    /**
     * Интерфейс слушателя.
     */
    public interface ClearDialogListener {
        void onOKtoClear();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity())
                .setMessage("Delete all entries?")
                .setPositiveButton(android.R.string.ok, (d, i) -> callback.onOKtoClear())
                .setNegativeButton(android.R.string.cancel, (d, i) -> {})
                .create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            callback = (ClearDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(
                    String.format("%s must implement DateDialogListener",
                            context.toString()));
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        callback = null;
    }
}