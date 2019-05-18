package ru.job4j.store;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Класс - SQLiteHelper создаёт базу данных на устройстве
 * и позволяет с ней рабботать.
 *
 * @author Шавва Максим.
 * @version 1.1
 * @since 17.05.2019г.
 */
public class ExamBaseHelper extends SQLiteOpenHelper {
    private static final String DB = "exams.db";
    private static final int VERSION = 1;
    private static ExamBaseHelper singleton = null;

    private ExamBaseHelper(Context context) {
        super(context, DB, null, VERSION);
    }

    public synchronized static ExamBaseHelper getInstance(Context context) {
        if (singleton == null) {
            singleton = new ExamBaseHelper(context);
        }
        return singleton;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE " + ExamDbSchema.ExamTable.NAME + " (" +
                        "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        ExamDbSchema.ExamTable.Cols.TITLE + " TEXT, " +
                        ExamDbSchema.ExamTable.Cols.DATE + " INTEGER, " +
                        ExamDbSchema.ExamTable.Cols.RESULT + " INTEGER" +
                        ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
    }
}