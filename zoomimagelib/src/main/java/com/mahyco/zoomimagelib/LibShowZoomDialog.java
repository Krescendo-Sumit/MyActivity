package com.mahyco.zoomimagelib;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

public class LibShowZoomDialog {

    private static Dialog mZoomAlertDialog;

    public static void hideZoomDialog() {
        if (mZoomAlertDialog != null && mZoomAlertDialog.isShowing()) {
            mZoomAlertDialog.dismiss();
        }
    }

    public static void showZoomDialog(Bitmap bitmap, Context mContext) {
        try {
            mZoomAlertDialog = null;
            androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(mContext,
                    R.style.LibZoomDialogTheme);
            LayoutInflater inflater = LayoutInflater.from(mContext);
            View dialogLayout = inflater.inflate(R.layout.lib_zoom_image, null);

            ZoomLibImageView imageView = dialogLayout.findViewById(R.id.lib_zoom_image_view);
            ImageView back = dialogLayout.findViewById(R.id.lib_dialog_back_image_view);
            imageView.setImageBitmap(bitmap);
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mZoomAlertDialog.dismiss();
                }
            });
            builder.setView(dialogLayout);
            mZoomAlertDialog = builder.create();
            mZoomAlertDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
