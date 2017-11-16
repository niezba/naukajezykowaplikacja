package com.example.mniez.myapplication.DatabaseAccess;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.mniez.myapplication.ObjectHelper.Course;
import com.example.mniez.myapplication.ObjectHelper.Exam;
import com.example.mniez.myapplication.ObjectHelper.ExamQuestion;
import com.example.mniez.myapplication.ObjectHelper.Language;
import com.example.mniez.myapplication.ObjectHelper.Lecture;
import com.example.mniez.myapplication.ObjectHelper.Lesson;
import com.example.mniez.myapplication.ObjectHelper.QuestionAnswerType;
import com.example.mniez.myapplication.ObjectHelper.Test;
import com.example.mniez.myapplication.ObjectHelper.TestQuestion;
import com.example.mniez.myapplication.ObjectHelper.Word;

import java.util.ArrayList;

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
    private static final String IS_AVATAR_LOCAL = "is_avatar_local";
    private static final String AVATAR_LOCAL = "avatar_local_src";

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
    private static final String IS_COMPLETED_LOCAL = "is_completed_local";
    private static final String ANSWER_CONCATENATION = "answer_concatenation";

    //tabela tests
    private static final String IS_COMPLETED = "is_completed";

    //tabela languages
    private static final String LANGUAGE_NAME = "language_name";

    //tabela questionsAnswerTypes
    private static final String TYPE_NAME = "type_name";

    //tabela lectures
    private static final String LECTURE_URL = "url";
    private static final String IS_LECTURE_LOCAL = "is_lecture_local";
    private static final String LECTURE_LOCAL = "lecture_local_src";

    //tabela questionsForTests
    private static final String ANSWER_ID = "answer_id";
    private static final String ANSWER_TYPE_ID = "answer_type_id";
    private static final String POINTS = "points";
    private static final String QUESTION = "question";
    private static final String QUESTION_TYPE_ID = "question_type_id";
    private static final String TEST_ID = "test_id";
    private static final String OTHER_ANSWERONE = "other_answerone";
    private static final String OTHER_ANSWERTWO = "other_answertwo";
    private static final String OTHER_ANSWERTHREE = "other_answerthree";
    private static final String EXAM_ID = "exam_id";

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
    private static final String IS_NATIVE_SOUND_LOCAL = "is_native_sound_local";
    private static final String NATIVE_SOUND_LOCAL = "native_sound_local";
    private static final String IS_TRANSLATED_SOUND_LOCAL = "is_translated_sound_local";
    private static final String TRANSLATED_SOUND_LOCAL = "translated_sound_local";
    private static final String IS_PICTURE_LOCAL = "is_picture_local";
    private static final String PICTURE_LOCAL = "picture_local";

    //utworzenie tabeli courses
    private static final String CREATE_TABLE_COURSES = "CREATE TABLE IF NOT EXISTS " + TABLE_COURSES
            + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + ACCESS_CODE + " TEXT, " + AVATAR + " TEXT," + COURSENAME + " TEXT, "
            + CREATED_AT + " DATETIME, " + DESCRIPTION + " TEXT, " + LEARNED_LANGUAGE_ID + " INT," + LEVEL_ID + " INT, " + NATIVE_LANGUAGE_ID + " INT, "
            + TAGS + " TEXT," + TEACHER_ID + " INT, " + TEACHER_NAME  + " TEXT, " + TEACHER_SURNAME + " TEXT, " + LEVEL_NAME + " TEXT, " + LEARNED_LANGUAGE_NAME
            + " TEXT, " + NATIVE_LANGUAGE_NAME + " TEXT, " + IS_AVATAR_LOCAL + " INT, " + AVATAR_LOCAL + " TEXT" + ");";

    //utworzenie tabeli difficulty
    private static final String CREATE_TABLE_DIFFICULTY = "CREATE TABLE  IF NOT EXISTS " + TABLE_DIFFICULTY
            + "(" + KEY_ID + " INTEGER PRIMARY KEY, " +  LEVEL_NAME + " TEXT" + ");";

    //utworzenie tabeli lessons
    private static final String CREATE_TABLE_LESSONS = "CREATE TABLE IF NOT EXISTS " + TABLE_LESSONS
            + "(" + LESSON_ID + " INTEGER PRIMARY KEY, " + COURSE_ID + " INT, " + DESCRIPTION + " TEXT, " + LESSON_NUMBER + " INT, " + NAME + " TEXT, " + OVERALL_POINTS + " INT, "
            + USER_POINTS + " INT" + ");";

    //utworzenie tabeli exams
    private static final String CREATE_TABLE_EXAMS = "CREATE TABLE IF NOT EXISTS " + TABLE_EXAMS
            + "(" + KEY_ID + " INTEGER PRIMARY KEY, " + DESCRIPTION + " TEXT, " + LESSON_ID + " INT, " + NAME + " TEXT, " + POINTS_TO_PASS + " INT, "
            + IS_NEW  + " INT, " + IS_PASSED + " INT, " + SCORE + " REAL, " + GRADE + " INT, " + IS_LOCAL + " INT, " + IS_COMPLETED_LOCAL + " INT, " + ANSWER_CONCATENATION + " TEXT" + ");";

    //utworzenie tabeli languages
    private static final String CREATE_TABLE_LANGUAGES = "CREATE TABLE IF NOT EXISTS " + TABLE_LANGUAGES
            + "(" + KEY_ID + " INTEGER PRIMARY KEY, " + LANGUAGE_NAME + " TEXT" + ");";

    //utworzenie tabeli answertypes
    private static final String CREATE_TABLE_ANSWERTYPES = "CREATE TABLE IF NOT EXISTS " + TABLE_ANSWERTYPES
            + "(" + KEY_ID + "  INTEGER PRIMARY KEY, " + TYPE_NAME + " TEXT" + ");";

    //utworzenie tabeli lectures
    private static final String CREATE_TABLE_LECTURES = "CREATE TABLE IF NOT EXISTS " + TABLE_LECTURES
            + "(" + KEY_ID + " INTEGER PRIMARY KEY, " + LESSON_ID + " INT, " + NAME + " TEXT, " + LECTURE_URL + " TEXT, " + IS_LECTURE_LOCAL + " INT, " + LECTURE_LOCAL + " TEXT" + ");";

    //utworzenie tabeli tests
    private static final String CREATE_TABLE_TESTS = "CREATE TABLE IF NOT EXISTS " + TABLE_TESTS
            + "(" + KEY_ID + " INTEGER PRIMARY KEY, " + DESCRIPTION + " TEXT, "+ LESSON_ID + " INT, " + IS_NEW  + " INT, " + IS_COMPLETED + " INT, "
            + SCORE + " REAL, " + IS_LOCAL + " INT, " + IS_COMPLETED_LOCAL + " INT, " + ANSWER_CONCATENATION + " TEXT" + ");";

    //utworzenie tabeli testquestions
    private static final String CREATE_TABLE_TESTQUESTIONS = "CREATE TABLE IF NOT EXISTS " + TABLE_TESTQUESTIONS
            + "(" + KEY_ID + " INTEGER PRIMARY KEY, " + ANSWER_ID + " INT, " + ANSWER_TYPE_ID + " INT, " + POINTS + " INT, " + QUESTION + " TEXT, "
            + QUESTION_TYPE_ID + " INT, " + TEST_ID + " INT, " + OTHER_ANSWERONE + " INT, " + OTHER_ANSWERTWO + " INT, " + OTHER_ANSWERTHREE + " INT" + ");";

    //utworzenie tabeli examquestions
    private static final String CREATE_TABLE_EXAMQUESTIONS = "CREATE TABLE IF NOT EXISTS " + TABLE_EXAMQUESTIONS
            + "(" + KEY_ID + " INTEGER PRIMARY KEY, " + ANSWER_ID + " INT, " + ANSWER_TYPE_ID + " INT, " + POINTS + " INT, " + QUESTION + " TEXT, "
            + QUESTION_TYPE_ID + " INT, " + EXAM_ID + " INT, " + OTHER_ANSWERONE + " INT, " + OTHER_ANSWERTWO + " INT, " + OTHER_ANSWERTHREE + " INT" + ");";

    //utworzenie tabeli words
    private static final String CREATE_TABLE_WORDS = "CREATE TABLE IF NOT EXISTS " + TABLE_WORDS
            + "(" + KEY_ID + " INTEGER PRIMARY KEY, " + NATIVE_DEFINITION + " TEXT, " + NATIVE_LANGUAGE_ID + " INT, " + NATIVE_SOUND + " TEXT, "
            + NATIVE_WORD + " TEXT, " + PICTURE + " TEXT, " + TAGS + " TEXT, " + TRANSLATED_DEFINITION + " TEXT, "
            + TRANSLATED_LANGUAGE_ID + " INT, " + TRANSLATED_SOUND + " TEXT, " + TRANSLATED_WORD + " TEXT, " + CREATOR_ID + " INT, " +
            IS_NATIVE_SOUND_LOCAL + " INT, " + NATIVE_SOUND_LOCAL + " TEXT, " + IS_TRANSLATED_SOUND_LOCAL + " INT, " + TRANSLATED_SOUND_LOCAL + " TEXT, " +
            IS_PICTURE_LOCAL + " INT, " + PICTURE_LOCAL + " TEXT" + ");";

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
        values.put(IS_AVATAR_LOCAL, course.getIsAvatarLocal());
        values.put(AVATAR_LOCAL, course.getAvatarLocal());
        long course_id = db.insertWithOnConflict(TABLE_COURSES, null, values, SQLiteDatabase.CONFLICT_IGNORE);
        db.close();
        return course_id;
    }

    public long updateCourse(Course course) {
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
        long course_id = db.update(TABLE_COURSES, values, KEY_ID + '=' + course.getId(), null);
        db.close();
        return course_id;
    }

    public long updateCourseFullSynch(Course course) {
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
        values.put(IS_AVATAR_LOCAL, course.getIsAvatarLocal());
        values.put(AVATAR_LOCAL, course.getAvatarLocal());
        long course_id = db.update(TABLE_COURSES, values, KEY_ID + '=' + course.getId(), null);
        db.close();
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
                cs.setIsAvatarLocal(c.getInt(c.getColumnIndex(IS_AVATAR_LOCAL)));
                cs.setAvatarLocal(c.getString(c.getColumnIndex(AVATAR_LOCAL)));
                System.out.println(cs.getLevelName());
                courses.add(cs);
            } while (c.moveToNext());
        }
        db.close();
        return courses;
    }

    public Course selectCourse(int courseId) {
        Course cs = new Course();
        String selectQuery = "SELECT * FROM " + TABLE_COURSES + " WHERE " + KEY_ID + " = " + courseId;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            do {
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
                cs.setIsAvatarLocal(c.getInt(c.getColumnIndex(IS_AVATAR_LOCAL)));
                cs.setAvatarLocal(c.getString(c.getColumnIndex(AVATAR_LOCAL)));
                System.out.println(cs.getLevelName());
            } while (c.moveToNext());
        }
        db.close();
        return cs;
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
        db.close();
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
        db.close();
        return lessons;
    }

    public ArrayList<Integer> selectAllLessonsIdsForCourse(Integer courseId) {
        ArrayList<Integer> lessonIds = new ArrayList<>();
        String selectQuery = "SELECT " + LESSON_ID + " FROM " + TABLE_LESSONS + " WHERE " + COURSE_ID + " = " + courseId;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            do {
                Integer lessonId = c.getInt(c.getColumnIndex(LESSON_ID));
                lessonIds.add(lessonId);
            } while (c.moveToNext());
        }
        db.close();
        return lessonIds;
    }

    public long insertTest(Test test) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID, test.getId());
        values.put(DESCRIPTION, test.getDescription());
        values.put(LESSON_ID, test.getLessonId());
        values.put(IS_NEW, test.getIsNew());
        values.put(IS_COMPLETED, test.getIsCompleted());
        values.put(SCORE, test.getScore());
        values.put(IS_LOCAL, test.getIsLocal());
        values.put(IS_COMPLETED_LOCAL, test.getIsCompletedLocal());
        values.put(ANSWER_CONCATENATION, test.getAnswersConcatenation());
        long test_id = db.insertWithOnConflict(TABLE_TESTS, null, values, SQLiteDatabase.CONFLICT_IGNORE);
        db.close();
        return test_id;
    }

    public long updateTest(Test test) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID, test.getId());
        values.put(DESCRIPTION, test.getDescription());
        values.put(LESSON_ID, test.getLessonId());
        values.put(IS_NEW, test.getIsNew());
        values.put(IS_COMPLETED, test.getIsCompleted());
        values.put(SCORE, test.getScore());
        values.put(IS_LOCAL, test.getIsLocal());
        long test_id = db.update(TABLE_TESTS, values, KEY_ID + '=' + test.getId(), null);
        db.close();
        return test_id;
    }

    public long updateTestFullSynch(Test test) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID, test.getId());
        values.put(DESCRIPTION, test.getDescription());
        values.put(LESSON_ID, test.getLessonId());
        values.put(IS_NEW, test.getIsNew());
        values.put(IS_COMPLETED, test.getIsCompleted());
        values.put(SCORE, test.getScore());
        values.put(IS_LOCAL, test.getIsLocal());
        values.put(IS_COMPLETED_LOCAL, test.getIsCompletedLocal());
        values.put(ANSWER_CONCATENATION, test.getAnswersConcatenation());
        long test_id = db.update(TABLE_TESTS, values, KEY_ID + '=' + test.getId(), null);
        db.close();
        return test_id;
    }

    public ArrayList<Test> selectAllTestsForLesson(Integer lessonId) {
        ArrayList<Test> tests = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_TESTS + " WHERE " + LESSON_ID + " = " + lessonId;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            do {
                Test ts = new Test();
                ts.setId(c.getInt(c.getColumnIndex(KEY_ID)));
                ts.setLessonId(c.getInt(c.getColumnIndex(LESSON_ID)));
                ts.setDescription(c.getString(c.getColumnIndex(DESCRIPTION)));
                ts.setIsNew(c.getInt(c.getColumnIndex(IS_NEW)));
                ts.setIsCompleted(c.getInt(c.getColumnIndex(IS_COMPLETED)));
                ts.setScore(c.getInt(c.getColumnIndex(SCORE)));
                ts.setIsLocal(c.getInt(c.getColumnIndex(IS_LOCAL)));
                System.out.println(ts.getDescription());
                tests.add(ts);
            } while (c.moveToNext());
        }
        db.close();
        return tests;
    }

    public int calculateTotalPointsForTest(int testId) {
        int testTotal = 0;
        String selectQuery = "SELECT SUM(" + POINTS +") AS pointssum FROM " + TABLE_TESTQUESTIONS + " WHERE " + TEST_ID + "=" + testId;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            do {
                testTotal = testTotal + c.getInt(c.getColumnIndex("pointssum"));

            } while (c.moveToNext());
        }
        db.close();
        return testTotal;
    }

    public int calculateTotalPointsForExam(int examId) {
        int testTotal = 0;
        String selectQuery = "SELECT SUM(" + POINTS +") AS pointssum FROM " + TABLE_EXAMQUESTIONS + " WHERE " + EXAM_ID + "=" + examId;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            do {
                testTotal = testTotal + c.getInt(c.getColumnIndex("pointssum"));

            } while (c.moveToNext());
        }
        db.close();
        return testTotal;
    }

    public long insertExam(Exam exam) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID, exam.getId());
        values.put(DESCRIPTION, exam.getDescription());
        values.put(LESSON_ID, exam.getLessonId());
        values.put(POINTS_TO_PASS, exam.getPointsToPass());
        values.put(IS_NEW, exam.getIsNew());
        values.put(SCORE, exam.getScore());
        values.put(IS_LOCAL, exam.getIsLocal());
        values.put(IS_PASSED, exam.getIsPassed());
        values.put(GRADE, exam.getGrade());
        values.put(IS_COMPLETED_LOCAL, exam.getIsCompletedLocal());
        values.put(ANSWER_CONCATENATION, exam.getAnswersConcatenation());
        long test_id = db.insertWithOnConflict(TABLE_EXAMS, null, values, SQLiteDatabase.CONFLICT_IGNORE);
        db.close();
        return test_id;
    }

    public long updateExam(Exam exam) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID, exam.getId());
        values.put(DESCRIPTION, exam.getDescription());
        values.put(LESSON_ID, exam.getLessonId());
        values.put(POINTS_TO_PASS, exam.getPointsToPass());
        values.put(IS_NEW, exam.getIsNew());
        values.put(SCORE, exam.getScore());
        values.put(IS_LOCAL, exam.getIsLocal());
        values.put(IS_PASSED, exam.getIsPassed());
        values.put(GRADE, exam.getGrade());
        long test_id = db.update(TABLE_EXAMS, values, KEY_ID + '=' + exam.getId(), null);
        db.close();
        return test_id;
    }

    public long updateExamFullSynch(Exam exam) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID, exam.getId());
        values.put(DESCRIPTION, exam.getDescription());
        values.put(LESSON_ID, exam.getLessonId());
        values.put(POINTS_TO_PASS, exam.getPointsToPass());
        values.put(IS_NEW, exam.getIsNew());
        values.put(SCORE, exam.getScore());
        values.put(IS_LOCAL, exam.getIsLocal());
        values.put(IS_PASSED, exam.getIsPassed());
        values.put(GRADE, exam.getGrade());
        values.put(IS_COMPLETED_LOCAL, exam.getIsCompletedLocal());
        values.put(ANSWER_CONCATENATION, exam.getAnswersConcatenation());
        long test_id = db.update(TABLE_EXAMS, values, KEY_ID + '=' + exam.getId(), null);
        db.close();
        return test_id;
    }

    public ArrayList<Exam> selectAllExamsForLesson(Integer lessonId) {
        ArrayList<Exam> exams = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_EXAMS + " WHERE " + LESSON_ID + " = " + lessonId;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            do {
                Exam ex = new Exam();
                ex.setId(c.getInt(c.getColumnIndex(KEY_ID)));
                ex.setLessonId(c.getInt(c.getColumnIndex(LESSON_ID)));
                ex.setDescription(c.getString(c.getColumnIndex(DESCRIPTION)));
                ex.setIsNew(c.getInt(c.getColumnIndex(IS_NEW)));
                ex.setPointsToPass(c.getColumnIndex(POINTS_TO_PASS));
                ex.setIsPassed(c.getInt(c.getColumnIndex(IS_PASSED)));
                ex.setScore(c.getInt(c.getColumnIndex(SCORE)));
                ex.setGrade(c.getInt(c.getColumnIndex(GRADE)));
                ex.setIsLocal(c.getInt(c.getColumnIndex(IS_LOCAL)));
                System.out.println(ex.getDescription());
                exams.add(ex);
            } while (c.moveToNext());
        }
        db.close();
        return exams;
    }

    public long insertLecture(Lecture lecture) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID, lecture.getId());
        values.put(LESSON_ID, lecture.getLessonId());
        values.put(NAME, lecture.getName());
        values.put(LECTURE_URL, lecture.getLectureUrl());
        values.put(IS_LECTURE_LOCAL, lecture.getIsLectureLocal());
        values.put(LECTURE_LOCAL, lecture.getLectureLocal());
        long lecture_id = db.insertWithOnConflict(TABLE_LECTURES, null, values, SQLiteDatabase.CONFLICT_IGNORE);
        db.close();
        return lecture_id;
    }

    public long updateLecture(Lecture lecture) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID, lecture.getId());
        values.put(LESSON_ID, lecture.getLessonId());
        values.put(NAME, lecture.getName());
        values.put(LECTURE_URL, lecture.getLectureUrl());
        long lecture_id = db.update(TABLE_LECTURES, values, KEY_ID + '=' + lecture.getId(), null);
        db.close();
        return lecture_id;
    }

    public long updateLectureFullSynch(Lecture lecture) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID, lecture.getId());
        values.put(LESSON_ID, lecture.getLessonId());
        values.put(NAME, lecture.getName());
        values.put(LECTURE_URL, lecture.getLectureUrl());
        values.put(IS_LECTURE_LOCAL, lecture.getIsLectureLocal());
        values.put(LECTURE_LOCAL, lecture.getLectureLocal());
        long lecture_id = db.update(TABLE_LECTURES, values, KEY_ID + '=' + lecture.getId(), null);
        db.close();
        return lecture_id;
    }

    public ArrayList<Lecture> selectAllLecturesForLesson(Integer lessonId) {
        ArrayList<Lecture> lectures = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_LECTURES + " WHERE " + LESSON_ID + " = " + lessonId;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            do {
                Lecture lt = new Lecture();
                lt.setId(c.getInt(c.getColumnIndex(KEY_ID)));
                lt.setLessonId(c.getInt(c.getColumnIndex(LESSON_ID)));
                lt.setName(c.getString(c.getColumnIndex(NAME)));
                lt.setLectureUrl(c.getString(c.getColumnIndex(LECTURE_URL)));
                lt.setIsLectureLocal(c.getInt(c.getColumnIndex(IS_LECTURE_LOCAL)));
                lt.setLectureLocal(c.getString(c.getColumnIndex(LECTURE_LOCAL)));
                System.out.println(lt.getName());
                lectures.add(lt);
            } while (c.moveToNext());
        }
        db.close();
        return lectures;
    }

    public Lecture selectSingleLecture(Integer lectureId) {
        Lecture lt = new Lecture();
        String selectQuery = "SELECT * FROM " + TABLE_LECTURES + " WHERE " + KEY_ID + " = " + lectureId;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            do {
                lt.setId(c.getInt(c.getColumnIndex(KEY_ID)));
                lt.setLessonId(c.getInt(c.getColumnIndex(LESSON_ID)));
                lt.setName(c.getString(c.getColumnIndex(NAME)));
                lt.setLectureUrl(c.getString(c.getColumnIndex(LECTURE_URL)));
                lt.setIsLectureLocal(c.getInt(c.getColumnIndex(IS_LECTURE_LOCAL)));
                lt.setLectureLocal(c.getString(c.getColumnIndex(LECTURE_LOCAL)));
                System.out.println(lt.getName());
            } while (c.moveToNext());
        }
        db.close();
        return lt;
    }

    public long insertTestQuestion(TestQuestion testQuestion) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID, testQuestion.getId());
        values.put(ANSWER_ID, testQuestion.getAnswerId());
        values.put(ANSWER_TYPE_ID, testQuestion.getAnswerTypeId());
        values.put(POINTS, testQuestion.getPoints());
        values.put(QUESTION, testQuestion.getQuestion());
        values.put(QUESTION_TYPE_ID, testQuestion.getQuestionTypeId());
        values.put(TEST_ID, testQuestion.getTestId());
        values.put(OTHER_ANSWERONE, testQuestion.getOtherAnswerOneId());
        values.put(OTHER_ANSWERTWO, testQuestion.getOtherAnswerTwoId());
        values.put(OTHER_ANSWERTHREE, testQuestion.getOtherAnswerThreeId());
        long testquestion_id = db.insertWithOnConflict(TABLE_TESTQUESTIONS, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
        return  testquestion_id;
    }

    public ArrayList<TestQuestion> selectAllQuestionsForTest(Integer testId) {
        ArrayList<TestQuestion> testQuestions = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_TESTQUESTIONS + " WHERE " + TEST_ID + " = " + testId;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            do {
                TestQuestion ts = new TestQuestion();
                ts.setId(c.getInt(c.getColumnIndex(KEY_ID)));
                ts.setAnswerId(c.getInt(c.getColumnIndex(ANSWER_ID)));
                ts.setAnswerTypeId(c.getInt(c.getColumnIndex(ANSWER_TYPE_ID)));
                ts.setPoints(c.getInt(c.getColumnIndex(POINTS)));
                ts.setQuestion(c.getString(c.getColumnIndex(QUESTION)));
                ts.setQuestionTypeId(c.getInt(c.getColumnIndex(QUESTION_TYPE_ID)));
                ts.setTestId(c.getInt(c.getColumnIndex(TEST_ID)));
                ts.setOtherAnswerOneId(c.getInt(c.getColumnIndex(OTHER_ANSWERONE)));
                ts.setOtherAnswerTwoId(c.getInt(c.getColumnIndex(OTHER_ANSWERTWO)));
                ts.setOtherAnswerThreeId(c.getInt(c.getColumnIndex(OTHER_ANSWERTHREE)));
                System.out.println(ts.getQuestion());
                testQuestions.add(ts);
            } while (c.moveToNext());
        }
        db.close();
        return testQuestions;
    }

    public long insertExamQuestion(ExamQuestion examQuestion) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID, examQuestion.getId());
        values.put(ANSWER_ID, examQuestion.getAnswerId());
        values.put(ANSWER_TYPE_ID, examQuestion.getAnswerTypeId());
        values.put(POINTS, examQuestion.getPoints());
        values.put(QUESTION, examQuestion.getQuestion());
        values.put(QUESTION_TYPE_ID, examQuestion.getQuestionTypeId());
        values.put(EXAM_ID, examQuestion.getExamId());
        values.put(OTHER_ANSWERONE, examQuestion.getOtherAnswerOneId());
        values.put(OTHER_ANSWERTWO, examQuestion.getOtherAnswerTwoId());
        values.put(OTHER_ANSWERTHREE, examQuestion.getOtherAnswerThreeId());
        long examquestion_id = db.insertWithOnConflict(TABLE_EXAMQUESTIONS, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
        return  examquestion_id;
    }

    public ArrayList<ExamQuestion> selectAllQuestionsForExam(Integer examId) {
        ArrayList<ExamQuestion> examQuestions = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_EXAMQUESTIONS + " WHERE " + EXAM_ID + " = " + examId;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            do {
                ExamQuestion es = new ExamQuestion();
                es.setId(c.getInt(c.getColumnIndex(KEY_ID)));
                es.setAnswerId(c.getInt(c.getColumnIndex(ANSWER_ID)));
                es.setAnswerTypeId(c.getInt(c.getColumnIndex(ANSWER_TYPE_ID)));
                es.setPoints(c.getInt(c.getColumnIndex(POINTS)));
                es.setQuestion(c.getString(c.getColumnIndex(QUESTION)));
                es.setQuestionTypeId(c.getInt(c.getColumnIndex(QUESTION_TYPE_ID)));
                es.setExamId(c.getInt(c.getColumnIndex(EXAM_ID)));
                es.setOtherAnswerOneId(c.getInt(c.getColumnIndex(OTHER_ANSWERONE)));
                es.setOtherAnswerTwoId(c.getInt(c.getColumnIndex(OTHER_ANSWERTWO)));
                es.setOtherAnswerThreeId(c.getInt(c.getColumnIndex(OTHER_ANSWERTHREE)));
                System.out.println(es.getQuestion());
                examQuestions.add(es);
            } while (c.moveToNext());
        }
        db.close();
        return examQuestions;
    }

    public long insertWord(Word word) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID, word.getId());
        values.put(NATIVE_DEFINITION, word.getNativeDefinition());
        values.put(NATIVE_LANGUAGE_ID, word.getNativeLanguageId());
        values.put(NATIVE_SOUND, word.getNativeSound());
        values.put(NATIVE_WORD, word.getNativeWord());
        values.put(PICTURE, word.getPicture());
        values.put(TAGS, word.getTags());
        values.put(TRANSLATED_DEFINITION, word.getTranslatedDefinition());
        values.put(TRANSLATED_LANGUAGE_ID, word.getTranslatedLanguageId());
        values.put(TRANSLATED_SOUND, word.getTranslatedSound());
        values.put(TRANSLATED_WORD, word.getTranslatedWord());
        values.put(CREATOR_ID, word.getCreatorId());
        values.put(IS_NATIVE_SOUND_LOCAL, word.getIsNativeSoundLocal());
        values.put(IS_TRANSLATED_SOUND_LOCAL, word.getIsTranslatedSoundLocal());
        values.put(NATIVE_SOUND_LOCAL, word.getNativeSoundLocal());
        values.put(TRANSLATED_SOUND_LOCAL, word.getTranslatedSoundLocal());
        values.put(IS_PICTURE_LOCAL, word.getIsPictureLocal());
        values.put(PICTURE_LOCAL, word.getPictureLocal());
        long word_id = db.insertWithOnConflict(TABLE_WORDS, null, values, SQLiteDatabase.CONFLICT_IGNORE);
        db.close();
        return word_id;
    }

    public long updateWord(Word word) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID, word.getId());
        values.put(NATIVE_DEFINITION, word.getNativeDefinition());
        values.put(NATIVE_LANGUAGE_ID, word.getNativeLanguageId());
        values.put(NATIVE_SOUND, word.getNativeSound());
        values.put(NATIVE_WORD, word.getNativeWord());
        values.put(PICTURE, word.getPicture());
        values.put(TAGS, word.getTags());
        values.put(TRANSLATED_DEFINITION, word.getTranslatedDefinition());
        values.put(TRANSLATED_LANGUAGE_ID, word.getTranslatedLanguageId());
        values.put(TRANSLATED_SOUND, word.getTranslatedSound());
        values.put(TRANSLATED_WORD, word.getTranslatedWord());
        values.put(CREATOR_ID, word.getCreatorId());
        long word_id = db.update(TABLE_WORDS, values, KEY_ID + '=' + word.getId(), null);
        db.close();
        return word_id;
    }

    public long updateWordFullSynch(Word word) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID, word.getId());
        values.put(NATIVE_DEFINITION, word.getNativeDefinition());
        values.put(NATIVE_LANGUAGE_ID, word.getNativeLanguageId());
        values.put(NATIVE_SOUND, word.getNativeSound());
        values.put(NATIVE_WORD, word.getNativeWord());
        values.put(PICTURE, word.getPicture());
        values.put(TAGS, word.getTags());
        values.put(TRANSLATED_DEFINITION, word.getTranslatedDefinition());
        values.put(TRANSLATED_LANGUAGE_ID, word.getTranslatedLanguageId());
        values.put(TRANSLATED_SOUND, word.getTranslatedSound());
        values.put(TRANSLATED_WORD, word.getTranslatedWord());
        values.put(CREATOR_ID, word.getCreatorId());
        values.put(IS_NATIVE_SOUND_LOCAL, word.getIsNativeSoundLocal());
        values.put(IS_TRANSLATED_SOUND_LOCAL, word.getIsTranslatedSoundLocal());
        values.put(NATIVE_SOUND_LOCAL, word.getNativeSoundLocal());
        values.put(TRANSLATED_SOUND_LOCAL, word.getTranslatedSoundLocal());
        values.put(IS_PICTURE_LOCAL, word.getIsPictureLocal());
        values.put(PICTURE_LOCAL, word.getPictureLocal());
        long word_id = db.update(TABLE_WORDS, values, KEY_ID + '=' + word.getId(), null);
        db.close();
        return word_id;
    }

    public Word getParticularWordData(Integer wordId) {
        Word wd = new Word();
        String selectQuery = "SELECT * FROM " + TABLE_WORDS + " WHERE " + KEY_ID + " = " + wordId;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            do {
                wd.setId(c.getInt(c.getColumnIndex(KEY_ID)));
                wd.setNativeDefinition(c.getString(c.getColumnIndex(NATIVE_DEFINITION)));
                wd.setNativeLanguageId(c.getInt(c.getColumnIndex(NATIVE_LANGUAGE_ID)));
                wd.setNativeSound(c.getString(c.getColumnIndex(NATIVE_SOUND)));
                wd.setNativeWord(c.getString(c.getColumnIndex(NATIVE_WORD)));
                wd.setPicture(c.getString(c.getColumnIndex(PICTURE)));
                wd.setTags(c.getString(c.getColumnIndex(TAGS)));
                wd.setTranslatedDefinition(c.getString(c.getColumnIndex(TRANSLATED_DEFINITION)));
                wd.setTranslatedLanguageId(c.getInt(c.getColumnIndex(TRANSLATED_LANGUAGE_ID)));
                wd.setTranslatedSound(c.getString(c.getColumnIndex(TRANSLATED_SOUND)));
                wd.setTranslatedWord(c.getString(c.getColumnIndex(TRANSLATED_WORD)));
                wd.setCreatorId(c.getInt(c.getColumnIndex(CREATOR_ID)));
                wd.setIsNativeSoundLocal(c.getInt(c.getColumnIndex(IS_NATIVE_SOUND_LOCAL)));
                wd.setNativeSoundLocal(c.getString(c.getColumnIndex(NATIVE_SOUND_LOCAL)));
                wd.setIsTranslatedSoundLocal(c.getInt(c.getColumnIndex(IS_TRANSLATED_SOUND_LOCAL)));
                wd.setTranslatedSoundLocal(c.getString(c.getColumnIndex(TRANSLATED_SOUND_LOCAL)));
                wd.setIsPictureLocal(c.getInt(c.getColumnIndex(IS_PICTURE_LOCAL)));
                wd.setPictureLocal(c.getString(c.getColumnIndex(PICTURE_LOCAL)));
            } while (c.moveToNext());
        }
        db.close();
        return wd;
    }

    public long insertLanguage(Language language) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID, language.getId());
        values.put(LANGUAGE_NAME, language.getLanguageName());
        long language_id = db.insertWithOnConflict(TABLE_LANGUAGES, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
        return language_id;
    }

    public Language getLanguage(Integer languageId) {
        Language lg = new Language();
        String selectQuery = "SELECT * FROM " + TABLE_LANGUAGES + " WHERE " + KEY_ID + " = " + languageId;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            do {
                lg.setId(c.getInt(c.getColumnIndex(KEY_ID)));
                lg.setLanguageName(c.getString(c.getColumnIndex(LANGUAGE_NAME)));
            } while (c.moveToNext());
        }
        db.close();
        return lg;
    }

    public long insertAnswertypes(QuestionAnswerType qaType) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID, qaType.getId());
        values.put(TYPE_NAME, qaType.getTypeName());
        long answertype_id = db.insertWithOnConflict(TABLE_ANSWERTYPES, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
        return answertype_id;
    }

    public QuestionAnswerType getAnswertype(Integer answertypeId) {
        QuestionAnswerType qat = new QuestionAnswerType();
        String selectQuery = "SELECT * FROM " + TABLE_ANSWERTYPES + " WHERE " + KEY_ID + " = " + answertypeId;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            do {
                qat.setId(c.getInt(c.getColumnIndex(KEY_ID)));
                qat.setTypeName(c.getString(c.getColumnIndex(TYPE_NAME)));
            } while (c.moveToNext());
        }
        db.close();
        return qat;
    }

    public String selectTestName(int testId) {
        String testName =  new String();
        String selectQuery = "SELECT " + DESCRIPTION + " FROM " + TABLE_TESTS + " WHERE " + KEY_ID + " = " + testId;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            do {
                testName = c.getString(c.getColumnIndex(DESCRIPTION));
            } while (c.moveToNext());
        }
        db.close();
        return testName;
    }

    public String selectExamName(int examId) {
        String testName =  new String();
        String selectQuery = "SELECT " + DESCRIPTION + " FROM " + TABLE_EXAMS + " WHERE " + KEY_ID + " = " + examId;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            do {
                testName = c.getString(c.getColumnIndex(DESCRIPTION));
            } while (c.moveToNext());
        }
        db.close();
        return testName;
    }

    public String selectQuestionAnswerType(int answerTypeId) {
        String answerTypeName = new String();
        String selectQuery = "SELECT " + TYPE_NAME + " FROM " + TABLE_ANSWERTYPES + " WHERE " + KEY_ID + " = " + answerTypeId;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            do {
                answerTypeName = c.getString(c.getColumnIndex(TYPE_NAME));
            } while (c.moveToNext());
        }
        db.close();
        return answerTypeName;
    }

    public long updateScoreForTest(int testId, int score) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues args = new ContentValues();
        args.put(SCORE, score);
        db.update(TABLE_TESTS, args, KEY_ID + "=" + testId, null);
        return score;
    }

    public long updateScoreForTestLocal(int testId, int score, int isCompletedLocal, String answersConcat) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues args = new ContentValues();
        args.put(SCORE, score);
        args.put(IS_COMPLETED_LOCAL, isCompletedLocal);
        args.put(ANSWER_CONCATENATION, answersConcat);
        db.update(TABLE_TESTS, args, KEY_ID + "=" + testId, null);
        return score;
    }

    public ArrayList<Test> getAllLocallyCompletedTests() {
        ArrayList<Test> tests = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_TESTS + " WHERE " + IS_COMPLETED_LOCAL + " = 1 AND " + ANSWER_CONCATENATION + " IS NOT NULL";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            do {
                Test ts = new Test();
                ts.setId(c.getInt(c.getColumnIndex(KEY_ID)));
                ts.setLessonId(c.getInt(c.getColumnIndex(LESSON_ID)));
                ts.setDescription(c.getString(c.getColumnIndex(DESCRIPTION)));
                ts.setIsNew(c.getInt(c.getColumnIndex(IS_NEW)));
                ts.setIsCompleted(c.getInt(c.getColumnIndex(IS_COMPLETED)));
                ts.setScore(c.getInt(c.getColumnIndex(SCORE)));
                ts.setIsLocal(c.getInt(c.getColumnIndex(IS_LOCAL)));
                ts.setIsCompletedLocal(c.getInt(c.getColumnIndex(IS_COMPLETED_LOCAL)));
                ts.setAnswersConcatenation(c.getString(c.getColumnIndex(ANSWER_CONCATENATION)));
                System.out.println(ts.getDescription());
                tests.add(ts);
            } while (c.moveToNext());
        }
        db.close();
        return tests;
    }

    public long updateScoreForExam(int examId, int score, int grade) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues args = new ContentValues();
        args.put(SCORE, score);
        args.put(GRADE, grade);
        db.update(TABLE_EXAMS, args, KEY_ID + "=" + examId, null);
        return score;
    }

    public long updateScoreForExamLocal(int examId, int score, int grade, int isCompletedLocal, String answersConcat) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues args = new ContentValues();
        args.put(SCORE, score);
        args.put(GRADE, grade);
        args.put(IS_COMPLETED_LOCAL, isCompletedLocal);
        args.put(ANSWER_CONCATENATION, answersConcat);
        db.update(TABLE_EXAMS, args, KEY_ID + "=" + examId, null);
        return score;
    }

    public ArrayList<Exam> getAllLocallyCompletedExams() {
        ArrayList<Exam> exams = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_EXAMS + " WHERE " + IS_COMPLETED_LOCAL + " = 1 AND " + ANSWER_CONCATENATION + " IS NOT NULL";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            do {
                Exam ex = new Exam();
                ex.setId(c.getInt(c.getColumnIndex(KEY_ID)));
                ex.setLessonId(c.getInt(c.getColumnIndex(LESSON_ID)));
                ex.setDescription(c.getString(c.getColumnIndex(DESCRIPTION)));
                ex.setIsNew(c.getInt(c.getColumnIndex(IS_NEW)));
                ex.setIsPassed(c.getInt(c.getColumnIndex(IS_PASSED)));
                ex.setScore(c.getInt(c.getColumnIndex(SCORE)));
                ex.setIsLocal(c.getInt(c.getColumnIndex(IS_LOCAL)));
                ex.setIsCompletedLocal(c.getInt(c.getColumnIndex(IS_COMPLETED_LOCAL)));
                ex.setAnswersConcatenation(c.getString(c.getColumnIndex(ANSWER_CONCATENATION)));
                System.out.println(ex.getDescription());
                exams.add(ex);
            } while (c.moveToNext());
        }
        db.close();
        return exams;
    }
}
