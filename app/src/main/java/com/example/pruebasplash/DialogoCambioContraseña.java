package com.example.pruebasplash;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

import androidx.fragment.app.DialogFragment;

public class DialogoCambioContraseña extends DialogFragment {

    public static String TAG = "Multichoice";

    public interface OnInputListener {
        void sendInput(String input, String input2);
    }
    public OnInputListener mOnInputListener;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        View view = inflater.inflate(R.layout.layout_dialogo, container, false);
        Button mActionCancel = view.findViewById(R.id.action_cancel);
        Button mActionOk = view.findViewById(R.id.action_ok);
        EditText caja1 = view.findViewById(R.id.passCaja);
        EditText caja2 = view.findViewById(R.id.passCaja2);
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout

                // Add action buttons
        mActionCancel.setOnClickListener(
                new View.OnClickListener() {
                    @Override public void onClick(View v)
                    {
                        Log.d(TAG, "onClick: closing dialog");
                        getDialog().dismiss();
                    }
                });

        mActionOk.setOnClickListener(
                new View.OnClickListener() {
                    @Override public void onClick(View v)
                    {
                        Log.d(TAG, "onClick: capturing input");
                        String pass1 = caja1.getText().toString();
                        String pass2 = caja2.getText().toString();
                        mOnInputListener.sendInput(pass1, pass2);
                        getDialog().dismiss();
                    }
                });
        return view;
    }

    @Override public void onAttach(Context context)
    {
        super.onAttach(context);
        try {
            mOnInputListener
                    = (OnInputListener)getActivity();
        }
        catch (ClassCastException e) {
            Log.e(TAG, "onAttach: ClassCastException: "
                    + e.getMessage());
        }
    }
}
