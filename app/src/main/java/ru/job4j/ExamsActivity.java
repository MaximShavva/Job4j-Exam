package ru.job4j;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import ru.job4j.store.ExamBaseHelper;
import ru.job4j.store.ExamDbSchema;

/**
 * Класс - Активность - содержит в себе RecyclerView - список зкзаменов.
 *
 * @author Шавва Максим.
 * @version 1.1
 * @since 17.05.2019г.
 */
public class ExamsActivity extends AppCompatActivity
        implements ClearDialog.ClearDialogListener,
        UpdateExamDialog.ExamDialogListener {

    /**
     * Список всех экзаменов.
     */
    private List<Exam> exams = new ArrayList<>();

    /**
     * Объект представления прокручиваемого списка.
     */
    private RecyclerView recycler;

    /**
     * База данных с экзаменами.
     */
    private SQLiteDatabase store;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exams);
        this.recycler = findViewById(R.id.exams);
        this.recycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        try {
            this.store = ExamBaseHelper.getInstance(this).getWritableDatabase();
        } catch (SQLiteException e) {
            startActivity(new Intent(ExamsActivity.this, MainActivity.class));
        }
        updateUI();
    }

    /**
     * Переопределяем метод, т.к. база изменилась, а активность
     * вызывается та же (используем для неё ключ singleInstance)
     */
    @Override
    protected void onRestart() {
        super.onRestart();
        exams.clear();
        updateUI();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.exams, menu);
        return true;
    }

    /**
     * Реагируем на выбор пункта меню.
     *
     * @param item Элемент меню, на котором был клик.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_item:
                Bundle bundle = new Bundle();
                bundle.putInt("id", -1);
                DialogFragment update = new UpdateExamDialog();
                update.setArguments(bundle);
                update.show(getSupportFragmentManager(), "update_dialog");
                return true;
            case R.id.delete_item:
                DialogFragment dialog = new ClearDialog();
                dialog.show(getSupportFragmentManager(), "clear_dialog");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Изменяем базу при завершении ввода нового Экзамена.
     */
    @Override
    public void onUpdateClick(String input) {
        ContentValues value = new ContentValues();
        value.put(ExamDbSchema.ExamTable.Cols.TITLE, input);
        value.put(ExamDbSchema.ExamTable.Cols.DATE, -1);
        value.put(ExamDbSchema.ExamTable.Cols.RESULT, -1);
        try (Cursor cursor = this.store.query(
                ExamDbSchema.ExamTable.NAME, new String[]{"_id"},
                null, null, null, null, null)) {
            store.insert(ExamDbSchema.ExamTable.NAME, null, value);
            cursor.moveToLast();
            exams.add(new Exam(cursor.getInt(0), input, -1, -1));
        } catch (SQLiteException e) {
            Toast.makeText(this, "DataBase unavailable", Toast.LENGTH_SHORT).show();
        }
        recycler.getAdapter().notifyDataSetChanged();
    }

    /**
     * При клике в диалоге "Удалить".
     * Для exams.removeIf(exam -> exam.getId() == id) нужен API 24.
     * Поэтому использую цикл.
     */
    @Override
    public void onDeleteClick(int id) {
        for (int i = 0; i != exams.size(); i++) {
            if (exams.get(i).getId() == id) {
                exams.remove(i);
                recycler.getAdapter().notifyDataSetChanged();
                break;
            }
        }
        store.delete(ExamDbSchema.ExamTable.NAME,
                "_id = ?",
                new String[]{Integer.toString(id)});
    }

    /**
     * Калбэк диалогового меню.
     * Очищаем список экзаменов.
     */
    @Override
    public void onOKtoClear() {
        store.delete(ExamDbSchema.ExamTable.NAME, null, null);
        exams.clear();
        recycler.getAdapter().notifyDataSetChanged();
    }

    /**
     * При клике в диалоговом окне "пройти тест".
     */
    public void onPassClick(int id) {
        Intent intent = new Intent(this.getApplicationContext(), MainActivity.class);
        Log.d("ExamActivity", "pass exam ID = " + id);
        intent.putExtra("id", id);
        startActivity(intent);
    }

    /**
     * Присваиваем нашему recycler адаптер для List exams.
     */
    private void updateUI() {
        try (Cursor cursor = this.store.query(
                ExamDbSchema.ExamTable.NAME,
                null, null, null,
                null, null, null)) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                exams.add(new Exam(
                        cursor.getInt(cursor.getColumnIndex("_id")),
                        cursor.getString(cursor.getColumnIndex("title")),
                        cursor.getLong(cursor.getColumnIndex("date")),
                        cursor.getInt(cursor.getColumnIndex("result"))
                ));
                cursor.moveToNext();
            }
        } catch (SQLiteException e) {
            Toast.makeText(this, "DataBase unavailable", Toast.LENGTH_SHORT).show();
        }
        this.recycler.setAdapter(new ExamAdapter(exams));
    }

    /**
     * Холдер - оболочка для нашего макета result_exam.xml.
     */
    class ExamHolder extends RecyclerView.ViewHolder {
        private View view;

        public ExamHolder(@NonNull View view) {
            super(view);
            this.view = view;
        }
    }

    class ExamAdapter extends RecyclerView.Adapter<ExamHolder> {

        /**
         * Список всех экзаменов.
         */
        private List<Exam> exams;

        /**
         * Формируем удобный формат вывода даты.
         */
        private SimpleDateFormat df = new SimpleDateFormat(
                "d MMMM yyyy 'at' HH:mm", Locale.ENGLISH);

        public ExamAdapter(List<Exam> exams) {
            this.exams = exams;
        }

        @NonNull
        @Override
        public ExamHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.info_exam, parent, false);
            return new ExamHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ExamHolder holder, int i) {
            final Exam exam = this.exams.get(i);
            TextView info = holder.view.findViewById(R.id.info);
            TextView result = holder.view.findViewById(R.id.result_exam);
            TextView date = holder.view.findViewById(R.id.date);
            info.setText(exam.getName());
            result.setText(exam.getResult() == -1 ? "n/a" :
                    String.format(Locale.ENGLISH, "result: %d%%", exam.getResult()));
            date.setText(exam.getTime() == -1 ? "n/a" : df.format(new Date(exam.getTime())));
            info.setOnClickListener(view -> {
                Bundle bundle = new Bundle();
                bundle.putInt("id", exam.getId());
                DialogFragment update = new UpdateExamDialog();
                update.setArguments(bundle);
                update.show(getSupportFragmentManager(), "update_dialog");
            });
        }

        @Override
        public int getItemCount() {
            return this.exams.size();
        }
    }

    /**
     * При удалении активности закрываем базу.
     */
    @Override
    protected void onDestroy() {
        if (store != null) store.close();
        super.onDestroy();
    }
}