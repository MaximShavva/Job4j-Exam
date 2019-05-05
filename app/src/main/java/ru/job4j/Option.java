package ru.job4j;

/**
 * Класс - вариант ответа.
 *
 * @author Шавва Максим.
 * @version 1.
 * @since 6.05.2019г.
 */
public class Option {

    /**
     * ID варианта ответа.
     */
    private int id;

    /**
     * Текст варианта ответа.
     */
    private String text;

    public Option(int id, String text) {
        this.id = id;
        this.text = text;
    }

    public int getId() {
        return id;
    }

    public String getText() {
        return text;
    }
}