package ru.job4j.testtask;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import org.json.JSONException;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import ru.job4j.R;

/**
 * Активность, которая управляет остальными фрагментами приложения.
 *
 * @author Шавва Максим.
 * @version 1.
 * @since 16.05.2019г.
 */
public class Lister extends AppCompatActivity
        implements ProfessionFragment.OnProfessionListener,
        StaffFragment.OnStaffListener {

    /**
     * Используем менеджер фрагментов в нескольких методах,
     * поэтому храним ссылку на него в классе активности.
     */
    private FragmentManager fm;

    /**
     * Содержит список работников.
     */
    private List<Employee> staff = new ArrayList<>();

    /**
     * Флаг видимости кнопки назад.
     */
    private boolean backVisible = false;

    /**
     * Храним ссылки на фрагменты, чтобы не создавать их
     * поновой каждый раз.
     */
    private StaffFragment staffFragment;
    private EmployeeFragment employeeFragment;

    /**
     * Вызывается при создании пользовательского интерфейса.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lister);
        setStaff();
        fm = getSupportFragmentManager();
        if (null == fm.findFragmentById(R.id.list_container)) {
            ProfessionFragment professionFragment = new ProfessionFragment();
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
        return true;
    }

    /**
     * Вызывается когда нужно перерисовать меню действий.
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem back = menu.findItem(R.id.back);
        MenuItem show = menu.findItem(R.id.show);
        back.setVisible(backVisible);
        show.setVisible(!backVisible);
        return super.onPrepareOptionsMenu(menu);
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
            case R.id.back:
                fm.popBackStack();
                if (fm.getBackStackEntryCount() == 2) {
                    backVisible = false;
                    invalidateOptionsMenu();
                }
                break;
        }
        return true;
    }

    /**
     * Заполняем список работниками из base.json.
     */
    private void setStaff() {
        EmployeeSupplier supplier = new EmployeeSupplier();
        try {
            staff = supplier.getStaff(this, "base.json");
        } catch (IOException e) {
            Toast.makeText(this,
                    "Reading data from file is fault!",
                    Toast.LENGTH_LONG)
                    .show();
        } catch (JSONException e) {
            Toast.makeText(this,
                    "It Fails to read Questions!",
                    Toast.LENGTH_SHORT)
                    .show();
        }
    }

    /**
     * @param filter профессия, по которой формируем список.
     * @return Отфильтрованный список работников.
     */
    public List<Employee> getStaff(String filter) {
        List<Employee> result = new ArrayList<>();
        if (!"unfiltered".equals(filter)) {
            for (Employee employee : staff) {
                if (employee.getOccupation().getProfession().equals(filter))
                    result.add(employee);
            }
        } else result = staff;
        return result;
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
     * @param lastName Фамилия работника.
     */
    @Override
    public void onEmployeeClick(String lastName) {
        Bundle bundle = new Bundle();
        for (Employee employee : staff) {
            if (employee.getLast().equals(lastName)) {
                fillBundle(bundle, employee);
                break;
            }
        }
        if (null == employeeFragment) {
            employeeFragment = new EmployeeFragment();
        }
        employeeFragment.setArguments(bundle);
        fm.beginTransaction()
                .replace(R.id.list_container, employeeFragment)
                .addToBackStack(null)
                .commit();
    }

    /**
     * @param bundle Кладём в объект данные по работнику.
     * @param employee работник, данные которого используем.
     */
    private void fillBundle(Bundle bundle, Employee employee) {
        SimpleDateFormat df = new SimpleDateFormat("d MMMM yyyy", Locale.ENGLISH);
        bundle.putString("name", employee.getName());
        bundle.putString("last", employee.getLast());
        bundle.putString("birthday", df.format(employee.getBirthday()));
        bundle.putString("photo", employee.getPhoto());
        bundle.putString("profession", employee.getOccupation().getProfession());
        bundle.putInt("code", employee.getOccupation().getCode());
    }
}