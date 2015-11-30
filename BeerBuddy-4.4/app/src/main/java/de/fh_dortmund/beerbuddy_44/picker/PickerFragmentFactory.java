package de.fh_dortmund.beerbuddy_44.picker;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;

import java.util.Calendar;
import java.util.Date;

import lombok.Setter;

public class PickerFragmentFactory {

    public static class DatePickerFragment extends DialogFragment {
        @Setter
        private DatePickerDialog.OnDateSetListener onDateSetListener;


        private static final String MOVE_IN_DATE_KEY = "Date_KEY";

        public static DatePickerFragment newInstance(Date date, DatePickerDialog.OnDateSetListener onDateSetListener) {
            DatePickerFragment pickerFragment = new DatePickerFragment();
            pickerFragment.setOnDateSetListener(onDateSetListener);

            //Pass the date in a bundle.
            Bundle bundle = new Bundle();
            bundle.putSerializable(MOVE_IN_DATE_KEY, date);
            pickerFragment.setArguments(bundle);
            return pickerFragment;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            super.onCreateDialog(savedInstanceState);

            Date initialDate = (Date) getArguments().getSerializable(MOVE_IN_DATE_KEY);
            int[] yearMonthDay = ymdTripleFor(initialDate);
            DatePickerDialog dialog = new DatePickerDialog(getActivity(), onDateSetListener, yearMonthDay[0], yearMonthDay[1],
                    yearMonthDay[2]);
            return dialog;
        }

        private int[] ymdTripleFor(Date date) {

            Calendar cal = Calendar.getInstance();
            if (date != null) {
                cal.setTime(date);
            }
            return new int[]{cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)};
        }
    }
}