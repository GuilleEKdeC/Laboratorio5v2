package dam.isi.frsf.utn.edu.ar.laboratorio5v2;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import dam.isi.frsf.utn.edu.ar.laboratorio5v2.dao.ProyectoDAO;

public class MainActivity extends AppCompatActivity {

    /*----------------------------- Declaración de Variables -------------------------------------*/
    private ListView lvTareas;
    private ProyectoDAO proyectoDAO;
    private Cursor cursor;
    private TareaCursorAdapter tca;

    /*-------------------------------------- On Create -------------------------------------------*/
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Crea la toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Crea el botón flotante y un listener para iniciar un ALTA TAREA si es presionado
        FloatingActionButton fb_alta = (FloatingActionButton) findViewById(R.id.fab);
        fb_alta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intActAlta = new Intent(MainActivity.this, AltaTareaActivity.class);
                intActAlta.putExtra("ID_TAREA", 0);
                startActivityForResult(intActAlta, 0);
            }
        });

        //Crea el listView con las tareas ya programadas
        lvTareas = (ListView) findViewById(R.id.listaTareas); //listaTareas es el listView que se encuentra en el content_main
        //Esto es mas que nada es a nivel de diseño con el objetivo de crear unas lineas mas anchas entre item y item
        lvTareas.setDividerHeight(5);
    }//Fin On Create

    /*-------------------------------------- On Resume -------------------------------------------*/
    protected void onResume() {
        super.onResume();

        Log.d("LAB05-MAIN", "en resume");
        proyectoDAO = new ProyectoDAO(MainActivity.this);
        proyectoDAO.open();
        cursor = proyectoDAO.listaTareas(1);
        tca = new TareaCursorAdapter(MainActivity.this, cursor, proyectoDAO);
        lvTareas.setAdapter(tca);
     //   registerForContextMenu(lvTareas);
        Log.d("LAB05-MAIN", "fin resume");
    }

    /*-------------------------------------- On Pause --------------------------------------------*/
    protected void onPause() {
        super.onPause();

        Log.d("LAB05-MAIN", "on pausa");
        if (cursor != null) cursor.close();
        if (proyectoDAO != null) proyectoDAO.close();
        Log.d("LAB05-MAIN", "fin on pausa");

    }

    /*------------------------------- On Create Options Menú -------------------------------------*/
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /*------------------------------ On Options Item Selected ------------------------------------*/
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.desviaciones) {
            Intent intDesv = new Intent(this, DesviosActivity.class);
            startActivity(intDesv);
        }
        return super.onOptionsItemSelected(item);
    }

    /*------------------------------- On Create Context Menú -------------------------------------*/
/*    public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo menuInfo){
        super.onCreateContextMenu(contextMenu,view,menuInfo);

        MenuInflater inflater = getMenuInflater();
        if(view.getId() == R.id.listaTareas){
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
            contextMenu.setHeaderTitle(lvTareas.getAdapter().getItem(info.position).toString());
            inflater.inflate(R.menu.menu_contextual_borrar,contextMenu);
            Toast.makeText(MainActivity.this, "En menu contextual", Toast.LENGTH_LONG).show();
        }
    }

    /*------------------------------- On Context Item Selected -----------------------------------*/
 /*   public boolean onContextItemSelected(MenuItem item){

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()){
            case R.id.it_aceptar:
                Toast.makeText(MainActivity.this, "Clickie ACEPTAR", Toast.LENGTH_LONG).show();
                return true;
            case R.id.it_cancelar:
                Toast.makeText(MainActivity.this, "Clickie CANCELAR", Toast.LENGTH_LONG).show();
                return true;
            default:
                Toast.makeText(MainActivity.this, "Clickie en otro lado", Toast.LENGTH_LONG).show();
                return super.onContextItemSelected(item);
        }


    }

    /*---------------------------------- on Activity Result --------------------------------------*/
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        tca.handlerRefresh.sendEmptyMessage(1);
    }
}