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
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import java.text.SimpleDateFormat;
import java.util.Date;

import myactvity.mahyco.LoginActivity;
import myactvity.mahyco.R;


/**
 * Created by theappguruz on 07/05/16.
 */
public class PhoneStateReceiver extends BroadcastReceiver {
    Context c;
    private static int lastState = TelephonyManager.CALL_STATE_IDLE;
    private static Date callStartTime;
    private static boolean isIncoming;
    private static String savedNumber;
    String inommingnumber = "";

    public static String NOTIFICATION_ID = "notification-id";
    public static String NOTIFICATION = "notification";

    public class myPhoneStateChangeListener extends PhoneStateListener {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
            if (state == TelephonyManager.CALL_STATE_RINGING) {
                String phoneNumber = incomingNumber;
              //  Toast.makeText(c, "" + phoneNumber, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        c = context;
        inommingnumber = "";
        try {
            String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
            if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                Toast.makeText(context, "Incomming call new brother.", Toast.LENGTH_SHORT).show();
                inommingnumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
                if (inommingnumber != null) {
                   // Toast.makeText(context, " Number " + inommingnumber, Toast.LENGTH_SHORT).show();
                }

            }
          //  Preferences.save(context, Preferences.NOTIFICATIONTIME, "11");
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            String str = sdf.format(new Date());
            String dt[] = str.split(":");
            Log.i("Chr", dt[0]);
            String sptime = Preferences.get(context, Preferences.NOTIFICATIONTIME).toString().trim();
            String sptimeslot = Preferences.get(context, Preferences.NOTIFICATIONTIMESLOT).toString().trim();
           String message ="Please properly start and end your tour.";
           int hr=Integer.parseInt(dt[0]);
           int s=0;
           int slot=0;
           if(sptimeslot==null||sptimeslot.equals(""))
           {
               sptimeslot="0";
           }

           if(hr>=5 && hr<10)
           {
               s=1;
               slot=1;
               message="Good Morning, Please start travel.";
           }else if(hr>=10 && hr<12)
           {
               s=1;
               slot=2;
               message="Start your day journey.";
           }
           else if(hr>=12 && hr<15)
           {
               s=1;
               slot=3;
               message="Upload data if anything remains.";
           }
           else if(hr>=15 && hr<17)
           {
               s=1;
               slot=4;

               message="Day is near to end , complete your task.";
           }
           else if(hr>=17 && hr<19)
           {
               s=1;
               slot=5;
               message="Day almost end , Please upload data and end travel properly.";
           }
           else if(hr>=19 && hr<22)
           {
               s=1;
               slot=6;
               message="Good Evening , Please check you end travel or not.";
           }
   // 12   6
           if(s==1) {
               if (sptime.equals(dt[0].trim()) && sptimeslot.equals(""+slot)) {
                   Log.i("Notification", "No Notification");
               } else {
                   Log.i("Notification", "Show Notification");
                   Preferences.save(context, Preferences.NOTIFICATIONTIME, dt[0]);
                   Preferences.save(context, Preferences.NOTIFICATIONTIMESLOT, ""+slot);
                   sendNotification(message, null);
               }
           }

        } catch (Exception e) {

        }


        if (intent.getAction().equals("android.intent.action.NEW_OUTGOING_CALL")) {
            savedNumber = intent.getExtras().getString("android.intent.extra.PHONE_NUMBER");
          //  Toast.makeText(context, "" + savedNumber, Toast.LENGTH_SHORT).show();
        } else {
            String stateStr = intent.getExtras().getString(TelephonyManager.EXTRA_STATE);
            String number = intent.getExtras().getString(TelephonyManager.EXTRA_INCOMING_NUMBER);

            int state = 0;
            if (stateStr.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
                state = TelephonyManager.CALL_STATE_IDLE;
            } else if (stateStr.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
                state = TelephonyManager.CALL_STATE_OFFHOOK;
            } else if (stateStr.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                state = TelephonyManager.CALL_STATE_RINGING;
            }

            onCallStateChanged(context, state, savedNumber, intent);


        }
    }

    public void onCallStateChanged(Context context, int state, String number, Intent intent) {
        if (lastState == state) {
            //No change, debounce extras
            return;
        }
        switch (state) {
            case TelephonyManager.CALL_STATE_RINGING:
                isIncoming = true;
                callStartTime = new Date();
                onIncomingCallStarted(context, number, callStartTime, intent);
                break;
            case TelephonyManager.CALL_STATE_OFFHOOK:
                //Transition of ringing->offhook are pickups of incoming calls.  Nothing done on them
                if (lastState != TelephonyManager.CALL_STATE_RINGING) {
                    isIncoming = false;
                    callStartTime = new Date();
                    onOutgoingCallStarted(context, number, callStartTime, intent);
                }
                break;
            case TelephonyManager.CALL_STATE_IDLE:
                //Went to idle-  this is the end of a call.  What type depends on previous state(s)
                if (lastState == TelephonyManager.CALL_STATE_RINGING) {
                    //Ring but no pickup-  a miss
                    onMissedCall(context, number, callStartTime, intent);
                } else if (isIncoming) {
                    onIncomingCallEnded(context, number, callStartTime, new Date(), intent);
                } else {
                    onOutgoingCallEnded(context, number, callStartTime, new Date(), intent);
                }
                break;
        }
        lastState = state;
//        Intent intent1 = new Intent("CallApp");
//        context.sendBroadcast(intent1);
    }

    protected void onIncomingCallStarted(Context ctx, String number, Date start, Intent intent) {
       // Toast.makeText(ctx, "calling from " + number, Toast.LENGTH_SHORT).show();
    }

    protected void onOutgoingCallStarted(Context ctx, String number, Date start, Intent intent) {
       // Toast.makeText(ctx, "calling to " + number, Toast.LENGTH_SHORT).show();
    }

    protected void onIncomingCallEnded(Context ctx, String number, Date start, Date end, Intent intent) {
       // Toast.makeText(ctx, "calling from " + number + " ended ", Toast.LENGTH_SHORT).show();

    }

    protected void onOutgoingCallEnded(Context ctx, String number, Date start, Date end, Intent intent) {
        //Toast.makeText(ctx, "calling to 123 " + number + " ended ", Toast.LENGTH_SHORT).show();
    }

    protected void onMissedCall(Context ctx, String number, Date start, Intent intent) {
       // Toast.makeText(ctx, "missed call from " + number + " sim ", Toast.LENGTH_SHORT).show();
        ///  saveData(ctx, number, intent, "Missed Call");
    }

    private void sendNotification(String messageBody, Bitmap image) {
        try {
          //  Toast.makeText(c, "" + messageBody, Toast.LENGTH_SHORT).show();
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
          //  Toast.makeText(c, "Error in broadcast receiver " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public static int NOTIFICATION_ID_COMMON = 5;

}


