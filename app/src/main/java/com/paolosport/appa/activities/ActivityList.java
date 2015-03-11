package com.paolosport.appa.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.paolosport.appa.R;
import com.paolosport.appa.persistencia.AdminSQLiteOpenHelper;
import com.paolosport.appa.persistencia.dao.LocalDAO;
import com.paolosport.appa.persistencia.dao.MarcaDAO;
import com.paolosport.appa.persistencia.dao.PersonaDAO;
import com.paolosport.appa.persistencia.dao.PrestamoDAO;
import com.paolosport.appa.persistencia.entities.Prestamo;
import com.paolosport.appa.ui.FiltrosFragment;
import com.paolosport.appa.ui.PrestamoAdapter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

public class ActivityList extends ActionBarActivity {

    FiltrosFragment filtrosFrag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        filtrosFrag = new FiltrosFragment();

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.add(R.id.fragment_container, filtrosFrag);
        fragmentTransaction.commit();
        fragmentManager.executePendingTransactions();


        AdminSQLiteOpenHelper adminSQLiteOpenHelper = new AdminSQLiteOpenHelper(getApplicationContext());
        MarcaDAO marcaDAO = new MarcaDAO(getApplicationContext(), adminSQLiteOpenHelper);
        LocalDAO localDAO = new LocalDAO(getApplicationContext(), adminSQLiteOpenHelper);
        PersonaDAO personaDAO = new PersonaDAO(getApplicationContext(), adminSQLiteOpenHelper);
        PrestamoDAO prestamoDAO = new PrestamoDAO(getApplicationContext(), adminSQLiteOpenHelper);
        prestamoDAO.setPersonaDAO(personaDAO);
        prestamoDAO.setMarcaDAO(marcaDAO);
        prestamoDAO.setLocalDAO(localDAO);

        prestamoDAO.open();
        ArrayList<Prestamo> prestamos = prestamoDAO.retrieveAll();
        prestamoDAO.close();

        PrestamoAdapter adapter = new PrestamoAdapter(this, R.layout.prestamo_item, prestamos);

        if (prestamos == null)
            Toast.makeText(this, "no hay datos", Toast.LENGTH_SHORT);

        ListView lstPrestamos = (ListView) findViewById(R.id.lstPrestamos);
        lstPrestamos.setAdapter(adapter);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_list, menu);
        return true;
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

    public void getImage(View view) {

        final CharSequence[] options = {getString(R.string.take_photo),
                getString(R.string.from_gallery),
                getString(R.string.cancel)};

        AlertDialog.Builder builder = new AlertDialog.Builder(ActivityList.this);

        builder.setTitle(getString(R.string.choose_image));
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals(getString(R.string.take_photo))) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                    startActivityForResult(intent, 1);
                } else if (options[item].equals(getString(R.string.from_gallery))) {
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);
                } else if (options[item].equals(getString(R.string.cancel))) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    } // end method  getImage

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                File f = new File(Environment.getExternalStorageDirectory().toString());
                for (File temp : f.listFiles()) {
                    if (temp.getName().equals("temp.jpg")) {
                        f = temp;
                        break;
                    }
                }
                try {
                    Bitmap bitmap;
                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                    bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(),
                            bitmapOptions);
                    filtrosFrag.publishImage(bitmap);
                    String path = android.os.Environment
                            .getExternalStorageDirectory()
                            + File.separator
                            + "Phoenix" + File.separator + "default";
                    f.delete();
                    OutputStream outFile = null;
                    File file = new File(path, String.valueOf(System.currentTimeMillis()) + ".jpg");
                    try {
                        outFile = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 85, outFile);
                        outFile.flush();
                        outFile.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (requestCode == 2) {
                Uri selectedImage = data.getData();
                String[] filePath = {MediaStore.Images.Media.DATA};
                Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);
                c.close();
                // Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
                Bitmap thumbnail = null;
                try {
                    Uri uri = Uri.parse( "file://" + picturePath );
                    thumbnail = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                // Log.w("path of image from gallery......******************.........", picturePath + "");
                filtrosFrag.publishImage(thumbnail);
            }
        }
    }
}
