package kr.ac.cbnu.miplab.miplabintern01;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.FileNotFoundException;
import java.io.IOException;

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

    private void initView(){
        imageView = (ImageView) findViewById(R.id.imageView);
        buttonCamera = (Button) findViewById(R.id.btn_camera);
        buttonGallery = (Button) findViewById(R.id.btn_gallery);
    }

    private void initListener(){

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

                intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                
                startActivityForResult(intent, PICK_FROM_GALLERY);

                /*
                Intent intent = new Intent().setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);

                intent.putExtra("crop", "true");
                intent.putExtra("aspectX", 0);
                intent.putExtra("aspectY", 0);
                intent.putExtra("outputX", 200);
                intent.putExtra("outputY", 150);

                try {
                    intent.putExtra("return-data", true);
                    startActivityForResult(Intent.createChooser(intent, "Choose an image"), PICK_FROM_GALLERY);
                } catch (ActivityNotFoundException e) {
                    // Do nothing for now
                }*/
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == PICK_FROM_CAMERA) {
            Bundle extras = data.getExtras();
            if (extras != null) {
                Bitmap photo = extras.getParcelable("data");
                imageView.setImageBitmap(photo);
            }
        }
        if (requestCode == PICK_FROM_GALLERY) {

            try {
                //Uri에서 이미지 이름을 얻어온다.
                //String name_Str = getImageNameToUri(data.getData());

                //이미지 데이터를 비트맵으로 받아온다.
                Bitmap image_bitmap 	= MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                ImageView image = (ImageView)findViewById(R.id.imageView);

                //배치해놓은 ImageView에 set
                image.setImageBitmap(image_bitmap);

                //Toast.makeText(getBaseContext(), "name_Str : "+name_Str , Toast.LENGTH_SHORT).show();

            }
            catch (FileNotFoundException e) { 		e.printStackTrace(); 			}
            catch (IOException e)                 {		e.printStackTrace(); 			}
            catch (Exception e)		         {             e.printStackTrace();			}


            /*
            Bundle extras2 = data.getExtras();
            Log.e("GALLERY : ", data.getExtras().toString());
            if (extras2 != null) {
                Bitmap photo = extras2.getParcelable("data");
                imageView.setImageBitmap(photo);
                Log.e("if문 안 : ", "들어왔다");
            }*/
        }
    }
}
