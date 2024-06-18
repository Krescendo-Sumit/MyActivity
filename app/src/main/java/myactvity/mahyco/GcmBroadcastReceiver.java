package myactvity.mahyco;

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

public class GcmBroadcastReceiver extends BroadcastReceiver {
    public static String NOTIFICATION_ID = "notification-id";
    public static String NOTIFICATION = "notification";
    public static String NOTIFICATIONCHANNELID_FCM = "channel_id_fcm";
    public static String NOTIFICATION_FCM = "notify_fcm";
    @Override
    public void onReceive(Context context, Intent intent1) {
        try {

            Intent intent = new Intent(context, LoginActivity.class);
            //intent.putExtra("isFromNotification",true);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0 /* Request code */, intent,
                    PendingIntent.FLAG_MUTABLE);

            Uri default_msg_tone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.appicon);
            int color = context.getResources().getColor(R.color.design_default_color_background);

            NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            int importance = 0;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                importance = NotificationManager.IMPORTANCE_HIGH;
            }

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                NotificationChannel mChannel = new NotificationChannel(
                        NOTIFICATIONCHANNELID_FCM, NOTIFICATION_FCM, importance);
                notificationManager.createNotificationChannel(mChannel);
            }
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, NOTIFICATIONCHANNELID_FCM)
                    .setColor(color)
                    .setSmallIcon(R.drawable.icon)
                    .setLargeIcon(bitmap)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText("My Activity"))
                    .setContentTitle(context.getResources().getString(R.string.app_name))
                    .setOnlyAlertOnce(true)
                    .setAutoCancel(true)
                    .setContentText("My Activity")

                    .setContentIntent(pendingIntent)
                    .setVibrate(new long[]{500, 500})
                    .setSound(default_msg_tone);


            // NotificationManager notificationmanager = (NotificationManager) mcontext.getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(NOTIFICATION_ID_COMMON++, mBuilder.build());
        } catch (Exception e) {
            Log.d("Error is ", e.getMessage());
            Toast.makeText(context, "Error in broadcast receiver " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    public static int NOTIFICATION_ID_COMMON = 5;
}
