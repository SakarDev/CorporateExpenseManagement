package com.example.assignment;


import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "MyDBName.db";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        db.execSQL("PRAGMA foreign_keys=ON");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // create the table
        db.execSQL("create table users " +
                "(user_id integer primary key, email text, phone text, address text, password text, role text)"
        );
        // 0->id    1->email    2->phone    3->address  4->password     5->role

        // create table-categories
        db.execSQL("create table categories " +
                "(category_id integer primary key, category text)"
        );
        // create table-userExpenses
//        db.execSQL("create table expenses " +
//                "(expense_id integer primary key, email text, category text, price integer, date text)"
//        );
        db.execSQL("create table expenses " +
                "(expense_id integer primary key, user_id integer, category_id integer, price text, date text," +
                "FOREIGN KEY(user_id) REFERENCES users(user_id) ON DELETE CASCADE, " +
                "FOREIGN KEY(category_id) REFERENCES categories(category_id) ON DELETE CASCADE)"
        );
        // 2->category
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // pass the name of the table
        db.execSQL("DROP TABLE IF EXISTS users");
        onCreate(db);
    }

    // pass the parameters you want to insert
    public boolean insert(String email, String phone, String address, String password, String role) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        // put the parameter names
        contentValues.put("email", email);
        contentValues.put("phone", phone);
        contentValues.put("address", address);
        contentValues.put("password", password);
        contentValues.put("role", role);
        // table name is first parameter
        long result = db.insert("users", null, contentValues);
        // if insertion fails it returns -1
        if (result == -1)
            return false;
        return true;
    }

    public boolean delete(String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        // put table name and column name
        Cursor cursor = db.rawQuery("select * from users where email=\"" + email + "\"", null);
        if (cursor.getCount() > 0) {
            // put table name and column name
            long result = db.delete("users", "email = ? ", new String[]{email});
            if (result == -1) {
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    public boolean update(Integer user_id, String email, String phone, String address, String password, String role) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        // put the parameter names
        contentValues.put("email", email);
        contentValues.put("phone", phone);
        contentValues.put("address", address);
        contentValues.put("password", password);
        contentValues.put("role", role);
        // put table name and column name
        Cursor cursor = db.rawQuery("select * from users where user_id = ?", new String[]{Integer.toString(user_id)});
        if (cursor.getCount() > 0) {
            // put table name and column name
            long result = db.update("users", contentValues, "user_id = ? ", new String[]{Integer.toString(user_id)});
            if (result == -1) {
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    public Cursor getData(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        // put table name and column name
        // if column name is string you should add \" email \"
        Cursor cursor = db.rawQuery("select * from users where email=\"" + email + "\"", null);
        cursor.moveToFirst();
        return cursor;
        // to check if data retrieved by cursor cursor.getCount() != 0
    }

    @SuppressLint("Range")
    public ArrayList<String> getAllData() {
        ArrayList<String> array_list = new ArrayList<String>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        // put table name
        Cursor res = db.rawQuery("select * from users", null);
        res.moveToFirst();
        while (res.isAfterLast() == false) {
            // put column name
            array_list.add(res.getString(1));
            res.moveToNext();
        }
        return array_list;
    }

    @SuppressLint("Range")
    public ArrayList<String> getAllEmployeesData() {
        ArrayList<String> array_list = new ArrayList<String>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        // put table name
        // where clause ensures that only employees will be retrieved(not admins)
        Cursor res = db.rawQuery("select * from users where role == '0'", null);
        res.moveToFirst();
        while (res.isAfterLast() == false) {
            // put column name
            array_list.add(res.getString(1));
            res.moveToNext();
        }
        return array_list;
    }


    //    ---------------------------------category
    //
    public boolean insertCategory(String category) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        // put the parameter names
        contentValues.put("category", category);
        // table name is first parameter
        long result = db.insert("categories", null, contentValues);
        // if insertion fails it returns -1
        if (result == -1)
            return false;
        return true;
    }

    @SuppressLint("Range")
    public ArrayList<String> getAllCategories() {
        ArrayList<String> array_list = new ArrayList<String>();

        SQLiteDatabase db = this.getReadableDatabase();
        // put table name
        Cursor res = db.rawQuery("select * from categories", null);
        res.moveToFirst();
        while (res.isAfterLast() == false) {
            // put column name
            array_list.add(res.getString(1));
            res.moveToNext();
        }
        return array_list;
    }

    public Cursor getCategory(String category) {
        SQLiteDatabase db = this.getReadableDatabase();
        // put table name and column name
        // if column name is string you should add \" email \"
        Cursor cursor = db.rawQuery("select * from categories where category=\"" + category + "\"", null);
        cursor.moveToFirst();
        return cursor;
        // to check if data retrieved by cursor cursor.getCount() != 0
    }

    public boolean updateCategory(Integer category_id, String category) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        // put the parameter names
        contentValues.put("category", category);
        // put table name and column name
        Cursor cursor = db.rawQuery("select * from categories where category_id = ?", new String[]{Integer.toString(category_id)});
        if (cursor.getCount() > 0) {
            // put table name and column name
            long result = db.update("categories", contentValues, "category_id = ? ", new String[]{Integer.toString(category_id)});
            if (result == -1) {
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    public boolean deleteCategory(String category) {
        SQLiteDatabase db = this.getWritableDatabase();
        // put table name and column name
        Cursor cursor = db.rawQuery("select * from categories where category=\"" + category + "\"", null);
        if (cursor.getCount() > 0) {
            // put table name and column name
            long result = db.delete("categories", "category = ? ", new String[]{category});
            if (result == -1) {
                return false;
            } else {
                return true;
            }
        }
        return false;
    }


    //    --------------------------Transaction
    public boolean insertExpense(String email, String category, String price, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        // put the parameter names

        int user_id = Integer.parseInt(this.getData(email).getString(0));
        int category_id = Integer.parseInt(this.getCategory(category).getString(0));

        contentValues.put("user_id", user_id);
        contentValues.put("category_id", category_id);
        contentValues.put("price", price);
        contentValues.put("date", date);
        // table name is first parameter
        long result = db.insert("expenses", null, contentValues);
        // if insertion fails it returns -1
        if (result == -1)
            return false;
        return true;
    }

    @SuppressLint("Range")
    public ArrayList<String> getAllCurrentUserTransactions(String user_id) {
        ArrayList<String> array_list = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "select category, price, date from expenses join categories using(category_id) join users " +
                "using(user_id) where user_id=\"" + user_id + "\"";

        Cursor res = db.rawQuery(query, null);
        res.moveToFirst();
        while (res.isAfterLast() == false) {
            // put column name
            array_list.add(res.getString(0) + ", " + res.getString(1) + ", " + res.getString(2));
            res.moveToNext();
        }
        return array_list;
    }

    public Cursor getExpenseId(String email, String category, String price, String date) {
        SQLiteDatabase db = this.getReadableDatabase();
        // put table name and column name
        // if column name is string you should add \" email \"

        String query = "select expense_id from expenses JOIN categories using(category_id)" +
                "join users using(user_id)" +
                "where email =\"" + email + "\"" +
                "and category =\"" + category + "\"" +
                "and price =\"" + price + "\"" +
                "and date =\"" + date + "\"";

        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        return cursor;
        // to check if data retrieved by cursor cursor.getCount() != 0
    }

    public Cursor getTransaction(int expense_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        // put table name and column name
        // if column name is string you should add \" category \"
        Cursor cursor = db.rawQuery("select * from expenses where expense_id = " + expense_id, null);
        cursor.moveToFirst();
        return cursor;
        // to check if data retrieved by cursor cursor.getCount() != 0
    }

    public boolean deleteTransaction(int expense_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        // put table name and column name
        Cursor cursor = db.rawQuery("select * from expenses where expense_id=" + expense_id, null);
        if (cursor.getCount() > 0) {
            // put table name and column name
            long result = db.delete("expenses", "expense_id = ? ", new String[]{Integer.toString(expense_id)});
            if (result == -1) {
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    public boolean updateTransaction(Integer expense_id, String email, String category, String price, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        int user_id = Integer.parseInt(this.getData(email).getString(0));
        int category_id = Integer.parseInt(this.getCategory(category).getString(0));

        // put the parameter names
        contentValues.put("expense_id", expense_id);
        contentValues.put("user_id", user_id);
        contentValues.put("category_id", category_id);
        contentValues.put("price", price);
        contentValues.put("date", date);
        // put table name and column name
        Cursor cursor = db.rawQuery("select * from expenses where expense_id = ?", new String[]{Integer.toString(expense_id)});
        if (cursor.getCount() > 0) {
            // put table name and column name
            long result = db.update("expenses", contentValues, "expense_id = ? ", new String[]{Integer.toString(expense_id)});
            if (result == -1) {
                return false;
            } else {
                return true;
            }
        }
        return false;
    }



    public Cursor getTotalUserExpense(String user_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "select sum(price) from expenses group by user_id HAVING user_id=\"" + user_id + "\"";
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        return cursor;
        // to check if data retrieved by cursor cursor.getCount() != 0
    }


    //    history activity
    @SuppressLint("Range")
    public ArrayList<String> getCategoryAndTotalPrice(String user_id) {
        ArrayList<String> array_list = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "select category, sum(price) from expenses JOIN categories USING(category_id) group by category_id, user_id " +
                "HAVING user_id=\"" + user_id + "\"";

        Cursor res = db.rawQuery(query, null);
        res.moveToFirst();
        while (res.isAfterLast() == false) {
            // put column name
            array_list.add(res.getString(0) + ", " + res.getString(1) );
            res.moveToNext();
        }
        return array_list;
    }






}