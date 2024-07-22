package myactvity.mahyco.newupload;


import static myactvity.mahyco.GcmBroadcastReceiver.NOTIFICATIONCHANNELID_FCM;
import static myactvity.mahyco.GcmBroadcastReceiver.NOTIFICATION_FCM;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import myactvity.mahyco.LoginActivity;
import myactvity.mahyco.R;

public class NotificationReceiver extends BroadcastReceiver {
  Context c;
    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            this.c = context;
          /*

          NotificationHelper notificationHelper = new NotificationHelper(context);
          notificationHelper.createNotification();

          */
            sendNotification("Attention : End Travel and please upload data.Otherwise you lost day activity.", null);
         // Toast.makeText(context, "Alarm Called ", Toast.LENGTH_SHORT).show();
            Log.i("Alarm called", "true");

        }catch (Exception e)
        {
            Log.i("Alarm called", "true"+e.getMessage());
        }
    }
    private void sendNotification(String messageBody, Bitmap image) {
        try {
           // Toast.makeText(c, "" + messageBody, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(c, LoginActivity.class);
            //intent.putExtra("isFromNotification",true);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(c, 0 /* Request code */, intent,
                    PendingIntent.FLAG_MUTABLE);

            Uri default_msg_tone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Bitmap bitmap = BitmapFactory.decodeResource(c.getResources(), R.drawable.noti);
            int color = c.getResources().getColor(R.color.design_default_color_background);

            NotificationManager mNotificationManager = (NotificationManager) c.getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationManager notificationManager = (NotificationManager) c.getSystemService(Context.NOTIFICATION_SERVICE);
            int importance = 0;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                importance = NotificationManager.IMPORTANCE_HIGH;
            }

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                NotificationChannel mChannel = new NotificationChannel(
                        NOTIFICATIONCHANNELID_FCM, NOTIFICATION_FCM, importance);
                notificationManager.createNotificationChannel(mChannel);
            }
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(c, NOTIFICATIONCHANNELID_FCM)
                    .setColor(color)
                    .setSmallIcon(R.drawable.noti)
                    .setLargeIcon(bitmap)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(messageBody))
                    .setContentTitle(c.getResources().getString(R.string.app_name))
                    .setOnlyAlertOnce(true)
                    .setAutoCancel(true)
                    .setContentText(messageBody)

                    .setContentIntent(pendingIntent)
                    .setVibrate(new long[]{500, 500})
                    .setSound(default_msg_tone);


            // NotificationManager notificationmanager = (NotificationManager) mcontext.getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(NOTIFICATION_ID_COMMON++, mBuilder.build());
        } catch (Exception e) {
            Log.d("Error is ", e.getMessage());
            Toast.makeText(c, "Error in broadcast receiver " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public static int NOTIFICATION_ID_COMMON = 5;
}