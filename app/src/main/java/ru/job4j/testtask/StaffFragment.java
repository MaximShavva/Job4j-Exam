package ru.job4j.testtask;


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

import java.util.ArrayList;
import java.util.List;

import ru.job4j.R;

/**
 * Фрагмент отрисовывает список работников по профессиям согласно фильтру.
 *
 * @author Шавва Максим.
 * @version 1.1
 * @since 2.05.2019г.
 */
public class StaffFragment extends Fragment {

    /**
     * Хранит сылку на активность - слушателя нажатия на элемент списка.
     */
    private OnStaffListener mListener;

    /**
     * Ссылка на адаптер
     */
    private EmployeeAdapter adapter;

    /**
     * Интерфейс слушателя нажатия на элемент списка.
     */
    public interface OnStaffListener {
        void onEmployeeClick(Employee employee);
        void onStaffFragmentCreated(String filter);
    }

    /**
     * При присоединении фрагмента сохраняем контекст в mListener.
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnStaffListener) {
            mListener = (OnStaffListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnStaffListener");
        }
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
     * При создании фрагмента назначаем списку работников
     * LayoutManager и адаптер.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profession, container, false);
        RecyclerView recycler = (RecyclerView) view;
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new EmployeeAdapter();
        recycler.setAdapter(adapter);
        Bundle bundle = getArguments();
        String filter = bundle.getString("filter");
        mListener.onStaffFragmentCreated(filter);
        return view;
    }

    public EmployeeAdapter getAdapter() {
        return adapter;
    }

    /**
     * Холдер - оболочка для нашего макета .xml.
     */
    class EmployeeHolder extends RecyclerView.ViewHolder {
        private View view;

        public EmployeeHolder(@NonNull View view) {
            super(view);
            this.view = view;
        }
    }

    /**
     * Класс-адаптер для нашего списка работников RecyclerView.
     */
    class EmployeeAdapter extends RecyclerView.Adapter<EmployeeHolder> {

        /**
         * Список всех работников.
         */
        private List<Employee> staff = new ArrayList<>();

        @NonNull
        @Override
        public EmployeeHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.employee_brief, parent, false);
            return new EmployeeHolder(view);
        }

        /**
         * Биндим элемент списка, устанавливаем слушателя нажания на текст.
         */
        @Override
        public void onBindViewHolder(@NonNull EmployeeHolder holder, int i) {
            final Employee employee = this.staff.get(i);
            TextView name = holder.view.findViewById(R.id.first_name);
            TextView last = holder.view.findViewById(R.id.last_name);
            TextView occupy = holder.view.findViewById(R.id.occupy);
            name.setText(employee.getName());
            last.setText(employee.getLast());
            occupy.setText(employee.getOccupation().getProfession());
            holder.view.setOnClickListener(
                    view -> mListener.onEmployeeClick(employee)
            );
        }

        public void setStaff(List<Employee> staff) {
            this.staff.clear();
            this.staff.addAll(staff);
            notifyDataSetChanged();
        }

        @Override
        public int getItemCount() {
            return this.staff.size();
        }
    }
}