package ru.job4j.testtask;

import android.app.Activity;
import android.content.res.AssetManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import ru.job4j.testtask.Employee;

/**
 * Класс для Формирования списка работников из JSon-файла.
 *
 * @author Шавва Максим.
 * @version 1.
 * @since 15.05.2019г.
 */
public class EmployeeSupplier {

    /**
     * Содержит список работников
     */
    private final List<Employee> staff = new ArrayList<>();

    /**
     * Считываем данные из assets.
     */
    private String getStringFromAssetFile(Activity context, String fileName)
            throws IOException {
        AssetManager am = context.getAssets();
        InputStream is = am.open(fileName);
        return streamToString(is);
    }

    /**
     * Вычитываем из потока строку.
     */
    private String streamToString(InputStream is) throws IOException {
        int size = is.available();
        byte[] buffer = new byte[size];
        is.read(buffer);
        is.close();
        return new String(buffer, "UTF-8");
    }

    /**
     * формируем список stuff из json - строки.
     *
     * @param js Строка в формате JSon.
     */
    private void setStaff(String js) throws JSONException {
        JSONObject obj = new JSONObject(js);
        JSONArray profiles = obj.getJSONArray("profiles");
        for (int i = 0; i != profiles.length(); i++) {
            JSONObject profile = profiles.getJSONObject(i);
            staff.add(new Employee(
                    profile.getString("firstName"),
                    profile.getString("lastName"),
                    profile.getString("birthDay"),
                    profile.getString("photo"),
                    new Profession(
                            profile.getString("profession"),
                            profile.getInt("code"))
            ));
        }
    }

    /**
     * @param context Ссылка на вызывающую активность.
     * @param file    Имя файла JSON-данных.
     * @return Список работников.
     */
    public List<Employee> getStaff(Activity context, String file)
            throws JSONException, IOException {
        String data = getStringFromAssetFile(context, file);
        setStaff(data);
        return staff;
    }
}