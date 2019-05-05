package ru.job4j;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

/**
 * Класс для проверки методов жизненного цикла Activity.
 * Содержит счётчик поворотов экрана и вывод его в ЛОГ.
 *
 * @author Шавва Максим.
 * @version 1.
 * @since 5.05.2019г.
 */
public class MainActivity extends AppCompatActivity {

    /**
     * Используется для фильтрации вывода в LogCat.
     */
    private static final String TAG = "ExamActivity";

    /**
     * Содержит счётчик числа поворотов экрана.
     */
    private int spins;

    /**
     * @param savedInstanceState Используем для восстановления значения spins.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate");
        if (savedInstanceState != null) {
            this.spins = savedInstanceState.getInt("spins", -1);
            Log.d(TAG, String.format("Количество поворотов экрана = %d", spins));
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
    }

    /**
     * @param outState Увеличиваем переменную spins и сохраняем её в bundle.
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("spins", ++spins);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }
}