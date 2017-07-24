package com.example.vale.adminbebe.actividades.main;

import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by vale on 24/07/17.
 */

public class ListenerDialog implements DialogInterface.OnClickListener {

    private Context context;

    public ListenerDialog (Context context)
    {
        this.context = context;
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {

        if (DialogInterface.BUTTON_POSITIVE == i)
        {
            //ha contestado sí, que quiere borrar
            MainActivity ma = (MainActivity)context;
            ma.borrarSeleccionados();

        } else {
            //ha contestado que no
            dialogInterface.dismiss();
        }


    }
}
