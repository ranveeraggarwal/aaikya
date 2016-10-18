package com.ranveeraggarwal.letrack.storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ranveeraggarwal.letrack.models.Person;

/**
 * Created by raagga on 18-10-2016.
 */

public class PersonDBHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE = "personDatabase";

    private static final String PERSON_TABLE = "person";
    private static final String LEAVES_TABLE = "leaves";

    private static final String P_ID = "id";
    private static final String P_NAME = "name";
    private static final String P_OCCUPATION = "occupation";
    private static final String P_FREQUENCY = "frequency";
    private static final String P_STARTDATE = "start_date";
    private static final String P_SALARY = "salary";

    private static final String L_ID = "id";
    private static final String L_PID = "person_id";
    private static final String L_DATE = "date";
    private static final String L_FNO = "f_no";

    private Context context;

    public PersonDBHandler(Context context) {
        super(context, DATABASE, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_PERSON_TABLE =
                "CREATE TABLE " + PERSON_TABLE + "("
                        + P_ID + " INTEGER PRIMARY KEY,"
                        + P_NAME + " TEXT,"
                        + P_OCCUPATION + " TEXT,"
                        + P_FREQUENCY + " INTEGER,"
                        + P_STARTDATE + " INTEGER,"
                        + P_SALARY + " INTEGER"
                        + ");";
        db.execSQL(CREATE_PERSON_TABLE);

        String CREATE_LEAVES_TABLE =
                "CREATE TABLE " + LEAVES_TABLE + "("
                        + L_ID + " INTEGER PRIMARY KEY,"
                        + L_PID + " INTEGER,"
                        + L_DATE + " INTEGER,"
                        + L_FNO + " INTEGER"
                        + ");";
        db.execSQL(CREATE_LEAVES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + LEAVES_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + PERSON_TABLE);
        onCreate(db);
    }

    public ContentValues getPersonContentValues(String name, String occupation, int frequency, int startDate, int salary) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(P_NAME, name);
        contentValues.put(P_OCCUPATION, occupation);
        contentValues.put(P_FREQUENCY, frequency);
        contentValues.put(P_STARTDATE, startDate);
        contentValues.put(P_SALARY, salary);

        return contentValues;
    }

    public ContentValues getLeavesContentValues(int pid, long date, int fno) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(L_PID, pid);
        contentValues.put(L_DATE, date);
        contentValues.put(L_FNO, fno);

        return contentValues;
    }

    public Person insertPerson(String name, String occupation, int frequency, int startDate, int salary) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = getPersonContentValues(name, occupation, frequency, startDate, salary);
            db.insert(PERSON_TABLE, null, contentValues);
            db.close();
            return new Person(name, frequency, occupation, startDate);
        } catch (Exception e) {

        }
        return null;
    }
}
