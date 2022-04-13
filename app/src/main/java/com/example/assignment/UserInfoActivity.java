package com.example.assignment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


public class UserInfoActivity extends AppCompatActivity {

    EditText editTextEmailUser, editTextPhoneUser, editTextAddressUser, editTextPasswordUser;
    String email, password, address, phone = "";
    DBHelper db;

    String id = "0";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        editTextEmailUser = (EditText) findViewById(R.id.editTextEmailUser);
        editTextPhoneUser = (EditText) findViewById(R.id.editTextPhoneUser);
        editTextAddressUser = (EditText) findViewById(R.id.editTextAddressUser);
        editTextPasswordUser = (EditText) findViewById(R.id.editTextPasswordUser);
        db = new DBHelper(this);


        Bundle extras = getIntent().getExtras();
        if(extras != null){
            if(getIntent().hasExtra("email")){
                id = extras.getString("id");
                email = extras.getString("email");
                phone = extras.getString("phone");
                address = extras.getString("address");
                password = extras.getString("password");
                editTextEmailUser.setText(email);
                editTextPhoneUser.setText(phone);
                editTextAddressUser.setText(address);
                editTextPasswordUser.setText(password);
            }

        }
    }

    public void deleteMethod(View view) {
        String email = editTextEmailUser.getText().toString();
        String address = editTextAddressUser.getText().toString();
        String phone = editTextPhoneUser.getText().toString();
        String password = editTextPasswordUser.getText().toString();

        if(isValid(email) && isValid(address) && isValid(phone) && isValid(password)){
            Cursor cursor = db.getData(email);
            if(cursor.getCount() != 0){
                if(db.delete(email)){
                    Toast.makeText(getApplicationContext(), "Account deleted successfully!", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(UserInfoActivity.this, MainActivity.class);
                    startActivity(i);
                }else{
                    Toast.makeText(getApplicationContext(), "Failed to delete account!", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(getApplicationContext(), "Email does not exist", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(getApplicationContext(), "Fill all the fields!", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean isValid(String input){
        return input.length() != 0;
    }

    public void updateMethod(View view) {
        String newEmail = editTextEmailUser.getText().toString();
        String newPhone = editTextPhoneUser.getText().toString();
        String newAddress = editTextAddressUser.getText().toString();
        String newPassword = editTextPasswordUser.getText().toString();
        String oldEmail = "";
        String oldPhone = "";
        String oldAddress = "";
        String oldPassword = "";

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            if(getIntent().hasExtra("email")){
                oldEmail = extras.getString("email");
                oldPhone = extras.getString("phone");
                oldAddress = extras.getString("address");
                oldPassword = extras.getString("password");
            }
        }

        int myID = Integer.parseInt(id);
        if(isValid(newEmail) && isValid(newPhone) && isValid(newAddress) && isValid(newPassword)){
            Cursor cursor = db.getData(oldEmail);
            if(cursor.getCount() != 0){
                if(!newEmail.equals(oldEmail) || !newPhone.equals(oldPhone) ||
                        !newAddress.equals(oldAddress) || !newPassword.equals(oldPassword)){
                    if(db.update(myID, newEmail, newPhone, newAddress, newPassword, "0")){
                        Toast.makeText(getApplicationContext(), "Account updated successfully!", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(UserInfoActivity.this, MainActivity.class);
                        i.putExtra("email", newEmail);
                        i.putExtra("phone", newPhone);
                        i.putExtra("address", newAddress);
                        i.putExtra("password", newPassword);
                        startActivity(i);
                    }else{
                        Toast.makeText(getApplicationContext(), "Failed to update account!", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "No change made!", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(getApplicationContext(), "Account does not exist", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(getApplicationContext(), "Fill all the fields!", Toast.LENGTH_SHORT).show();
        }
    }

    public void userTransactionMethod(View view) {
        Intent i = new Intent(getApplicationContext(), EmployeeActivity.class);
        i.putExtra("email", email);
        i.putExtra("user_id", db.getData(email).getString(0));
        i.putExtra("cameFromUserInfo", "cameFromUserInfo");
        startActivity(i);
    }
}