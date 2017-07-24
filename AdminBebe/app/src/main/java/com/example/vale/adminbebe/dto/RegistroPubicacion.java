package com.example.vale.adminbebe.dto;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.List;

/**
 * Created by vale on 13/06/17.
 */

public class RegistroPubicacion implements Comparable<RegistroPubicacion>, Serializable {

    private String fecha;
    private String uri;
    private String mensaje;

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public final static String SEPARADOR_RUTA_MENSAJE = "+"; //CARACTER DE CONTROL PARA SEPARAR A FECHA Y LA RUTA Y ALAMCENAR ASÍ CAA REGISTRO COMO PREFERENCIA


    public RegistroPubicacion ()
    {
        super();
    }

    public RegistroPubicacion(String fecha, String uri, String mensaje) {
        this.fecha = fecha;
        this.uri = uri;
        this.mensaje = mensaje;
    }

    public boolean isValido ()
    {
        boolean esvalido = false;

        esvalido = this.fecha != null && this.mensaje != null && this.mensaje.length() > 0 && this.uri != null && this.uri.length() > 0 && this.fecha.length() <= 10;

        return esvalido;

    }


    /*con esto, obtengo una versión serializable de lo que será un registro clave-VALOR en el fichero
    de preferences
     */
    @Override
    public String toString() {
        return fecha + SEPARADOR_RUTA_MENSAJE + uri + SEPARADOR_RUTA_MENSAJE + mensaje;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    @Override
    public int compareTo(@NonNull RegistroPubicacion registroPubicacion) {
        int dev = 0;
        String fecha_this = null;
        String fecha_other = null;
        String fecha_this_dd_mm_aa [] = new String[3];
        String fecha_other_dd_mm_aa [] = new String[3];

            fecha_this = this.fecha;//OBTENGO LAS FECHAS, CRITERIO DE ORDENACIÓN
            fecha_other = registroPubicacion.fecha;

            fecha_this_dd_mm_aa = fecha_this.split("/");
            fecha_other_dd_mm_aa = fecha_other.split("/");

            fecha_this = fecha_this_dd_mm_aa[2] + fecha_this_dd_mm_aa[1] + fecha_this_dd_mm_aa[0];
            fecha_other = fecha_other_dd_mm_aa[2] + fecha_other_dd_mm_aa[1] + fecha_other_dd_mm_aa[0];

            dev = fecha_this.compareTo(fecha_other);

        return dev;
    }

    /**
     *
     * @param rp
     * @param lrp la lista no está vacía
     * @return
     */
    public static boolean pertenece (RegistroPubicacion rp, @NonNull List<RegistroPubicacion> lrp)
    {
        boolean encontrado = false;
        int tamanio = lrp.size();
        int pos_actual = 0;
        RegistroPubicacion rp_aux = null;

            do {

                rp_aux = lrp.get(pos_actual);
                encontrado = rp_aux.getFecha().equals(rp.getFecha());
                pos_actual = pos_actual+1;

            }while ((!encontrado) && (pos_actual<tamanio));


        return encontrado;
    }

    public static void eliminarSeleccionado (RegistroPubicacion rp, List<RegistroPubicacion> lrp)
    {
        boolean encontrado = false;
        int tamanio = lrp.size();
        int pos_actual = 0;
        RegistroPubicacion rp_aux = null;

        do {

            rp_aux = lrp.get(pos_actual);
            encontrado = rp_aux.getFecha().equals(rp.getFecha());
            if (encontrado)
            {
                lrp.remove(pos_actual);
            }
            pos_actual = pos_actual+1;

        }while ((!encontrado) && (pos_actual<tamanio));

    }
}
