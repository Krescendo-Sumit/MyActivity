package myactvity.mahyco.app;

/**
 * Created by SHAJIB on 7/4/2017.
 */
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import myactvity.mahyco.Utility;
import myactvity.mahyco.helper.Messageclass;
import myactvity.mahyco.helper.SearchableSpinner;
import myactvity.mahyco.helper.SqliteDatabase;


public class Function {

    // Project Created by Ferdousur Rahman Shajib
    // www.androstock.com

    private static final String OPEN_WEATHER_MAP_URL =
            "http://api.openweathermap.org/data/2.5/weather?lat=%s&lon=%s&units=metric";

    private static final String OPEN_WEATHER_MAP_API = "66f1394a9d2c5d008387be1da9d44861";

    public static String setWeatherIcon(int actualId, long sunrise, long sunset){
        int id = actualId / 100;
        String icon = "";
        if(actualId == 800){
            long currentTime = new Date().getTime();
            if(currentTime>=sunrise && currentTime<sunset) {
                icon = "&#xf00d;";
            } else {
                icon = "&#xf02e;";
            }
        } else {
            switch(id) {
                case 2 : icon = "&#xf01e;";
                    break;
                case 3 : icon = "&#xf01c;";
                    break;
                case 7 : icon = "&#xf014;";
                    break;
                case 8 : icon = "&#xf013;";
                    break;
                case 6 : icon = "&#xf01b;";
                    break;
                case 5 : icon = "&#xf019;";
                    break;
            }
        }
        return icon;
    }



    public interface AsyncResponse {

        void processFinish(String output1, String output2, String output3, String output4, String output5, String output6, String output7, String output8);
    }





    public static class placeIdTask extends AsyncTask<String, Void, JSONObject> {

        public AsyncResponse delegate = null;//Call back interface

        public placeIdTask(AsyncResponse asyncResponse) {
            delegate = asyncResponse;//Assigning call back interfacethrough constructor
        }

        @Override
        protected JSONObject doInBackground(String... params) {

            JSONObject jsonWeather = null;
            try {
                jsonWeather = getWeatherJSON(params[0], params[1]);
            } catch (Exception e) {
                Log.d("Error", "Cannot process JSON results", e);
            }


            return jsonWeather;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            try {
                if(json != null){
                    JSONObject details = json.getJSONArray("weather").getJSONObject(0);
                    JSONObject main = json.getJSONObject("main");
                    DateFormat df = DateFormat.getDateTimeInstance();


                    String city = json.getString("name").toUpperCase(Locale.US) + ", " + json.getJSONObject("sys").getString("country");
                    String description = details.getString("description").toUpperCase(Locale.US);
                    String temperature = String.format("%.2f", main.getDouble("temp"))+ "°";
                    String humidity = main.getString("humidity") + "%";
                    String pressure = main.getString("pressure") + " hPa";
                    String updatedOn = df.format(new Date(json.getLong("dt")*1000));
                    String iconText = setWeatherIcon(details.getInt("id"),
                            json.getJSONObject("sys").getLong("sunrise") * 1000,
                            json.getJSONObject("sys").getLong("sunset") * 1000);

                    delegate.processFinish(city, description, temperature, humidity, pressure, updatedOn, iconText, ""+ (json.getJSONObject("sys").getLong("sunrise") * 1000));

                }
            } catch (JSONException e) {
                //Log.e(LOG_TAG, "Cannot process JSON results", e);
            }



        }
    }



    public static JSONObject getWeatherJSON(String lat, String lon){
        try {
            URL url = new URL(String.format(OPEN_WEATHER_MAP_URL, lat, lon));
            HttpURLConnection connection =
                    (HttpURLConnection)url.openConnection();

            connection.addRequestProperty("x-api-key", OPEN_WEATHER_MAP_API);

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));

            StringBuffer json = new StringBuffer(1024);
            String tmp="";
            while((tmp=reader.readLine())!=null)
                json.append(tmp).append("\n");
            reader.close();

            JSONObject data = new JSONObject(json.toString());

            // This value will be 404 if the request was not
            // successful
            if(data.getInt("cod") != 200){
                return null;
            }

            return data;
        }catch(Exception e){
            return null;
        }
    }
    public static void bindState(SearchableSpinner spState, SqliteDatabase mDatabase, Context mContext, Messageclass msclass) {

        try {
            spState.setAdapter(null);
            String str = null;
            try {
                List<GeneralMaster> list = new ArrayList<GeneralMaster>();
                String searchQuery = "SELECT distinct state,state_code  FROM VillageLevelMaster order by state asc  ";
                Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
                list.add(new GeneralMaster("SELECT STATE",
                        "SELECT STATE"));

                cursor.moveToFirst();
                while (cursor.isAfterLast() == false) {
                    list.add(new GeneralMaster(cursor.getString(1),
                            cursor.getString(0).toUpperCase()));

                    cursor.moveToNext();
                }
                cursor.close();
                ArrayAdapter<GeneralMaster> adapter = new ArrayAdapter<GeneralMaster>
                        (mContext, android.R.layout.simple_spinner_dropdown_item, list);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spState.setAdapter(adapter);
            } catch (Exception e) {
                e.printStackTrace();

            }

        } catch (Exception ex) {
            msclass.showMessage(ex.getMessage());
            ex.printStackTrace();

        }
    }

    public static List<GeneralMaster>  getBindStatelist(SearchableSpinner spState, SqliteDatabase mDatabase, Context mContext, Messageclass msclass) {
        List<GeneralMaster> list = new ArrayList<GeneralMaster>();

        try {
            spState.setAdapter(null);
            String str = null;
            try {

                String searchQuery = "SELECT distinct state,state_code  FROM VillageLevelMaster order by state asc  ";
                Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
                list.add(new GeneralMaster("SELECT STATE",
                        "SELECT STATE"));

                cursor.moveToFirst();
                while (cursor.isAfterLast() == false) {
                    list.add(new GeneralMaster(cursor.getString(1),
                            cursor.getString(0).toUpperCase()));

                    cursor.moveToNext();
                }
                cursor.close();
                ArrayAdapter<GeneralMaster> adapter = new ArrayAdapter<GeneralMaster>
                        (mContext, android.R.layout.simple_spinner_dropdown_item, list);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spState.setAdapter(adapter);
            } catch (Exception e) {
                e.printStackTrace();

            }

        } catch (Exception ex) {
            msclass.showMessage(ex.getMessage());
            ex.printStackTrace();

        }
        return list;
    }



    public static void bindDist(String state, SearchableSpinner spDist, SqliteDatabase mDatabase, Context mContext, Messageclass msclass) {
        try {
            spDist.setAdapter(null);
            // dialog.setMessage("Loading....");
            //dialog.show();
            String str = null;
            try {
                List<GeneralMaster> list = new ArrayList<GeneralMaster>();
                String searchQuery = "SELECT distinct district,district_code  FROM VillageLevelMaster" +
                        " where state_code='" + state + "' order by district asc  ";

                // String searchQuery = "SELECT distinct district,district_code  FROM VillageLevelMaster order by district asc  ";
                Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
                list.add(new GeneralMaster("SELECT DISTRICT",
                        "SELECT DISTRICT"));

                cursor.moveToFirst();
                while (cursor.isAfterLast() == false) {
                    list.add(new GeneralMaster(cursor.getString(0),
                            cursor.getString(0).toUpperCase()));

                    cursor.moveToNext();
                }
                cursor.close();
                ArrayAdapter<GeneralMaster> adapter = new ArrayAdapter<GeneralMaster>
                        (mContext, android.R.layout.simple_spinner_dropdown_item, list);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spDist.setAdapter(adapter);
                // dialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
                // dialog.dismiss();
            }

        } catch (Exception ex) {
            msclass.showMessage(ex.getMessage());
            ex.printStackTrace();
            // dialog.dismiss();
        }
    }


    public static List<GeneralMaster> getbindDistlist(String state, SearchableSpinner spDist, SqliteDatabase mDatabase, Context mContext, Messageclass msclass) {
        List<GeneralMaster> list = new ArrayList<GeneralMaster>();
        try {
            spDist.setAdapter(null);
            // dialog.setMessage("Loading....");
            //dialog.show();
            String str = null;
            try {

                String searchQuery = "SELECT distinct district,district_code  FROM VillageLevelMaster" +
                        " where state='" + state + "' order by district asc  ";

                // String searchQuery = "SELECT distinct district,district_code  FROM VillageLevelMaster order by district asc  ";
                Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
                list.add(new GeneralMaster("SELECT DISTRICT",
                        "SELECT DISTRICT"));

                cursor.moveToFirst();
                while (cursor.isAfterLast() == false) {
                    list.add(new GeneralMaster(cursor.getString(0),
                            cursor.getString(0).toUpperCase()));

                    cursor.moveToNext();
                }
                cursor.close();
                ArrayAdapter<GeneralMaster> adapter = new ArrayAdapter<GeneralMaster>
                        (mContext, android.R.layout.simple_spinner_dropdown_item, list);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spDist.setAdapter(adapter);
                // dialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
                // dialog.dismiss();
            }

        } catch (Exception ex) {
            msclass.showMessage(ex.getMessage());
            ex.printStackTrace();
            // dialog.dismiss();
        }
        return list;
    }



    public static List<GeneralMaster> getbindTalukalist(String dist, SearchableSpinner spTaluka, Context baseContext, SqliteDatabase mDatabase, Messageclass msclass) {
        List<GeneralMaster> list = new ArrayList<GeneralMaster>();

        try {
            spTaluka.setAdapter(null);
            //.setMessage("Loading....");
            // dialog.show();
            String str = null;
            try {

                // str = cx.new getTaluka(dist).execute().get();
                String district = dist;//.substring(0, 1).toUpperCase() + dist.substring(1).toLowerCase();

                String searchQuery = "SELECT distinct taluka,taluka_code  FROM VillageLevelMaster where upper(district)='" + district.toUpperCase() + "' order by taluka asc  ";
                Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
                list.add(new GeneralMaster("SELECT TALUKA",
                        "SELECT TALUKA"));

                cursor.moveToFirst();
                while (cursor.isAfterLast() == false) {
                    list.add(new GeneralMaster(cursor.getString(0),
                            cursor.getString(0).toUpperCase()));

                    cursor.moveToNext();
                }
                cursor.close();
                ArrayAdapter<GeneralMaster> adapter = new ArrayAdapter<GeneralMaster>
                        (baseContext, android.R.layout.simple_spinner_dropdown_item, list);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spTaluka.setAdapter(adapter);
                // dialog.dismiss();


            } catch (Exception ex) {
                msclass.showMessage(ex.getMessage());
                ex.printStackTrace();
            }
        } catch (Exception ex) {
            msclass.showMessage(ex.getMessage());
            ex.printStackTrace();
            // dialog.dismiss();
        }
        return  list;
    }


    public static void bindTaluka(String dist, SearchableSpinner spTaluka, Context baseContext, SqliteDatabase mDatabase, Messageclass msclass) {
        try {
            spTaluka.setAdapter(null);
            //.setMessage("Loading....");
            // dialog.show();
            String str = null;
            try {

                // str = cx.new getTaluka(dist).execute().get();

                List<GeneralMaster> list = new ArrayList<GeneralMaster>();
                String searchQuery = "SELECT distinct taluka,taluka_code  FROM VillageLevelMaster where upper(district)='" + dist.toUpperCase() + "' order by taluka asc  ";
                Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
                list.add(new GeneralMaster("SELECT TALUKA",
                        "SELECT TALUKA"));

                cursor.moveToFirst();
                while (cursor.isAfterLast() == false) {
                    list.add(new GeneralMaster(cursor.getString(0),
                            cursor.getString(0).toUpperCase()));

                    cursor.moveToNext();
                }
                cursor.close();
                ArrayAdapter<GeneralMaster> adapter = new ArrayAdapter<GeneralMaster>
                        (baseContext, android.R.layout.simple_spinner_dropdown_item, list);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spTaluka.setAdapter(adapter);
                // dialog.dismiss();


            } catch (Exception ex) {
                msclass.showMessage(ex.getMessage());
                ex.printStackTrace();
            }
        } catch (Exception ex) {
            msclass.showMessage(ex.getMessage());
            ex.printStackTrace();
            // dialog.dismiss();
        }
    }


    public static void bindcroptype(MultiSelectionSpinner spCropType,  String Croptype, Context mContext, SqliteDatabase mDatabase, Messageclass msclass) {
        try {
            //st
            List<GeneralMaster> Croplist = new ArrayList<GeneralMaster>();
            String myTable = "Table1";//Set name of your table
            String searchQuery = "";
            if (Croptype.equals("V")) {
                searchQuery = "SELECT distinct CropName,CropType  FROM CropMaster where CropType='" + Croptype + "' ";

            } else {
                //searchQuery = "SELECT distinct CropName,CropType  FROM CropMaster where CropType<>'V' ";
                searchQuery = "SELECT distinct CropName,CropType  FROM CropMaster  ";

            }
            Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
            getCropArrayList(searchQuery, mDatabase, spCropType,null, mContext);

            cursor.moveToFirst();
            while (cursor.isAfterLast() == false) {

                Croplist.add(new GeneralMaster(cursor.getString(1),
                        cursor.getString(0)));

                cursor.moveToNext();
            }
            cursor.close();
            ArrayAdapter<GeneralMaster> adapter = new ArrayAdapter<GeneralMaster>
                    (mContext, android.R.layout.simple_spinner_dropdown_item, Croplist);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        } catch (Exception ex) {
            msclass.showMessage(ex.getMessage());
            ex.printStackTrace();
        }
    }

    public static void bindcroptype(MultiSelectionSpinner spCropType, MultiSelectionSpinner    spCropType1,  String Croptype, Context mContext, SqliteDatabase mDatabase, Messageclass msclass) {
        try {
            //st
            List<GeneralMaster> Croplist = new ArrayList<GeneralMaster>();
            String myTable = "Table1";//Set name of your table
            String searchQuery = "";
            if (Croptype.equals("V")) {
                searchQuery = "SELECT distinct CropName,CropType  FROM CropMaster where CropType='" + Croptype + "' ";

            } else {
                //searchQuery = "SELECT distinct CropName,CropType  FROM CropMaster where CropType<>'V' ";
                searchQuery = "SELECT distinct CropName,CropType  FROM CropMaster  ";

            }
            Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
            getCropArrayList(searchQuery, mDatabase, spCropType,spCropType1, mContext);

            cursor.moveToFirst();
            while (cursor.isAfterLast() == false) {

                Croplist.add(new GeneralMaster(cursor.getString(1),
                        cursor.getString(0)));

                cursor.moveToNext();
            }
            cursor.close();
            ArrayAdapter<GeneralMaster> adapter = new ArrayAdapter<GeneralMaster>
                    (mContext, android.R.layout.simple_spinner_dropdown_item, Croplist);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        } catch (Exception ex) {
            msclass.showMessage(ex.getMessage());
            ex.printStackTrace();
        }
    }


    /**
     * <P>Method is used to get the product list according  to crop type and set items </P>
     * @param searchQuery
     */
    private static void getCropArrayList(String searchQuery, SqliteDatabase mDatabase, MultiSelectionSpinner spCropType,  MultiSelectionSpinner spCropType1     , Context mcontext) {

        String[] array;
        try {
            JSONObject object = new JSONObject();
            object.put("Table", mDatabase.getResults(searchQuery));

            JSONArray jArray = object.getJSONArray("Table");//new JSONArray(result);
            array = new String[jArray.length() + 1];
            array[0] = "SELECT CROP";
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject jObject = jArray.getJSONObject(i);
                array[i + 1] = jObject.getString("CropName").trim();
            }

            if (array.length > 0) {
                spCropType.setItems(array);
                spCropType.hasNoneOption(true);
                spCropType.setSelection(new int[]{0});

                if(spCropType1!=null){
                    spCropType1.setItems(array);
                    spCropType1.hasNoneOption(true);
                    spCropType1.setSelection(new int[]{0});}
            }

        } catch (Exception ex) {
            Utility.showAlertDialog("Error", ex.getMessage(), mcontext);
            ex.printStackTrace();

        }
    }



    public static void bindProductName(MultiSelectionSpinner spProductName, String croptype, Context mcontext, SqliteDatabase mDatabase, Messageclass msclass) {
        //st
        try {
            List<GeneralMaster> Croplist = new ArrayList<GeneralMaster>();
            String myTable = "Table1";//Set name of your table
            String searchQuery = "";
            StringBuilder nameBuilder = new StringBuilder();

            if (croptype.length() > 0) {
                for (String n : croptype.substring(1, croptype.length() - 1).split(",")) {
                    nameBuilder.append("'").append(n.trim().replace("'", "\\'")).append("',");
                    searchQuery = "SELECT * FROM CropMaster WHERE CropName  IN (" + nameBuilder.toString().substring(0, nameBuilder.length() - 1) + ") ORDER BY 'CropName'";

                }
            } else {
                Log.d("Crop type", "First time");
            }

            Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);

            getArrayList(searchQuery,mcontext,mDatabase,spProductName, null);
            cursor.close();
            ArrayAdapter<GeneralMaster> adapter = new ArrayAdapter<GeneralMaster>
                    (mcontext, android.R.layout.simple_spinner_dropdown_item, Croplist);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static  String[] getbindProductNamelist(MultiSelectionSpinner spProductName, String croptype, Context mcontext, SqliteDatabase mDatabase, Messageclass msclass) {
        List<GeneralMaster> list = new ArrayList<GeneralMaster>();
        String[] ar= null;
        try {
            String myTable = "Table1";//Set name of your table
            String searchQuery = "";
            StringBuilder nameBuilder = new StringBuilder();

            if (croptype.length() > 0) {
                for (String n : croptype.substring(1, croptype.length() - 1).split(",")) {
                    nameBuilder.append("'").append(n.trim().replace("'", "\\'")).append("',");
                    searchQuery = "SELECT * FROM CropMaster WHERE CropName  IN (" + nameBuilder.toString().substring(0, nameBuilder.length() - 1) + ") ORDER BY 'CropName'";

                }
            } else {
                Log.d("Crop type", "First time");
            }

            Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);

            ar = getArrayList(searchQuery,mcontext,mDatabase,spProductName, null);
            cursor.close();
            ArrayAdapter<GeneralMaster> adapter = new ArrayAdapter<GeneralMaster>
                    (mcontext, android.R.layout.simple_spinner_dropdown_item, list);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return  ar;
    }




    public static void bindProductName(MultiSelectionSpinner spProductName, MultiSelectionSpinner spProductName1, String croptype, Context mcontext, SqliteDatabase mDatabase, Messageclass msclass) {
        //st
        try {
            List<GeneralMaster> Croplist = new ArrayList<GeneralMaster>();
            String myTable = "Table1";//Set name of your table
            String searchQuery = "";
            StringBuilder nameBuilder = new StringBuilder();

            if (croptype.length() > 0) {
                for (String n : croptype.substring(1, croptype.length() - 1).split(",")) {
                    nameBuilder.append("'").append(n.trim().replace("'", "\\'")).append("',");
                    searchQuery = "SELECT * FROM CropMaster WHERE CropName  IN (" + nameBuilder.toString().substring(0, nameBuilder.length() - 1) + ") ORDER BY 'CropName'";

                }
            } else {
                Log.d("Crop type", "First time");
            }

            Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);

            getArrayList(searchQuery,mcontext,mDatabase,spProductName, spProductName1);
            cursor.close();
            ArrayAdapter<GeneralMaster> adapter = new ArrayAdapter<GeneralMaster>
                    (mcontext, android.R.layout.simple_spinner_dropdown_item, Croplist);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * <P>Method is used to get the product list according  to crop type and set items </P>
     * @param searchQuery
     * @param spProductName
     */
    private static String[] getArrayList(String searchQuery, Context mcontext, SqliteDatabase mDatabase, MultiSelectionSpinner spProductName, MultiSelectionSpinner spProductName1) {

        String[] array=null;
        try {
            JSONObject object = new JSONObject();
            object.put("Table", mDatabase.getResults(searchQuery));

            JSONArray jArray = object.getJSONArray("Table");//new JSONArray(result);
            array = new String[jArray.length() + 1];
            array[0] = "SELECT PRODUCT";
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject jObject = jArray.getJSONObject(i);
                array[i + 1] = jObject.getString("ProductName").trim();
            }

            if (array.length > 0) {
                spProductName.setItems(array);
                spProductName.hasNoneOption(true);
                spProductName.setSelection(new int[]{0});

                if(spProductName1!=null) {
                    spProductName1.setItems(array);
                    spProductName1.hasNoneOption(true);
                    spProductName1.setSelection(new int[]{0});
                }
            }

        } catch (Exception ex) {
            Utility.showAlertDialog("Error", ex.getMessage(), mcontext);
            ex.printStackTrace();

        }
        return array;
    }
    /**
     * <P>Method to get the current date time</P>
     * @return
     */
    public static String getCurrentDate() {
        /*SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date today = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            today = Calendar.getInstance().getTime();
        }
        SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        String finalDate = timeFormat.format(today);
*/
        Date entrydate = new Date();
        final String InTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(entrydate);


        return InTime;
    }


    public static String ConvertDateFormat(String entryDt)  {

        Date myDate = null;
        String finalDate ="";
        SimpleDateFormat dateFormat;

        if(entryDt!=null) {

            try {

                dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                myDate = dateFormat.parse(entryDt);

            } catch (ParseException e) {

                try {
                    dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
                    myDate = dateFormat.parse(entryDt);

                } catch (ParseException e1) {
                    try {
                        dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                        myDate = dateFormat.parse(entryDt);
                    } catch (ParseException ex2) {

                        try {

                            dateFormat = new SimpleDateFormat("M/d/yyyy");
                            myDate = dateFormat.parse(entryDt);

                        } catch (ParseException ex) {
                            ex.printStackTrace();
                        }
                    }


                }
            }

            SimpleDateFormat timeFormat = new SimpleDateFormat("dd MMM yyyy");
            finalDate = timeFormat.format(myDate);

        }

        return  finalDate;
    }





}
