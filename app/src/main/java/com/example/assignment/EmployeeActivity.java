package com.example.assignment;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class EmployeeActivity extends AppCompatActivity implements AddTransactionDialog.TransactionDialogListener,
        ModifyTransactionDialog.TransactionDialogListener {
    DBHelper db;
    ListView listViewTransactions;
    FloatingActionButton fab;
    String email;
    String user_id;
    ArrayList array_list;
    ArrayAdapter arrayAdapter;
    TextView textViewTotal;
    String totalExp = "Total: ";
    String query;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee);
        db = new DBHelper(this);
        listViewTransactions = findViewById(R.id.listViewTransactions);
        textViewTotal = findViewById(R.id.textViewTotal);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            email = extras.getString("email");
            user_id = extras.getString("user_id");
            array_list = db.getAllCurrentUserTransactions(user_id);
            arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, array_list);
            listViewTransactions.setAdapter(arrayAdapter);
        }

        textViewTotal.setText("Total: ");
        Cursor cursor = db.getTotalUserExpense(user_id);
        if(cursor.getCount() != 0){
            totalExp = cursor.getString(0).toString();
            textViewTotal.setText("Total: " + totalExp +" $");
        }

        listViewTransactions.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String clickedItem = array_list.get(i).toString();
                String[] parts = clickedItem.split(", ");
                String category = parts[0].trim();
                String price = parts[1].trim();
                String date = parts[2].trim();

                Bundle extras = getIntent().getExtras();
                if (extras != null) {
                    email = extras.getString("email");
                }
                String expense_id = db.getExpenseId(email, category, price, date).getString(0);

                ModifyTransactionDialog dialogFragment = new ModifyTransactionDialog();
                Bundle bundle = new Bundle();
                bundle.putString("category",category);
                bundle.putString("price",price);
                bundle.putString("date",date);
                bundle.putString("expense_id",expense_id);
                dialogFragment.setArguments(bundle);
                dialogFragment.show((EmployeeActivity.this).getSupportFragmentManager(),"Modify transaction");
            }
        });

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAddTransaction();
            }
        });
        if(getIntent().hasExtra("cameFromUserInfo")){
            fab.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.employee_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.itemHistory:
                Intent i = new Intent(getApplicationContext(), HistoryActivity.class);
                i.putExtra("email", email);
                i.putExtra("user_id", user_id);
                startActivity(i);
                return true;
            case R.id.itemReminder:
                Intent intent = new Intent(getApplicationContext(), ReminderActivity.class);
                startActivity(intent);
                return true;
            case R.id.itemLogout:
                // Broadcast intent to logout
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("com.package.ACTION_LOGOUT");
                sendBroadcast(broadcastIntent);
                Intent i2 = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(i2);
                finish();
                // Finish one activity after you start another. This way your task stack will have only one activity, i.e., no back history.
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public boolean isValid(String input) {
        return input.length() != 0;
    }


//    opens the AddTransaction dialog
    public void openAddTransaction() {
        AddTransactionDialog addTransactionDialog = new AddTransactionDialog();
        addTransactionDialog.show(getSupportFragmentManager(), "Add Transaction");
    }

    @Override
    public void addTransaction(String category, String priceStr, String dateStr) {
        if (isValid(category) && isValid(priceStr) && isValid(dateStr)) {
            try {
                int price = Integer.parseInt(priceStr);
                if (price > 0) {
                    if (db.insertExpense(email, category, priceStr, dateStr)) {
                        Toast.makeText(getApplicationContext(), "Transaction added successfully!", Toast.LENGTH_SHORT).show();
                        reloadListView();
                    } else {
                        Toast.makeText(getApplicationContext(), "Failed to add transaction!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Please enter a valid price!", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Please enter a valid price!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Fill all the fields!", Toast.LENGTH_SHORT).show();
        }
    }
    public void reloadListView(){
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            array_list = db.getAllCurrentUserTransactions(user_id);
            arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, array_list);
            listViewTransactions.setAdapter(arrayAdapter);
        }
        textViewTotal.setText("Total: ");
        Cursor cursor = db.getTotalUserExpense(user_id);
        if(cursor.getCount() != 0){
            totalExp = cursor.getString(0).toString();
            textViewTotal.setText("Total: " + totalExp +" $");
        }
    }

    @Override
    public void deleteTransaction(String expense_id) {
        int expense_id_int = Integer.parseInt(expense_id);
        if(isValid(expense_id)){
            Cursor cursor = db.getTransaction(expense_id_int);
            if(cursor.getCount() != 0){
                if(db.deleteTransaction(expense_id_int)){
                    Toast.makeText(getApplicationContext(), "Transaction deleted successfully!", Toast.LENGTH_SHORT).show();
                    reloadListView();
                }else{
                    Toast.makeText(getApplicationContext(), "Failed to delete transaction!", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(getApplicationContext(), "Transaction does not exist", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(getApplicationContext(), "Fill all the fields!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void updateTransaction(String expense_id, String oldCategory, String newCategory, String oldPrice, String newPrice, String oldDate, String newDate) {
        int expense_id_int = Integer.parseInt(expense_id);

        if(isValid(newCategory) && isValid(newPrice) && isValid(newDate) ){
            Cursor cursor = db.getTransaction(expense_id_int);
            if(cursor.getCount() != 0){
                if(!newCategory.equals(oldCategory) || !newPrice.equals(oldPrice) || !newDate.equals(oldDate) ){
                    if(db.updateTransaction(expense_id_int, email, newCategory, newPrice, newDate)){
                        Toast.makeText(getApplicationContext(), "Transaction updated successfully!", Toast.LENGTH_SHORT).show();
                        reloadListView();
                    }else{
                        Toast.makeText(getApplicationContext(), "Failed to update transaction!", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "No change made!", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(getApplicationContext(), "Transaction does not exist", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(getApplicationContext(), "Fill all the fields!", Toast.LENGTH_SHORT).show();
        }
    }

}