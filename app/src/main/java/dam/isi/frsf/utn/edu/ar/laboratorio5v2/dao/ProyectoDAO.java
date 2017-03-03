package dam.isi.frsf.utn.edu.ar.laboratorio5v2.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import java.util.List;

import dam.isi.frsf.utn.edu.ar.laboratorio5v2.modelo.Prioridad;
import dam.isi.frsf.utn.edu.ar.laboratorio5v2.modelo.Proyecto;
import dam.isi.frsf.utn.edu.ar.laboratorio5v2.modelo.Tarea;
import dam.isi.frsf.utn.edu.ar.laboratorio5v2.modelo.Usuario;

public class ProyectoDAO {


    private static final String _SQL_TAREAS_X_PROYECTO =
            "SELECT "
                +ProyectoDBMetadata.TABLA_TAREAS_ALIAS+"."+ProyectoDBMetadata.TablaTareasMetadata._ID+" as "+ProyectoDBMetadata.TablaTareasMetadata._ID+
            ", "+ProyectoDBMetadata.TablaTareasMetadata.TAREA +
            ", "+ProyectoDBMetadata.TablaTareasMetadata.HORAS_PLANIFICADAS +
            ", "+ProyectoDBMetadata.TablaTareasMetadata.MINUTOS_TRABAJADOS +
            ", "+ProyectoDBMetadata.TablaTareasMetadata.PRIORIDAD +
            ", "+ProyectoDBMetadata.TABLA_PRIORIDAD_ALIAS+"."+ProyectoDBMetadata.TablaPrioridadMetadata.PRIORIDAD +" as "+ProyectoDBMetadata.TablaPrioridadMetadata.PRIORIDAD_ALIAS+
            ", "+ProyectoDBMetadata.TablaTareasMetadata.RESPONSABLE +
            ", "+ProyectoDBMetadata.TABLA_USUARIOS_ALIAS+"."+ProyectoDBMetadata.TablaUsuariosMetadata.USUARIO +" as "+ProyectoDBMetadata.TablaUsuariosMetadata.USUARIO_ALIAS+
            ", "+ProyectoDBMetadata.TablaTareasMetadata.PROYECTO +
            ", "+ProyectoDBMetadata.TABLA_PROYECTO_ALIAS+"."+ProyectoDBMetadata.TablaProyectoMetadata.TITULO +" as "+ProyectoDBMetadata.TablaProyectoMetadata.PROYECTO_ALIAS+
            ", "+ProyectoDBMetadata.TablaTareasMetadata.FINALIZADA +

            " FROM "
                +ProyectoDBMetadata.TABLA_PROYECTO + " "+ProyectoDBMetadata.TABLA_PROYECTO_ALIAS+
            ", "+ProyectoDBMetadata.TABLA_USUARIOS + " "+ProyectoDBMetadata.TABLA_USUARIOS_ALIAS+
            ", "+ProyectoDBMetadata.TABLA_PRIORIDAD + " "+ProyectoDBMetadata.TABLA_PRIORIDAD_ALIAS+
            ", "+ProyectoDBMetadata.TABLA_TAREAS + " "+ProyectoDBMetadata.TABLA_TAREAS_ALIAS+

            " WHERE "
                +ProyectoDBMetadata.TABLA_TAREAS_ALIAS+"."+ProyectoDBMetadata.TablaTareasMetadata.PROYECTO+" = "+ProyectoDBMetadata.TABLA_PROYECTO_ALIAS+"."+ProyectoDBMetadata.TablaProyectoMetadata._ID +" AND "
                +ProyectoDBMetadata.TABLA_TAREAS_ALIAS+"."+ProyectoDBMetadata.TablaTareasMetadata.RESPONSABLE+" = "+ProyectoDBMetadata.TABLA_USUARIOS_ALIAS+"."+ProyectoDBMetadata.TablaUsuariosMetadata._ID +" AND "
                +ProyectoDBMetadata.TABLA_TAREAS_ALIAS+"."+ProyectoDBMetadata.TablaTareasMetadata.PRIORIDAD+" = "+ProyectoDBMetadata.TABLA_PRIORIDAD_ALIAS+"."+ProyectoDBMetadata.TablaPrioridadMetadata._ID +" AND "
                +ProyectoDBMetadata.TABLA_TAREAS_ALIAS+"."+ProyectoDBMetadata.TablaTareasMetadata.PROYECTO+" = ?";

    /*--------------------------------------------------------------------------------------------*/
    private static final String _SQL_TAREA_X_ID =
            "SELECT "
                    +ProyectoDBMetadata.TABLA_TAREAS_ALIAS+"."+ProyectoDBMetadata.TablaTareasMetadata._ID+" as "+ProyectoDBMetadata.TablaTareasMetadata._ID+
                    ", "+ProyectoDBMetadata.TablaTareasMetadata.TAREA +
                    ", "+ProyectoDBMetadata.TablaTareasMetadata.HORAS_PLANIFICADAS +
                    ", "+ProyectoDBMetadata.TablaTareasMetadata.MINUTOS_TRABAJADOS +
                    ", "+ProyectoDBMetadata.TablaTareasMetadata.PRIORIDAD +
                    ", "+ProyectoDBMetadata.TablaTareasMetadata.RESPONSABLE +
                    ", "+ProyectoDBMetadata.TablaTareasMetadata.PROYECTO +
                    ", "+ProyectoDBMetadata.TablaTareasMetadata.FINALIZADA +

                    " FROM "
                        +ProyectoDBMetadata.TABLA_TAREAS + " "+ProyectoDBMetadata.TABLA_TAREAS_ALIAS+

                    " WHERE "
                        +ProyectoDBMetadata.TABLA_TAREAS_ALIAS+"."+ProyectoDBMetadata.TablaTareasMetadata._ID+" = ?";

    /*--------------------------------------------------------------------------------------------*/
    private static final String _SQL_PROYECTO_X_ID =
            "SELECT "
                    +ProyectoDBMetadata.TABLA_PROYECTO_ALIAS+"."+ProyectoDBMetadata.TablaProyectoMetadata._ID+" as "+ProyectoDBMetadata.TablaProyectoMetadata._ID+
                    ", "+ProyectoDBMetadata.TablaProyectoMetadata.TITULO +
            " FROM "
                    +ProyectoDBMetadata.TABLA_PROYECTO + " "+ProyectoDBMetadata.TABLA_PROYECTO_ALIAS+
            " WHERE "
                    +ProyectoDBMetadata.TABLA_PROYECTO_ALIAS+"."+ProyectoDBMetadata.TablaProyectoMetadata._ID+" = ?";

    /*--------------------------------------------------------------------------------------------*/
    private static final String _SQL_PRIORIDAD_X_ID =
            "SELECT "
                    +ProyectoDBMetadata.TABLA_PRIORIDAD_ALIAS+"."+ProyectoDBMetadata.TablaPrioridadMetadata._ID+" as "+ProyectoDBMetadata.TablaPrioridadMetadata._ID+
                    ", "+ProyectoDBMetadata.TablaPrioridadMetadata.PRIORIDAD +
                    " FROM "
                    +ProyectoDBMetadata.TABLA_PRIORIDAD + " "+ProyectoDBMetadata.TABLA_PRIORIDAD_ALIAS+
                    " WHERE "
                    +ProyectoDBMetadata.TABLA_PRIORIDAD_ALIAS+"."+ProyectoDBMetadata.TablaPrioridadMetadata._ID+" = ?";
    /*--------------------------------------------------------------------------------------------*/
    private static final String _SQL_USUARIO_X_ID =
            "SELECT "
                    +ProyectoDBMetadata.TABLA_USUARIOS_ALIAS+"."+ProyectoDBMetadata.TablaUsuariosMetadata._ID+" as "+ProyectoDBMetadata.TablaUsuariosMetadata._ID+
                    ", "+ProyectoDBMetadata.TablaUsuariosMetadata.USUARIO +
                    ", "+ProyectoDBMetadata.TablaUsuariosMetadata.MAIL +
                    " FROM "
                    +ProyectoDBMetadata.TABLA_USUARIOS + " "+ProyectoDBMetadata.TABLA_USUARIOS_ALIAS+
                    " WHERE "
                    +ProyectoDBMetadata.TABLA_USUARIOS_ALIAS+"."+ProyectoDBMetadata.TablaUsuariosMetadata._ID+" = ?";

    /*--------------------------------------------------------------------------------------------*/
    private static final String _SQL_USUARIO_X_USUARIO =    //osea, por nombre
            "SELECT "
                    +ProyectoDBMetadata.TABLA_USUARIOS_ALIAS+"."+ProyectoDBMetadata.TablaUsuariosMetadata._ID+" as "+ProyectoDBMetadata.TablaUsuariosMetadata._ID+
            " FROM "
                    +ProyectoDBMetadata.TABLA_USUARIOS + " "+ProyectoDBMetadata.TABLA_USUARIOS_ALIAS+
            " WHERE "
                    +ProyectoDBMetadata.TABLA_USUARIOS_ALIAS+"."+ProyectoDBMetadata.TablaUsuariosMetadata.USUARIO+" = ?";

    /*--------------------------------------------------------------------------------------------*/
    private static final String _SQL_TAREA_X_CondicionTerminada =
            "SELECT "
                    +ProyectoDBMetadata.TABLA_TAREAS_ALIAS+"."+ProyectoDBMetadata.TablaTareasMetadata._ID+" as "+ProyectoDBMetadata.TablaTareasMetadata._ID+
                    ", "+ProyectoDBMetadata.TablaTareasMetadata.TAREA +
                    ", "+ProyectoDBMetadata.TablaTareasMetadata.HORAS_PLANIFICADAS +
                    ", "+ProyectoDBMetadata.TablaTareasMetadata.MINUTOS_TRABAJADOS +
                    ", "+ProyectoDBMetadata.TablaTareasMetadata.PRIORIDAD +
                    ", "+ProyectoDBMetadata.TABLA_PRIORIDAD_ALIAS+"."+ProyectoDBMetadata.TablaPrioridadMetadata.PRIORIDAD +" as "+ProyectoDBMetadata.TablaPrioridadMetadata.PRIORIDAD_ALIAS+
                    ", "+ProyectoDBMetadata.TablaTareasMetadata.RESPONSABLE +
                    ", "+ProyectoDBMetadata.TABLA_USUARIOS_ALIAS+"."+ProyectoDBMetadata.TablaUsuariosMetadata.USUARIO +" as "+ProyectoDBMetadata.TablaUsuariosMetadata.USUARIO_ALIAS+
                    ", "+ProyectoDBMetadata.TablaTareasMetadata.PROYECTO +
                    ", "+ProyectoDBMetadata.TABLA_PROYECTO_ALIAS+"."+ProyectoDBMetadata.TablaProyectoMetadata.TITULO +" as "+ProyectoDBMetadata.TablaProyectoMetadata.PROYECTO_ALIAS+
                    ", "+ProyectoDBMetadata.TablaTareasMetadata.FINALIZADA +

                    " FROM "
                    +ProyectoDBMetadata.TABLA_PROYECTO + " "+ProyectoDBMetadata.TABLA_PROYECTO_ALIAS+
                    ", "+ProyectoDBMetadata.TABLA_USUARIOS + " "+ProyectoDBMetadata.TABLA_USUARIOS_ALIAS+
                    ", "+ProyectoDBMetadata.TABLA_PRIORIDAD + " "+ProyectoDBMetadata.TABLA_PRIORIDAD_ALIAS+
                    ", "+ProyectoDBMetadata.TABLA_TAREAS + " "+ProyectoDBMetadata.TABLA_TAREAS_ALIAS+

                    " WHERE "
                    +ProyectoDBMetadata.TABLA_TAREAS_ALIAS+"."+ProyectoDBMetadata.TablaTareasMetadata.PROYECTO+" = "+ProyectoDBMetadata.TABLA_PROYECTO_ALIAS+"."+ProyectoDBMetadata.TablaProyectoMetadata._ID +" AND "
                    +ProyectoDBMetadata.TABLA_TAREAS_ALIAS+"."+ProyectoDBMetadata.TablaTareasMetadata.RESPONSABLE+" = "+ProyectoDBMetadata.TABLA_USUARIOS_ALIAS+"."+ProyectoDBMetadata.TablaUsuariosMetadata._ID +" AND "
                    +ProyectoDBMetadata.TABLA_TAREAS_ALIAS+"."+ProyectoDBMetadata.TablaTareasMetadata.PRIORIDAD+" = "+ProyectoDBMetadata.TABLA_PRIORIDAD_ALIAS+"."+ProyectoDBMetadata.TablaPrioridadMetadata._ID +" AND "
                    +ProyectoDBMetadata.TABLA_TAREAS_ALIAS+"."+ProyectoDBMetadata.TablaTareasMetadata.FINALIZADA+" = ?"
                    +" AND (" +
                            "(" +
                                "(("+ProyectoDBMetadata.TABLA_TAREAS_ALIAS+"."+ProyectoDBMetadata.TablaTareasMetadata.HORAS_PLANIFICADAS+"* 60)"+" > "+ProyectoDBMetadata.TABLA_TAREAS_ALIAS+"."+ProyectoDBMetadata.TablaTareasMetadata.MINUTOS_TRABAJADOS+")"
                                +" AND "
                                +"(("+ProyectoDBMetadata.TABLA_TAREAS_ALIAS+"."+ProyectoDBMetadata.TablaTareasMetadata.MINUTOS_TRABAJADOS+" + ?) >= ("+ProyectoDBMetadata.TABLA_TAREAS_ALIAS+"."+ProyectoDBMetadata.TablaTareasMetadata.HORAS_PLANIFICADAS+"* 60))"
                            +") OR "+
                            "(" +
                                "(("+ProyectoDBMetadata.TABLA_TAREAS_ALIAS+"."+ProyectoDBMetadata.TablaTareasMetadata.HORAS_PLANIFICADAS+"* 60)"+" <= "+ProyectoDBMetadata.TABLA_TAREAS_ALIAS+"."+ProyectoDBMetadata.TablaTareasMetadata.MINUTOS_TRABAJADOS+")"
                                +" AND "
                                +"(("+ProyectoDBMetadata.TABLA_TAREAS_ALIAS+"."+ProyectoDBMetadata.TablaTareasMetadata.MINUTOS_TRABAJADOS+" - ?) <= ("+ProyectoDBMetadata.TABLA_TAREAS_ALIAS+"."+ProyectoDBMetadata.TablaTareasMetadata.HORAS_PLANIFICADAS+"* 60))"
                            +")"+
                          ")";

    private ProyectoOpenHelper dbHelper;    //Helper que proporciona las comprobaciones para acceder y el acceso a la BdD
    private SQLiteDatabase db;              //BASE de DATOS!


    /*------------------------------------ CONSTRUCTOR -------------------------------------------*/
    public ProyectoDAO(Context c){  //Solo inicializa el Helper
        this.dbHelper = new ProyectoOpenHelper(c);
    }

    /*----------------------------------------- Open ---------------------------------------------*/
    public void open(){
        this.open(false);
    }  //abre la bdd por defecto en solo lectura

    /*----------------------------------------- Open ---------------------------------------------*/
    public void open(Boolean toWrite){  //Recién acá se abre la BdD
        if(toWrite) {
            db = dbHelper.getWritableDatabase();    //true: abre para escritura
        }
        else{
            db = dbHelper.getReadableDatabase();    //false: abre para solo lectura
        }
    }

    /*----------------------------------------- Close --------------------------------------------*/
    /*  Cuando terminamos de trabajar con la base de datos, o a más tardar cuando la actividad es
        cerradada, simplemente sobre la instancia de SQLiteOpenHelper invocamos al método close().*/
    public void close(){    //"Cerrar" la BdD, implica no permitir su escritura solamente, pero si su lectura??
        db = dbHelper.getReadableDatabase();
    }

    /*------------------------------------- Lista Tareas -----------------------------------------*/
    /* Cursor: .Colección de filas que se devuelven desde una SQLiteDatabase.
                .Este objeto tiene métodos para recorrer filas una a la vez como un cursor hacia adelante y recuperar las filas sólo cuando sea necesario.
                .También puede saltar hacia adelante o hacia atrás si es necesario mediante la aplicación de cualidades de ventanas.
                .Y se va a utilizar para leer los valores de columna para cualquier fila actual.    */
    public Cursor listaTareas(Integer idProyecto){

        Cursor cursorPry = db.rawQuery("SELECT "+ProyectoDBMetadata.TablaProyectoMetadata._ID+ " FROM "+ProyectoDBMetadata.TABLA_PROYECTO,null);
        Integer idPry= 0;

        if(cursorPry.moveToFirst()){
            idPry=cursorPry.getInt(0);
        }
        cursorPry.close();

        Cursor cursor = null;
        cursor = db.rawQuery(_SQL_TAREAS_X_PROYECTO,new String[]{idPry.toString()});

        return cursor;
    }
/*=================================================================================================================================================================*/
    /*------------------------------------ Nueva Tarea -------------------------------------------*/
    public void nuevaTarea(Tarea t){

        ContentValues valores = new ContentValues();

        valores.put(ProyectoDBMetadata.TablaTareasMetadata.TAREA,t.getDescripcion());
        valores.put(ProyectoDBMetadata.TablaTareasMetadata.HORAS_PLANIFICADAS, t.getHorasEstimadas());
        valores.put(ProyectoDBMetadata.TablaTareasMetadata.MINUTOS_TRABAJADOS,t.getMinutosTrabajados());
        valores.put(ProyectoDBMetadata.TablaTareasMetadata.PRIORIDAD,t.getPrioridad().getId());
        valores.put(ProyectoDBMetadata.TablaTareasMetadata.RESPONSABLE, t.getResponsable().getId());
        valores.put(ProyectoDBMetadata.TablaTareasMetadata.PROYECTO, t.getProyecto().getId());
        valores.put(ProyectoDBMetadata.TablaTareasMetadata.FINALIZADA, t.getFinalizada());

        try {
            db.insert(ProyectoDBMetadata.TABLA_TAREAS, null, valores);
        } catch (SQLiteException ex) {
            ex.printStackTrace();
        }
    }

    /*------------------------------------ Actualizar Tarea --------------------------------------*/
    public void actualizarTarea(Tarea t){

        ContentValues valores = new ContentValues();

        valores.put(ProyectoDBMetadata.TablaTareasMetadata.TAREA,t.getDescripcion());
        valores.put(ProyectoDBMetadata.TablaTareasMetadata.HORAS_PLANIFICADAS, t.getHorasEstimadas());
        valores.put(ProyectoDBMetadata.TablaTareasMetadata.MINUTOS_TRABAJADOS,t.getMinutosTrabajados());
        valores.put(ProyectoDBMetadata.TablaTareasMetadata.PRIORIDAD,t.getPrioridad().getId());
        valores.put(ProyectoDBMetadata.TablaTareasMetadata.RESPONSABLE, t.getResponsable().getId());
        valores.put(ProyectoDBMetadata.TablaTareasMetadata.PROYECTO, t.getProyecto().getId());
        valores.put(ProyectoDBMetadata.TablaTareasMetadata.FINALIZADA, t.getFinalizada());

        db.update(ProyectoDBMetadata.TABLA_TAREAS, valores, ProyectoDBMetadata.TABLA_TAREAS+"."+ProyectoDBMetadata.TablaTareasMetadata._ID+" = ?", new String[]{t.getId().toString()});
      }

    /*------------------------------------- Borrar Tarea -----------------------------------------*/
    public void borrarTarea(Tarea t){
        db.delete(ProyectoDBMetadata.TABLA_TAREAS, ProyectoDBMetadata.TablaTareasMetadata._ID + " = " + t.getId().toString(),null);
    }
    /*------------------------------------- Get Cursor Tarea -----------------------------------------*/
    public Cursor getCursorTarea(Integer idTarea){

        Cursor cursor = null;
        cursor = db.rawQuery(_SQL_TAREA_X_ID,new String[]{idTarea.toString()});

        return cursor;
    }

    /*------------------------------------- Get Tarea -----------------------------------------*/
    public Tarea getTarea(Integer idTarea){

        String descripcion = "";
        Integer horasEstimadas = 0;
        Integer minutosTrabajados = 0;
        Boolean finalizada = false;
        Proyecto proyecto = new Proyecto();
        Prioridad prioridad = new Prioridad();
        Usuario responsable = new Usuario();
        Tarea tarea;
        Integer idProy;
        Integer idPrioridad;
        Integer idResponsable;

        Cursor cursor = null;
        cursor = getCursorTarea(idTarea);

        if(cursor.moveToFirst()) {
            descripcion = cursor.getString(cursor.getColumnIndex(ProyectoDBMetadata.TablaTareasMetadata.TAREA));
            horasEstimadas = cursor.getInt(cursor.getColumnIndex(ProyectoDBMetadata.TablaTareasMetadata.HORAS_PLANIFICADAS));
            minutosTrabajados = cursor.getInt(cursor.getColumnIndex(ProyectoDBMetadata.TablaTareasMetadata.MINUTOS_TRABAJADOS));
            finalizada = (cursor.getInt(cursor.getColumnIndex(ProyectoDBMetadata.TablaTareasMetadata.FINALIZADA))== 1);

            idProy = cursor.getInt(cursor.getColumnIndex(ProyectoDBMetadata.TablaTareasMetadata.PROYECTO));
            idPrioridad = cursor.getInt(cursor.getColumnIndex(ProyectoDBMetadata.TablaTareasMetadata.PRIORIDAD));
            idResponsable = cursor.getInt(cursor.getColumnIndex(ProyectoDBMetadata.TablaTareasMetadata.RESPONSABLE));

            proyecto = getProyecto(idProy);
            prioridad = getPrioridad(idPrioridad);
            responsable = getUsuario(idResponsable);
            tarea = new Tarea(idTarea,descripcion,horasEstimadas,minutosTrabajados,prioridad,responsable,proyecto,finalizada);
        }
        else {
            tarea = new Tarea(idTarea,descripcion,horasEstimadas,minutosTrabajados,prioridad,responsable,proyecto,finalizada);
        }

        cursor.close();

        return tarea;
    }

    /*------------------------------------- Get Cursor Proyecto ----------------------------------*/
    public Cursor getCursorProyecto(Integer idProyecto){

        Cursor cursor = null;
        cursor = db.rawQuery(_SQL_PROYECTO_X_ID,new String[]{idProyecto.toString()});

        return cursor;
    }

    /*------------------------------------- Get Proyecto -----------------------------------------*/
    public Proyecto getProyecto(Integer idProyecto){

        Cursor cursor = null;
        cursor = getCursorProyecto(idProyecto);
        Proyecto proyecto;

        if(cursor.moveToFirst()) {
            Integer id = cursor.getInt(cursor.getColumnIndex(ProyectoDBMetadata.TablaProyectoMetadata._ID));
            String nombre = cursor.getString(cursor.getColumnIndex(ProyectoDBMetadata.TablaProyectoMetadata.TITULO));
            proyecto = new Proyecto(id, nombre);
        }
        else{
            proyecto = new Proyecto(1,"Móviles");
        }

            cursor.close();
        return proyecto;
    }

    /*---------------------------------- Get Cursor Prioridad ------------------------------------*/
    public Cursor getCursorPrioridad(Integer idPrioridad){

        Cursor cursor = null;
        cursor = db.rawQuery(_SQL_PRIORIDAD_X_ID,new String[]{idPrioridad.toString()});

        return cursor;
    }

    /*------------------------------------- Get Prioridad ----------------------------------------*/
    public Prioridad getPrioridad(Integer idPrioridad){

        Cursor cursor = null;
        cursor = getCursorPrioridad(idPrioridad);
        Prioridad prioridad;

        if(cursor.moveToFirst()) {
            Integer id = cursor.getInt(cursor.getColumnIndex(ProyectoDBMetadata.TablaPrioridadMetadata._ID));
            String nombre = cursor.getString(cursor.getColumnIndex(ProyectoDBMetadata.TablaPrioridadMetadata.PRIORIDAD));
            prioridad = new Prioridad(id,nombre);
        }
        else {
            prioridad = new Prioridad(4,"Desconocida"); //valor por defecto
        }

        cursor.close();
     return prioridad;
    }
    /*---------------------------------- Get Lista Usuario --------------------------------------*/
    public String[] getListaUsuarios(){

        String [] s = null;
        int aux = 0;
        Cursor cursor = null;
        cursor = db.rawQuery("SELECT "+ProyectoDBMetadata.TablaUsuariosMetadata._ID+","+ProyectoDBMetadata.TablaUsuariosMetadata.USUARIO+ " FROM "+ProyectoDBMetadata.TABLA_USUARIOS,null);
        if(cursor.moveToFirst()){
            s = new String[cursor.getCount()];
            do {
                s[aux]=cursor.getString(1);
                aux++;
            }while(cursor.moveToNext());
        }
        cursor.close();
        return s;
    }
    /*---------------------------------- Get Cursor Usuarios --------------------------------------*/
    public Cursor getCursorUsuarios(){

        Cursor cursor = null;
        cursor = db.rawQuery("SELECT "+ProyectoDBMetadata.TablaUsuariosMetadata._ID+","+ProyectoDBMetadata.TablaUsuariosMetadata.USUARIO+ " FROM "+ProyectoDBMetadata.TABLA_USUARIOS,null);

        return cursor;
    }

    /*---------------------------------- Get Cursor Usuario --------------------------------------*/
    public Cursor getCursorUsuario(Integer idUsuario){

        Cursor cursor = null;
        cursor = db.rawQuery(_SQL_USUARIO_X_ID,new String[]{idUsuario.toString()});

        return cursor;
    }

    /*------------------------------------- Get Usuario -----//-----------------------------------*/
    public Usuario getUsuario(Integer idUsuario){

        Cursor cursor = null;
        cursor = getCursorUsuario(idUsuario);
        Usuario usuario;

        if(cursor.moveToFirst()) {
            Integer id = cursor.getInt(cursor.getColumnIndex(ProyectoDBMetadata.TablaUsuariosMetadata._ID));
            String nombre = cursor.getString(cursor.getColumnIndex(ProyectoDBMetadata.TablaUsuariosMetadata.USUARIO));
            String mail = cursor.getString(cursor.getColumnIndex(ProyectoDBMetadata.TablaUsuariosMetadata.MAIL));
            usuario = new Usuario(id, nombre, mail);
        }
        else {
            usuario = new Usuario(1,"Superman","alinfinito@ymas.alla");
        }

        cursor.close();
     return usuario;
    }

    /*-------------------------------- Get Id Responsable ----------------------------------------*/
    public Integer getIdResponsable(String s){

        Integer id = 0; //por defecto
        Cursor cursor = null;
        cursor = db.rawQuery(_SQL_USUARIO_X_USUARIO,new String[]{s});
        if(cursor.moveToFirst()){
            id = cursor.getInt(0);
        }
        cursor.close();
        return id;
    }
    /*---------------------------------- Listar Prioridades --------------------------------------*/
    public List<Prioridad> listarPrioridades(){
        return null;
    }

    /*------------------------------------ Listar Usuarios ---------------------------------------*/
    public List<Usuario> listarUsuarios(){
        return null;
    }

    /*-------------------------------------- Finalizar -------------------------------------------*/
    public void finalizar(Integer idTarea){
        //Establecemos los campos-valores a actualizar
        ContentValues valores = new ContentValues();    /* conjunto de pares clave/valor que son utilizados para insertar o actualizar una fila de datos. */
        valores.put(ProyectoDBMetadata.TablaTareasMetadata.FINALIZADA,1);
        SQLiteDatabase mydb =dbHelper.getWritableDatabase();
        mydb.update(ProyectoDBMetadata.TABLA_TAREAS, valores, "_id=?", new String[]{idTarea.toString()});
    }

    /*----------------------------- Listar Desvíos Planificación ---------------------------------*/
    /* Retorna una lista de todas las tareas que tardaron más (en exceso) o menos (por defecto)
      que el tiempo planificado y con un desvío máximo establecido (por exceso o defecto).
      si la bandera soloTerminadas es true, se busca en las tareas terminadas, sino en todas.     */
    public Cursor listarDesviosPlanificacion(Boolean soloTerminadas, Integer desvioMaximoMinutos){

        Cursor cursor = null;
        Integer terminadas = 0;
        if (soloTerminadas) {terminadas = 1;}
        else                {terminadas = 0;}
        cursor = db.rawQuery(_SQL_TAREA_X_CondicionTerminada,new String[]{terminadas.toString(),desvioMaximoMinutos.toString(),desvioMaximoMinutos.toString()});

        return cursor;
    }

}



