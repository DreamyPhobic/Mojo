package com.mojostudios.mojopay;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Harsh Gupta on 19-Mar-18.
 */

public class PersonsListDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "personslist.db";
    private static final int DATABASE_VERSION = 1 ;
    private static final String TABLE_NAME = "Persons";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_Person_NAME = "name";
    private static final String COLUMN_Person_credit = "credit";
    private static final String COLUMN_Person_debit = "debit";
    private static final String COLUMN_Person_Total = "total";
    private static final String COLUMN_CREDIT_DESCRIPTION="c_des";
    private static final String COLUMN_DEBIT_DESCRIPTION="d_des";
    private static final String COLUMN_Person_IMAGE = "image";
    private Toast m_currentToast;



    PersonsListDBHelper(Context context) {super(context, DATABASE_NAME ,null, DATABASE_VERSION);}

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(" CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_Person_NAME + " TEXT NOT NULL, " +
                COLUMN_Person_credit + " INTEGER NOT NULL, " +
                COLUMN_Person_debit + " INTEGER NOT NULL, " +
                COLUMN_Person_Total + " INTEGER NOT NULL, "+
                COLUMN_Person_IMAGE + " BLOB, "+
                COLUMN_CREDIT_DESCRIPTION + "TEXT, "+
                COLUMN_DEBIT_DESCRIPTION + "TEXT);"
        );

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // you can implement here migration process
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        this.onCreate(db);
    }
    /**create record**/
    void saveNewPerson(Person Person) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_Person_NAME, Person.getName());
        values.put(COLUMN_Person_IMAGE, Person.getImage());
        values.put(COLUMN_Person_credit,Person.getCredit());
        values.put(COLUMN_Person_debit, Person.getDebit());
        values.put(COLUMN_Person_Total,Person.getTotal());
        // insert
        Log.d("saveNewPerson","initial true");
        db.insert(TABLE_NAME,null, values);
        Log.d("saveNewPerson","final true");
        db.close();
    }

    /**Query records, give options to filter results**/
    List<Person> personList(String filter) {
        String query;
        if(filter.equals("")){
            //regular query
            query = "SELECT  * FROM " + TABLE_NAME;
        }else{
            //filter results by filter option provided
            query = "SELECT  * FROM " + TABLE_NAME + " ORDER BY "+ filter;
        }

        List<Person> PersonLinkedList = new LinkedList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Person Person;

        if (cursor.moveToFirst()) {
            do {
                Person = new Person();

                Person.setID(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
                Person.setName(cursor.getString(cursor.getColumnIndex(COLUMN_Person_NAME)));
                Person.setCredit(cursor.getInt(cursor.getColumnIndex(COLUMN_Person_credit)));
                Person.setDebit(cursor.getInt(cursor.getColumnIndex(COLUMN_Person_debit)));
                Person.setTotal(cursor.getInt(cursor.getColumnIndex(COLUMN_Person_Total)));
                Person.setImage(cursor.getString(cursor.getColumnIndex(COLUMN_Person_IMAGE)));
                PersonLinkedList.add(Person);
            } while (cursor.moveToNext());
        }


        return PersonLinkedList;
    }

    /**Query only 1 record**/
    Person getPerson(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT  * FROM " + TABLE_NAME + " WHERE _id="+ id;
        Cursor cursor = db.rawQuery(query, null);

        Person receivedPerson = new Person();
        if(cursor.getCount() > 0) {
            cursor.moveToFirst();
            receivedPerson.setID(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
            receivedPerson.setName(cursor.getString(cursor.getColumnIndex(COLUMN_Person_NAME)));
            receivedPerson.setCredit(cursor.getInt(cursor.getColumnIndex(COLUMN_Person_credit)));
            receivedPerson.setDebit(cursor.getInt(cursor.getColumnIndex(COLUMN_Person_debit)));
            receivedPerson.setTotal(cursor.getInt(cursor.getColumnIndex(COLUMN_Person_Total)));
           // receivedPerson.setImage(cursor.getString(cursor.getColumnIndex(COLUMN_Person_IMAGE)));
        }
        return receivedPerson;
    }


    /**delete record**/
    public void deletePersonRecord(int id, Context context) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME,COLUMN_ID+"=?",new String[] { String.valueOf(id)});
        // db.execSQL("DELETE FROM "+TABLE_NAME+" WHERE _id='"+id+"'");
        db.close();
        //Toast.makeText(context, "Deleted successfully.", Toast.LENGTH_SHORT).show();
        showToast("Deleted successfully.",context);
    }

    /**update record**/
    void updatePersonRecord(int PersonId, Context context, Person updatedPerson) {
        SQLiteDatabase db = this.getWritableDatabase();
        //you can use the constants above instead of typing the column names
        db.execSQL("UPDATE  "+TABLE_NAME+" SET name ='"+ updatedPerson.getName() + "', credit ='" + updatedPerson.getCredit()+ "', debit ='"+ updatedPerson.getDebit() + "', Total ='"+ updatedPerson.getTotal() + "', image ='"+ updatedPerson.getImage() + "'  WHERE _id='" + PersonId + "'");
        // Toast.makeText(context, "Updated successfully.", Toast.LENGTH_SHORT).show();
        showToast("Updated successfully.",context);
    }

    int getallCount(){
        return personList("").size();
    }

    void update(){
        SQLiteDatabase db=this.getWritableDatabase();
        String query="UPDATE SQLITE_SEQUENCE SET SEQ=0 WHERE NAME="+"'"+TABLE_NAME+"'";
        db.execSQL(query);
        db.close();
    }

    private void showToast(String text, Context context)
    {
        if(m_currentToast == null)
        {
            m_currentToast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
            m_currentToast.show();
        }

        else{ m_currentToast.setText(text);
            m_currentToast.setDuration(Toast.LENGTH_SHORT);
            m_currentToast.show();}
    }
}

