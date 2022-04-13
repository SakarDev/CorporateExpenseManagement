package com.example.assignment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;


public class LoginActivity extends AppCompatActivity {

    EditText editTextEmailLogin, editTextPasswordLogin;
    CheckBox checkBoxShowPass;
    DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        /**logout broadcast receiver **/
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.package.ACTION_LOGOUT");
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d("onReceive","Logout in progress");
                //At this point you should start the login activity and finish this one
                finish();
            }
        }, intentFilter);
        //** end of broadcast receiver **//




        db = new DBHelper(this);

        editTextEmailLogin = findViewById(R.id.editTextEmailLogin);
        editTextPasswordLogin = findViewById(R.id.editTextPasswordLogin);
        checkBoxShowPass = findViewById(R.id.checkBoxShowPass);

        checkBoxShowPass.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    editTextPasswordLogin.setInputType(InputType.TYPE_CLASS_TEXT);
                } else {
                    editTextPasswordLogin.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
            }
        });
    }

    public void loginMethod(View view) {

        String email = editTextEmailLogin.getText().toString();
        String password = editTextPasswordLogin.getText().toString();

        if (isValid(email) && isValid(password)) {
            Cursor cursor = db.getData(email);
            // if email exists
            if (cursor.getCount() != 0) {
                if (db.getData(email).getString(1).equals(email) && db.getData(email).getString(4).equals(password)) {
                    // if role==0 means it's an employee
                    if (db.getData(email).getString(5).equals("0")){
                        Toast.makeText(this, "Welcome " + email, Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(LoginActivity.this, EmployeeActivity.class);
                        i.putExtra("email", email);
                        i.putExtra("user_id", db.getData(email).getString(0));
                        startActivity(i);
                    }
                    // else it's admin
                    else{
                        Toast.makeText(this, "Welcome " + email, Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(i);
                    }
                } else {
                    Toast.makeText(this, "Wrong username or password!", Toast.LENGTH_SHORT).show();
                }
            }else {
                Toast.makeText(this, "Wrong username or password!", Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(getApplicationContext(), "Fill all the fileds!", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean isValid(String input) {
        return input.length() != 0;
    }






}