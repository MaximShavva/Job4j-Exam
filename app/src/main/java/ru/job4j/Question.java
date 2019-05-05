package ru.job4j;

import java.util.List;

/**
 * Класс - сам вопрос с вариантами ответов и правильным ответом.
 *
 * @author Шавва Максим.
 * @version 1.
 * @since 6.05.2019г.
 */
public class Question {

    /**
     * ID Вопроса.
     */
    private int id;

    /**
     * Текст вопроса.
     */
    private String text;

    /**
     * Список вариантов ответа.
     */
    private List<Option> options;

    /**
     * Правильный овтет.
     */
    private int answer;

    /**
     * ответ пользователя.
     */
    private int given;

    public Question(int id, String text, List<Option> options, int answer) {
        this.id = id;
        this.text = text;
        this.options = options;
        this.answer = answer;
        this.given = -1;
    }

    public String getText() {
        return text;
    }

    public List<Option> getOptions() {
        return options;
    }

    public int getAnswer() {
        return answer;
    }

    public int getGiven() {
        return given;
    }

    public void setGiven(int given) {
        this.given = given;
    }
}
