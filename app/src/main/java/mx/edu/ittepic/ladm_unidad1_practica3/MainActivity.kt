package mx.edu.ittepic.ladm_unidad1_practica3

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import java.io.*

class MainActivity : AppCompatActivity() {
    var vector : Array<Int> = Array(10, {0})
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        asignar.setOnClickListener {
            insertarValores()
        }

        mostrar.setOnClickListener {
            mostrar()
        }

        guardar.setOnClickListener {
            guardarDatos()
        }

        leer.setOnClickListener {
            leerArchivo()
        }

    }//Fin del onCreate

    fun insertarValores(){

        try {


            if(Editvalor.text.isEmpty() || Editposicion.text.isEmpty()){
                mensaje("No deje campos vacios")
                limpiaCajitas()
                return
            }//Fin del if para comprobar que las cajitas no estan vacias

            var posicion = Editposicion.text.toString().toInt()
            var valor = Editvalor.text.toString().toInt()

            if(posicion < 0 || posicion > 9)
            {
                mensaje("Inserte en un rango de posiciones validas (0..9) ")
                return
            }

            vector[posicion] = valor
            mensaje("Dato insertado correctamente")

        }catch(error : NumberFormatException)
        {
            mensaje(error.message.toString())
        }
    }//Fin del metodo para insertar valores

    fun limpiaCajitas(){
        Editvalor.setText("")
        Editposicion.setText("")
        Editnombre.setText("")
        Editabrir.setText("")
    }


    fun mostrar(){

        var data =""
        (0..9).forEach {
            data += "[ ${vector[it]} ]"
            data = data + "\n"

        }
        mensaje(data)
    }

    fun guardarDatos(){
        if(noSD()){
            mensaje("No hay memoria Externa")
            return
        }
        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_DENIED ){

            //Para solicitar los permisos se usa
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE),0)
        }

        try {
            //Ruta y nombre del archivo se ocupan para referenciar a la memoria
            var nomb = Editnombre.text.toString()
            var rutaSD = Environment.getExternalStorageDirectory()
            var datosArchivo = File(rutaSD.absolutePath,nomb)

            var flujoSalida = OutputStreamWriter(FileOutputStream(datosArchivo))

            //Codigo para referenciar a la memoria externa

            var data=""
            (0..9).forEach {
                data += "[ ${vector[it]} ]"
                data = data + "\n"

            }

            flujoSalida.write(data)
            flujoSalida.flush()
            flujoSalida.close()

            mensaje("EXITO se guardo el archivo correctamente")
            limpiaCajitas()

        }catch (error: IOException){
            mensaje(error.message.toString())
        }


    }//Fin del metodo para leer el archivo en la SD

    fun leerArchivo() {

        try {

            if(noSD())
            {
                mensaje( "No se encontró la memoria externa")
                return
            }

            if(Editabrir.text.toString().isEmpty())
            {
                mensaje( "No deje el campo nombre vacio")
                return
            }

            //Ruta y nombre del archivo se ocupan para referenciar a la memoria
            var abrir = Editabrir.text.toString()
            var rutaSD = Environment.getExternalStorageDirectory()
            var datosArchivo = File(rutaSD.absolutePath,abrir)
            var flujoEntrada = BufferedReader(InputStreamReader(FileInputStream(datosArchivo)))


            var data = flujoEntrada.readLine()
            

            flujoEntrada.close()
            mensaje( "El archivo se abrio correcatamente")



        }catch (error: IOException){
            mensaje(error.message.toString())
        }
    }

    fun mensaje(m: String){
        AlertDialog.Builder(this)
            .setTitle("Mensaje")
            .setMessage(m)
            .setPositiveButton("Ok"){d,i->}
            .show()
    }



    fun noSD():Boolean{
        var estado = Environment.getExternalStorageState()

        if(estado!= Environment.MEDIA_MOUNTED){
            return true
        }

        return false
    }


}//Fin de la clase Activity
 