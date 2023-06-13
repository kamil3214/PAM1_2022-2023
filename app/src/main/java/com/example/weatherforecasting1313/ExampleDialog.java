package com.example.weatherforecasting1313;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;


public class ExampleDialog extends AppCompatDialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Help")
                .setMessage("Choose date and time using buttons and enter name or names of cities you want to track separated by comma. Weather data available for next 5 days (120h). 'Time from keyboard mode' allows you to input hour next to city name example: 'berlin 14, warsaw 19' when you want to get forecast for Berlin at 14:00 and Warsaw at 19:00.")
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

        return builder.create();
    }
}
