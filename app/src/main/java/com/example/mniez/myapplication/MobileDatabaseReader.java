package com.example.mniez.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mniez on 09.10.2017.
 */

public class MobileDatabaseReader extends SQLiteOpenHelper {

    /*--------- ZBUDOWANIE BAZY-------------------------*/
    private Context mKontekst;
    //podstawowe dane dot. bazy
    private static final String LOG = "mobileDatabaseReader";
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "dummyDatabase";

    //nazwy tabel
    private static final String TABLE_COURSES = "courses";
    private static final String TABLE_DIFFICULTY = "difficultyLevel";
    private static final String TABLE_LESSONS = "lessons";
    private static final String TABLE_EXAMS = "exams";
    private static final String TABLE_LANGUAGES = "languages";
    private static final String TABLE_ANSWERTYPES = "questionsAnswerTypes";
    private static final String TABLE_LECTURES = "lectures";
    private static final String TABLE_TESTS = "tests";
    private static final String TABLE_TESTQUESTIONS = "questionsForTests";
    private static final String TABLE_EXAMQUESTIONS = "questionsForExams";
    private static final String TABLE_WORDS = "words";

    //wspolne pola dla tabel
    private static final String KEY_ID = "id";
    private static final String DESCRIPTION = "description";
    private static final String NAME = "name";
    private static final String LESSON_ID = "lesson_id";

    //tabela courses
    private static final String ACCESS_CODE = "access_code";
    private static final String AVATAR = "avatar";
    private static final String COURSENAME = "course_name";
    private static final String CREATED_AT = "created_at";
    private static final String LEARNED_LANGUAGE_ID = "learning_language_id";
    private static final String LEVEL_ID = "level_id";
    private static final String NATIVE_LANGUAGE_ID = "native_language_id";
    private static final String TAGS = "tags";
    private static final String TEACHER_ID = "teacher_id";
    private static final String TEACHER_NAME = "teacher_name";
    private static final String TEACHER_SURNAME = "teacher_surname";
    private static final String LEARNED_LANGUAGE_NAME = "learned_language_name";
    private static final String NATIVE_LANGUAGE_NAME = "native_language_name";

    //tabela difficultyLevel
    private static final String LEVEL_NAME = "level_name";

    //tabela lessons
    private static final String COURSE_ID = "course_id";
    private static final String LESSON_NUMBER = "lesson_number";
    private static final String OVERALL_POINTS = "overall_points";
    private static final String USER_POINTS = "user_points";

    //tabela exams
    private static final String POINTS_TO_PASS = "points_to_pass";
    private static final String IS_NEW = "is_new";
    private static final String IS_PASSED = "is_passed";
    private static final String SCORE = "score";
    private static final String GRADE = "grade";
    private static final String IS_LOCAL = "is_local";

    //tabela tests
    private static final String IS_COMPLETED = "is_completed";

    //tabela languages
    private static final String LANGUAGE_NAME = "language_name";

    //tabela questionsAnswerTypes
    private static final String TYPE_NAME = "type_name";

    //tabela lectures
    private static final String LECTURE_URL = "url";

    //tabela questionsForTests
    private static final String ANSWER_ID = "answer_id";
    private static final String ANSWER_TYPE_ID = "answer_type_id";
    private static final String POINTS = "points";
    private static final String QUESTION = "question";
    private static final String QUESTION_TYPE_ID = "question_type_id";
    private static final String TEST_ID = "test_id";

    //tabela words
    private static final String NATIVE_DEFINITION = "native_definition";
    private static final String NATIVE_SOUND = "native_sound";
    private static final String NATIVE_WORD = "native_word";
    private static final String PICTURE = "picture";
    private static final String TRANSLATED_DEFINITION = "translated_definition";
    private static final String TRANSLATED_LANGUAGE_ID = "translated_language_id";
    private static final String TRANSLATED_SOUND = "translated_sound";
    private static final String TRANSLATED_WORD = "translated_word";
    private static final String CREATOR_ID = "user_id";

    //utworzenie tabeli courses
    private static final String CREATE_TABLE_COURSES = "CREATE TABLE IF NOT EXISTS " + TABLE_COURSES
            + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + ACCESS_CODE + " TEXT, " + AVATAR + " TEXT," + COURSENAME + " TEXT, "
            + CREATED_AT + " DATETIME, " + DESCRIPTION + " TEXT, " + LEARNED_LANGUAGE_ID + " INT," + LEVEL_ID + " INT, " + NATIVE_LANGUAGE_ID + " INT, "
            + TAGS + " TEXT," + TEACHER_ID + " INT, " + TEACHER_NAME  + " TEXT, " + TEACHER_SURNAME + " TEXT, " + LEVEL_NAME + " TEXT, " + LEARNED_LANGUAGE_NAME
            + " TEXT, " + NATIVE_LANGUAGE_NAME + " TEXT" + ");";

    //utworzenie tabeli difficulty
    private static final String CREATE_TABLE_DIFFICULTY = "CREATE TABLE  IF NOT EXISTS " + TABLE_DIFFICULTY
            + "(" + KEY_ID + " INTEGER PRIMARY KEY, " +  LEVEL_NAME + " TEXT" + ");";

    //utworzenie tabeli lessons
    private static final String CREATE_TABLE_LESSONS = "CREATE TABLE IF NOT EXISTS " + TABLE_LESSONS
            + "(" + LESSON_ID + " INTEGER PRIMARY KEY, " + COURSE_ID + " INT, " + DESCRIPTION + " TEXT, " + LESSON_NUMBER + " INT, " + NAME + " TEXT, " + OVERALL_POINTS + " INT, "
            + USER_POINTS + " INT" + ");";

    //utworzenie tabeli exams
    private static final String CREATE_TABLE_EXAMS = "CREATE TABLE IF NOT EXISTS " + TABLE_EXAMS
            + "(" + KEY_ID + " INTEGER PRIMARY KEY, " + DESCRIPTION + " TEXT, " + LESSON_ID + " INT, " + NAME + " TEXT, " + POINTS_TO_PASS + " INT"
            + IS_NEW  + " INT, " + IS_PASSED + " INT, " + SCORE + " REAL, " + GRADE + " INT, " + IS_LOCAL + " INT" + ");";

    //utworzenie tabeli languages
    private static final String CREATE_TABLE_LANGUAGES = "CREATE TABLE IF NOT EXISTS " + TABLE_LANGUAGES
            + "(" + KEY_ID + " INTEGER PRIMARY KEY, " + LANGUAGE_NAME + " TEXT" + ");";

    //utworzenie tabeli exams
    private static final String CREATE_TABLE_ANSWERTYPES = "CREATE TABLE IF NOT EXISTS " + TABLE_ANSWERTYPES
            + "(" + KEY_ID + "  INTEGER PRIMARY KEY, " + TYPE_NAME + " TEXT" + ");";

    //utworzenie tabeli exams
    private static final String CREATE_TABLE_LECTURES = "CREATE TABLE IF NOT EXISTS " + TABLE_LECTURES
            + "(" + KEY_ID + " INTEGER PRIMARY KEY, " + LESSON_ID + " INT, " + NAME + " TEXT, " + LECTURE_URL + " TEXT" + ");";

    //utworzenie tabeli exams
    private static final String CREATE_TABLE_TESTS = "CREATE TABLE IF NOT EXISTS " + TABLE_TESTS
            + "(" + KEY_ID + " INTEGER PRIMARY KEY, " + DESCRIPTION + " TEXT, "+ LESSON_ID + " INT, " + IS_NEW  + " INT, " + IS_COMPLETED + " INT, "
            + SCORE + " REAL, " + IS_LOCAL + " INT" + ");";

    //utworzenie tabeli exams
    private static final String CREATE_TABLE_TESTQUESTIONS = "CREATE TABLE IF NOT EXISTS " + TABLE_TESTQUESTIONS
            + "(" + KEY_ID + " INTEGER PRIMARY KEY, " + ANSWER_ID + " INT, " + ANSWER_TYPE_ID + " INT, " + POINTS + " INT, " + QUESTION + " TEXT, "
            + QUESTION_TYPE_ID + " INT, " + TEST_ID + " INT" + ");";

    //utworzenie tabeli exams
    private static final String CREATE_TABLE_EXAMQUESTIONS = "CREATE TABLE IF NOT EXISTS " + TABLE_EXAMQUESTIONS
            + "(" + KEY_ID + " INTEGER PRIMARY KEY, " + ANSWER_ID + " INT, " + ANSWER_TYPE_ID + " INT, " + POINTS + " INT, " + QUESTION + " TEXT, "
            + QUESTION_TYPE_ID + " INT, " + TEST_ID + " INT" + ");";

    //utworzenie tabeli exams
    private static final String CREATE_TABLE_WORDS = "CREATE TABLE IF NOT EXISTS " + TABLE_WORDS
            + "(" + KEY_ID + " INTEGER PRIMARY KEY, " + NATIVE_DEFINITION + " TEXT, " + NATIVE_LANGUAGE_ID + " INT, " + NATIVE_SOUND + " TEXT, "
            + NATIVE_WORD + " TEXT, " + PICTURE + " TEXT, " + TAGS + " TEXT, " + TRANSLATED_DEFINITION + " TEXT, "
            + TRANSLATED_LANGUAGE_ID + " INT, " + TRANSLATED_SOUND + " TEXT, " + TRANSLATED_WORD + " TEXT, " + CREATOR_ID + " INT" + ");";

    public MobileDatabaseReader(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mKontekst = context;
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_COURSES);
        db.execSQL(CREATE_TABLE_DIFFICULTY);
        db.execSQL(CREATE_TABLE_LESSONS);
        db.execSQL(CREATE_TABLE_EXAMS);
        db.execSQL(CREATE_TABLE_LANGUAGES);
        db.execSQL(CREATE_TABLE_ANSWERTYPES);
        db.execSQL(CREATE_TABLE_LECTURES);
        db.execSQL(CREATE_TABLE_TESTS);
        db.execSQL(CREATE_TABLE_TESTQUESTIONS);
        db.execSQL(CREATE_TABLE_EXAMQUESTIONS);
        db.execSQL(CREATE_TABLE_WORDS);
        Log.i(LOG, "Database creation");
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COURSES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DIFFICULTY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LESSONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXAMS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LANGUAGES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ANSWERTYPES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LECTURES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TESTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TESTQUESTIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXAMQUESTIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WORDS);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public long insertCourse(Course course) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID, course.getId());
        values.put(ACCESS_CODE, course.getAccessCode());
        values.put(AVATAR, course.getAvatar());
        values.put(COURSENAME, course.getCourseName());
        values.put(CREATED_AT, String.valueOf(course.getCreatedAt()));
        values.put(DESCRIPTION, course.getDescription());
        values.put(LEARNED_LANGUAGE_ID, course.getLearningLanguageId());
        values.put(LEVEL_ID, course.getLevelId());
        values.put(NATIVE_LANGUAGE_ID, course.getNativeLanguageId());
        values.put(TAGS, course.getTags());
        values.put(TEACHER_ID, course.getTeacherId());
        values.put(TEACHER_NAME, course.getTeacherName());
        values.put(TEACHER_SURNAME, course.getTeacherSurname());
        values.put(LEVEL_NAME, course.getLevelName());
        values.put(LEARNED_LANGUAGE_NAME, course.getLearnedLanguageName());
        values.put(NATIVE_LANGUAGE_NAME, course.getNativeLanguageName());

        long course_id = db.insertWithOnConflict(TABLE_COURSES, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        return course_id;
    }

    public ArrayList<Course> selectAllCourses() {
        ArrayList<Course> courses = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_COURSES;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            do {
                Course cs = new Course();
                cs.setId(c.getInt(c.getColumnIndex(KEY_ID)));
                cs.setAccessCode(c.getString(c.getColumnIndex(ACCESS_CODE)));
                cs.setAvatar(c.getString(c.getColumnIndex(AVATAR)));
                cs.setCourseName(c.getString(c.getColumnIndex(COURSENAME)));
                cs.setCreatedAt(c.getString(c.getColumnIndex(CREATED_AT)));
                cs.setDescription(c.getString(c.getColumnIndex(DESCRIPTION)));
                cs.setLearningLanguageId(c.getInt(c.getColumnIndex(LEARNED_LANGUAGE_ID)));
                cs.setLevelId(c.getInt(c.getColumnIndex(LEVEL_ID)));
                cs.setNativeLanguageId(c.getInt(c.getColumnIndex(NATIVE_LANGUAGE_ID)));
                cs.setTags(c.getString(c.getColumnIndex(TAGS)));
                cs.setTeacherId(c.getInt(c.getColumnIndex(TEACHER_ID)));
                cs.setTeacherName(c.getString(c.getColumnIndex(TEACHER_NAME)));
                cs.setTeacherSurname(c.getString(c.getColumnIndex(TEACHER_SURNAME)));
                cs.setLevelName(c.getString(c.getColumnIndex(LEVEL_NAME)));
                cs.setLearnedLanguageName(c.getString(c.getColumnIndex(LEARNED_LANGUAGE_NAME)));
                cs.setNativeLanguageName(c.getString(c.getColumnIndex(NATIVE_LANGUAGE_NAME)));
                System.out.println(cs.getLevelName());
                courses.add(cs);
            } while (c.moveToNext());
        }
        return courses;
    }

    public long insertLesson(Lesson lesson, int courseId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(LESSON_ID, lesson.getLessonId());
        values.put(COURSE_ID, courseId);
        values.put(DESCRIPTION, lesson.getDescription());
        values.put(NAME, lesson.getName());
        values.put(LESSON_NUMBER, lesson.getLessonNumber());
        values.put(OVERALL_POINTS, lesson.getOverallPoints());
        values.put(USER_POINTS, lesson.getUserPoints());
        long lesson_id = db.insertWithOnConflict(TABLE_LESSONS, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        return lesson_id;
    }

    public ArrayList<Lesson> selectAllLessonsForCourse(Integer courseId) {
        ArrayList<Lesson> lessons = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_LESSONS + " WHERE " + COURSE_ID + " = " + courseId;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            do {
                Lesson ls = new Lesson();
                ls.setLessonId(c.getInt(c.getColumnIndex(LESSON_ID)));
                ls.setCourseId(c.getInt(c.getColumnIndex(COURSE_ID)));
                ls.setDescription(c.getString(c.getColumnIndex(DESCRIPTION)));
                ls.setName(c.getString(c.getColumnIndex(NAME)));
                ls.setLessonNumber(c.getInt(c.getColumnIndex(LESSON_NUMBER)));
                ls.setOverallPoints(c.getInt(c.getColumnIndex(OVERALL_POINTS)));
                ls.setUserPoints(c.getInt(c.getColumnIndex(USER_POINTS)));
                System.out.println(ls.getName());
                lessons.add(ls);
            } while (c.moveToNext());
        }
        return lessons;
    }
}
