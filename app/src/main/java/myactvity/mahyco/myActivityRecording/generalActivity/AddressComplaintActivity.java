package myactvity.mahyco.myActivityRecording.generalActivity;

import android.content.Intent;
import android.net.Uri;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import myactvity.mahyco.R;

public class AddressComplaintActivity extends AppCompatActivity {
    Button btnAddressComplaint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_complaint);
        btnAddressComplaint = (Button) findViewById(R.id.btnAddressComplaint);

        btnAddressComplaint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("http://ccf.mahyco.com/"); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
    }
}
