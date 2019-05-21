package ru.job4j.testtask;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.List;

import ru.job4j.R;
import ru.job4j.store.ExamBaseHelper;

/**
 * Активность, которая управляет остальными фрагментами приложения.
 *
 * @author Шавва Максим.
 * @version 1.1
 * @since 2.05.2019г.
 */
public class Lister extends AppCompatActivity
        implements ProfessionFragment.OnProfessionListener,
        StaffFragment.OnStaffListener,
        EmployeeModel.LoadEmployeeCallback {

    public static final String TAG = "ExamActivity";

    /**
     * Используем менеджер фрагментов в нескольких методах,
     * поэтому храним ссылку на него в классе активности.
     */
    private FragmentManager fm;

    /**
     * ActionBar нужен для доступа к кнопке "назад".
     */
    private ActionBar actionBar;

    /**
     * Модель работы с базой данных.
     */
    private EmployeeModel model;

    /**
     * Флаг видимости кнопки назад.
     */
    private boolean backVisible = false;

    /**
     * Сохраняем ссылку на работника для обращения к ней из фрагмента.
     */
    private Employee employee;

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    /**
     * Храним ссылки на фрагменты, чтобы не создавать их
     * поновой каждый раз.
     */
    private StaffFragment staffFragment;
    private EmployeeFragment employeeFragment;
    private ProfessionFragment professionFragment;

    /**
     * Вызывается при создании пользовательского интерфейса.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lister);
        model = new EmployeeModel(ExamBaseHelper.getInstance(this));
        fm = getSupportFragmentManager();
        if (null == fm.findFragmentById(R.id.list_container)) {
            professionFragment = new ProfessionFragment();
            fm.beginTransaction()
                    .add(R.id.list_container, professionFragment)
                    .addToBackStack(null)
                    .commit();
        }
    }

    /**
     * Вызывается при создании меню.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_lister, menu);
        actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        return true;
    }

    /**
     * Вызывается когда нужно перерисовать меню действий.
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem show = menu.findItem(R.id.show);
        show.setVisible(!backVisible);
        String title = getString(R.string.app_name);
        String back = getString(R.string.backward);
        actionBar.setTitle(backVisible ? back : title);
        actionBar.setDisplayHomeAsUpEnabled(backVisible);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.d(TAG, "BackStack: " + fm.getBackStackEntryCount());
        if (fm.getBackStackEntryCount() == 1) {
            backVisible = false;
            invalidateOptionsMenu();
        }
        if (fm.getBackStackEntryCount() == 0) finish();
    }

    /**
     * Реакция на клик по кнопке меню (show или back).
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.show:
                if (null == staffFragment) {
                    staffFragment = new StaffFragment();
                }
                Bundle bundle = new Bundle();
                bundle.putString("filter", "unfiltered");
                staffFragment.setArguments(bundle);
                fm.beginTransaction()
                        .replace(R.id.list_container, staffFragment)
                        .addToBackStack(null)
                        .commit();
                backVisible = true;
                invalidateOptionsMenu();
                break;
            case android.R.id.home:
                fm.popBackStack();
                Log.d(TAG, "BackStack: " + fm.getBackStackEntryCount());
                if (fm.getBackStackEntryCount() == 2) {
                    backVisible = false;
                    invalidateOptionsMenu();
                }
                break;
        }
        return true;
    }

    /**
     * После создания ProfessionFragment(), он сообщает об этом
     * вызовом этого метода.
     */
    @Override
    public void onProfFragmentCreated() {
        model.loadProfession(this);
    }

    /**
     * Колбэк из StaffFragment сообщает, что фрагмент создан.
     * Теперь заполняем адаптер списка данными.
     */
    @Override
    public void onStaffFragmentCreated(String filter) {
        model.loadEmployee(this, filter);
    }

    /**
     * Колбэк вызывается из модели, когда данные загружены из базы.
     */
    @Override
    public void onEmployeeLoaded(List<Employee> employees) {
        staffFragment.getAdapter().setStaff(employees);
    }

    /**
     * Колбэк вызывается из модели, когда список профессий загружен из базы.
     */
    @Override
    public void onProfessionLoaded(List<String> professions) {
        professionFragment.getAdapter().setProfessions(professions);
    }

    /**
     * Кoлбэк вызывается из модели, когда данные не удалось загрузить.
     */
    @Override
    public void onLoadError(Exception e) {
        Toast.makeText(getBaseContext(),
                "DataBase unavailable: " + e.getMessage(),
                Toast.LENGTH_SHORT)
                .show();
    }

    /**
     * Вызываем, когда нужно отрисовать фрагмент работниками
     * определённой профессии.
     *
     * @param profession фильтр профессий.
     */
    @Override
    public void onProfessionClick(String profession) {
        if (null == staffFragment) {
            staffFragment = new StaffFragment();
        }
        Bundle bundle = new Bundle();
        bundle.putString("filter", profession);
        staffFragment.setArguments(bundle);
        fm.beginTransaction()
                .replace(R.id.list_container, staffFragment)
                .addToBackStack(null)
                .commit();
        backVisible = true;
        invalidateOptionsMenu();
    }

    /**
     * Вызываем, когда нужно отрисовать фрагмент с профилем работника.
     *
     * @param employee работник, данные которого передадим во фрагмент-профиль.
     */
    @Override
    public void onEmployeeClick(Employee employee) {
        this.employee = employee;
        if (null == employeeFragment) {
            employeeFragment = new EmployeeFragment();
        }
        fm.beginTransaction()
                .replace(R.id.list_container, employeeFragment)
                .addToBackStack(null)
                .commit();
    }
}