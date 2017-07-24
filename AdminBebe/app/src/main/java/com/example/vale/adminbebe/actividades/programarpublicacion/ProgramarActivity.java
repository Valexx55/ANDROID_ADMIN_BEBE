package com.example.vale.adminbebe.actividades.programarpublicacion;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.vale.adminbebe.R;
import com.example.vale.adminbebe.actividades.main.MainActivity;
import com.example.vale.adminbebe.dto.RegistroPubicacion;

import java.io.IOException;

public class ProgramarActivity extends AppCompatActivity {



    private String last_uri;
    private DialogoCambioMensaje dialogo_cambio;
    private String fecha_edicion;

    public String getFecha_edicion() {
        return fecha_edicion;
    }


    /**
     *
     * Método privado que setea la vista inicial y asocia el Listener al FAB
     */
    private void initActivity ()
    {
        Button carpeta = (Button)findViewById(R.id.icono_carpeta);
        Button fecha = (Button)findViewById(R.id.icono_fecha);
        Button mensaje = (Button)findViewById(R.id.icono_texto);
        TextView plus = (TextView)findViewById(R.id.icono_plus);

        Typeface face= Typeface.createFromAsset(getAssets(), "iconosmaterial.ttf");

        carpeta.setTypeface(face);
        fecha.setTypeface(face);
        plus.setTypeface(face);
        mensaje.setTypeface(face);

        plus.setText(R.string.plus_ico);
        fecha.setText(R.string.fecha_ico);
        carpeta.setText(R.string.carpeta_ico);
        mensaje.setText(R.string.mensa_ico);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new ListenerFAB(this));
    }

    private void setImagenSeleccionada (Uri uri_foto) throws IOException {
        try
        {
            last_uri =  uri_foto.toString();
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri_foto);

            LinearLayout cuadro_foto = (LinearLayout) findViewById(R.id.cuadro_foto);
            ImageView imageView = new ImageView(this);
            imageView.setImageBitmap(bitmap);

            cuadro_foto.removeAllViewsInLayout();
            cuadro_foto.addView(imageView);

            imageView.setScaleType(ImageView.ScaleType.FIT_XY);


        }catch (Exception e)
        {
            Log.e("MENSAJE", "ERROR AL CARGAR LA FOTO", e);
            throw e;
        }

    }

    private void setMensajeSeleccionado (String mensaje)
    {
        TextView tvmensaje = (TextView)findViewById(R.id.mensaje);
        tvmensaje.setText(mensaje);
    }

    private void initLayout (RegistroPubicacion rp)
    {

        try {

            setImagenSeleccionada(Uri.parse(rp.getUri()));
            setFechaSeleccionada(rp.getFecha());
            setMensajeSeleccionado(rp.getMensaje());

        }catch (Exception e)
        {
            Log.e("MENSAJE", "ERROR", e);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_programar);

        initActivity();
        Bundle b = getIntent().getExtras();
        if (b != null)
        {
            Log.d("MENSAJE", "viene con tema");
            RegistroPubicacion rp = (RegistroPubicacion)b.get("rp");
            initLayout (rp);
            fecha_edicion = rp.getFecha();//me quedo con la fecha de edición, ya ue si cambia al guardar, habrá que borrar el registro anterior.


        } else {
            Log.d("MENSAJE", "viene sin tema");
            fecha_edicion = null;
        }


    }

    public String getLast_uri ()
    {
        return last_uri;
    }

    public void mostarCalendario(View v) {
        DialogFragment fragment_calendario = new SelectorFecha();
        fragment_calendario.show(getSupportFragmentManager(), "timePicker");
    }

    public void mensaje_editado (View v)
    {
        setMensajeSeleccionado(((EditText)dialogo_cambio.findViewById(R.id.nuevo_mensaje)).getText().toString());
        dialogo_cambio.dismiss();
        Log.d("MENSAJE", "MENSAJE EDITADO");
    }

    public void editarMensaje (View v)
    {
        dialogo_cambio = new DialogoCambioMensaje(this);
        dialogo_cambio.requestWindowFeature(Window.FEATURE_NO_TITLE); //quito el mensaje
        dialogo_cambio.setContentView(R.layout.dialogo_mensaje);
        dialogo_cambio.show();

    }

    public void setFechaSeleccionada (String fecha_string)
    {
        TextView tv = (TextView)findViewById(R.id.fecha);
        tv.setText(fecha_string);
    }

    public void seleccionarFoto (View v)
    {
        Intent intentpidefoto = new Intent ();
        intentpidefoto.setAction(Intent.ACTION_PICK);
        intentpidefoto.setType("image/*");//TIPO MIME

        startActivityForResult(intentpidefoto, 30);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            //El usuario ha seleccionado una foto
            Uri uri = data.getData();

            try {
                //last_uri =  uri.toString(); //ruta de la foto, m la guartdo en la propiedad
                setImagenSeleccionada(uri);


            } catch (IOException e) {
                Log.e("ERROR", "Error al manipular la foto elegida", e);
            }

        } else {
            Log.e("ERROR", "El usuario le dio a salir ");
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        Menu menu1 = MainActivity.getMenu_bar();//elimino
        menu1.removeItem(2);//el icono de borrar para que no salga en caso de que estuviera al volver a la actividad princiapal
    }
}
