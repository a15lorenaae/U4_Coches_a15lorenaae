package lorena.u4_coches_a15lorenaae;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class U4_Coches_a15lorenaae extends AppCompatActivity {
    boolean sdDisponible=false;
    boolean sdAccesoEscritura=false;
    File dirFicheiroSD;
    File rutaCompleta;
    RadioButton rbengadir;
    RadioButton rbsobreescribir;
    private static final int dialogolista=1;
    AlertDialog.Builder venta;
    public static String nomeFicheiro="ficheiro_memoriaexterna.txt";
    TextView tv;
    Intent i = null;
    //Todo o codigo esta recollido nos apuntes de clase
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_u4__coches_a15lorenaae);
        tv=(TextView)findViewById(R.id.txtv);
        rbengadir=(RadioButton)findViewById(R.id.radioengadir);
        rbsobreescribir=(RadioButton) findViewById(R.id.radiosobreescribir);
        comprobarestadosd();
        establecerdirectorioficheiro();
    }
    //Mostramos dialogo
    public void verdialogo(View v){
        showDialog(1);
        ArrayList<String>marcas=new ArrayList<String>();
        marcas=OnLerClick(i.getExtras().getString("Ruta")) ;


    }
    //Comprobamos o estado da tarxetasd para saber si está montada e poder traballar con ela
    public void comprobarestadosd(){
        String estado= Environment.getExternalStorageState();
        Log.e("SD", estado);
        if(estado.equals(Environment.MEDIA_MOUNTED)){
            sdDisponible=true;
            sdAccesoEscritura=true;
        }else if(estado.equals(Environment.MEDIA_MOUNTED_READ_ONLY))
            sdDisponible=true;

    }
    //Establecemos donde imos ter o ficheiro que creamos na memoria externa
public void establecerdirectorioficheiro(){
    if(sdDisponible){
        dirFicheiroSD=getExternalFilesDir(null);
        rutaCompleta=new File(dirFicheiroSD.getAbsolutePath(),nomeFicheiro);
    }
}


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_u4__coches_a15lorenaae, menu);
        return true;
    }


    public void Engadir_Sobreescribir(View v) {
        //Para saber a data actual, pero fai sincronizada co avd
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd_HH:mm:ss");
        String data = sdf.format(new Date());
        EditText textoedit = (EditText) findViewById(R.id.textoeditado);
        RadioButton rbsobreescribir = (RadioButton) findViewById(R.id.radiosobreescribir);
        boolean sobreescribir = false;
        tv.setText("");
        //Si está vacio o edittext, pide que introduzcamos unha marca
        if(textoedit.getText().toString().equals("")){
            Toast.makeText(this,"Introduce unha palabra para insertar no ficheiro",Toast.LENGTH_SHORT).show();
        }
        try {
            if (sdAccesoEscritura) {
                //Si se pode escribir na sd, pois vamos a poder engadir unha marca ou sobreescribila en función do radiobuttom que escollamos
                if (rbsobreescribir.isChecked()) {
                        OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(rutaCompleta, sobreescribir));
                        osw.write(textoedit.getText() + "  " + data + "\n");
                        osw.close();
                        Log.i("Ruta completa do ficheiro", " " + rutaCompleta.toString() + "\n");
                        Log.i("Linea que se engade", textoedit.getText().toString() + " " + data + "\n");
                        textoedit.setText("");

                    }
                if (rbengadir.isChecked()) {
                        OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(rutaCompleta, true));
                        osw.write(textoedit.getText() + "  " + data + "\n");
                        osw.close();
                        Log.i("Ruta completa do ficheiro", "" + rutaCompleta.toString() + "\n");
                        Log.i("Linea que se engade",textoedit.getText().toString()+" " + data + "\n");
                        textoedit.setText("");
                    }
                }else
                        Toast.makeText(this, "A tarxeta SD non está en modo acceso escritura", Toast.LENGTH_SHORT).show();
                }
                catch (Exception ex) {
                Log.e("SD", "Error escribindo no ficheiro");
                }

    }
    //Crease un BufferReader para poder manipular cadeas de texto
    public ArrayList<String> OnLerClick(String ruta){
        ArrayList <String>marcascoche1=new ArrayList<String>();
        String linha="";
        if (sdDisponible) {
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(ruta)));
                while ((linha = br.readLine()) != null)
                    marcascoche1.add(linha);

                br.close();

            } catch (Exception ex) {
                Toast.makeText(this, "Problemas lendo o ficheiro", Toast.LENGTH_SHORT).show();
                Log.e("SD", "Erro lendo o ficheiro.");
            }
        } else
            Toast.makeText(this, "A tarxeta SD non está dispoñible", Toast.LENGTH_SHORT).show();
        return marcascoche1;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    //Facemos o dialogo para escoller si usamos spinner ou listview,dialogo_lista e o usado a continuación:
    protected Dialog onCreateDialog(int id) {

            venta = new AlertDialog.Builder(this);
            venta.setIcon(android.R.drawable.ic_dialog_info);
            venta.setTitle("Escolle Spinner ou ListView");
            venta.setItems(R.array.arrays, new DialogInterface.OnClickListener() {
                //Chamamos polas otras clases donde creamos o listview e o spinner
                //Usase o intent.putExtra para gardar o valor clave (RutaCompleta)
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String[] opcions = getResources().getStringArray(R.array.arrays);

                    if (which == 0) {

                        i = new Intent(U4_Coches_a15lorenaae.this, spinner.class);
                        i.putExtra("Ruta", rutaCompleta.toString());
                        startActivity(i);

                    }
                    else
                        i = new Intent(U4_Coches_a15lorenaae.this, listview.class);
                        i.putExtra("Ruta", rutaCompleta.toString());
                        startActivity(i);

                }
            });
            return venta.create();
        }
    }




