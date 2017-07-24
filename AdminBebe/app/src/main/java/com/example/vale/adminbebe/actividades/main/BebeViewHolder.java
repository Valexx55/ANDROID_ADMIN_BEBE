package com.example.vale.adminbebe.actividades.main;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.vale.adminbebe.R;
import com.example.vale.adminbebe.dto.RegistroPubicacion;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vale on 18/07/17.
 */

public class BebeViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener{

    private TextView mensaje_rec;
    private TextView fecha_rec;
    private ImageView imagen_rec;


    /**
     * Se invoca este método al realizar un click prolongado sobre un elemento de la lista
     * lo que se interpreta, en principio, como una selección del elemento
     * @param menu
     * @param v
     * @param menuInfo
     */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

        //TODO REFACTORIZAR EL CÓDIGO PARA AÚNAR LA CASUÍSTICA DE CLIC Y EL LONG-CLICK, ya que es muy similar
        RegistroPubicacion rp = (RegistroPubicacion) v.findViewById(R.id.imagen_rec).getTag();
        Log.d("MENSAJE" , rp.toString());

        AdapterBebeList.setDoble_click_procesado(true);

        if (AdapterBebeList.isEn_edicion())
        {
            Log.d("MENSAJE" , "ESTA EN EDICION");
            List<RegistroPubicacion> lrp_ed = AdapterBebeList.getRegistros_seleccionados();
            if (RegistroPubicacion.pertenece(rp, lrp_ed))
            {
                Log.d("MENSAJE" , "YA ESTABA SELECCIONADO. SE DESELECCIONA");
                //SE DESELECCIONA
                v.setAlpha(1.0f);
                //se elimina de la lista
                RegistroPubicacion.eliminarSeleccionado(rp, lrp_ed);
                //SI NOS QUEDAMOS SIN ELEMENTOS SELECCIONADOS
                if (lrp_ed.size() == 0)
                {
                    Log.d("MENSAJE" , "LA LISTA SE QUEDÓ VACÍA");
                    //SE MAPONE A FALSE EL MODO EDICIÓN
                    AdapterBebeList.setEn_edicion(false);
                    //SE BORRA EL MENÚ DE BORRAR
                    Menu menu1 = MainActivity.getMenu_bar();
                    menu1.removeItem(2);
                    //SE PONE A NULL LA LISTA para ahorrar memoria, puesto que se hace NEW al iniciar la seleción
                    AdapterBebeList.setRegistros_seleccionados(null);


                }

            } else
            {
                //no pertenece, registro nuevo seleccionado
                Log.d("MENSAJE" , "SE HA SELECCIONADO UN REGISTRO NUEVO, YA HABIENDO ALGUNO");
                //SE MARCA COMO SELECCIONADO
                v.setAlpha(0.5f);
                //SE AÑADE A LA LISTA
                lrp_ed.add(rp);

            }

            AdapterBebeList.setRegistros_seleccionados(lrp_ed);
        } else {
            //no estaba en edición
            Log.d("MENSAJE" , "NO HABÍA NINGUNO EN EDICIÓN, SE INAUGURA");

            //DIBUJAR MENÚ
            Menu menu1 = MainActivity.getMenu_bar();
            MenuItem mi = menu1.add(Menu.NONE, 2, 2, "BORRAR");
            mi.setIcon(R.drawable.ic_delete_white_24dp);
            mi.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS|MenuItem.SHOW_AS_ACTION_WITH_TEXT);

            //SE PONE EN EDICIÓN
            AdapterBebeList.setEn_edicion(true);
            //SE MARCAR VISIBLE SELECCIOANDO
            v.setAlpha(0.5f);
            //SE INICIA LA LISTA
            List<RegistroPubicacion> lrp_ed = new ArrayList<RegistroPubicacion>();
            lrp_ed.add(rp);
            AdapterBebeList.setRegistros_seleccionados(lrp_ed);

        }


        //Log.d("MENSAJE", "POS_TOCADA = " + 5);

    }

    public BebeViewHolder (View itemView) {

        super(itemView);
        itemView.setOnCreateContextMenuListener(this);
        mensaje_rec = (TextView)itemView.findViewById(R.id.texto_rec);
        fecha_rec = (TextView)itemView.findViewById(R.id.fecha_rec);
        imagen_rec = (ImageView) itemView.findViewById(R.id.imagen_rec);
    }


    public void cargarRPenHolder(RegistroPubicacion rp, Bitmap detalle) {


        try{
            mensaje_rec.setText(rp.getMensaje());
            fecha_rec.setText(rp.getFecha());
            imagen_rec.setImageBitmap(detalle);

            imagen_rec.setTag(rp);

        }catch (Exception ex)
        {
            Log.d("MENSAJE", "ERROR CARGANDO LA FFOTO en la lista", ex);
        }


    }


}
