package ru.job4j;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

/**
 * Фрагмент - диалог для добавления и удаления элементов exams.
 *
 * @author Шавва Максим.
 * @version 1.1
 * @since 17.05.2019г.
 */
public class UpdateExamDialog extends DialogFragment {

    /**
     * Получаем id из базы и передаём его в onUpdateClick.
     */
    int id;

    /**
     * Ссылка на слушателя (контекст фрагмента).
     */
    private ExamDialogListener callback;

    /**
     * Интерфейс слушателя. Метод вызываем с введённым
     * именем экзаменуемого.
     */
    public interface ExamDialogListener {
        void onUpdateClick(String s);
        void onDeleteClick(int id);
        void onPassClick(int id);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            callback = (ExamDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(
                    String.format("%s must implement ExamDialogListener",
                            context.toString()));
        }
    }

    /**
     * При отсоединении фрагмента вызываем следующий диалог.
     */
    @Override
    public void onDetach() {
        super.onDetach();
        callback = null;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        id = bundle.getInt("id");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        if (id == -1) {
            fillToAdd(builder);
        } else {
            fillToModify(builder);
        }
        return builder.create();
    }

    /**
     * Заполняем окно диалога, когда кликнули по меню -> "добавить" (id = -1)
     */
private void fillToAdd(AlertDialog.Builder builder) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.exam_update, null);
        builder.setTitle("Input Exam:")
                .setView(view)
                .setPositiveButton(android.R.string.ok, (dialogInterface, id) -> {
                    EditText text = view.findViewById(R.id.title);
                    callback.onUpdateClick(text.getText().toString());
                })
                .setNegativeButton(android.R.string.cancel, (dialogInterface, id) -> {});
    }

    /**
     * Заполняем окно диалога, когда кликнули по списку (id != -1)
     */
    private void fillToModify(AlertDialog.Builder builder) {
        builder.setMessage("Make you choose:")
                .setPositiveButton("Pass test", (d, i) -> callback.onPassClick(id))
                .setNegativeButton("Delete test", (d, i) -> callback.onDeleteClick(id));
    }
}