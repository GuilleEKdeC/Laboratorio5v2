package dam.isi.frsf.utn.edu.ar.laboratorio5v2;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import dam.isi.frsf.utn.edu.ar.laboratorio5v2.dao.ProyectoDAO;
import dam.isi.frsf.utn.edu.ar.laboratorio5v2.modelo.Prioridad;
import dam.isi.frsf.utn.edu.ar.laboratorio5v2.modelo.Proyecto;
import dam.isi.frsf.utn.edu.ar.laboratorio5v2.modelo.Tarea;
import dam.isi.frsf.utn.edu.ar.laboratorio5v2.modelo.Usuario;

import android.widget.SimpleCursorAdapter;

public class AltaTareaActivity extends AppCompatActivity {

   //private ProyectoDAO myDao;
    Integer idTarea, i_minutosTrabajados;

    EditText et_descripcion;
    EditText et_horasEstimadas;
    SeekBar sb_prioridad;
    TextView tv_prioridad;
    Spinner sp_responsable;
    Button btnGuardar;
    Button btnCancelar;

    String s_descripcion, s_horasE, s_responsable;
    Integer i_prioridad;

    String[] listaResponsables;

    Boolean bDesc, bHoras, finalizada;

    Intent intAlta;
    ProyectoDAO myDao;
    Cursor cursor;

    /*------------------------------------ on Create ---------------------------------------------*/
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alta_tarea);

        // Manejo del Intent
        intAlta = getIntent();
        idTarea = intAlta.getExtras().getInt("ID_TAREA");
        // Fin manejo del Intent

        et_descripcion = (EditText) findViewById(R.id.et_descripcion_alta);
        et_horasEstimadas = (EditText) findViewById(R.id.et_horasEstimadas_alta);
        sb_prioridad = (SeekBar) findViewById(R.id.sb_prioridad_alta);
        tv_prioridad = (TextView) findViewById(R.id.tv_prioridad_alta);
        sp_responsable = (Spinner) findViewById(R.id.sp_responsable_alta);
        btnGuardar = (Button) findViewById(R.id.btnGuardar_alta);
        btnCancelar = (Button)findViewById(R.id.btnCancelar_alta);

        s_descripcion = null;
        s_horasE = null;
        i_minutosTrabajados = 0;
        i_prioridad = 2; //por defecto empieza en "Media"
        s_responsable = null;

        bDesc = false;
        bHoras = false;
        finalizada = false;

        myDao = new ProyectoDAO(AltaTareaActivity.this);
        myDao.open();

        // Compruebo si es edición y cargo los datos en tal caso
        if(idTarea != 0){
            final Tarea t = myDao.getTarea(idTarea);
            et_descripcion.setText(t.getDescripcion());
            et_horasEstimadas.setText(String.valueOf(t.getHorasEstimadas()));
            int p = t.getPrioridad().getId();
            switch (p){
               case 1:{  p = 3; break; }
               case 2:{  p = 2; break; }
               case 3:{  p = 1; break; }
               case 4:{  p = 0; break; }
               default:{ p = 0; break; }
            }
            sb_prioridad.setProgress(p);
            tv_prioridad.setText(t.getPrioridad().getPrioridad());
            sp_responsable.post(new Runnable() {
                public void run() {
                    sp_responsable.setSelection(t.getResponsable().getId()-1);
                }
            });
            finalizada = t.getFinalizada();
            i_minutosTrabajados = t.getMinutosTrabajados();
        }

        //------------------------ Manejo del Seekbar (Prioridad Seleccionada)--------------------//
        sb_prioridad.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
            {
                //la Seekbar siempre empieza en cero, si queremos que el valor mínimo sea otro podemos modificarlo
                i_prioridad = progress+1;
                switch(i_prioridad) {
                    case 1: {
                        tv_prioridad.setText("Baja");
                        i_prioridad = 4;
                        break;
                    }
                    case 2: {
                        tv_prioridad.setText("Media");
                        i_prioridad = 3;
                        break;
                    }
                    case 3: {
                        tv_prioridad.setText("Alta");
                        i_prioridad = 2;
                        break;
                    }
                    case 4:{
                        tv_prioridad.setText("Urgente");
                        i_prioridad = 1;
                        break;
                    }
                    default:
                        break;
                }
            }

             public void onStartTrackingTouch(SeekBar arg0){
                 i_prioridad = 2;
            }

             public void onStopTrackingTouch(SeekBar arg0){
            }
        });
        //-------------------------------- Fin del Seekbar ---------------------------------------//

        //--------------------------Manejo Spinner (Usuario seleccionado)-------------------------//
        /*SimpleCursorAdapter: Los parámetros necesarios son:
         - contexto de nuestra aplicación,
         - un layout con el diseño básico que queremos repetir,
         - un objeto Cursor con una consulta a nuestra base de datos,
         - una lista con los nombres de los registros que queremos visualizar en cada layout,
         - una lista con los id de recurso de los elementos del layout donde queremos visualizar estos datos y finalmente
         - un campo de opciones.
         */
        //cursor = myDao.getCursorUsuarios();
        //SimpleCursorAdapter adapter= new SimpleCursorAdapter(this,android.R.layout.simple_spinner_item,cursor, new String[] {"USUARIO"}, new int[] {android.R.id.text1});

        // Setea el Spinner con los usuarios posibles, cargados de la bdd y pasados a un array
        listaResponsables = myDao.getListaUsuarios();
        ArrayAdapter adaptadorUsuarios = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, listaResponsables);
        sp_responsable.setAdapter(adaptadorUsuarios);

        // Listener Spinner-------------------------------------------------------------------------
        sp_responsable.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                //Guardamos en una variable el Responsable seleccionado
                s_responsable = (String) sp_responsable.getItemAtPosition(position);
                sp_responsable.clearFocus();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {  }

        });// Fin Listener
        //-------------------------------------Fin del Spinner------------------------------------//

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0)
            {
                //---- Extraemos los datos del campo "Descripción"
                s_descripcion = et_descripcion.getText().toString();
                if(s_descripcion.equals("")){ // si no ha ingresado descripción del puesto
                    Toast.makeText(getBaseContext(),R.string.warningDesc, Toast.LENGTH_SHORT).show();
                    et_descripcion.requestFocus();
                }
                else bDesc = true;

                //---- Extraemos los datos del campo "Horas Estimadas"
                s_horasE = et_horasEstimadas.getText().toString();
                if(s_horasE.equals("")){ // si no ha ingresado la cantidad de horas por trabajar
                    Toast.makeText(getBaseContext(),R.string.warningHoras, Toast.LENGTH_SHORT).show();
                    et_horasEstimadas.requestFocus();
                }
                else bHoras = true;

                if(bDesc && bHoras) {
                    // creo mi nueva instancia de Tarea
                    Integer idT = 0;
                    if (idTarea == 0) {       //si es una nueva Tarea
                        Cursor c = myDao.listaTareas(1);
                        if (c.moveToPosition(c.getCount() - 1)) {
                            idT = c.getInt(0) + 1;
                        }
                        c.close();
                    } else {
                        idT = idTarea;
                    }
                    Prioridad prio = myDao.getPrioridad(i_prioridad);
                    Usuario us = myDao.getUsuario(myDao.getIdResponsable(s_responsable));
                    Proyecto proy = myDao.getProyecto(1);   //el por defecto
                    Tarea tarea = new Tarea(idT, s_descripcion, Integer.parseInt(s_horasE), i_minutosTrabajados, prio, us, proy, finalizada);

                    // Incorporo la nueva Tarea a la bdd
                    if (idTarea == 0) {      //insertar nueva Tarea
                        myDao.nuevaTarea(tarea);
                        setResult(RESULT_OK, intAlta);
                    } else {                   //editar Tarea existente
                        myDao.actualizarTarea(tarea);
                    }
                    finish();
                }
            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0){
                if (idTarea == 0) {
                    setResult(RESULT_OK, intAlta);
                }
                finish();
            }
        });
    }
}
