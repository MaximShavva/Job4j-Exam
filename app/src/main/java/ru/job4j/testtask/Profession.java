package ru.job4j.testtask;

/**
 * Класс содержит данные о конкретной профессии.
 *
 * @author Шавва Максим.
 * @version 1.
 * @since 16.05.2019г.
 */
public class Profession {
    /**
     * Название.
     */
    private String profession;

    /**
     * Код профессии.
     */
    private int code;

    public Profession(String profession, int code) {
        this.profession = profession;
        this.code = code;
    }

    public String getProfession() {
        return profession;
    }

    public int getCode() {
        return code;
    }

    @Override
    public String toString() {
        return "profession: '" + profession + '\'' + ", code: " + code;
    }
}