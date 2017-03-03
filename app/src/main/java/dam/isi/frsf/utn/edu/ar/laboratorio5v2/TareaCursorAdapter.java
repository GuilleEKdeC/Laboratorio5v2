package dam.isi.frsf.utn.edu.ar.laboratorio5v2;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import dam.isi.frsf.utn.edu.ar.laboratorio5v2.dao.ProyectoDAO;
import dam.isi.frsf.utn.edu.ar.laboratorio5v2.dao.ProyectoDBMetadata;
import dam.isi.frsf.utn.edu.ar.laboratorio5v2.modelo.Tarea;

public class TareaCursorAdapter extends CursorAdapter {


    /*----------------------------- Declaración de Variables -------------------------------------*/
    private LayoutInflater inflador;
    private ProyectoDAO myDao;
    private Context contexto;
    private long segundos;
    private Tarea tarea;
    private Integer idTareaActual;
    private boolean borrar = false;

    /*------------------------------------ CONSTRUCTOR -------------------------------------------*/
    public TareaCursorAdapter(Context contexto, Cursor c, ProyectoDAO dao) {
        super(contexto, c, false);
        myDao = dao;
        this.contexto = contexto;
    }

    /*-------------------------------------- New View --------------------------------------------*/
    //para generar una nueva fila
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {

        inflador = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View vista = inflador.inflate(R.layout.fila_tarea, viewGroup, false);
        return vista;
    }

    /*------------------------------------- Bind View --------------------------------------------*/
    //para cargar datos en cada elemento de la fila
    public void bindView(View view, final Context context, final Cursor cursor) {
        //obtener la posicion de la fila actual y asignarla a los botones y checkboxes

        idTareaActual = null;

        // Referencias UI.
        TextView nombre = (TextView) view.findViewById(R.id.tareaTitulo);

        TextView responsable = (TextView) view.findViewById(R.id.tareaResponsable);
        TextView prioridad = (TextView) view.findViewById(R.id.tareaPrioridad);

        final TextView tiempoAsignado = (TextView) view.findViewById(R.id.tareaMinutosAsignados);
        TextView tiempoTrabajado = (TextView) view.findViewById(R.id.tareaMinutosTrabajados);
        final CheckBox finalizada = (CheckBox) view.findViewById(R.id.tareaFinalizada);

        final Button btnFinalizar = (Button) view.findViewById(R.id.tareaBtnFinalizada);
        final Button btnEditar = (Button) view.findViewById(R.id.tareaBtnEditarDatos);
        final Button btnBorrar = (Button) view.findViewById(R.id.tareaBtnBorrarDatos);
        final ToggleButton btnEstado = (ToggleButton) view.findViewById(R.id.tareaBtnTrabajando);

        // Asignaciones
        final String s_nombre = cursor.getString(cursor.getColumnIndex(ProyectoDBMetadata.TablaTareasMetadata.TAREA));
        nombre.setText(s_nombre);

        final Integer horasAsigandas = cursor.getInt(cursor.getColumnIndex(ProyectoDBMetadata.TablaTareasMetadata.HORAS_PLANIFICADAS));
        tiempoAsignado.setText(horasAsigandas * 60+ " minutos");

        final Integer minutosTrabajados= cursor.getInt(cursor.getColumnIndex(ProyectoDBMetadata.TablaTareasMetadata.MINUTOS_TRABAJADOS));
        tiempoTrabajado.setText(minutosTrabajados + " minutos");

        final Integer i_prioridad = cursor.getInt(cursor.getColumnIndex(ProyectoDBMetadata.TablaTareasMetadata.PRIORIDAD));
        prioridad.setText(" - "+cursor.getString(cursor.getColumnIndex(ProyectoDBMetadata.TablaPrioridadMetadata.PRIORIDAD_ALIAS)));

        final Integer i_responsable = cursor.getInt(cursor.getColumnIndex(ProyectoDBMetadata.TablaTareasMetadata.RESPONSABLE));
        responsable.setText(cursor.getString(cursor.getColumnIndex(ProyectoDBMetadata.TablaUsuariosMetadata.USUARIO_ALIAS)));

        final Integer i_proyecto = cursor.getInt(cursor.getColumnIndex(ProyectoDBMetadata.TablaTareasMetadata.PROYECTO));

        finalizada.setChecked(cursor.getInt(cursor.getColumnIndex(ProyectoDBMetadata.TablaTareasMetadata.FINALIZADA)) == 1);
        finalizada.setTextIsSelectable(false);

        /*----------------------------- Toggle Button Estado ("TRABAJAR") ------------------------*/
        /* En este botón, se podría tener en cuenta de considerar si ya se finalizó la tarea para no
        permitir "trabajarla" mas, pero no lo hacemos ya que consideramos que luego de haberla tildado
        como "finalizada", podrían surgir cambios o errores que consumirían mas tiempo, por lo que
        dejamos que se pueda seguir trabajando */
        btnEstado.setTag(cursor.getInt((cursor.getColumnIndex("_id"))));
        btnEstado.setOnClickListener(new View.OnClickListener(){

            public void onClick(View view){

                final Integer idTarea = (Integer) view.getTag();                    //obtiene el _id de la fila del cursor anteriormente seteado
                if(idTareaActual==null) {                                           //si es la primera vez que se presiona una Tarea, le asigno el id de la tarea seleccionada
                    idTareaActual = idTarea;
                }
                if(idTareaActual==idTarea) {                                        //si la tarea selec, coincide con la actual, tomamos los tiempos
                    if (btnEstado.isChecked()) {                                    //si comenzó a trabajar en esa tarea, setea el tiempo
                        segundos = System.currentTimeMillis();
                    }                                                               //si deja de trabajar en esa tarea. Para simular la aplicación vamos a tomar que 5 segundos equivalen a un minuto y
                    else {                                                          // en el momento en que presione el botón que dejo de trabajar genera un evento de actualización del valor en la base de datos
                        segundos = (System.currentTimeMillis() - segundos) / 1000;
                        //ahora modifico el tiempo en la bdd y actualizo la interfaz => utilizo otro hilo y msj respectivamente
                        tarea = new Tarea(idTarea,
                                s_nombre, horasAsigandas,
                                (minutosTrabajados + (Integer.valueOf(String.valueOf(segundos / 5)))),
                                myDao.getPrioridad(i_prioridad),
                                myDao.getUsuario(i_responsable),
                                myDao.getProyecto(i_proyecto),
                                finalizada.isChecked());
                        segundos = 0;
                        Thread hiloActualizarTiempo = new Thread(new Runnable() {
                            public void run() {
                                myDao.actualizarTarea(tarea);
                                handlerRefresh.sendEmptyMessage(1);
                            }
                        });//fin-hilo
                        hiloActualizarTiempo.start();
                        idTareaActual = null;
                    }//fin-else
                }//fin-if
                else{
                    btnEstado.setChecked(!btnEstado.isChecked());
                    Toast.makeText(context, "Acción Inválida - Ya EXISTE una Tarea en Ejecución", Toast.LENGTH_LONG).show();
                }
            }//fin-onClick
        });//fin-setOnClickListener

        /*----------------------------------- Button Editar --------------------------------------*/
        //para conocer el id de la fila o la posición en un evento previamente lo asignamos al tag del compoennte (en el caso del botón de edición).
        btnEditar.setTag(cursor.getInt(cursor.getColumnIndex("_id")));  //setea en el tag el int del _id
        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Integer idTarea = (Integer) view.getTag();        //obtiene el _id de la fila del cursor anteriormente seteado
                Intent intEditarAct = new Intent(contexto, AltaTareaActivity.class);
                intEditarAct.putExtra("ID_TAREA", idTarea);
                context.startActivity(intEditarAct);
                handlerRefresh.sendEmptyMessage(1);
            }
        });

        /*----------------------------------- Button Borrar --------------------------------------*/
        //para conocer el id de la fila o la posición en un evento previamente lo asignamos al tag del compoennte (en el caso del botón de edición).
        btnBorrar.setTag(cursor.getInt(cursor.getColumnIndex("_id")));  //setea en el tag el int del _id
        btnBorrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(borrar) {
                    final Integer idTarea = (Integer) view.getTag();        //obtiene el _id de la fila del cursor anteriormente seteado
                    Tarea t = myDao.getTarea(idTarea);
                    myDao.borrarTarea(t);
                    handlerRefresh.sendEmptyMessage(1);
                    borrar = false;
                }
                else{
                    Toast.makeText(contexto, "A presionado BORRAR, si desea confirmar esta acción, presione nuevamente", Toast.LENGTH_LONG).show();
                    borrar = true;
                }
            }
        });

        /*---------------------------------- Button Finalizar ------------------------------------*/
        btnFinalizar.setTag(cursor.getInt(cursor.getColumnIndex("_id")));
        btnFinalizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    final Integer idTarea = (Integer) view.getTag();
                    Thread backGroundUpdate = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Log.d("LAB05-MAIN", "finalizar tarea : --- " + idTarea);
                            myDao.finalizar(idTarea);
                            handlerRefresh.sendEmptyMessage(1);
                        }
                    });
                    backGroundUpdate.start();
            }
        });
    }//Fin Bind View

    /*-------------------------------------- Handler ---------------------------------------------*/
   Handler handlerRefresh = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message inputMessage) {
            TareaCursorAdapter.this.changeCursor(myDao.listaTareas(1));
        }
    };
}


