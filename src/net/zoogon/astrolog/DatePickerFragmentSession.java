package net.zoogon.astrolog;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class DatePickerFragmentSession extends DialogFragment{

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        
        int year = getArguments().getInt("year");
        int month = getArguments().getInt("month");
        int day = getArguments().getInt("day");
 
        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), (EditSessionActivity)getActivity(), year, month, day);
    }

}
