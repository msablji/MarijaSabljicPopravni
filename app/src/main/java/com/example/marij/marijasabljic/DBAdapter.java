package com.example.marij.marijasabljic;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by marij on 23.2.2018..
 */

public class DBAdapter {
    static final String KEY_ID = "_id";
    static final String KEY_IDSPORTASA = "_idsportasa";
    static final String KEY_NAZIV = "naziv";
    static final String KEY_VRSTA = "vrsta";
    static final String KEY_OLIMPIJSKI = "olimpijski";
    static final String KEY_IME = "ime";
    static final String KEY_PREZIME = "prezime";
    static final String TAG = "DBAdapter";

    static final String DATABASE_NAME = "MyDB";
    static final String DATABASE_TABLESPORT = "sport";
    static final String DATABASE_TABLESPORTAS = "sportas";
    static final int DATABASE_VERSION = 2;

    static final String DATABASE_CREATE_TABLESPORT=
            "create table sport (_id integer not null, "
                    + "naziv text not null, vrsta text not null, olimpijski integer not null);";

    static final String DATABASE_CREATE_TABLESPORTAS=
            "create table sportas (id_sportasa integer not null, "
                    + "ime text not null, prezime text not null);";


    final Context context;

    DatabaseHelper DBHelper;
    SQLiteDatabase db;

    public DBAdapter(Context ctx)
    {
        this.context = ctx;
        DBHelper = new DatabaseHelper(context);
    }

    private static class DatabaseHelper extends SQLiteOpenHelper
    {
        DatabaseHelper(Context context)
        {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db)
        {
            try {
                db.execSQL(DATABASE_CREATE_TABLESPORT);
                db.execSQL(DATABASE_CREATE_TABLESPORTAS);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
        {
            Log.w(TAG, "Upgrading db from" + oldVersion + "to"
                    + newVersion );
            db.execSQL("DROP TABLE IF EXISTS sport");
            db.execSQL("DROP TABLE IF EXISTS sportas");
            onCreate(db);
        }
    }

    //---opens the database---
    public DBAdapter open() throws SQLException
    {
        db = DBHelper.getWritableDatabase();
        return this;
    }

    //---closes the database---
    public void close()
    {
        DBHelper.close();
    }

    //---insert
    public long insertSport(long id, String naziv, String vrsta, long olimpijski)
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_ID, id);
        initialValues.put(KEY_NAZIV, naziv);
        initialValues.put(KEY_VRSTA, vrsta);
        initialValues.put(KEY_OLIMPIJSKI, olimpijski);
        return db.insert(DATABASE_TABLESPORT, null, initialValues);
    }

    //--insert
    public long insertSportas(long id_sportasa, String ime, String prezime)
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_IDSPORTASA, id_sportasa);
        initialValues.put(KEY_IME, ime);
        initialValues.put(KEY_PREZIME, prezime);
        return db.insert(DATABASE_TABLESPORTAS, null, initialValues);
    }

    //---deletes a particular sport---
    public boolean deleteSport(long id)
    {
        return db.delete(DATABASE_TABLESPORT, KEY_ID + "=" + id, null) > 0;
    }

    //--deletes film for sportas--
    public boolean deleteSportas(long filmId)
    {
        return db.delete(DATABASE_TABLESPORTAS, KEY_IDSPORTASA + "=" + filmId, null) > 0;
    }

    //---retrieves all sports---
    public Cursor getAllSports()
    {
        return db.query(DATABASE_TABLESPORT, new String[] {KEY_ID, KEY_NAZIV,
                KEY_VRSTA, KEY_OLIMPIJSKI}, null, null, null, null, null);
    }

    //---retrieves all sportas---
    public Cursor getAllSportas()
    {
        return db.query(DATABASE_TABLESPORTAS, new String[] {KEY_IDSPORTASA, KEY_IME, KEY_PREZIME
        }, null, null, null, null,null);
    }

    //---retrieves a particular sport---
    public Cursor getSport(String ime) throws SQLException
    {
        Cursor mCursor =
                db.query(true, DATABASE_TABLESPORT, new String[] {KEY_ID, KEY_NAZIV,
                                KEY_VRSTA, KEY_OLIMPIJSKI}, KEY_NAZIV + "=" + ime, null,
                        null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }

        return mCursor;
    }

    //--retrieves a particular sportas--
    public Cursor getSportas(long id) throws SQLException
    {
        Cursor mCursor =
                db.query(DATABASE_TABLESPORTAS, new String[] {KEY_IDSPORTASA, KEY_IME, KEY_PREZIME}, KEY_IDSPORTASA + "=" +id, null,
                        null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }



}