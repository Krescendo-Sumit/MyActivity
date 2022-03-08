
package myactvity.mahyco.services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;

//import com.farmer.myproject.Home;
//import farmmech.mahyco.services.MsgNotification;
//import com.farmer.myproject.R;

import myactvity.mahyco.LoginActivity;
import myactvity.mahyco.R;
import myactvity.mahyco.app.Config;
import myactvity.mahyco.helper.SqliteDatabase;
import myactvity.mahyco.utils.NotificationUtils;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private NotificationUtils notificationUtils;
    private static final String TAG = "MyFirebaseMsgService";
    private SqliteDatabase mDatabase;
    boolean flag=false;
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        try {
            //mDatabase = SqliteDatabase.getInstance(this);
            /*flag = mDatabase.isTableExists("NotificationDetail");
            if (flag == false) {
                mDatabase.CreateTable("NotificationDetail");
            }*/

            Map<String, String> data = remoteMessage.getData();
            String title ="";
            String message ="";

            if (data.isEmpty())
            {
                /*if(remoteMessage.getNotification().getTitle().isEmpty())
                {
                    title ="MAHYCO";
                }
                else
                {
                    title =remoteMessage.getNotification().getTitle();//.getTitle();
                } */
                title ="MAHYCO";//remoteMessage.getNotification().getTitle();//.getTitle();
                message = remoteMessage.getNotification().getBody();

            }
            else
            {
                title = data.get("title");
                message = data.get("message");

            }
            Date entrydate = new Date();
            // DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
            String endate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(entrydate);
           // mDatabase.insertNotification(title, message, endate);

            sendNotification2(title, message);//remoteMessage.getData().toString());
        /*if (remoteMessage.getData().toString().length() > 0) {
            Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());
            try {

                JSONObject json = new JSONObject(remoteMessage.getData().toString());
//                sendPushNotification(json);
                sendNotification(json);
            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }
        } */
        }
        catch (Exception ex)
        {
            Log.e(TAG, "Exception: " + ex.getMessage());
        }
    }

    private void handleNotification(String message) {
        if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
            // app is in foreground, broadcast the push message
            Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
            pushNotification.putExtra("message", message);
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

            // play notification sound
            NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
            notificationUtils.playNotificationSound();
        }else{
            // If the app is in background, firebase itself handles the notification
        }
    }

    private void handleDataMessage(JSONObject json) {
        Log.e(TAG, "push json: " + json.toString());

        try {
            JSONObject data = json.getJSONObject("data");
            String title = data.getString("title");
            String message = data.getString("message");
            boolean isBackground = data.getBoolean("is_background");
            String imageUrl = data.getString("image");
            String timestamp = data.getString("timestamp");
            JSONObject payload = data.getJSONObject("payload");

            Log.e(TAG, "title: " + title);
            Log.e(TAG, "message: " + message);
            Log.e(TAG, "isBackground: " + isBackground);
            Log.e(TAG, "payload: " + payload.toString());
            Log.e(TAG, "imageUrl: " + imageUrl);
            Log.e(TAG, "timestamp: " + timestamp);


            if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
                // app is in foreground, broadcast the push message
                Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
                pushNotification.putExtra("message", message);
                LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

                // play notification sound
                NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
                notificationUtils.playNotificationSound();
            } else {
                // app is in background, show the notification in notification tray

                 Intent resultIntent = new Intent(getApplicationContext(), LoginActivity.class);
                 resultIntent.putExtra("message", message);

                // check for image attachment
                if (TextUtils.isEmpty(imageUrl)) {
                    showNotificationMessage(getApplicationContext(), title, message, timestamp, resultIntent);
                } else {
                    // image is present, show notification with image
                    showNotificationMessageWithBigImage(getApplicationContext(), title, message, timestamp, resultIntent, imageUrl);
                }
            }
        } catch (JSONException e) {
            Log.e(TAG, "Json Exception: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
    }

    private void showNotificationMessage(Context context, String title, String message, String timeStamp, Intent intent) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent);
    }


    private void showNotificationMessageWithBigImage(Context context, String title, String message, String timeStamp, Intent intent, String imageUrl) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent, imageUrl);
    }
    private void sendNotification(String title,String message)//JSONObject json)

    {

        int requestID = (int) System.currentTimeMillis();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("title",title);
        intent.putExtra("message",message);
        Date entrydate =  new Date();
        // DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
        String endate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(entrydate);
        intent.putExtra("time",endate);
        intent.setAction("myString"+ requestID);
        //Log.e(TAG, "Notification JSON " + json.toString());
        try{
           /* JSONObject data = json.getJSONObject("data");
            String title = data.getString("title");
            String message = data.getString("message"); */
            //String imageUrl = data.getString("image");
          //TempStart
          /*  PendingIntent pendingIntent = PendingIntent.getActivity(this, requestID,
                    intent,PendingIntent.FLAG_ONE_SHOT); //FLAG_UPDATE_CURRENT

            Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Bitmap bitmap= BitmapFactory.decodeResource(this.getResources(),
                    R.drawable.appicon);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.appicon)
                    .setContentTitle("MAHYCO")
                    .setContentText(message)
                    .setAutoCancel(true)
                    .setLargeIcon(bitmap)
                    .setSound(defaultSoundUri)

                    .setContentIntent(pendingIntent);


            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.notify(requestID , notificationBuilder.build());
*/
//TempEnd

            //st
            String idChannel = "my_channel_01";
            Intent mainIntent;

            mainIntent = new Intent(this, LoginActivity.class);

            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, mainIntent, 0);

            NotificationManager mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

            NotificationChannel mChannel = null;
            int importance = NotificationManager.IMPORTANCE_HIGH;

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, null);
            builder.setContentTitle(this.getString(R.string.app_name))
                    .setSmallIcon(R.drawable.appicon)
                    .setContentIntent(pendingIntent)
                    .setContentText(message);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                mChannel = new NotificationChannel(idChannel, this.getString(R.string.app_name), importance);
                // Configure the notification channel.
               // mChannel.setDescription(context.getString(R.string.alarm_notification));
                mChannel.enableLights(true);

                mChannel.setLightColor(Color.RED);
                mChannel.setDescription(message);
                mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                mNotificationManager.createNotificationChannel(mChannel);
            } else {
                builder.setContentTitle(this.getString(R.string.app_name))
                        .setSmallIcon(R.drawable.appicon)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setColor(ContextCompat.getColor(this, R.color.lightgray))
                        .setVibrate(new long[]{100, 250})
                        .setLights(Color.YELLOW, 500, 5000)
                        .setAutoCancel(true);

                mNotificationManager.notify(requestID , builder.build());
            }
            //end

        }
        /*catch (JSONException e) {
           Log.e(TAG, "Json Exception: " + e.getMessage());
        } */
        catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
    }
    private void sendNotification2(String title,String message)//JSONObject json)

    {

        int requestID = (int) System.currentTimeMillis();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("title",title);
        intent.putExtra("message",message);
        Date entrydate =  new Date();
        // DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
        String endate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(entrydate);
        intent.putExtra("time",endate);
        intent.setAction("myString"+ requestID);
        //Log.e(TAG, "Notification JSON " + json.toString());
        try{
           /* JSONObject data = json.getJSONObject("data");
            String title = data.getString("title");
            String message = data.getString("message"); */
            //String imageUrl = data.getString("image");
            //TempStart
          /*  PendingIntent pendingIntent = PendingIntent.getActivity(this, requestID,
                    intent,PendingIntent.FLAG_ONE_SHOT); //FLAG_UPDATE_CURRENT

            Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Bitmap bitmap= BitmapFactory.decodeResource(this.getResources(),
                    R.drawable.appicon);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.appicon)
                    .setContentTitle("MAHYCO")
                    .setContentText(message)
                    .setAutoCancel(true)
                    .setLargeIcon(bitmap)
                    .setSound(defaultSoundUri)

                    .setContentIntent(pendingIntent);


            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.notify(requestID , notificationBuilder.build());
*/
//TempEnd

            //st
            Intent mainIntent;

            mainIntent = new Intent(this, LoginActivity.class);

            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, mainIntent, 0);

            NotificationManager mNotificationManager;;
             String NOTIFICATIONCHANNELID_FCM = "channel_id_fcm";

             String NOTIFICATION_FCM = "notify_fcm";

            mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

            Uri default_msg_tone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.appicon);

            int color = this.getResources().getColor(R.color.Dashboard);





            NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

            int importance = NotificationManager.IMPORTANCE_HIGH;



            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

                NotificationChannel mChannel = new NotificationChannel(

                        NOTIFICATIONCHANNELID_FCM, NOTIFICATION_FCM, importance);

                notificationManager.createNotificationChannel(mChannel);

            }

            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, NOTIFICATIONCHANNELID_FCM)
                    .setColor(color)
                    .setSmallIcon(R.drawable.noti)
                    .setLargeIcon(bitmap)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                    .setContentTitle(getResources().getString(R.string.app_name))
                    .setOnlyAlertOnce(true)
                    .setAutoCancel(true)
                    .setContentText(message)
                    .setContentIntent(pendingIntent)
                    .setVibrate(new long[]{500, 500})
                    .setSound(default_msg_tone);
            // NotificationManager notificationmanager = (NotificationManager) objContext.getSystemService(Context.NOTIFICATION_SERVICE);

            mNotificationManager.notify(requestID++, mBuilder.build());
            //end

        }
        /*catch (JSONException e) {
           Log.e(TAG, "Json Exception: " + e.getMessage());
        } */
        catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
    }
}

