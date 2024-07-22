package myactvity.mahyco.travelreport;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import myactvity.mahyco.R;

public class ActivityTravelReportTriggered extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel_report_triggered);
    }

    public class MyTravelModel {
        boolean Status;
        String Message;
        ResultModel Result;
    }

    public class ResultModel {
        MyTravelReportModels myTravelReportModels;
        MyTravelKMModel myTravelKMModel;
    }

    public class MyTravelReportModels {

    }

    public class MyTravelKMModel {

    }

}