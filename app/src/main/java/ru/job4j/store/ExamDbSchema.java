package ru.job4j.store;

/**
 * Класс содержит константы для базы данных экзаменов..
 *
 * @author Шавва Максим.
 * @version 1.
 * @since 17.05.2019г.
 */
public class ExamDbSchema {
    public static final class ExamTable {
        public static final String NAME = "exams";

        public static final class Cols {
            public static final String TITLE = "title";
            public static final String DATE = "date";
            public static final String RESULT = "result";
        }
    }
}
