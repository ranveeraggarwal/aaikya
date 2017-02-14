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

import static com.ranveeraggarwal.letrack.utilities.RepetitiveUI.shortToastMaker;
import static com.ranveeraggarwal.letrack.utilities.Utilities.addDays;
import static com.ranveeraggarwal.letrack.utilities.Utilities.getCurrentDate;

public class DatabaseAdapter {

    private DatabaseHelper helper;
    private Context context;

    public DatabaseAdapter(Context context) {
        this.context = context;
        helper = new DatabaseHelper(context);
    }

    private ContentValues getPersonContentValues(String name, String description, int frequency, int startDate) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.P_NAME, name);
        contentValues.put(DatabaseHelper.P_DESCRIPTION, description);
        contentValues.put(DatabaseHelper.P_FREQUENCY, frequency);
        contentValues.put(DatabaseHelper.P_STARTDATE, startDate);

        return contentValues;
    }

    private ContentValues getLeavesContentValues(long pid, long date, int fno) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.L_PID, pid);
        contentValues.put(DatabaseHelper.L_DATE, date);
        contentValues.put(DatabaseHelper.L_FNO, fno);

        return contentValues;
    }

    public long insertPerson(String name, String description, int frequency, int startDate) {
        try {
            SQLiteDatabase db = helper.getWritableDatabase();
            ContentValues contentValues = getPersonContentValues(name, description, frequency, startDate);
            long id = db.insert(DatabaseHelper.PERSON_TABLE, null, contentValues);
            db.close();
            return id;
        } catch (Exception e) {
            shortToastMaker(context, "Person Insert Failed");
            Log.e("DatabaseError", "Person Insert Failed");
        }
        return -1;
    }

    public List<Person> getPersonList() {
        SQLiteDatabase db = helper.getWritableDatabase();
        String[] columns = {DatabaseHelper.P_ID, DatabaseHelper.P_NAME, DatabaseHelper.P_DESCRIPTION, DatabaseHelper.P_FREQUENCY, DatabaseHelper.P_STARTDATE};
        List<Person> allPeople = new ArrayList<>();
        try {
            Cursor cursor = db.query(DatabaseHelper.PERSON_TABLE, columns, null, null, null, null, null);
            allPeople = new ArrayList<>();
            while (cursor.moveToNext()) {
                Person currentPerson = new Person(
                        cursor.getLong(cursor.getColumnIndex(DatabaseHelper.P_ID)),
                        cursor.getString(cursor.getColumnIndex(DatabaseHelper.P_NAME)),
                        Integer.valueOf(cursor.getString(cursor.getColumnIndex(DatabaseHelper.P_FREQUENCY))),
                        cursor.getString(cursor.getColumnIndex(DatabaseHelper.P_DESCRIPTION)),
                        0,
                        Integer.valueOf(cursor.getString(cursor.getColumnIndex(DatabaseHelper.P_STARTDATE)))
                );
                currentPerson.setLeaves(getLeavesForDate(getCurrentDate(), currentPerson.getId()).size());
                allPeople.add(currentPerson);
            }
            cursor.close();
            db.close();
        } catch (Exception e) {
            shortToastMaker(context, "Person List Fetch Failed");
            Log.e("DatabaseError", "Person List Fetch Failed");
        }
        return allPeople;
    }

    public int updatePerson(Person updatedPerson) {
        try {
            SQLiteDatabase db = helper.getWritableDatabase();
            ContentValues contentValues = getPersonContentValues(updatedPerson.getName(), updatedPerson.getDescription(),
                    updatedPerson.getFrequency(), updatedPerson.getStartDate());
            String[] whereArgs = {Long.toString(updatedPerson.getId())};
            int count = db.update(DatabaseHelper.PERSON_TABLE, contentValues, DatabaseHelper.P_ID + "=? ", whereArgs);
            db.close();
            return count;
        } catch (Exception e) {
            shortToastMaker(context, "Couldn't Update Person");
            Log.e("DatabaseError", "Couldn't Update Person");
        }
        return 0;
    }

    public List<Leave> getLeavesForPerson(long pid) {
        SQLiteDatabase db = helper.getWritableDatabase();
        List<Leave> allDates = new ArrayList<>();
        try {
            String selectQuery = "SELECT * FROM " + DatabaseHelper.LEAVES_TABLE + " WHERE "
                    + DatabaseHelper.L_PID + "=" + pid + ";";
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
            shortToastMaker(context, "Person Leaves Get Failed");
            Log.e("DatabaseError", "Person Leaves Get Failed");
        }
        return allDates;
    }

    public List<Leave> getLeavesInRange(long startDate, long endDate, long pid) {
        startDate = addDays(startDate, -1);
        endDate = addDays(endDate, -2);
        SQLiteDatabase db = helper.getWritableDatabase();
        List<Leave> allDates = new ArrayList<>();
        try {
            String selectQuery = "SELECT * FROM " + DatabaseHelper.LEAVES_TABLE + " WHERE "
                    + DatabaseHelper.L_PID + "=" + pid + " AND " + DatabaseHelper.L_DATE
                    + " BETWEEN " + startDate + " AND " + endDate + ";";
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
            shortToastMaker(context, "Person Leaves Range Failed");
            Log.e("DatabaseError", "Person Leaves Range Failed");
        }
        return allDates;
    }

    public List<Leave> getLeavesForDate(long date, long pid) {
        SQLiteDatabase db = helper.getWritableDatabase();
        List<Leave> allDates = new ArrayList<>();
        try {
            String selectQuery = "SELECT * FROM " + DatabaseHelper.LEAVES_TABLE + " WHERE "
                    + DatabaseHelper.L_PID + "=" + pid + " AND " + DatabaseHelper.L_DATE
                    + "=" + date + ";";
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
            shortToastMaker(context, "Person Leaves Date Failed");
            Log.e("DatabaseError", "Person Leaves Date Failed");
        }
        return allDates;
    }

    public long insertLeave(long pid, long date, int fno) {
        try {
            SQLiteDatabase db = helper.getWritableDatabase();
            ContentValues contentValues = getLeavesContentValues(pid, date, fno);
            long id = db.insert(DatabaseHelper.LEAVES_TABLE, null, contentValues);
            db.close();
            return id;
        } catch (Exception e) {
            shortToastMaker(context, "Leave Insert Failed");
            Log.e("DatabaseError", "Leave Insert Failed");
        }
        return -1;
    }

    public int deleteLeave(long pid, long date, int fno) {
        try {
            SQLiteDatabase db = helper.getWritableDatabase();
            String[] whereArgs = {pid + "", date + "", fno + ""};
            int count = db.delete(DatabaseHelper.LEAVES_TABLE, DatabaseHelper.L_PID + "=? AND " + DatabaseHelper.L_DATE + "=? AND " + DatabaseHelper.L_FNO + "=?", whereArgs);
            db.close();
            return count;
        } catch (Exception e) {
            shortToastMaker(context, "Leave Delete Failed");
            Log.e("DatabaseError", "Leave Delete Failed");
        }
        return 0;
    }

    public void refreshDatabase() {
        try {
            SQLiteDatabase db = helper.getWritableDatabase();
            db.execSQL("DROP TABLE IF EXISTS " + DatabaseHelper.LEAVES_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + DatabaseHelper.PERSON_TABLE);
            helper.onCreate(db);
        } catch (Exception e) {
            shortToastMaker(context, "Database Refresh Failed");
            Log.e("DatabaseError", "Database Refresh Failed");
        }
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {

        private static final int DATABASE_VERSION = 3;

        private static final String DATABASE = "personDatabase";

        private static final String PERSON_TABLE = "person";
        private static final String LEAVES_TABLE = "leaves";

        private static final String P_ID = "_id";
        private static final String P_NAME = "name";
        private static final String P_DESCRIPTION = "description";
        private static final String P_FREQUENCY = "frequency";
        private static final String P_STARTDATE = "start_date";

        private static final String L_ID = "_id";
        private static final String L_PID = "person_id";
        private static final String L_DATE = "date";
        private static final String L_FNO = "f_no";

        private DatabaseHelper(Context context) {
            super(context, DATABASE, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            String CREATE_PERSON_TABLE =
                    "CREATE TABLE " + PERSON_TABLE + "("
                            + P_ID + " INTEGER PRIMARY KEY,"
                            + P_NAME + " TEXT,"
                            + P_DESCRIPTION + " TEXT,"
                            + P_FREQUENCY + " INTEGER,"
                            + P_STARTDATE + " INTEGER"
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
