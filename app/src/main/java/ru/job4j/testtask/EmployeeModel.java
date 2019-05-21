package ru.job4j.testtask;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ru.job4j.store.ExamBaseHelper;
import ru.job4j.store.StaffDbSchema;

/**
 * Класс - модель (MVP).
 * В роли презентера выступает Активность Lister,
 * В роли View - фрагмент.
 *
 * @author Шавва Максим.
 * @version 1.
 * @since 2.05.2019г.
 */
public class EmployeeModel {

    /**
     * Ссылка на синглтон - хелпер базы данных.
     */
    private final ExamBaseHelper helper;

    public EmployeeModel(ExamBaseHelper dbHelper) {
        this.helper = dbHelper;
    }

    /**
     * Колбэки реализованы в презентере (Lister, например).
     */
    interface LoadEmployeeCallback {
        void onEmployeeLoaded(List<Employee> employees);
        void onProfessionLoaded(List<String> professions);
        void onLoadError(Exception e);
    }

    /**
     * Загружаем из базы асинхронно, передавая результат в callback.onLoad();
     */
    public void loadEmployee(LoadEmployeeCallback callback, String filter) {
        LoadEmployeeTask loadEmployeeTask = new LoadEmployeeTask(callback, filter);
        loadEmployeeTask.execute();
    }

    /**
     * Загружаем профессии из базы асинхронно. Передаём результат в
     */
    public void loadProfession(LoadEmployeeCallback callback) {
        LoadProfessionTask loadProfessionTask = new LoadProfessionTask(callback);
        loadProfessionTask.execute();
    }

    /**
     * В этом классе асинхронно вытаскиваем все профессии из базы в set,
     * затем возвращаем их в виде списка ArrayList<String>
     * в метод void onProfessionLoaded(List<String> professions).
     */
    class LoadProfessionTask extends AsyncTask<Void, Void, List<String>> {

        private final LoadEmployeeCallback callback;

        LoadProfessionTask(LoadEmployeeCallback callback) {
            this.callback = callback;
        }

        @Override
        protected List<String> doInBackground(Void... voids) {
            Set<String> profs = new HashSet<>();
            try (SQLiteDatabase db = helper.getReadableDatabase()) {
                Cursor cursor = db.query(StaffDbSchema.StaffTable.STAFF,
                        new String[]{StaffDbSchema.StaffTable.Cols.PROFESSION},
                        null, null,
                        null, null, null);
                int prof = cursor.getColumnIndex(StaffDbSchema.StaffTable.Cols.PROFESSION);
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    profs.add(cursor.getString(prof));
                    cursor.moveToNext();
                }
                cursor.close();
            } catch (SQLiteException e) {
                callback.onLoadError(e);
            }
            return new ArrayList<>(profs);
        }

        @Override
        protected void onPostExecute(List<String> professions) {
            if (callback != null) {
                callback.onProfessionLoaded(professions);
            }
        }
    }

    /**
     * В этом классе асинхронно вытаскиваем все данные из базы,
     * затем возвращаем список List<Employee>
     * в метод onEmployeeLoaded(List<Employee> employees).
     */
    class LoadEmployeeTask extends AsyncTask<Void, Void, List<Employee>> {

        private final LoadEmployeeCallback callback;
        private final String filter;

        LoadEmployeeTask(LoadEmployeeCallback callback, String filter) {
            this.callback = callback;
            this.filter = filter;
        }

        @Override
        protected List<Employee> doInBackground(Void... params) {
            List<Employee> staff = new ArrayList<>();
            try (SQLiteDatabase db = helper.getReadableDatabase()) {
                Cursor cursor = cursorObtain(db);
                int first = cursor.getColumnIndex(StaffDbSchema.StaffTable.Cols.FIRSTNAME);
                int last = cursor.getColumnIndex(StaffDbSchema.StaffTable.Cols.LASTNAME);
                int birth = cursor.getColumnIndex(StaffDbSchema.StaffTable.Cols.BIRTHDAY);
                int photo = cursor.getColumnIndex(StaffDbSchema.StaffTable.Cols.PHOTOURL);
                int prof = cursor.getColumnIndex(StaffDbSchema.StaffTable.Cols.PROFESSION);
                int code = cursor.getColumnIndex(StaffDbSchema.StaffTable.Cols.CODE);
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    Employee employee = new Employee(
                            cursor.getString(first),
                            cursor.getString(last),
                            new Date(cursor.getLong(birth)),
                            cursor.getString(photo),
                            new Profession(cursor.getString(prof), cursor.getInt(code))
                    );
                    staff.add(employee);
                    cursor.moveToNext();
                }
                cursor.close();
            } catch (SQLiteException e) {
                callback.onLoadError(e);
            }
            return staff;
        }

        /**
         * Получаем курсор: Если ключ unfiltered - все элементы,
         * иначе - только записи с профессией filter.
         */
        private Cursor cursorObtain(SQLiteDatabase db) throws SQLiteException {
            Cursor cursor;
            if ("unfiltered".equals(filter)) {
                cursor = db.query(StaffDbSchema.StaffTable.STAFF,
                        null, null, null,
                        null, null, null);
            } else {
                cursor = db.query(StaffDbSchema.StaffTable.STAFF,
                        null,
                        StaffDbSchema.StaffTable.Cols.PROFESSION + " = ?",
                        new String[] {filter},
                        null, null, null);
            }
            return cursor;
        }

        @Override
        protected void onPostExecute(List<Employee> staff) {
            if (callback != null) {
                callback.onEmployeeLoaded(staff);
            }
        }
    }
}