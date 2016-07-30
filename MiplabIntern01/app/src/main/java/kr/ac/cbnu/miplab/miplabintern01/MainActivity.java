package kr.ac.cbnu.miplab.miplabintern01;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private static final int PICK_FROM_CAMERA = 1;
    private static final int PICK_FROM_GALLERY = 2;
    private ImageView imageView;
    private Button buttonCamera, buttonGallery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initListener();
    }

    private void initView() {
        imageView = (ImageView) findViewById(R.id.imageView);
        buttonCamera = (Button) findViewById(R.id.btn_camera);
        buttonGallery = (Button) findViewById(R.id.btn_gallery);
    }

    private void initListener() {

        buttonCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI.toString());

                intent.putExtra("crop", "true");
                intent.putExtra("aspectX", 0);
                intent.putExtra("aspectY", 0);
                intent.putExtra("outputX", 200);
                intent.putExtra("outputY", 150);

                try {
                    intent.putExtra("return-data", true);
                    startActivityForResult(intent, PICK_FROM_CAMERA);
                } catch (ActivityNotFoundException e) {
                    // Do nothing for now
                }
            }
        });

        buttonGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);

                intent.putExtra("crop", "true");
                intent.putExtra("aspectX", 0);
                intent.putExtra("aspectY", 0);
                intent.putExtra("outputX", 200);
                intent.putExtra("outputY", 150);

                try {
                    intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, PICK_FROM_GALLERY);
                } catch (ActivityNotFoundException e) {
                    // Do nothing for now
                }
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        Bundle extras = data.getExtras();

        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            switch (requestCode) {
                case PICK_FROM_CAMERA:
                    imageView.setImageBitmap(photo);
                    break;

                case PICK_FROM_GALLERY:
                    imageView.setImageBitmap(photo);
                    break;
            }

            saveImage(photo);
        }
    }

    private void saveImage(Bitmap photo) {

        final File saveDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + File.separator + "MIPLAB" + File.separator);

        String filename = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date(System.currentTimeMillis()));

        File fileCacheItem = new File(saveDir + File.separator + filename + ".jpg");
        Log.e("FILE NAME : ", saveDir + File.separator + filename + ".jpg");
        OutputStream out = null;

        if (!saveDir.exists() && !saveDir.isDirectory()) {
            saveDir.mkdir(); /* Create Directory "MIPMAP" */
        }

        try {
            fileCacheItem.createNewFile();
            out = new FileOutputStream(fileCacheItem);
            photo.compress(Bitmap.CompressFormat.JPEG, 100, out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
