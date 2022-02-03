package com.vansuita.pickimage.dialog;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.keep.Keep;
import com.vansuita.pickimage.listeners.IPickCancel;
import com.vansuita.pickimage.listeners.IPickClick;
import com.vansuita.pickimage.listeners.IPickResult;
import com.vansuita.pickimage.resolver.IntentResolver;

import static android.app.Activity.RESULT_OK;

/*Added on 9th Sept 2021 for Camera and Gallery Option*/

public class GalleryCameraPickImageDialog extends GalleryCameraPickImageBaseDialog {

    public static GalleryCameraPickImageDialog newInstance(PickSetup setup) {
        GalleryCameraPickImageDialog frag = new GalleryCameraPickImageDialog();
        Bundle bundle = new Bundle();
        bundle.putSerializable(SETUP_TAG, setup);
        frag.setArguments(bundle);
        return frag;
    }

    public static GalleryCameraPickImageDialog build(PickSetup setup, IPickResult pickResult) {
        GalleryCameraPickImageDialog d = GalleryCameraPickImageDialog.newInstance(setup);
        d.setOnPickResult(pickResult);
        return d;
    }

    public static GalleryCameraPickImageDialog build(IPickResult pickResult) {
        return build(new PickSetup(), pickResult);
    }

    public static GalleryCameraPickImageDialog build(PickSetup setup) {
        return build(setup, null);
    }

    public static GalleryCameraPickImageDialog build() {
        return build();
    }

    public GalleryCameraPickImageDialog show(FragmentActivity fragmentActivity) {
        return show(fragmentActivity.getSupportFragmentManager());
    }

    public GalleryCameraPickImageDialog show(FragmentManager fragmentManager) {
        //super.show(fragmentManager, DIALOG_FRAGMENT_TAG);
        showFragment(fragmentManager, DIALOG_FRAGMENT_TAG); /*Custom method, added by Rajshri 21th May 2021 TODO:Need to Test*/
        return this;
    }

    public void showFragment(FragmentManager manager, String tag) { /*Custom method, added by Rajshri 21th May 2021*/
        Log.d("Custom","*******************Testing Custom Show Fragment");
        FragmentTransaction ft = manager.beginTransaction();
        ft.add(this, tag);
        //ft.commit();
        ft.commitAllowingStateLoss();
    }

    @Override
    public void onCameraClick() {
        launchCamera();
    }

    @Override
    public void onGalleryClick() {
        launchGallery();
    }

    @Override
    public GalleryCameraPickImageDialog setOnClick(IPickClick onClick) {
        return (GalleryCameraPickImageDialog) super.setOnClick(onClick);
    }

    @Override
    public GalleryCameraPickImageDialog setOnPickResult(IPickResult onPickResult) {
        return (GalleryCameraPickImageDialog) super.setOnPickResult(onPickResult);
    }

    @Override
    public GalleryCameraPickImageDialog setOnPickCancel(IPickCancel onPickCancel)
    {
        return (GalleryCameraPickImageDialog) super.setOnPickCancel(onPickCancel);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IntentResolver.REQUESTER) {
            if (resultCode == RESULT_OK) {
                //Show progress
                showProgress(true);

                //Handle the image result async
                getAsyncResult().execute(data);
            } else {
                dismissAllowingStateLoss();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == IntentResolver.REQUESTER) {
            boolean granted = true;

            for (Integer i : grantResults)
                granted = granted && i == PackageManager.PERMISSION_GRANTED;

            if (granted) {
                if (!launchSystemDialog()) {
                    // See if the CAMERA permission is among the granted ones
                    int cameraIndex = -1;
                    for (int i = 0; i < permissions.length; i++) {
                        if (permissions[i].equals(Manifest.permission.CAMERA)) {
                            cameraIndex = i;
                            break;
                        }
                    }

                    if (cameraIndex == -1) {
                        launchGallery();
                    } else {
                        launchCamera();
                    }
                }
            } else {
                dismissAllowingStateLoss();

                if (grantResults.length > 1)
                    Keep.with(getActivity()).askedForPermission();
            }
        }
    }

}


