package ru.mirea.koskin.mireaproject.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.DialogFragment;

import ru.mirea.koskin.mireaproject.MainActivity;
import ru.mirea.koskin.mireaproject.MyMediaPlayerService;
import ru.mirea.koskin.mireaproject.R;

public class HistoryDialogFragment extends DialogFragment {
    private EditText editText_Multi;
    private ExampleDialogListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_history, null);

        builder.setView(view)
                .setTitle("Add History")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("SAVE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String multitext = editText_Multi.getText().toString();
                        listener.applyTexts(multitext);
                    }
                });

        editText_Multi = view.findViewById(R.id.editText_Multi);
        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (ExampleDialogListener) context;
            Log.d("HistoryDialog", "Listener applied " + ((Activity)context).getClass().getSimpleName());
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "must implement ExampleDialogListener");
        }
    }

    public interface ExampleDialogListener {
        void applyTexts(String multitext);
    }
}
