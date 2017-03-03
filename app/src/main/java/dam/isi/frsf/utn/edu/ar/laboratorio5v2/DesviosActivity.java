package dam.isi.frsf.utn.edu.ar.laboratorio5v2;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import android.util.Log;
import android.widget.ListView;

import dam.isi.frsf.utn.edu.ar.laboratorio5v2.dao.ProyectoDAO;


public class DesviosActivity extends AppCompatActivity {

    private EditText et_desvio;
    private CheckBox cb_tareaT;
    private Button btnBuscar;
    private ListView lv_tareas;

    int desvio;
    boolean terminadas = false;

    private ProyectoDAO proyectoDAO;
    private Cursor cursor;
    private DesviosCursorAdapter dca;

    /*------------------------------------ on Create ---------------------------------------------*/
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_desvios);

        et_desvio = (EditText)findViewById(R.id.et_desviacion);
        cb_tareaT = (CheckBox) findViewById(R.id.cb__tareaTerminada);
        btnBuscar = (Button)findViewById(R.id.btn_buscar);
        lv_tareas = (ListView) findViewById(R.id.listaTareasConDesvios);
        //Esto es mas que nada es a nivel de diseño con el objetivo de crear unas lineas mas anchas entre item y item
        lv_tareas.setDividerHeight(5);

        proyectoDAO = new ProyectoDAO(DesviosActivity.this);
        proyectoDAO.open();

        //---------------- listenner Botón Buscar ---------------
        btnBuscar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                InputMethodManager inputMethodManager = (InputMethodManager)getSystemService((DesviosActivity.this).INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(et_desvio.getWindowToken(), 0);
                terminadas = cb_tareaT.isChecked();
                if(et_desvio.getText().toString().isEmpty()){   desvio = 0; }                                               //si no ingresaron nada en el desvio esperado
                else                                        {   desvio = Integer.parseInt(et_desvio.getText().toString()); }//si si ingresaron desvio
                cursor = proyectoDAO.listarDesviosPlanificacion(terminadas,desvio);
                dca = new DesviosCursorAdapter(DesviosActivity.this,cursor,proyectoDAO);
                lv_tareas.setAdapter(dca);
            }
        });//fin-listenner
    }

    /*-------------------------------------- On Resume -------------------------------------------*/
    protected void onResume() {
        super.onResume();

        Log.d("LAB05-MAIN","en resume");
        proyectoDAO = new ProyectoDAO(DesviosActivity.this);
        proyectoDAO.open();
        Log.d("LAB05-MAIN","fin resume");
    }

    /*-------------------------------------- On Pause --------------------------------------------*/
    protected void onPause() {
        super.onPause();

        Log.d("LAB05-MAIN","on pausa");
        if(cursor!=null) cursor.close();
        if(proyectoDAO!=null) proyectoDAO.close();
        Log.d("LAB05-MAIN","fin on pausa");

    }


}

