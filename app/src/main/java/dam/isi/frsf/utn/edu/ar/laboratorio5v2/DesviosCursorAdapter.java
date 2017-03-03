package dam.isi.frsf.utn.edu.ar.laboratorio5v2;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CursorAdapter;
import android.widget.TextView;

import dam.isi.frsf.utn.edu.ar.laboratorio5v2.dao.ProyectoDAO;
import dam.isi.frsf.utn.edu.ar.laboratorio5v2.dao.ProyectoDBMetadata;

public class DesviosCursorAdapter extends CursorAdapter {


    /*----------------------------- Declaración de Variables -------------------------------------*/
    private LayoutInflater inflador;
    private ProyectoDAO myDao;
    private Context contexto;

    /*------------------------------------ CONSTRUCTOR -------------------------------------------*/
    public DesviosCursorAdapter(Context contexto, Cursor c, ProyectoDAO dao) {
        super(contexto, c, false);
        myDao = dao;
        this.contexto = contexto;
    }

    /*-------------------------------------- New View --------------------------------------------*/
    //para generar una nueva fila
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {

        inflador = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View vista = inflador.inflate(R.layout.fila_desvios, viewGroup, false);

        return vista;
    }

    /*------------------------------------- Bind View --------------------------------------------*/
    //para cargar datos en cada elemento de la fila
    public void bindView(View view, final Context context, final Cursor cursor) {

        // Referencias UI.
        TextView nombre = (TextView) view.findViewById(R.id.tareaTituloD);
        TextView responsable = (TextView) view.findViewById(R.id.tareaResponsableD);
        TextView prioridad = (TextView) view.findViewById(R.id.tareaPrioridadD);

        final TextView tiempoAsignado = (TextView) view.findViewById(R.id.tareaMinutosAsignadosD);
        TextView tiempoTrabajado = (TextView) view.findViewById(R.id.tareaMinutosTrabajadosD);
        final CheckBox finalizada = (CheckBox) view.findViewById(R.id.tareaFinalizadaD);

        // Asignaciones
        final String s_nombre = cursor.getString(cursor.getColumnIndex(ProyectoDBMetadata.TablaTareasMetadata.TAREA));
        nombre.setText(s_nombre);

        final Integer horasAsigandas = cursor.getInt(cursor.getColumnIndex(ProyectoDBMetadata.TablaTareasMetadata.HORAS_PLANIFICADAS));
        tiempoAsignado.setText(horasAsigandas * 60 + " minutos");

        final Integer minutosTrabajados = cursor.getInt(cursor.getColumnIndex(ProyectoDBMetadata.TablaTareasMetadata.MINUTOS_TRABAJADOS));
        tiempoTrabajado.setText(minutosTrabajados + " minutos");

        prioridad.setText(" - " + cursor.getString(cursor.getColumnIndex(ProyectoDBMetadata.TablaPrioridadMetadata.PRIORIDAD_ALIAS)));

        responsable.setText(cursor.getString(cursor.getColumnIndex(ProyectoDBMetadata.TablaUsuariosMetadata.USUARIO_ALIAS)));

        finalizada.setChecked(cursor.getInt(cursor.getColumnIndex(ProyectoDBMetadata.TablaTareasMetadata.FINALIZADA)) == 1);
        finalizada.setTextIsSelectable(false);
    }
}

