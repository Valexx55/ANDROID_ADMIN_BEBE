package com.example.vale.adminbebe.actividades.main;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.widget.RecyclerView.Adapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.vale.adminbebe.R;
import com.example.vale.adminbebe.dto.RegistroPubicacion;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by vale on 23/06/16.
 */
public class AdapterBebeList extends Adapter<BebeViewHolder> {


    private List<RegistroPubicacion> datos;
    private Bitmap [] array_bitmpas;
    private static Context actividad_padre;
    private EscuchaRecView escuchaRecView;
    private static List<RegistroPubicacion> registros_seleccionados;
    private static boolean en_edicion;
    private static boolean doble_click_procesado;


    public static boolean isDoble_click_procesado() {
        return doble_click_procesado;
    }

    public static void setDoble_click_procesado(boolean doble_click_procesado) {
        AdapterBebeList.doble_click_procesado = doble_click_procesado;
    }

    public static Context getActividad_padre() {
        return actividad_padre;
    }

    public static void setActividad_padre(Context actividad_padre) {
        AdapterBebeList.actividad_padre = actividad_padre;
    }

    public static boolean isEn_edicion() {
        return en_edicion;
    }

    public static void setEn_edicion(boolean en_edicion) {
        AdapterBebeList.en_edicion = en_edicion;
    }



    public static List<RegistroPubicacion> getRegistros_seleccionados() {
        return registros_seleccionados;
    }

    public static void setRegistros_seleccionados(List<RegistroPubicacion> registros_seleccionados_n) {
        registros_seleccionados = registros_seleccionados_n;
    }




    //Creo la vista, con el Holder dentro
    @Override
    public BebeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        BebeViewHolder bebeViewHolder = null;

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View itemView = inflater.inflate(R.layout.layout_bebe_item, parent, false);

        itemView.setOnClickListener(this.escuchaRecView);

        bebeViewHolder = new BebeViewHolder(itemView);

        return bebeViewHolder;
    }

    //Al holder, le meto la info de libro que toca
    @Override
    public void onBindViewHolder(BebeViewHolder holder, int position) {

        RegistroPubicacion rp = datos.get(position);
        holder.cargarRPenHolder(rp, array_bitmpas[position]);

    }

    @Override
    public int getItemCount() {
        return datos.size();
    }


    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;if (height > reqHeight || width > reqWidth) {final int halfHeight = height / 2;
            final int halfWidth = width / 2;// Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }return inSampleSize;
    }


    /**
     * este método permite obtener la imagen escalada paa un tamaño previo calculado
     * con lo cual se disminuye a resolución y se ahorra memoria
     * @param uri_foto
     * @return
     * @throws IOException
     */
    private Bitmap obtenerBitMapEscalado (String uri_foto) throws IOException {
        Bitmap bdev = null;
        BitmapFactory.Options options = null;
        InputStream input = null;

        try {


            options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;//leo sin LEER!, sólo cargo las propiedades de la foto
            input = actividad_padre.getContentResolver().openInputStream(Uri.parse(uri_foto));

            BitmapFactory.decodeStream(input, null, options);//sólo cargo las propiedades de la foto, así evitas un outof memory en cualquier caso
            input.close();

            options.inSampleSize = calculateInSampleSize(options, 162, 180);//una vez obtenidas, calculo el tamaño necesitoo realmente, en este caso sale 4, como que con 1/4 parte tengo bastatne para llenar el ancho por el ato
            options.inJustDecodeBounds = false;//ahora digo que leo, pero de Verdad, con la proporción seteada (insamplesize)
            input = actividad_padre.getContentResolver().openInputStream(Uri.parse(uri_foto)); //

            bdev = BitmapFactory.decodeStream(input, null, options);//y ya sí cargo la foto con la resolución requerida
        }
        catch (Exception e)
        {
            Log.e("MENSAJE" , "ERROR", e);
            throw  e;
        }

        return bdev;
    }


    public AdapterBebeList(List<RegistroPubicacion> lista_rp, Context actividad_padre)
    {
        try {
                this.datos = lista_rp;
                this.actividad_padre = actividad_padre;
                this.escuchaRecView = new EscuchaRecView(actividad_padre);
                AdapterBebeList.registros_seleccionados = null;
                AdapterBebeList.en_edicion = false;
                AdapterBebeList.doble_click_procesado = false;

            //creo un array de bitmapas para evitar que con el reciclado de cada vista, se cree unnuevo bitmap en memoria
            //así, cuando e llama a onBidnViewHolder, ya se tiene en este array, el bitmap que reprsenta la uri de cada registro
                this.array_bitmpas = new Bitmap[lista_rp.size()];
                Bitmap bitmap_aux2 = null;
                int pos = 0;

                for (RegistroPubicacion rp : lista_rp)
                {

                        bitmap_aux2 = obtenerBitMapEscalado(rp.getUri());//además se hace de forma óptima
                        array_bitmpas[pos] = bitmap_aux2;
                        pos = pos+1;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
    }
}
