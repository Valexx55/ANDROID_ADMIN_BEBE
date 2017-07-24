package com.example.vale.adminbebe.actividades.programarpublicacion;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import com.example.vale.adminbebe.actividades.programarpublicacion.ProgramarActivity;

import java.util.Calendar;

/**
 * Created by vale on 12/06/17.
 */

public class SelectorFecha extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    private Activity activity_padre;



    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }


    public void onDateSet(DatePicker view, int year, int month, int day) {
        String fecha_formato_DD_MM_AAAA = null;
        String day_s, month_s, year_s = null;

        //obtengo el mes real
        month = month + 1;//el mes empieza en 0

        //paso a String
        month_s = String.valueOf(month);
        day_s = String.valueOf(day);
        year_s = String.valueOf(year);

        //formateo
        if (month_s.length()<2)
            month_s = '0'+month_s;

        if (day_s.length()<2)
            day_s = '0'+day_s;

        fecha_formato_DD_MM_AAAA = day_s+"/"+month_s+"/"+year_s;

        //Seteo
        ProgramarActivity pa = (ProgramarActivity)getActivity();
        pa.setFechaSeleccionada(fecha_formato_DD_MM_AAAA);
    }
}