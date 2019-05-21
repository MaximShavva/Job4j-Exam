package ru.job4j.testtask;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import ru.job4j.R;

/**
 * Фрагмент отрисовывает список всех профессий в базе данных.
 *
 * @author Шавва Максим.
 * @version 1.1
 * @since 2.05.2019г.
 */
public class ProfessionFragment extends Fragment {

    /**
     * Хранит сылку на активность - слушателя нажатия на элемент списка.
     */
    private OnProfessionListener mListener;

    /**
     * Адаптер списка профессий.
     */
    private ProfAdapter adapter;

    /**
     * Интерфейс слушателя нажатия на элемент списка.
     */
    public interface OnProfessionListener {
        void onProfessionClick(String profession);
        void onProfFragmentCreated();
    }

    /**
     * При присоединении фрагмента сохраняем контекст в mListener.
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnProfessionListener) {
            mListener = (OnProfessionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnProfessionListener");
        }
    }

    /**
     * При создании фрагмента назначаем списку профессий
     * LayoutManager и адаптер.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profession, container, false);
        RecyclerView recycler = (RecyclerView) view;
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ProfAdapter();
        recycler.setAdapter(adapter);
        mListener.onProfFragmentCreated();
        return view;
    }

    /**
     * При присоединении фрагмента.
     */
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * Возвращаем ссылку на адаптер.
     */
    public ProfAdapter getAdapter() {
        return adapter;
    }

    /**
     * Холдер - оболочка для нашего макета profession_info.xml.
     */
    class ProfHolder extends RecyclerView.ViewHolder {
        private View view;

        public ProfHolder(@NonNull View view) {
            super(view);
            this.view = view;
        }
    }

    /**
     * Класс-адаптер для нашего списка профессий RecyclerView.
     */
    class ProfAdapter extends RecyclerView.Adapter<ProfHolder> {

        /**
         * Список всех профессий.
         */
        private List<String> professions = new ArrayList<>();

        @NonNull
        @Override
        public ProfHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.profession_info, parent, false);
            return new ProfHolder(view);
        }

        /**
         * Биндим элемент списка, устанавливаем слушателя нажания на текст.
         */
        @Override
        public void onBindViewHolder(@NonNull ProfHolder holder, int i) {
            final String profession = this.professions.get(i);
            TextView item = holder.view.findViewById(R.id.profession);
            item.setText(profession);
            item.setOnClickListener(
                    view -> mListener.onProfessionClick(((TextView) view).getText().toString())
            );
        }

        /**
         * Обновляем список профессий в адаптере.
         *
         * @param professions список профессий.
         */
        public void setProfessions(List<String> professions) {
            this.professions.clear();
            this.professions.addAll(professions);
            notifyDataSetChanged();
        }

        @Override
        public int getItemCount() {
            return this.professions.size();
        }
    }
}