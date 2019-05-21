package ru.job4j.store;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ru.job4j.testtask.Employee;
import ru.job4j.testtask.EmployeeSupplier;

/**
 * Класс - SQLiteHelper создаёт базу данных на устройстве
 * и позволяет с ней рабботать.
 *
 * @author Шавва Максим.
 * @version 1.2
 * @since 21.05.2019г.
 */
public class ExamBaseHelper extends SQLiteOpenHelper {
    private static final String DB = "exams.db";
    private static final int VERSION = 2;
    private final String SOURCE = "base.json";
    private static ExamBaseHelper singleton = null;
    private Context context;

    private ExamBaseHelper(Context context) {
        super(context, DB, null, VERSION);
        this.context = context;
    }

    public synchronized static ExamBaseHelper getInstance(Context context) {
        if (singleton == null) {
            singleton = new ExamBaseHelper(context);
        }
        return singleton;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        makeTableExams(db);
        makeTableStaff(db);
        Log.d("ExamActivity", "onCreate in helper");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        if ( i < 2) {
            makeTableStaff(db);
        }
        Log.d("ExamActivity", "onUpdate in helper");
    }

    private void makeTableExams(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE " + ExamDbSchema.ExamTable.NAME + " (" +
                        "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        ExamDbSchema.ExamTable.Cols.TITLE + " TEXT, " +
                        ExamDbSchema.ExamTable.Cols.DATE + " INTEGER, " +
                        ExamDbSchema.ExamTable.Cols.RESULT + " INTEGER" +
                        ")"
        );
    }

    /**
     * Создаём
     */
    private void makeTableStaff(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE " + StaffDbSchema.StaffTable.STAFF + " (" +
                        "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        StaffDbSchema.StaffTable.Cols.FIRSTNAME + " TEXT, " +
                        StaffDbSchema.StaffTable.Cols.LASTNAME + " TEXT, " +
                        StaffDbSchema.StaffTable.Cols.BIRTHDAY + " INTEGER, " +
                        StaffDbSchema.StaffTable.Cols.PHOTOURL + " TEXT, " +
                        StaffDbSchema.StaffTable.Cols.PROFESSION + " TEXT, " +
                        StaffDbSchema.StaffTable.Cols.CODE + " INTEGER" +
                        ")"
        );
        insertDrink(db);
    }

    /**
     * Добавляем данные из JSON в SQLite.
     */
    private void insertDrink(SQLiteDatabase db) {
        EmployeeSupplier supplier = new EmployeeSupplier();
        List<Employee> staff = new ArrayList<>();
        try {
            staff = supplier.getStaff(context, SOURCE);
        } catch (IOException e) {
            Toast.makeText(context,"Reading data from file is fault!",
                    Toast.LENGTH_LONG).show();
        } catch (JSONException e) {
            Toast.makeText(context,"It Fails to read Questions!",
                    Toast.LENGTH_SHORT).show();
        }
        for (int i = 0; i != staff.size(); i++) {
            ContentValues profile = new ContentValues();
            profile.put(StaffDbSchema.StaffTable.Cols.FIRSTNAME, staff.get(i).getName());
            profile.put(StaffDbSchema.StaffTable.Cols.LASTNAME, staff.get(i).getLast());
            profile.put(StaffDbSchema.StaffTable.Cols.BIRTHDAY, staff.get(i).getBirthday().getTime());
            profile.put(StaffDbSchema.StaffTable.Cols.PHOTOURL, staff.get(i).getPhoto());
            profile.put(StaffDbSchema.StaffTable.Cols.PROFESSION, staff.get(i).getOccupation().getProfession());
            profile.put(StaffDbSchema.StaffTable.Cols.CODE, staff.get(i).getOccupation().getCode());
            db.insert(StaffDbSchema.StaffTable.STAFF, null, profile);
        }
    }
}