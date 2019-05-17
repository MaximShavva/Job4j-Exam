package ru.job4j.twofragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import ru.job4j.R;

/**
 * Класс - Активность - тест использования фрагментов в активности.
 *
 * @author Шавва Максим.
 * @version 1.
 * @since 8.05.2019г.
 */
public class HostFragmentActivity
        extends AppCompatActivity
        implements FirstFragment.OnNextButtonClickListener,
        SecondFragment.OnBackButtonClickListener {

    private FragmentManager fm;
    private Fragment firstFragment;
    private Fragment secondFragment;

    /**
     * Добавляем первый фрагмент в активность.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_fragment);
        fm = getSupportFragmentManager();
        firstFragment = fm.findFragmentById(R.id.fragment_container);
        if (firstFragment == null) {
            firstFragment = new FirstFragment();
            fm.beginTransaction()
                    .add(R.id.fragment_container, firstFragment)
                    .commit();
        }
    }

    /**
     * Меняем первый фрагмент на второй.
     */
    @Override
    public void onNextButtonClicked(String message) {
        Bundle bundle = new Bundle();
        bundle.putString("message", message);
        if (secondFragment == null) {
            secondFragment = new SecondFragment();
        }
        secondFragment.setArguments(bundle);
        fm.beginTransaction()
                .replace(R.id.fragment_container, secondFragment)
                .addToBackStack(null)
                .commit();
    }

    /**
     * Меняем второй фрагмент на первый.
     */
    @Override
    public void onBackButtonClicked(String message) {
        fm.popBackStackImmediate();
        TextView text = findViewById(R.id.message_back);
        text.setText(message);
    }
}