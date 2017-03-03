package dam.isi.frsf.utn.edu.ar.laboratorio5v2.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;

/*  Proporciona acceso a una SQLiteDatabase examinando si esa base de datos ya está instalada y disponible.
        •Si está disponible realiza una comprobación para ver si la versión es la misma y => proporciona una referencia a la SQLiteDatabase / ofrece una función de callback para migrar la base de datos antes de proporcionar la referencia.
        •Si la base de datos no existe, entonces ofrece una función de callback para de crear y llenar la base de datos.
*/
public class ProyectoOpenHelper extends SQLiteOpenHelper { //ya está implementada!!!!!!!!!!!!!!!!!!!
/*carga datos de un archivo en el directorio “asset” y crea la estructura de base de datos y los datos iniciales. Observar que esto sucede solo una vez.*/

    /*------------------------------ Declaración de Variables ------------------------------------*/
    private Context context;

    /*------------------------------------ CONSTRUCTOR -------------------------------------------*/
    /*  En el constructor se llama al método padre indicando el nombre de la base de datos y un
        cursorFactory, en general null. */
    public ProyectoOpenHelper(Context context) {
        super(context, ProyectoDBMetadata.NOMBRE_DB, null, ProyectoDBMetadata.VERSION_DB);//(context, Constants.DATABASE_NAME, null,Constants.DATABASE_VERSION);
        this.context = context;
    }

    /*------------------------------------- On Create --------------------------------------------*/
    /*  Las tareas típicas que deben hacerse en este método serán la creación de todas las tablas
        necesarias y la inserción de los datos iniciales si son necesarios */
    public void onCreate(SQLiteDatabase db) {
        try {
            ArrayList<String> tablas = this.leerTablas();
            for (String sql : tablas) {
                db.execSQL(sql);        /* execSQL: nos permite ejecutar las consultas para creación y manipulación de tablas. */
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*------------------------------------- On Upgrade -------------------------------------------*/
    /*  se lanzará automáticamente cuando sea necesaria una actualización de la estructura de la
        base de datos o una conversión de los datos.
        Recibe como parámetros, la versión actual de la base de datos en el sistema, y la nueva
        versión a la que se quiere convertir. Para que funcione apropiadamente, debemos aumentar en
        1 la versión.*/
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        //db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    /*------------------------------------ On Downgrade ------------------------------------------*/
    /*  Método inverso al método onUpgrade, es invocado si el código requiere un esquema anterior
        al actualmente en uso. En general tampoco es necesario implementarlo, pero podriamos hacerlo
         para mantener compatibilidad hacía atrás.*/
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    /*------------------------------------ Leer Tablas -------------------------------------------*/
    public ArrayList<String> leerTablas() throws IOException {

        InputStream is = context.getAssets().open("estructura-db.sql");
        InputStreamReader r = new InputStreamReader(is, Charset.forName("UTF-8"));
        BufferedReader br = new BufferedReader(r);
        String strLine;
        ArrayList<String> res = new ArrayList<String>();
        while ((strLine = br.readLine()) != null) { //mi
            Log.d("SQL", strLine);
            res.add(strLine);
        }
        br.close();
        r.close();
        is.close();
        return res;
    }
}

/*
- Si queremos controlar quien abre la base de datos, podemos implementar el método onOpen() aunque por lo general no es requerido.
*/
