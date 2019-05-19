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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        if (name != null ? !name.equals(employee.name) : employee.name != null) return false;
        if (last != null ? !last.equals(employee.last) : employee.last != null) return false;
        if (birthday != null ? !birthday.equals(employee.birthday) : employee.birthday != null)
            return false;
        if (photo != null ? !photo.equals(employee.photo) : employee.photo != null) return false;
        if (occupation != null ? !occupation.equals(employee.occupation) : employee.occupation != null)
            return false;
        return format != null ? format.equals(employee.format) : employee.format == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (last != null ? last.hashCode() : 0);
        result = 31 * result + (birthday != null ? birthday.hashCode() : 0);
        result = 31 * result + (photo != null ? photo.hashCode() : 0);
        result = 31 * result + (occupation != null ? occupation.hashCode() : 0);
        result = 31 * result + (format != null ? format.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "name='" + name + '\''
                + ", last='" + last + '\''
                + ", occupation=" + occupation;
    }
}