package com.example.vale.adminbebe.actividades.main;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;

import com.example.vale.adminbebe.R;
import com.example.vale.adminbebe.actividades.programarpublicacion.ProgramarActivity;
import com.example.vale.adminbebe.dto.RegistroPubicacion;

import java.util.List;

/**
 * Created by vale on 21/07/17.
 */

public class EscuchaRecView implements View.OnClickListener {

    private Context context;

    public EscuchaRecView (Context context)
    {
        this.context = context;
    }

    /**
     * Aquí se procesa un click sencillo (no prolongado) sobre un item de la lista
     * @param view
     */
    @Override
    public void onClick(View view) {

        RegistroPubicacion rp = (RegistroPubicacion) view.findViewById(R.id.imagen_rec).getTag();

        if (!AdapterBebeList.isDoble_click_procesado())
        {

            if (!AdapterBebeList.isEn_edicion()) {
                Log.d("MENSAJE", "No hay elementos seleccccionados y se ha pulsado");
                //Lanzamos la actividad (re) programar con la información del registro pulsado"
                Intent intent = new Intent(context, ProgramarActivity.class);
                intent.putExtra("rp", rp);
                context.startActivity(intent);
            }
            else {
                Log.d("MENSAJE", "Está en edición");
                List<RegistroPubicacion> lrp = AdapterBebeList.getRegistros_seleccionados();
                if (RegistroPubicacion.pertenece(rp, lrp)) {
                    Log.d("MENSAJE", "El elemento ya estaba seleccionado");
                    Log.d("MENSAJE", "YA ESTABA SELECCIONADO. SE DESELECCIONA");
                    //SE DESELECCIONA
                    view.setAlpha(1.0f);
                    //se elimina de la lista
                    RegistroPubicacion.eliminarSeleccionado(rp, lrp);
                    //SI NOS QUEDAMOS SIN ELEMENTOS SELECCIONADOS
                    if (lrp.size() == 0) {
                        Log.d("MENSAJE", "LA LISTA SE QUEDÓ VACÍA");
                        //SE MAPONE A FALSE EL MODO EDICIÓN
                        AdapterBebeList.setEn_edicion(false);
                        //SE BORRA EL MENÚ DE BORRAR
                        Menu menu1 = MainActivity.getMenu_bar();
                        menu1.removeItem(2);
                        //SE PONE A NULL LA LISTA para ahorrar memoria, puesto que se hace NEW al iniciar la seleción
                        AdapterBebeList.setRegistros_seleccionados(null);


                    }
                } else {
                    //SELECCIONO
                    view.setAlpha(0.5f);
                    //add A LA LISTA
                    lrp.add(rp);

                }
            }
        } else AdapterBebeList.setDoble_click_procesado(false);
    }
}
