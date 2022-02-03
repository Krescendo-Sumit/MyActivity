package myactvity.mahyco.app;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import androidx.core.content.FileProvider;
import myactvity.mahyco.R;
//mport com.bumptech.glide.load.engine.DiskCacheStrategy;
public class Indentcreate {

    private static Context context;
    ImageView imgChoosen;
  //  Context context;
    private static String imgPath = "";
    private static Uri imageUri = null;
    private static  String queryImageUrl = "";
    public  static File fileobj=null;


    public  static Intent getPickImageIntentnew(Context context1)
    {                context = context1;
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File f = new File(String.valueOf(context.getExternalFilesDir(Environment.DIRECTORY_DCIM)));
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, setImageUri());
                    //intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));

        return intent;

    }
    public  static Intent getPickImageIntent(Context context1) {
        context = context1;
        Intent chooserIntent = (Intent) null;
        List intentList = (List) (new ArrayList());
         Intent takePhotoIntent = new Intent("android.media.action.IMAGE_CAPTURE");
        takePhotoIntent.putExtra("output", (Parcelable)setImageUri());
        intentList = addIntentsToList((context) , intentList, takePhotoIntent);
        if (intentList.size() > 0) {
            chooserIntent = Intent.createChooser((Intent) intentList.remove((intentList.size() - 1)), context.getResources().getString(R.string.select_capture_image));
            Collection $this$toTypedArray$iv = (Collection) intentList;
            boolean $i$f$toTypedArray = false;
            Object[] var10002 = $this$toTypedArray$iv.toArray(new Parcelable[0]);
            if (var10002 == null) {
                throw new ClassCastException("null cannot be cast to non-null type kotlin.Array<T>");
            }

            chooserIntent.putExtra("android.intent.extra.INITIAL_INTENTS", (Parcelable[]) var10002);
        }

        return chooserIntent;
    }

    private static List addIntentsToList(Context context, List list, Intent intent) {
        List resInfo = context.getPackageManager().queryIntentActivities(intent, 0);
        Iterator var6 = resInfo.iterator();

        while (var6.hasNext()) {
            ResolveInfo resolveInfo = (ResolveInfo) var6.next();
            String packageName = resolveInfo.activityInfo.packageName;
            Intent targetedIntent = new Intent(intent);
            targetedIntent.setPackage(packageName);
            list.add(targetedIntent);
        }

        return list;
    }
    private static Uri setImageUri() {
        File folder = new File(String.valueOf(context.getExternalFilesDir(Environment.DIRECTORY_DCIM)));
        Log.d("MainActivity", "setImageUri Folder : " + folder.getAbsolutePath());
        folder.mkdirs();
        String timeStamp ="Image_Tmp.jpg";// new SimpleDateFormat("yyyyMMdd_HHmmss",
               // Locale.getDefault()).format(new Date());
        //timeStamp=timeStamp+context.getClass().getSimpleName()+".jpg";


        File file = new File(folder, timeStamp);
        Log.d("MainActivity", "setImageUri file : " + file.getAbsolutePath());
        if (file.exists()) {
            file.delete();
        }
        try {

            file.createNewFile();
           // val uri = FileProvider.getUriForFile(this, FILE_AUTHORITY, file)

            imageUri = FileProvider.getUriForFile(
                    context,
                    AppConstant.provider,
                    file
            );
            context.revokeUriPermission(imageUri, Intent.FLAG_GRANT_READ_URI_PERMISSION);

            fileobj=file;
            imgPath = file.getAbsolutePath();

        } catch (Exception e) {
            e.printStackTrace();
            Log.d("MainActivity", "setImageUri: " + e.toString());

        }



        return imageUri;
    }
    /*
    private final void handleImageRequest(final Intent data) {


        if (data != null && data.getData() != null && !data.getData().equals("")) {//Photo from gallery
            imageUri = data.getData();
            queryImageUrl = imageUri.getPath();
            queryImageUrl = FileUtils.compressImageFile(queryImageUrl, imageUri, MainActivity.this);
//            if () {
//
//            } else {
//                if (!imgPath.isEmpty() && !imgPath.equals(""))
//                    queryImageUrl = imgPath;
//                FileUtils.compressImageFile(queryImageUrl, imageUri, MainActivity.this);
//            }

        } else {
            if (!imgPath.isEmpty() && !imgPath.equals(""))
                queryImageUrl = imgPath;
            FileUtils.compressImageFile(queryImageUrl, imageUri, context);
        }
        imageUri = Uri.fromFile(new File(queryImageUrl));

        if (!queryImageUrl.isEmpty()) {

            Log.d("Rajshri", "Final image : " + queryImageUrl);

            Glide.with(context)
                    .asBitmap()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .load(queryImageUrl)
                    .into(imgChoosen);
        }
    } */

}
