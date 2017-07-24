package com.example.vale.adminbebe.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.vale.adminbebe.dto.RegistroPubicacion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by vale on 13/06/17.
 */

public class PreferencesAdmin {


    /**
     * EL FORMATO DE REGISTRO DE PREFERENCIAS ES LA FECHA COMO CLAVE
     * Y EL STRING FORMADO POR LA URI FOTO "+" MENSAJE COMO VALOR
     */

    public final static String PREFERENCES_USUARIO = "lista_publicaciones";//nombre del fichero de preferences perfil.xml será


    public static void grabarNuevaLista (Context context, Map<String, RegistroPubicacion> nuevo_mapa)
    {
        //TODO
        SharedPreferences sp = null;
        SharedPreferences.Editor editor = null;
        Map<String, RegistroPubicacion> mapa_viejo = PreferencesAdmin.obtenerTodosLosRegistros(context);
        Iterator<String> its = mapa_viejo.keySet().iterator();
        RegistroPubicacion rpaux = null;

        sp = context.getSharedPreferences(PREFERENCES_USUARIO, Context.MODE_PRIVATE);
        editor = sp.edit();
        String fecha_clave_actual = null;

        while (its.hasNext())
        {
            fecha_clave_actual = its.next();
            rpaux = nuevo_mapa.get(fecha_clave_actual);
            if (rpaux == null)
            {
                //el registro debe eleminarse del fichero, puesto que no está en la nueva colección
                editor.remove(fecha_clave_actual);
            }

        }

        editor.commit();

    }
    public static void guardar_publicacion(Context context, RegistroPubicacion registroPubicacion) {
        SharedPreferences sp = null;
        SharedPreferences.Editor editor = null;

            sp = context.getSharedPreferences(PREFERENCES_USUARIO, Context.MODE_PRIVATE);
            editor = sp.edit();
            editor.putString(registroPubicacion.getFecha(), registroPubicacion.getUri() + RegistroPubicacion.SEPARADOR_RUTA_MENSAJE + registroPubicacion.getMensaje());
            editor.commit();
    }

    public static void borrar_pubicacion(Context context, String fecha_clave) {
        SharedPreferences sp = null;
        SharedPreferences.Editor editor = null;

        sp = context.getSharedPreferences(PREFERENCES_USUARIO, Context.MODE_PRIVATE);
        editor = sp.edit();
        editor.remove(fecha_clave);
        editor.commit();
    }

    private static void mostrarMapa (Map<String,String> mp)
    {
        Iterator<String> it = mp.keySet().iterator();
        String record_aux = null;

        while (it.hasNext())
        {
            record_aux = mp.get(it.next());
            Log.d("REGISTRO", record_aux);
        }
    }

    /**
     *
     * @param mp el mapa con cada registro del fichero de preferencias, en el formato (key) fecha (vaulue) URI + MENSAJE
     * @return el mapa de Objetos de RegistroPrubicación
     */
    public static Map<String,RegistroPubicacion> deserializaPreferecesMap (Map<String,String> mp)
    {
        Map<String,RegistroPubicacion> mrp = null;
        Set<String> conjunto_claves = null;
        Iterator<String> iterador = null;
        String str_aux = null;
        String fecha_aux = null;
        String uri_aux = null;
        String mensa_aux = null;
        int indice_separador = -1;
        RegistroPubicacion rp_aux = null;

            mrp = new HashMap<String, RegistroPubicacion>();
            conjunto_claves = mp.keySet();
            iterador = conjunto_claves.iterator();

            while (iterador.hasNext())
            {
                fecha_aux = iterador.next();
                str_aux = mp.get(fecha_aux);
                indice_separador = str_aux.indexOf(RegistroPubicacion.SEPARADOR_RUTA_MENSAJE);
                uri_aux = str_aux.substring(0, indice_separador);//NO HAGO SPLIT POR SI ACA EL MENSAJE TRA EL SÍOBOLO +
                mensa_aux = str_aux.substring(indice_separador+1, str_aux.length());
                rp_aux = new RegistroPubicacion(fecha_aux, uri_aux, mensa_aux);
                mrp.put(fecha_aux, rp_aux);
            }

        return  mrp;
    }

    public static Map<String,RegistroPubicacion> obtenerTodosLosRegistros (Context context)
    {
        Map<String,RegistroPubicacion> mrp = null;
        Map<String,String> mp = null;

            SharedPreferences sp = context.getSharedPreferences(PREFERENCES_USUARIO, Context.MODE_PRIVATE);
            mp = (Map<String,String>)sp.getAll();
            mrp = deserializaPreferecesMap(mp);

        return mrp;


    }

    public static List<RegistroPubicacion> obtenerTodosLosRegistrosEnLista (Map<String,RegistroPubicacion> mrp)
    {
        List<RegistroPubicacion> lrp = null;

            lrp = new ArrayList<RegistroPubicacion>(mrp.size());
            Iterator<String> it = mrp.keySet().iterator();
            RegistroPubicacion rp_aux = null;

            while (it.hasNext())
            {
                rp_aux = mrp.get(it.next());
                lrp.add(rp_aux);
            }


        return lrp;


    }

}
