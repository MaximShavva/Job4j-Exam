package ru.job4j.testtask;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import ru.job4j.testtask.Employee;

/**
 * Класс для Формирования списка работников из JSon-файла.
 *
 * @author Шавва Максим.
 * @version 1.1
 * @since 21.05.2019г.
 */
public class EmployeeSupplier {

    /**
     * Содержит список работников
     */
    private final List<Employee> staff = new ArrayList<>();

    /**
     * Считываем данные из assets.
     */
    private String getStringFromAssetFile(Context context, String fileName)
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
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy", Locale.FRANCE);
        JSONObject obj = new JSONObject(js);
        JSONArray profiles = obj.getJSONArray("profiles");
        for (int i = 0; i != profiles.length(); i++) {
            JSONObject profile = profiles.getJSONObject(i);
            Date date;
            try {
                date = format.parse(profile.getString("birthDay"));
            } catch (ParseException e) {
                date = new Date(1);
            }
            staff.add(new Employee(
                    profile.getString("firstName"),
                    profile.getString("lastName"),
                    date,
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
    public List<Employee> getStaff(Context context, String file)
            throws JSONException, IOException {
        String data = getStringFromAssetFile(context, file);
        setStaff(data);
        return staff;
    }
}