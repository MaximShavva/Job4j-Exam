package ru.job4j.infragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
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

import ru.job4j.Exam;
import ru.job4j.R;

/**
 * Класс - Фрагмент - со списком экзаменов.
 *
 * @author Шавва Максим.
 * @version 1.
 * @since 13.05.2019г.
 */
public class ExamsFragment extends Fragment {

    /**
     * Ссылка на основную активность с этим фрагментом.
     */
    private OnItemClickListener callback;

    /**
     * Слушатель элемента списка.
     */
    public interface OnItemClickListener {
        void onItemClicked();
    }


    /**
     * Объект представления прокручиваемого списка.
     */
    private RecyclerView recycler;

    public ExamsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        callback = (OnItemClickListener) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.exams, container, false);
        this.recycler = (RecyclerView) view;
        this.recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        updateUI();
        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        callback = null;
    }

    /**
     * Присваиваем нашему recycler адаптер для List exams.
     */
    private void updateUI() {
        List<Exam> exams = new ArrayList<>();
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
            info.setOnClickListener(view -> callback.onItemClicked());
        }

        @Override
        public int getItemCount() {
            return this.exams.size();
        }
    }
}