package com.emprosoft7head.pdfcreator;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.pdf.PdfDocument;
import android.graphics.pdf.PdfRenderer;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    Bitmap mBitmap;
    ImageView viewpdf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(v -> {
            createDocumentPdf();
        });

        mBitmap =Bitmap.createBitmap(1000,1000,Bitmap.Config.ARGB_8888);
        viewpdf= findViewById(R.id.visorPdf);
    }

    public void createDocumentPdf() {

        // create a new document
        PdfDocument document = new PdfDocument();
        ScrollView content = findViewById(R.id.hacerpdf);
        File f = null;
        // crate a page description
        PdfDocument.PageInfo pageInfo = null;
        try {
            pageInfo = new PdfDocument.PageInfo.Builder(1000, 1000, 1).create();

        } catch (Exception e) {
            Log.e("eror", "sucede cuando no hay datos en la tabla");
        }
        // start a page
        if (pageInfo == null) {
            Toast.makeText(this, "no tienes datos", Toast.LENGTH_SHORT).show();
        } else {
            PdfDocument.Page page = document.startPage(pageInfo);
            // draw something on the page
            content.draw(page.getCanvas());

            // finish the page
            document.finishPage(page);
            // add more pages

            // write the document content
            f = new File(this.getExternalCacheDir(), "archivo_prueba.pdf");
            try {
                FileOutputStream fo = new FileOutputStream(f);
                document.writeTo(fo);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            // close the document
            document.close();
        }

    }


    public void readdocPdf(){
        PdfRenderer renderer = null;
        try{
         renderer = new PdfRenderer(getSeekableFileDescriptor());
    } catch (IOException e) {
        e.printStackTrace();
    }

        // let us just render all pages
        final int pageCount = renderer.getPageCount();
        for (int i = 0; i < pageCount; i++) {
            PdfRenderer.Page page = renderer.openPage(i);

try {
    page.render(mBitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);

}catch (Exception e){
    Log.e("error explota",e.toString());
}

            viewpdf.setImageBitmap(mBitmap);

            // close the page
            page.close();
        }

        // close the renderer
        renderer.close();


    }

    private ParcelFileDescriptor getSeekableFileDescriptor() {

        File ruta=new File(this.getExternalCacheDir(), "archivo_prueba.pdf");
        Uri uri=Uri.fromFile(ruta);
        ParcelFileDescriptor ParcelFileDescrip = null;
        try {

            ParcelFileDescrip= this.getContentResolver().openFileDescriptor(uri,"r");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return ParcelFileDescrip;
    }

    ;

        @Override
        public boolean onCreateOptionsMenu (Menu menu){
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.menu_main, menu);
            return true;
        }

        @Override
        public boolean onOptionsItemSelected (MenuItem item){
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

    public void abrirpdf(View view) {
        readdocPdf();

    }
}
