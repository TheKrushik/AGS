package info.krushik.android.ags.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import info.krushik.android.ags.adapters.LoginDataBaseAdapter;

public class DataBaseHelper extends SQLiteOpenHelper {

    public DataBaseHelper(Context context) {
        super(context, "AgsDB.db", null, 1);
    }

    // Called when no database exists in disk and the helper class needs
    // to create a new one.
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + LoginDataBaseAdapter.TABLE_NAME + " ("
                + LoginDataBaseAdapter.COLUMN_ID + " integer primary key autoincrement,"
                + LoginDataBaseAdapter.COLUMN_USERNAME + " text,"
                + LoginDataBaseAdapter.COLUMN_PASSWORD + " text);");

//        db.execSQL("CREATE TABLE " + Student.TABLE_NAME + " ("
//                + Student.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
//                + Student.COLUMN_FIRST_NAME + " TEXT NOT NULL,"
//                + Student.COLUMN_LAST_NAME + " TEXT NOT NULL,"
//                + Student.COLUMN_AGE + " INTEGER NOT NULL);");

    }

    // Called when there is a database version mismatch meaning that the version
    // of the database on disk needs to be upgraded to the current version.
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Log the version upgrade.
        Log.w("TaskDBAdapter", "Upgrading from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");

        // Upgrade the existing database to conform to the new version. Multiple
        // previous versions can be handled by comparing _oldVersion and _newVersion
        // values.
        // The simplest case is to drop the old table and create a new one.
        db.execSQL("DROP TABLE IF EXISTS " + "TEMPLATE");
        // Create a new one.
        onCreate(db);
    }

}
