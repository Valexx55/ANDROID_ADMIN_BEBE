package com.example.vale.adminbebe.actividades.main;

import android.Manifest;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.vale.adminbebe.util.PreferencesAdmin;
import com.example.vale.adminbebe.R;
import com.example.vale.adminbebe.dto.RegistroPubicacion;
import com.example.vale.adminbebe.actividades.programarpublicacion.ProgramarActivity;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private RecyclerView lista_items_rp_rec;
    private boolean creadaonce;


    private static Menu menu_bar;


    public static Menu getMenu_bar() {
        return menu_bar;
    }

    public static void setMenu_bar(Menu menu_bar) {
        MainActivity.menu_bar = menu_bar;
    }


    private void actualizarListado ()
    {
        Map<String, RegistroPubicacion> mp = PreferencesAdmin.obtenerTodosLosRegistros(this);
        List<RegistroPubicacion> lrp = PreferencesAdmin.obtenerTodosLosRegistrosEnLista (mp);

        //TODO ORDENAR LISTA
        Collections.sort(lrp);
        AdapterBebeList adapterBebeList = new AdapterBebeList(lrp, this);
        lista_items_rp_rec.setAdapter(adapterBebeList);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //TODO LISTAR TODOS LAS PROGRAMACIONES GUARDADAS
        setContentView(R.layout.activity_main);



        lista_items_rp_rec = (RecyclerView) findViewById(R.id.lista_rec_view);
        actualizarListado();
        lista_items_rp_rec.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        lista_items_rp_rec.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));


        /*lista_items_rp_rec.setLongClickable(true);
        registerForContextMenu(lista_items_rp_rec);*/

        creadaonce = true;
    }

   /* @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        // MenuItem mi1 = menu.add(Menu.NONE, 1, 1, "INSERTAR");
        MenuItem mi2 = menu.add(Menu.NONE, 2, 2, "BORRAR");
        //MenuItem mi3 = menu.add(Menu.NONE, 3, 3, "MODIFICAR");
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
        int position = info.position;

        Log.d("MENSAJE", "POS_TOCADA = " + position);

    }*/

    @Override
    protected void onResume() {
        super.onResume();

        if (creadaonce) //así me aseugro que al menos se ha llamado a oncreat y se ha hecho setcontentview una vez! si no, daría siempre null la vista recview
        {
            actualizarListado();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//pido permisos,
        creadaonce = false;
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 107);


    }

    //162x180 tamaño real
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu, menu);//Para que se reprsente el submenú superior
        menu_bar = menu;

        return true;
    }

    private Map<String, RegistroPubicacion> obtenerMapaNuevo ()
    {
        Map<String, RegistroPubicacion> mp = null;
            List<RegistroPubicacion> lrp_sel = AdapterBebeList.getRegistros_seleccionados();
            mp = PreferencesAdmin.obtenerTodosLosRegistros(this);
            Iterator<RegistroPubicacion> itl = lrp_sel.iterator();
            RegistroPubicacion rpaux = null;

            while (itl.hasNext()) //recorro la lista de elemmentos seleccionados y los elimino del hashmap
            {
                rpaux = itl.next();
                mp.remove(rpaux.getFecha());
            }
        return mp;
    }

    private void informarTamanioImangenRec ()
    {
         /*ImageView imagen_rec = (ImageView) findViewById(R.id.imagen_rec);

        Log.d ("MENSAJE ", "6ANCHOx2 " + imagen_rec.getMaxWidth() +  " ALTOx2 " + imagen_rec.getMaxHeight());
        Log.d ("MENSAJE ", "6ANCHO2 " + imagen_rec.getWidth() +  " ALTO2 " + imagen_rec.getHeight());
        Log.d ("MENSAJE ", "6ANCHO2.1 " + imagen_rec.getMeasuredWidth() +  " ALTO2.1 " + imagen_rec.getMeasuredHeight());
*/
    }

    public void borrarSeleccionados ()
    {
        Map<String, RegistroPubicacion> mp = obtenerMapaNuevo();//obtengo el mapa con la inteersseción delos registros iniciales menos los seleccionados
        PreferencesAdmin.grabarNuevaLista (this, mp);//grabo la nueva lista
        actualizarListado();//y hago que se dibuje

        Menu menu1 = MainActivity.getMenu_bar();//elimino
        menu1.removeItem(2);//el icono de borrar
    }

    private void mostrarDialogoConfirmacion ()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        ListenerDialog ld = new ListenerDialog(this);

        builder.setTitle("¿Está seguro?");
        builder.setMessage("Si elimina las publiaciones seleccionadas se borrarán de su móvil y del servidor");

        builder.setPositiveButton("PROCEDER", ld);
        builder.setNegativeButton("CANCELAR", ld);

        AlertDialog ad = builder.create();
        ad.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Log.d("MENSAJE", "Opción menú seleccionada " + item.getItemId());

        switch (item.getItemId())
        {
            case R.id.boton_ajustes:
                //LANZAR INTENT NUEVA PROGRAMACIÓN
                Intent i_programar = new Intent(this, ProgramarActivity.class);
                startActivity(i_programar);
                break;
            case 2:
                //BORRAR ITEMS SELECCIONADOS
               mostrarDialogoConfirmacion ();




                break;
            default: Log.e("MENSAJE", "Opción menú seleccionada desconocida");
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        //para un efecto totalmente "whatsapp" si está seleccionado registros
        //los deseleccionamos y ajustamos el menú
        if (AdapterBebeList.isEn_edicion())
        {
            AdapterBebeList.setEn_edicion(false);
            //SE BORRA EL MENÚ DE BORRAR
            Menu menu1 = MainActivity.getMenu_bar();
            menu1.removeItem(2);
            //SE PONE A NULL LA LISTA para ahorrar memoria, puesto que se hace NEW al iniciar la seleción
            AdapterBebeList.setRegistros_seleccionados(null);
            actualizarListado();//para que se quiten los marcados

        }else {
            super.onBackPressed();
        }
    }
}
