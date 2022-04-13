package com.example.assignment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import java.util.ArrayList;
import java.util.Calendar;

public class AddTransactionDialog extends AppCompatDialogFragment implements AdapterView.OnItemSelectedListener {
    Spinner spinnerTransactionCategory;
    EditText editTextPriceTransaction;
    EditText editTextDateTransaction;
    AddTransactionDialog.TransactionDialogListener listener;
    DBHelper db;
    String selectedCategoryStr ="";
    DatePickerDialog.OnDateSetListener setListener;
    String date;





    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (AddTransactionDialog.TransactionDialogListener) context;
        } catch (ClassCastException e) {
//            if we open this dialog from the employee activity but forget to implement this TransactionDialogListener then we get the following exception
            throw new ClassCastException(context.toString() +
                    "must implement Add TransactionDialogListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_transaction_dialog, null);

        editTextPriceTransaction = view.findViewById(R.id.editTextPriceTransaction);
        editTextDateTransaction = view.findViewById(R.id.editTextDateTransaction);
        spinnerTransactionCategory = view.findViewById(R.id.spinnerTransactionCategory);
        db = new DBHelper(getContext());
        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        editTextDateTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                        month = month +1;
                        date = day + "/"+month + "/"+year;
                        editTextDateTransaction.setText(date);
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });


        ArrayList array_list = db.getAllCategories();
        ArrayAdapter arrayAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_dropdown_item, array_list);
        spinnerTransactionCategory.setAdapter(arrayAdapter);
        spinnerTransactionCategory.setOnItemSelectedListener(this);

        //set the builder view to the layout file
        builder.setView(view)
                .setTitle("Add Transaction")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // onclick the NegativeButton which is cancel nothing happens only dismisses the dialog so we write nothing here
                    }
                })
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String category = selectedCategoryStr;
                        String priceStr = editTextPriceTransaction.getText().toString();
                        listener.addTransaction(category, priceStr, date);
                    }
                });
//        return the created dialog
        return builder.create();
    }

    public interface TransactionDialogListener {
        void addTransaction(String category,String priceStr, String date);
    }

    //    on item selected of the spinner
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        selectedCategoryStr = adapterView.getItemAtPosition(i).toString();
    }

    //on nothing selected of the spinner we do nothing (it was a must to override it since we implemented AdapterView.OnItemSelectedListener)
    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {}

}