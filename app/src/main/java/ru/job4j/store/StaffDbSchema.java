package ru.job4j.store;

/**
 * Схема для базы данных работников.
 *
 * @author Шавва Максим.
 * @version 1.
 * @since 21.05.2019г.
 */
public class StaffDbSchema {
    public static final class StaffTable {
        public static final String STAFF = "staff";

        public static final class Cols {
            public static final String FIRSTNAME = "firstName";
            public static final String LASTNAME = "lastName";
            public static final String BIRTHDAY = "birthDay";
            public static final String PHOTOURL = "photoUrl";
            public static final String PROFESSION = "profession";
            public static final String CODE = "code";
        }
    }
}