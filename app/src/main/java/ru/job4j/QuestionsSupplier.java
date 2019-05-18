package ru.job4j;

import android.app.Activity;
import android.content.res.AssetManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс для Формирования списка вопросов из JSon-файла.
 *
 * @author Шавва Максим.
 * @version 1.
 * @since 12.05.2019г.
 */
public class QuestionsSupplier {

    /**
     * Содержит список вопросов
     */
    private final List<Question> questions = new ArrayList<>();

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
     * формируем список questions из json - строки.
     *
     * @param js Строка в формате JSon.
     */
    private void setQuestions(String js) throws JSONException {
        JSONObject obj = new JSONObject(js);
        JSONArray qsts = obj.getJSONArray("questions");
        for (int i = 0; i != qsts.length(); i++) {
            JSONObject qst = qsts.getJSONObject(i);
            int id = qst.getInt("ID");
            String text = qst.getString("TEXT");
            JSONArray opts = qst.getJSONArray("OPTIONS");
            int answer = qst.getInt("ANSWER");
            questions.add(new Question(id, text, getOptions(opts), answer));
        }
    }

    /**
     * Вынес отдельно метод для прочтения списка вариантов ответа.
     *
     * @param opts список вариантов ответа в формате JSon.
     */
    private List<Option> getOptions(JSONArray opts) throws JSONException {
        List<Option> optionList = new ArrayList<>();
        for (int j = 0; j != opts.length(); j++) {
            JSONObject opt = opts.optJSONObject(j);
            optionList.add(new Option(
                    opt.getInt("OPTION_ID"),
                    opt.getString("OPTION_TEXT")
            ));
        }
        return optionList;
    }

    /**
     * @param context Ссылка на вызывающую активность.
     * @param file    Имя файла JSON-данных.
     * @return Список вопросов.
     */
    public List<Question> getQuestions(Activity context, String file)
            throws JSONException, IOException {
        String data = getStringFromAssetFile(context, file);
        setQuestions(data);
        return questions;
    }
}