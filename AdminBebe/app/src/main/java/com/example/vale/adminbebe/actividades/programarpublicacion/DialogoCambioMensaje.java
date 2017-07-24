package com.example.vale.adminbebe.actividades.programarpublicacion;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.EditText;
import android.widget.TextView;

import com.example.vale.adminbebe.R;
import com.example.vale.adminbebe.actividades.programarpublicacion.ProgramarActivity;

/**
 * Created by vale on 17/07/17.
 */

public class DialogoCambioMensaje extends Dialog{

    private ProgramarActivity pa;//guardo una referecne del padre para acceder a su botón

    public DialogoCambioMensaje(@NonNull Context context) {
        super(context);
        pa = (ProgramarActivity)context;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();


        TextView tv = (TextView)pa.findViewById(R.id.mensaje);
        tv.setText(((EditText)findViewById(R.id.nuevo_mensaje)).getText().toString());
    }
}
