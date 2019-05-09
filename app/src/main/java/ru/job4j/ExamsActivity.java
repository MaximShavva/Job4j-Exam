package ru.job4j;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Класс - Активность - содержит в себе RecyclerView - список зкзаменов.
 *
 * @author Шавва Максим.
 * @version 1.
 * @since 9.05.2019г.
 */
public class ExamsActivity extends AppCompatActivity {

    /**
     * Объект представления прокручиваемого списка.
     */
    private RecyclerView recycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exams);
        this.recycler = findViewById(R.id.exams);
        this.recycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        updateUI();
    }

    /**
     * Присваиваем нашему recycler адаптер для List exams.
     */
    private void updateUI() {
        List<Exam> exams = new ArrayList<Exam>();
        for (int index = 0; index != 100; index++) {
            exams.add(new Exam(index, String.format("Exam %s", index), System.currentTimeMillis(), index));
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
            result.setText(String.format(Locale.ENGLISH, "result: %d%%", exam.getResult()));
            date.setText(df.format(new Date(exam.getTime())));
            info.setOnClickListener(
                    view -> startActivity(new Intent(view.getContext(), MainActivity.class))
            );
        }

        @Override
        public int getItemCount() {
            return this.exams.size();
        }
    }
}