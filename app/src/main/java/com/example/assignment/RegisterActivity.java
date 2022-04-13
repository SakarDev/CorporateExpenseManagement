package com.example.assignment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


public class RegisterActivity extends AppCompatActivity {

    EditText editTextEmail, editTextPhone, editTextAddress, editTextPassword;
    DBHelper db;
    String id = "0";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        db = new DBHelper(this);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPhone = (EditText) findViewById(R.id.editTextPhone);
        editTextAddress = (EditText) findViewById(R.id.editTextAddress);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);

    }


    public void signupMethod(View view) {
        String email = editTextEmail.getText().toString();
        String address = editTextAddress.getText().toString();
        String phone = editTextPhone.getText().toString();
        String password = editTextPassword.getText().toString();

        if(isValid(email) && isValid(address) && isValid(phone) && isValid(password)){
            Cursor cursor = db.getData(email);
            if(cursor.getCount() == 0){
                if(db.insert(email, address, phone, password, "0")){
                    Toast.makeText(getApplicationContext(), "Account created successfully!", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(RegisterActivity.this, MainActivity.class);
                    startActivity(i);
                }else{
                    Toast.makeText(getApplicationContext(), "Failed to create account!", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(getApplicationContext(), "Email already exist!", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(getApplicationContext(), "Fill all the fileds!", Toast.LENGTH_SHORT).show();
        }

    }
    public boolean isValid(String input){
        return input.length() != 0;
    }




}