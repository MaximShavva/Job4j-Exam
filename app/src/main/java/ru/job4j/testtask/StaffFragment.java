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

import java.util.List;

import ru.job4j.R;

/**
 * Фрагмент отрисовывает список работников по профессиям согласно фильтру.
 *
 * @author Шавва Максим.
 * @version 1.
 * @since 16.05.2019г.
 */
public class StaffFragment extends Fragment {

    /**
     * Хранит сылку на активность - слушателя нажатия на элемент списка.
     */
    private OnStaffListener mListener;

    /**
     * Интерфейс слушателя нажатия на элемент списка.
     */
    public interface OnStaffListener {
        void onEmployeeClick(String lastname);
    }

    public StaffFragment() {
        // Required empty public constructor
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
        Bundle bundle = getArguments();
        String filter = bundle.getString("filter");
        List<Employee> staff = ((Lister) mListener).getStaff(filter);
        recycler.setAdapter(new EmployeeAdapter(staff));
        return view;
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
        private List<Employee> staff;

        public EmployeeAdapter(List<Employee> staff) {
            this.staff = staff;
        }

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
                    view -> mListener.onEmployeeClick(staff.get(i).getLast())
            );
        }

        @Override
        public int getItemCount() {
            return this.staff.size();
        }
    }
}