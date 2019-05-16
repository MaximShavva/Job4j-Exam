package ru.job4j.testtask;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Класс содержит данные профиля работника.
 *
 * @author Шавва Максим.
 * @version 1.
 * @since 16.05.2019г.
 */
public class Employee {

    /**
     * Имя работника.
     */
    private String name;

    /**
     * Фамилия работника.
     */
    private String last;

    /**
     * Дата рождения работника.
     */
    private Date birthday;

    /**
     * Ссылка на фотографию работника.
     */
    private String photo;

    /**
     * Данные по профессии работника.
     */
    private Profession occupation;

    SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy", Locale.FRANCE);

    public Employee(String name,
                    String last,
                    String birthday,
                    String photo,
                    Profession occupation) {
        this.name = name;
        this.last = last;
        this.photo = photo;
        this.occupation = occupation;
        try {
            this.birthday = format.parse(birthday);
        } catch (ParseException e) {
            this.birthday = new Date(1);
        }
    }

    public String getName() {
        return name;
    }

    public String getLast() {
        return last;
    }

    public Date getBirthday() {
        return birthday;
    }

    public String getPhoto() {
        return photo;
    }

    public Profession getOccupation() {
        return occupation;
    }

    public SimpleDateFormat getFormat() {
        return format;
    }

    @Override
    public String toString() {
        return "name='" + name + '\''
                + ", last='" + last + '\''
                + ", occupation=" + occupation;
    }
}