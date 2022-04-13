package com.example.assignment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements AddCategoryDialog.AddCategoryDialogListener, ModifyCategoryDialog.ModifyCategoryDialogListener {

    ListView listview;
    DBHelper db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DBHelper(this);
        listview = (ListView) findViewById(R.id.listViewTransactions);

        ArrayList array_list = db.getAllEmployeesData();
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, array_list);
        listview.setAdapter(arrayAdapter);


        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String email = array_list.get(i).toString();
                String phone = db.getData(email).getString(2);
                String address = db.getData(email).getString(3);
                String password = db.getData(email).getString(4);

                Intent intent = new Intent(MainActivity.this, UserInfoActivity.class);
                intent.putExtra("id", db.getData(email).getString(0));
                intent.putExtra("email", email);
                intent.putExtra("phone", phone);
                intent.putExtra("address", address);
                intent.putExtra("password", password);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.itemSignup:
                Intent i = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(i);
                return true;
            case R.id.itemAddCategory:
                openAddCategoryDialog();
                return true;
            case R.id.itemModifyCategory:
                openModifyCategoryDialog();
                return true;
            case R.id.itemLogout:
                // Broadcast intent to logout
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("com.package.ACTION_LOGOUT");
                sendBroadcast(broadcastIntent);
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
                // Finish one activity after you start another. This way your task stack will have only one activity, i.e., no back history.
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public boolean isValid(String input){
        return input.length() != 0;
    }


    public void openAddCategoryDialog(){
        AddCategoryDialog addCategoryDialog = new AddCategoryDialog();
        addCategoryDialog.show(getSupportFragmentManager(), "Add category");
    }

    // onclick of the add button of the dialog we get the input field data
    @Override
    public void addCategory(String category) {
        if(isValid(category) ){
            Cursor cursor = db.getCategory(category);
            if(cursor.getCount() == 0){
                if(db.insertCategory(category)){
                    Toast.makeText(getApplicationContext(), "Category added successfully!", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(), "Failed to add category!", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(getApplicationContext(), "Category already exist!", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(getApplicationContext(), "Fill all the fields!", Toast.LENGTH_SHORT).show();
        }
    }

    private void openModifyCategoryDialog() {
        ModifyCategoryDialog modifyCategoryDialog = new ModifyCategoryDialog();
        modifyCategoryDialog.show(getSupportFragmentManager(), "Modify Category");
    }

    @Override
    public void deleteCategory(String category) {
        if(isValid(category)){
            Cursor cursor = db.getCategory(category);
            if(cursor.getCount() != 0){
                if(db.deleteCategory(category)){
                    Toast.makeText(getApplicationContext(), "Category deleted successfully!", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(), "Failed to delete category!", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(getApplicationContext(), "Category does not exist", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(getApplicationContext(), "Fill all the fields!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void updateCategory(String oldCategory, String newCategory) {
        String id = db.getCategory(oldCategory).getString(0);
        int categoryID = Integer.parseInt(id);

        if(isValid(newCategory) ){
            Cursor cursor = db.getCategory(oldCategory);
            if(cursor.getCount() != 0){
                if(!newCategory.equals(oldCategory) ){
                    if(db.updateCategory(categoryID, newCategory)){
                        Toast.makeText(getApplicationContext(), "Category updated successfully!", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(getApplicationContext(), "Failed to update category!", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "No change made!", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(getApplicationContext(), "Category does not exist", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(getApplicationContext(), "Fill all the fields!", Toast.LENGTH_SHORT).show();
        }
    }

}