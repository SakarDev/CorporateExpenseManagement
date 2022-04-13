package com.example.assignment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

public class AddCategoryDialog extends AppCompatDialogFragment {
    EditText editTextCategory;
    AddCategoryDialogListener listener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (AddCategoryDialogListener) context;
        } catch (ClassCastException e) {
//            if we open this dialog from the main activity but forget to implement this AddCategoryDialogListener then we get the following exception
            throw new ClassCastException(context.toString()+
                    "must implement Add AddCategoryDialogListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_add_category_dialog, null);
        editTextCategory = view.findViewById(R.id.editTextCategory);


        //set the builder view to the layout file
        builder.setView(view)
                .setTitle("Add category")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
//                       onclick the negative button which is cancel nothing happens only dismisses the dialog so we write nothing here
                    }
                })
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String category = editTextCategory.getText().toString();
                        listener.addCategory(category);
                    }
                });
//        return the created dialog
        return builder.create();
    }

    public interface AddCategoryDialogListener{
        void addCategory(String category);
    }
}
