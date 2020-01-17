package tasnuvaoshin.com.retrofitimageupload;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 99 ;
    private static final int CAPTURE_REQUEST_CODE = 0;
    private static final int SELECT_REQUEST_CODE = 1;
    private Button captureImage,selectImage;
    private ImageView imageView;
    private OurRetrofitClient ourRetrofitClient;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        captureImage = findViewById(R.id.capture_image);
        selectImage = findViewById(R.id.select_image);
        imageView = findViewById(R.id.Image_view);
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://joy-technologies-ltd.com/test/").addConverterFactory(GsonConverterFactory.create()).build();
     ourRetrofitClient = retrofit.create(OurRetrofitClient.class);
       progressDialog = new ProgressDialog(MainActivity.this);
       progressDialog.setMessage("Image Upload....");

        captureImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(CheckPermission()) {
                    Intent capture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(capture, CAPTURE_REQUEST_CODE);
                }
            }
        });


        selectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(CheckPermission()) {
                    Intent select = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(select, SELECT_REQUEST_CODE);
                }
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

       switch (requestCode){

           case CAPTURE_REQUEST_CODE:
           {
             if(resultCode == RESULT_OK){

                 Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                 imageView.setImageBitmap(bitmap);
                 progressDialog.show();
                 ImageUpload(bitmap);

             }

           }
           break;

           case SELECT_REQUEST_CODE:
           {
               if(resultCode == RESULT_OK){

                   try {
                       Uri ImageUri = data.getData();
                       Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), ImageUri);
                       imageView.setImageBitmap(bitmap);
                       progressDialog.show();
                       ImageUpload(bitmap);
                   } catch (IOException e) {
                       e.printStackTrace();
                   }

               }
           }
           break;
       }

    }

    private void ImageUpload(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        String image = Base64.encodeToString(byteArrayOutputStream.toByteArray(),Base64.DEFAULT);
        String name = String.valueOf(Calendar.getInstance().getTimeInMillis());
      Call<ResponseClass> call = ourRetrofitClient.UploadImage(name,image);

      call.enqueue(new Callback<ResponseClass>() {
          @Override
          public void onResponse(Call<ResponseClass> call, Response<ResponseClass> response) {
              Toast.makeText(MainActivity.this, ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();

              progressDialog.dismiss();
          }

          @Override
          public void onFailure(Call<ResponseClass> call, Throwable t) {
              Toast.makeText(MainActivity.this, "Failed to Upload", Toast.LENGTH_SHORT).show();
              progressDialog.dismiss();
          }
      });

    }


    public boolean CheckPermission() {
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE) || ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.CAMERA) || ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Permission")
                        .setMessage("Please accept the permissions")
                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(MainActivity.this,
                                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                        MY_PERMISSIONS_REQUEST_LOCATION);


                                startActivity(new Intent(MainActivity
                                        .this, MainActivity.class));
                                MainActivity.this.overridePendingTransition(0, 0);
                            }
                        })
                        .create()
                        .show();


            } else {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }

            return false;
        } else {

            return true;

        }
    }
}
