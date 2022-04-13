package com.example.assignment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import java.util.ArrayList;

public class ModifyCategoryDialog extends AppCompatDialogFragment implements AdapterView.OnItemSelectedListener {
    Spinner spinnerCategoryModify;
    EditText editTextCategoryModify;
    ModifyCategoryDialog.ModifyCategoryDialogListener listener;
    DBHelper db;
    String selectedCategoryStr ="";

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (ModifyCategoryDialog.ModifyCategoryDialogListener) context;
        } catch (ClassCastException e) {
//            if we open this dialog from the main activity but forget to implement this ModifyCategoryDialogListener then we get the following exception
            throw new ClassCastException(context.toString() +
                    "must implement Add ModifyCategoryDialogListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_modify_category_dialog, null);
        editTextCategoryModify = view.findViewById(R.id.editTextCategoryModify);
        spinnerCategoryModify = view.findViewById(R.id.spinnerCategoryModify);
        db = new DBHelper(getContext());

        ArrayList array_list = db.getAllCategories();
        ArrayAdapter arrayAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_dropdown_item, array_list);
        spinnerCategoryModify.setAdapter(arrayAdapter);
        spinnerCategoryModify.setOnItemSelectedListener(this);


        //set the builder view to the layout file
        builder.setView(view)
                .setTitle("Modify category")
                .setNegativeButton("Update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String oldCategory = selectedCategoryStr;
                        String newCategory = editTextCategoryModify.getText().toString();
                        listener.updateCategory(oldCategory, newCategory);
                    }
                })
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String category = selectedCategoryStr;
                        listener.deleteCategory(category);
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

    public interface ModifyCategoryDialogListener {
        void deleteCategory(String category);
        void updateCategory(String oldCategory, String newCategory);
    }

//    on item selected of the spinner
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        selectedCategoryStr = adapterView.getItemAtPosition(i).toString();
    }

    //on nothing selected of the spinner
    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {}


}