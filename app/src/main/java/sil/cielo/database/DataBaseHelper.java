

package sil.cielo.database;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;


public class DataBaseHelper extends SQLiteOpenHelper {

    private static String DB_PATH;

    // the name of my DB
    private static String DB_NAME = "cieloDB.db";

    private SQLiteDatabase myDataBase;
    private final Context myContext;


    private static final int DATABASE_VERSION = 1;

    public DataBaseHelper(Context myContext) throws IOException {
        super(myContext, DB_NAME, null, DATABASE_VERSION);
        DB_PATH = myContext.getFilesDir() + "/Cielo/data/database/";
        this.myContext = myContext;

        if (!(new File(getLocalPath()).exists())) {
            boolean success = (new File(getLocalPath())).mkdirs();
            if (!success) {
                // Directory creation failed
            }
        } // check local path

    }

    private String getLocalPath() {
        return DB_PATH;
    }

    /**
     * Creates a empty database on the system and rewrites it with your own
     * database.
     */
    public void createDataBase() throws IOException {


        if (new File(DB_PATH + DB_NAME).exists()) {
            // do nothing - database already exist
            this.getWritableDatabase();
        } else {


            try {
                copyDataBase();
                //sq.close();

            } catch (IOException e) {
                System.out.println(e.getMessage());
            }

        }

    }


    public boolean checkDataBase() {

        SQLiteDatabase checkDB = null;
        boolean notNull = false;

        try {
            return myContext.getDatabasePath(DB_NAME).exists();

        } catch (SQLiteException e) {
            // database does't exist yet.
            notNull = false;
        } finally {
            if (checkDB != null) {
                checkDB.close();
                notNull = true;
            } else {
                notNull = false;
            }

        }

        if (notNull) {
            return true;
        } else {
            return false;
        }

    }


    private void copyDataBase() throws IOException {

        InputStream myInput = myContext.getAssets().open(DB_NAME);
        String outFileName = DB_PATH + DB_NAME;
        FileOutputStream myOutput = new FileOutputStream(outFileName);

        byte[] buffer = new byte[1024];
        int length = 0;
        while ((length = myInput.read(buffer)) > 0) {
            try {
                myOutput.write(buffer, 0, length);
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }

        }

        // Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();

    }

    public void openDataBase() throws SQLException {
        // Open the database
        String myPath = DB_PATH + DB_NAME;
        myDataBase = SQLiteDatabase.openOrCreateDatabase(myPath, null);
    }

    @Override
    public synchronized void close() {

        if (myDataBase != null) {
            myDataBase.close();
        }

        super.close();

    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        myDataBase = db;
        try {
            copyDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * onUpgrade is called whenever the app is upgraded and launched and the database version is not the same.
     *
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        myDataBase = db;
        try {
            copyDataBase();
        } catch (IOException e) {
            System.out.println(e.getMessage());

        }

    }


    public Cursor getAllStarsInDB() {
        String sql1 = null;

        sql1 = "select * from STARS";


        return myDataBase.rawQuery(sql1, null);

    }


}
