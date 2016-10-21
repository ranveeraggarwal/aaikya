package com.ranveeraggarwal.letrack.storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.ranveeraggarwal.letrack.models.Leave;
import com.ranveeraggarwal.letrack.models.Person;

import java.util.ArrayList;
import java.util.List;

import static com.ranveeraggarwal.letrack.utilities.Utilities.getCurrentDate;

/**
 * Created by raagga on 18-10-2016.
 */

public class DatabaseAdapter {

    private DatabaseHelper helper;

    public DatabaseAdapter(Context context) {
        helper = new DatabaseHelper(context);
    }

    private ContentValues getPersonContentValues(String name, String occupation, int frequency, int startDate, int salary) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.P_NAME, name);
        contentValues.put(DatabaseHelper.P_OCCUPATION, occupation);
        contentValues.put(DatabaseHelper.P_FREQUENCY, frequency);
        contentValues.put(DatabaseHelper.P_STARTDATE, startDate);
        contentValues.put(DatabaseHelper.P_SALARY, salary);

        return contentValues;
    }

    private ContentValues getLeavesContentValues(long pid, long date, int fno) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.L_PID, pid);
        contentValues.put(DatabaseHelper.L_DATE, date);
        contentValues.put(DatabaseHelper.L_FNO, fno);

        return contentValues;
    }

    public long insertPerson(String name, String occupation, int frequency, int startDate, int salary) {
        try {
            SQLiteDatabase db = helper.getWritableDatabase();
            ContentValues contentValues = getPersonContentValues(name, occupation, frequency, startDate, salary);
            long id = db.insert(DatabaseHelper.PERSON_TABLE, null, contentValues);
            db.close();
            return id;
        } catch (Exception e) {

        }
        return -1;
    }

    public List<Person> getPersonList() {
        SQLiteDatabase db = helper.getWritableDatabase();
        String[] columns = {DatabaseHelper.P_ID, DatabaseHelper.P_NAME, DatabaseHelper.P_OCCUPATION, DatabaseHelper.P_FREQUENCY, DatabaseHelper.P_STARTDATE, DatabaseHelper.P_SALARY};
        List<Person> allPeople = new ArrayList<>();
        try  {
            Cursor cursor = db.query(DatabaseHelper.PERSON_TABLE, columns, null, null, null, null, null);
            allPeople = new ArrayList<>();
            while (cursor.moveToNext()) {
                Person currentPerson = new Person(
                        cursor.getLong(cursor.getColumnIndex(DatabaseHelper.P_ID)),
                        cursor.getString(cursor.getColumnIndex(DatabaseHelper.P_NAME)),
                        Integer.valueOf(cursor.getString(cursor.getColumnIndex(DatabaseHelper.P_FREQUENCY))),
                        cursor.getString(cursor.getColumnIndex(DatabaseHelper.P_OCCUPATION)),
                        0,
                        Integer.valueOf(cursor.getString(cursor.getColumnIndex(DatabaseHelper.P_STARTDATE))),
                        Integer.valueOf(cursor.getString(cursor.getColumnIndex(DatabaseHelper.P_SALARY)))
                );
                currentPerson.setLeaves(getLeavesForDate(getCurrentDate(), currentPerson.getId()).size());
                allPeople.add(currentPerson);
            }
            cursor.close();
            db.close();
        } catch (Exception e) {

        }
        return allPeople;
    }

    public List<Leave> getLeavesInRange(long startDate, long endDate, long pid) {

        SQLiteDatabase db = helper.getWritableDatabase();
        List<Leave> allDates = new ArrayList<>();
        try {
            String selectQuery = "SELECT * FROM " + DatabaseHelper.LEAVES_TABLE + " WHERE "
                    + DatabaseHelper.L_PID + "=" + pid + " AND " + DatabaseHelper.L_DATE
                    + " BETWEEN "+ startDate +" AND " + endDate + ";";
            Cursor cursor = db.rawQuery(selectQuery, null);
            while (cursor.moveToNext()) {
                Leave leave = new Leave(
                        pid,
                        cursor.getLong(cursor.getColumnIndex(DatabaseHelper.L_DATE)),
                        cursor.getInt(cursor.getColumnIndex(DatabaseHelper.L_FNO))
                        );
                allDates.add(leave);
            }
            cursor.close();
            db.close();
        } catch (Exception e) {

        }
        return allDates;
    }

    public List<Leave> getLeavesForDate(long date, long pid) {
        SQLiteDatabase db = helper.getWritableDatabase();
        List<Leave> allDates = new ArrayList<>();
        try {
            String selectQuery = "SELECT * FROM " + DatabaseHelper.LEAVES_TABLE + " WHERE "
                    + DatabaseHelper.L_PID + "=" + pid + " AND " + DatabaseHelper.L_DATE
                    + "="+ date+ ";";
            Cursor cursor = db.rawQuery(selectQuery, null);
            while (cursor.moveToNext()) {
                Leave leave = new Leave(
                        pid,
                        cursor.getLong(cursor.getColumnIndex(DatabaseHelper.L_DATE)),
                        cursor.getInt(cursor.getColumnIndex(DatabaseHelper.L_FNO))
                );
                allDates.add(leave);
            }
            cursor.close();
            db.close();
        } catch (Exception e) {

        }
        return allDates;
    }

    public boolean checkLeave(long date, int fno, long pid) {
        SQLiteDatabase db = helper.getWritableDatabase();
        String selectString = "SELECT * FROM " + DatabaseHelper.LEAVES_TABLE + " WHERE " + DatabaseHelper.L_DATE + " = " + date
                + " AND " + DatabaseHelper.L_FNO + " = " + fno
                + " AND " + DatabaseHelper.L_PID + " = " + pid + ";";
        Cursor cursor = db.rawQuery(selectString, null);
        if (cursor.getCount() <= 0) {
            cursor.close();
            db.close();
            return false;
        }
        cursor.close();
        db.close();
        return true;
    }

    public long insertLeave(long pid, long date, int fno) {
        try {
            SQLiteDatabase db = helper.getWritableDatabase();
            ContentValues contentValues = getLeavesContentValues(pid, date, fno);
            long id = db.insert(DatabaseHelper.LEAVES_TABLE, null, contentValues);
            db.close();
            return id;
        } catch (Exception e) {

        }
        return -1;
    }

    public int deleteLeave(long pid, long date, int fno) {
        try {
            SQLiteDatabase db = helper.getWritableDatabase();
            String[] whereArgs = {pid+"", date+"", fno+""};
            int count = db.delete(helper.LEAVES_TABLE, helper.L_PID+"=? AND "+helper.L_DATE + "=? AND " + helper.L_FNO + "=?", whereArgs);
            db.close();
            return count;
        } catch (Exception e) {

        }
        return 0;
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {

        private static final int DATABASE_VERSION = 1;

        private static final String DATABASE = "personDatabase";

        private static final String PERSON_TABLE = "person";
        private static final String LEAVES_TABLE = "leaves";

        private static final String P_ID = "_id";
        private static final String P_NAME = "name";
        private static final String P_OCCUPATION = "occupation";
        private static final String P_FREQUENCY = "frequency";
        private static final String P_STARTDATE = "start_date";
        private static final String P_SALARY = "salary";

        private static final String L_ID = "_id";
        private static final String L_PID = "person_id";
        private static final String L_DATE = "date";
        private static final String L_FNO = "f_no";

        private Context context;

        private DatabaseHelper(Context context) {
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
    }
}
