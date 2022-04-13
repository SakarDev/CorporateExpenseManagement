package com.example.assignment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import java.util.ArrayList;
import java.util.Calendar;

public class ModifyTransactionDialog extends AppCompatDialogFragment implements AdapterView.OnItemSelectedListener {
    Spinner spinnerTransactionCategory;
    EditText editTextPriceTransaction;
    EditText editTextDateTransaction;
    DBHelper db;
    String selectedCategoryStr ="";
    String date;
    String categoryStr;
    String priceStr;
    String dateStr;
    String expense_id;

    ModifyTransactionDialog.TransactionDialogListener listener;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (ModifyTransactionDialog.TransactionDialogListener) context;
        } catch (ClassCastException e) {
//            if we open this dialog from the employee activity but forget to implement this TransactionDialogListener then we get the following exception
            throw new ClassCastException(context.toString() +
                    "must implement Add ModifyTransactionDialog");
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


        Bundle bundle = getArguments();
        categoryStr = bundle.getString("category","");
        priceStr = bundle.getString("price","");
        dateStr = bundle.getString("date","");
        expense_id = bundle.getString("expense_id","");

        spinnerTransactionCategory = view.findViewById(R.id.spinnerTransactionCategory);
        editTextPriceTransaction = view.findViewById(R.id.editTextPriceTransaction);
        editTextDateTransaction = view.findViewById(R.id.editTextDateTransaction);

        spinnerTransactionCategory.setSelection((arrayAdapter).getPosition(categoryStr));
        editTextDateTransaction.setText(dateStr+"");
        editTextPriceTransaction.setText(priceStr+"");

        //set the builder view to the layout file
        builder.setView(view)
                .setTitle("Modify Transaction")
                .setNegativeButton("Update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String oldCategory = categoryStr;
                        String newCategory = selectedCategoryStr;

                        String oldPrice = priceStr;
                        String newPrice = editTextPriceTransaction.getText().toString();

                        String oldDate = dateStr;
                        String newDate = editTextDateTransaction.getText().toString();

                        listener.updateTransaction(expense_id, oldCategory, newCategory, oldPrice, newPrice, oldDate, newDate);
                    }
                })
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        listener.deleteTransaction(expense_id);
                    }
                })
                .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // onclick the Neutral button which is cancel nothing happens only dismisses the dialog so we write nothing here
                    }
                });
//        return the created dialog
        return builder.create();
    }

    public interface TransactionDialogListener {
        void updateTransaction(String expense_id, String oldCategory, String newCategory, String oldPrice, String newPrice, String oldDate, String newDate);
        void deleteTransaction(String expense_id);
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