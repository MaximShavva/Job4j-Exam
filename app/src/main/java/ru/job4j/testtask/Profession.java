package ru.job4j.testtask;

import java.util.Objects;

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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Profession that = (Profession) o;
        return code == that.code && profession.equals(that.profession);
    }

    @Override
    public int hashCode() {
        int result = profession != null ? profession.hashCode() : 0;
        result = 31 * result + code;
        return result;
    }

    @Override
    public String toString() {
        return "profession: '" + profession + '\'' + ", code: " + code;
    }
}