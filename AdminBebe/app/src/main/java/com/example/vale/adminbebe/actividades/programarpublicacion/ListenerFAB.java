package com.example.vale.adminbebe.actividades.programarpublicacion;

import android.content.Context;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vale.adminbebe.util.PreferencesAdmin;
import com.example.vale.adminbebe.R;
import com.example.vale.adminbebe.dto.RegistroPubicacion;

/**
 * Created by vale on 13/06/17.
 */

public class ListenerFAB implements View.OnClickListener {

    private Context context;

    private static final String MENSAJE_OK = "FECHA PROGRMADA";

    public ListenerFAB (Context context)
    {
        this.context = context; //ventana padre programr activity
    }

    private void mostrarSnack (View v, String fecha)
    {
        Snackbar sb = Snackbar.make( v, MENSAJE_OK, Snackbar.LENGTH_SHORT);
        sb.setAction(fecha, this);
        sb.setActionTextColor(Color.YELLOW);
        sb.show();
    }

    private void mostrarErrorDatos ()
    {
        Toast toast = Toast.makeText(context, "¡SELECCIONE FOTO FECHA y MENSAJE!", Toast.LENGTH_LONG);
        toast.show();
    }

    @Override
    public void onClick(View view) {
        //Cuando el usuaroi clicka el botón
        //se guarda la publicación

        //Se obtiene los valores de ProgramarActivity
        if (view instanceof FloatingActionButton) {
            ProgramarActivity pa = (ProgramarActivity) context;
            String uri = pa.getLast_uri();
            String fecha = (String) ((TextView) pa.findViewById(R.id.fecha)).getText();
            String mensaje = ((TextView) pa.findViewById(R.id.mensaje)).getText().toString();

            //Y se almacenan los registros
            RegistroPubicacion rp = new RegistroPubicacion(fecha, uri, mensaje);

            if (rp.isValido()) {

                String fecha_ed = pa.getFecha_edicion();//si es
                if (null != fecha_ed)
                { //viene de una edición
                    Log.d("MENSAJE", "Viene de cambio de fecha");
                    if (!fecha_ed.equals(fecha))
                    {
                        //y ha modificado la fecha, toca borrar
                        Log.d("MENSAJE", "Ha modificado la fecha en la edición. Borrando . . .");
                        PreferencesAdmin.borrar_pubicacion(context, pa.getFecha_edicion());
                    }

                }
                //venga o nno de editar, hay que guardar y/o actuaizar
                Log.d("MENSAJE", "Guardo y/o actualizo Registro Publicación");
                PreferencesAdmin.guardar_publicacion(context, rp);

                mostrarSnack(view, fecha);
            }
            else {
                mostrarErrorDatos();
            }
        }
    }
}
