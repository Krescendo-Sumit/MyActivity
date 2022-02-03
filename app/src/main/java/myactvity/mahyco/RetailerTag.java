package myactvity.mahyco;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import myactvity.mahyco.app.GeneralMaster;

public class RetailerTag extends AppCompatActivity {

    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    TextView lblwelcome;
    public Spinner spDist, spTaluka, spState, spCropType, spProductName, spMyactvity,spComment;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retailer_tag);
        getSupportActionBar().hide(); //<< this
        context = this;

        spDist = (Spinner) findViewById(R.id.spDist);
        spTaluka = (Spinner) findViewById(R.id.spTaluka);
        spState = (Spinner) findViewById(R.id.spState);

        BindIntialData();
    }

    private  void BindIntialData() {

        spDist.setAdapter(null);
        List<GeneralMaster> gm = new ArrayList<GeneralMaster>();
        gm.add(new GeneralMaster("0","SELECT DISTRICT"));
        gm.add(new GeneralMaster("1000","JALNA"));

        ArrayAdapter<GeneralMaster> adapter = new ArrayAdapter<GeneralMaster>
                (this.context,android.R.layout.simple_spinner_dropdown_item, gm);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spDist.setAdapter(adapter);

        spTaluka.setAdapter(null);
        List<GeneralMaster> gm2 = new ArrayList<GeneralMaster>();
        gm2.add(new GeneralMaster("0","SELECT TALUKA"));
        gm2.add(new GeneralMaster("01","JALNA"));
        ArrayAdapter<GeneralMaster> adapter2 = new ArrayAdapter<GeneralMaster>
                (this.context,android.R.layout.simple_spinner_dropdown_item, gm2);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spTaluka.setAdapter(adapter2);


        spState.setAdapter(null);
        List<GeneralMaster> gm3 = new ArrayList<GeneralMaster>();
        gm3.add(new GeneralMaster("0","SELECT STATE"));
        gm3.add(new GeneralMaster("01","MAHARASHTRA"));
        ArrayAdapter<GeneralMaster> adapter3 = new ArrayAdapter<GeneralMaster>
                (this.context,android.R.layout.simple_spinner_dropdown_item, gm3);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spState.setAdapter(adapter3);


    }
}
