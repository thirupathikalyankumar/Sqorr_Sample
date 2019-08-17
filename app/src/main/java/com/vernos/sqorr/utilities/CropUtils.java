package com.vernos.sqorr.utilities;


import android.app.Activity;
import android.net.Uri;
import android.widget.Toast;

import com.theartofdev.edmodo.cropper.CropImage;


public class CropUtils {
    public static final int CROP_PIC = 2;
    public static void performCrop(Activity activity, Uri photoUri) {
        // take care of exceptions
        try {

            CropImage.activity(photoUri)
                    .start(activity);

        }
        // respond to users whose devices do not support the crop action
        catch (Exception anfe) {
            anfe.printStackTrace();
            Toast toast = Toast
                    .makeText(activity, "Device Doesn't support crop", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

}
