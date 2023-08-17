package com.mahyco.exportdbtocsv;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

public class ExportDbToCsv {

    private Context context;
    private SQLiteOpenHelper dbHelper;
    private String dbName;
    private String tableName;

    public void exportWholeDb(Context context, SQLiteOpenHelper dbHelper, String dbName) {
        this.context = context;
        this.dbHelper = dbHelper;
        this.dbName = dbName;
        new ExportWholeAsyncTask().execute();
    }

    public void exportParticularTable(Context context, SQLiteOpenHelper dbHelper, String dbName,
                                      String tableName) {
        this.context = context;
        this.dbHelper = dbHelper;
        this.dbName = dbName;
        this.tableName = tableName;
        new ExportTableAsyncTask().execute();
    }

    private class ExportTableAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected final Void doInBackground(Void... voids) {
            exportTable();
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            Toast.makeText(context, "Files created successfully", Toast.LENGTH_SHORT).show();
            super.onPostExecute(unused);
        }
    }

    private class ExportWholeAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected final Void doInBackground(Void... voids) {
            exportWholeDB();
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            Toast.makeText(context, "Files created successfully", Toast.LENGTH_SHORT).show();
            super.onPostExecute(unused);
        }
    }

    @SuppressLint("Range")
    private void exportWholeDB() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        File exportDir = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);

        Cursor c = db.rawQuery(
                "SELECT name FROM sqlite_master WHERE type='table' AND name != 'android_metadata' AND name != 'sqlite_sequence'", null);
        ArrayList<String> result = new ArrayList<String>();

        if (c.moveToFirst()) {
            while (!c.isAfterLast()) {
                result.add(c.getString(c.getColumnIndex("name")));
                c.moveToNext();
            }
        }

        if (!exportDir.exists()) {
            exportDir.mkdirs();
        }

        try {
            for (int i = 0; i < result.size(); i++) {
                File file = new File(exportDir, dbName + result.get(i) + ".csv");
                file.createNewFile();
                ExportCSVWriter csvWrite = new ExportCSVWriter(new FileWriter(file));
                Cursor curCSV = db.rawQuery("SELECT  * FROM " + result.get(i), null);
                csvWrite.writeNext(curCSV.getColumnNames());
                int columnCount = curCSV.getColumnCount();
                while (curCSV.moveToNext()) {
                    String[] arrStr = new String[columnCount];
                    for (int j = 0; j < columnCount; j++) {
                        arrStr[j] = curCSV.getString(j);
                    }
                    csvWrite.writeNext(arrStr);
                }
                csvWrite.close();
                curCSV.close();
            }
        } catch (Exception sqlEx) {
            Log.e("temporary", "sqlEx exception " + sqlEx.getMessage(), sqlEx);
        }
    }

    @SuppressLint("Range")
    private void exportTable() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        File exportDir = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
        if (!exportDir.exists()) {
            exportDir.mkdirs();
        }
        try {
            File file = new File(exportDir, dbName + tableName + ".csv");
            file.createNewFile();
            ExportCSVWriter csvWrite = new ExportCSVWriter(new FileWriter(file));
            Cursor curCSV = db.rawQuery("SELECT  * FROM " + tableName, null);
            csvWrite.writeNext(curCSV.getColumnNames());
            int columnCount = curCSV.getColumnCount();
            while (curCSV.moveToNext()) {
                String[] arrStr = new String[columnCount];
                for (int j = 0; j < columnCount; j++) {
                    arrStr[j] = curCSV.getString(j);
                }
                csvWrite.writeNext(arrStr);
            }
            csvWrite.close();
            curCSV.close();
        } catch (Exception sqlEx) {
            Log.e("temporary", "sqlEx exception " + sqlEx.getMessage(), sqlEx);
        }
    }
}