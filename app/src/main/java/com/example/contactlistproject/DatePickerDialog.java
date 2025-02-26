package com.example.contactlistproject;


import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;
import androidx.fragment.app.DialogFragment;
import java.util.Calendar;


public class DatePickerDialog extends DialogFragment
{
    Calendar selectedDate;





    public interface SaveDateListener {
        void didFinishDatePickerDialog(Calendar selectedTime);
    }

    public DatePickerDialog(Calendar selectedDate){
        this.selectedDate=selectedDate;
    }

    @Override
    public Dialog onCreateDialog( Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.select_date, null);

        CalendarView calendarView = view.findViewById(R.id.calendarView);
        Button btnSave = view.findViewById(R.id.buttonSelect);
        Button cancel=view.findViewById(R.id.buttonCancel);

        cancel.setOnClickListener(v -> {
            getDialog().dismiss();
        });

        btnSave.setOnClickListener(v -> {
            long selectedDateInMillis = calendarView.getDate();

            Calendar selectedCalendar = Calendar.getInstance();
            selectedCalendar.setTimeInMillis(selectedDateInMillis);
            saveItem(selectedDate);
        });

        builder.setView(view);
        return builder.create();

    }

    private void saveItem(Calendar selectedTime){
        SaveDateListener activity = (SaveDateListener) getActivity();
        activity.didFinishDatePickerDialog(selectedTime);
        TextView birthDay = getActivity().findViewById(R.id.textBirthday);

        birthDay.setText(android.text.format.DateFormat.format("MM/dd/yyyy", selectedTime));

        getDialog().dismiss();
    }


}