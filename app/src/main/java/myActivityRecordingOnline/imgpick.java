package myActivityRecordingOnline;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickResult;

import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.Date;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import myactvity.mahyco.R;

public class imgpick extends AppCompatActivity
        implements IPickResult,View.OnClickListener /*, IPickClick */ {
    public Button btnTakeFarmerPhoto;
    ImageView ivImage, roundedImageView;
    private Context context;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imgpick);

        /* Set your layout and bind your views here */
        /* ... ... ... */
        context = this;
        roundedImageView = (ImageView) findViewById(R.id.roundedImageView);
        btnTakeFarmerPhoto = (Button) findViewById(R.id.btnTakeFarmerPhoto);
        btnTakeFarmerPhoto.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnTakeFarmerPhoto:
                if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_DENIED) {
                    ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, 101);
                }
                PickImageDialog.build(new PickSetup()).show(this);
                break;
        }
    }


    @Override
    public void onPickResult(PickResult r) {
        if (r.getError() == null) {
            roundedImageView.setImageBitmap(r.getBitmap());
            if (roundedImageView.getDrawable() != null) {
                roundedImageView.setVisibility(View.VISIBLE);

            } else {
                roundedImageView.setVisibility(View.GONE);

            }
            Date entrydate = new Date();
            //Image path
            String pathImage = r.getPath();

            Log.d("encodedImage::", "logfile");
        } else {
            //Handle possible errors
            //TODO: do what you have to do with r.getError();
            Toast.makeText(this, r.getError().getMessage(), Toast.LENGTH_LONG).show();
        }
    }

   /* @Override
    public void onPickResult(final PickResult r) {
        if (r.getError() == null) {
            //If you want the Uri.
            //Mandatory to refresh image from Uri.
            //getImageView().setImageURI(null);

            //Setting the real returned image.
            //getImageView().setImageURI(r.getUri());

            //If you want the Bitmap.
            getImageView().setImageBitmap(r.getBitmap());

            //r.getPath();
        } else {
            //Handle possible errors
            //TODO: do what you have to do with r.getError();
            Toast.makeText(this, r.getError().getMessage(), Toast.LENGTH_LONG).show();
        }

        scrollToTop();
    }
*/

    //If you use setOnClick(this), you have to implements this bellow methods
    /*@Override
    public void onGalleryClick() {
        Toast.makeText(SampleActivity.this, "Gallery Click!", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onCameraClick() {
        Toast.makeText(SampleActivity.this, "Camera Click!", Toast.LENGTH_LONG).show();
    }*/
}
