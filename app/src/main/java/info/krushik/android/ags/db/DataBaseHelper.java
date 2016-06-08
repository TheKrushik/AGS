package info.krushik.android.ags.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import info.krushik.android.ags.adapters.LoginDataBaseAdapter;
import info.krushik.android.ags.objects.Client;

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

        db.execSQL("CREATE TABLE " + Client.TABLE_NAME + " ("
                + Client.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + Client.COLUMN_ID_CARD + " INTEGER NOT NULL,"
                + Client.COLUMN_FIRST_NAME + " TEXT NOT NULL,"
                + Client.COLUMN_LAST_NAME + " TEXT NOT NULL,"
                + Client.COLUMN_PHONE + " INTEGER NOT NULL,"
                + Client.COLUMN_EMAIL + " TEXT NOT NULL);");

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

    public boolean saveClient(Client client) {
        if (client.id == 0) {
            return insertClient(client) > 0;
        } else {
            return updateClient(client) > 0;
        }
    }

    public long insertClient(Client client){//сохранение студента(новый)
        SQLiteDatabase db = getWritableDatabase();
        long id = 0;

        try {
            ContentValues values = new ContentValues();

            values.put(Client.COLUMN_ID_CARD, client.idCard);
            values.put(Client.COLUMN_FIRST_NAME, client.FirstName);
            values.put(Client.COLUMN_LAST_NAME, client.LastName);
            values.put(Client.COLUMN_PHONE, client.Phone);
            values.put(Client.COLUMN_EMAIL, client.Email);

            id = db.insert(Client.TABLE_NAME, null, values);

        }catch (Exception e){
            e.printStackTrace();
        }
        return id;
    }

    public int updateClient(Client client){//обновление студента(возвр количество сохр записей)
        SQLiteDatabase db = getWritableDatabase();
        int count = 0;

        try {
            ContentValues values = new ContentValues();

            values.put(Client.COLUMN_ID_CARD, client.idCard);
            values.put(Client.COLUMN_FIRST_NAME, client.FirstName);
            values.put(Client.COLUMN_LAST_NAME, client.LastName);
            values.put(Client.COLUMN_PHONE, client.Phone);
            values.put(Client.COLUMN_EMAIL, client.Email);

            count = db.update(Client.TABLE_NAME, values, Client.COLUMN_ID + "=" + client.id, null);

        }catch (Exception e){
            e.printStackTrace();
        }
        return count;
    }

    public ArrayList<Client> getClients(){//чтение студентов
        SQLiteDatabase db = getWritableDatabase();
        ArrayList<Client> clients = new ArrayList<>();
        Cursor cursor = null;

        try {
            cursor = db.query(Client.TABLE_NAME, null, null, null, null, null, null);//ищем студентов
            if (cursor.moveToFirst()) {//проверяем что что-то нашло, перемещает курсор на первую строку в результате запроса;
                while (!cursor.isAfterLast()) {//не конец запроса?
                    Client client = new Client();// заполняем студента

                    client.id = cursor.getLong(cursor.getColumnIndex(Client.COLUMN_ID));
                    client.idCard = cursor.getLong(cursor.getColumnIndex(Client.COLUMN_ID_CARD));
                    client.FirstName = cursor.getString(cursor.getColumnIndex(Client.COLUMN_FIRST_NAME));
                    client.LastName = cursor.getString(cursor.getColumnIndex(Client.COLUMN_LAST_NAME));
                    client.Phone = cursor.getLong(cursor.getColumnIndex(Client.COLUMN_PHONE));
                    client.Email = cursor.getString(cursor.getColumnIndex(Client.COLUMN_EMAIL));

                    clients.add(client);
                    cursor.moveToNext();//перемещает курсор на следующую строку;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (cursor != null) {
                cursor.close();//обязательно следует закрывать для освобождения памяти
            }
        }
        return  clients;
    }

    public Client getClient(long id){//чтение 1го студента по id
        SQLiteDatabase db = getWritableDatabase();
        Client client = null;
        Cursor cursor = null;

        try {
            cursor = db.query(Client.TABLE_NAME, null, Client.COLUMN_ID + "=" + id, null, null, null, null);//ищем указанного студента
            if (cursor.moveToFirst()) {//проверяем что что-то нашло
                client = new Client();// заполняем студента

                client.id = cursor.getLong(cursor.getColumnIndex(Client.COLUMN_ID));
                client.idCard = cursor.getLong(cursor.getColumnIndex(Client.COLUMN_ID_CARD));
                client.FirstName = cursor.getString(cursor.getColumnIndex(Client.COLUMN_FIRST_NAME));
                client.LastName = cursor.getString(cursor.getColumnIndex(Client.COLUMN_LAST_NAME));
                client.Phone = cursor.getLong(cursor.getColumnIndex(Client.COLUMN_PHONE));
                client.Email = cursor.getString(cursor.getColumnIndex(Client.COLUMN_EMAIL));
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return  client;
    }

}
