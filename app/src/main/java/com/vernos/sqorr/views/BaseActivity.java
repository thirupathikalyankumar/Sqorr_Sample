package com.vernos.sqorr.views;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.text.format.Formatter;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.theartofdev.edmodo.cropper.CropImage;
import com.vernos.sqorr.R;
import com.vernos.sqorr.utilities.CropUtils;
import com.vernos.sqorr.utilities.FetchAddressTask;
import com.vernos.sqorr.utilities.FileUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Locale;


public class BaseActivity extends AppCompatActivity implements
        FetchAddressTask.OnTaskCompleted {

    private static final String TAG = BaseActivity.class.getSimpleName();
    public BaseActivity mActivity;
    private static final int REQUEST_LOCATION_PERMISSION = 1;

    private FusedLocationProviderClient mFusedLocationClient;
    private LocationCallback mLocationCallback;
    private static final int REQUEST_CAMERA_CODE = 999;
    //For Image
    final int SELECT_FILE = 1;
    private File imageFile;
    protected Uri savedpath;
    protected Bitmap bitmap;
    private String timeStamp;
    private boolean isPhotoTaken, isFromGallery;
    private boolean isCropDone = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = this;
        // Initialize the FusedLocationClient.
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(
                this);

        // Initialize the location callbacks.
        mLocationCallback = new LocationCallback() {
            /**
             * This is the callback that is triggered when the
             * FusedLocationClient updates your location.
             * @param locationResult The result containing the device location.
             */
            @Override
            public void onLocationResult(LocationResult locationResult) {
                // If tracking is turned on, reverse geocode into an address
                new FetchAddressTask(BaseActivity.this, BaseActivity.this)
                        .execute(locationResult.getLastLocation());

            }
        };
        startTrackingLocation();

    }

    private void startTrackingLocation() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]
                            {Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSION);
        } else {
            mFusedLocationClient.requestLocationUpdates
                    (getLocationRequest(),
                            mLocationCallback,
                            null /* Looper */);


        }
    }


    public String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        String ip = Formatter.formatIpAddress(inetAddress.hashCode());
                        Log.i(TAG, "***** IP=" + ip);
                        return ip;
                    }
                }
            }
        } catch (SocketException ex) {
            Log.e(TAG, ex.toString());
        }
        return null;
    }
   /* private void stopTrackingLocation() {
        if (mTrackingLocation) {
            mTrackingLocation = false;
            mLocationButton.setText(R.string.start_tracking_location);
            mLocationTextView.setText(R.string.textview_hint);
            mRotateAnim.end();
        }
    }
*/


    private LocationRequest getLocationRequest() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        return locationRequest;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_LOCATION_PERMISSION:

                // If the permission is granted, get the location, otherwise,
                // show a Toast
                if (grantResults.length > 0
                        && grantResults[0]
                        == PackageManager.PERMISSION_GRANTED) {
                    startTrackingLocation();
                } else {
                    Toast.makeText(this,
                            R.string.location_permission_denied,
                            Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public void onTaskCompleted(String result) {
        // Update the UI
        // mLocationTextView.setText(getString(R.string.address_text,result, System.currentTimeMillis()));
        String ipAddress = getLocalIpAddress();

        Toast.makeText(this, "Location is " + result + "\n" + ipAddress, Toast.LENGTH_LONG).show();
        //Log.e(TAG, "onTaskCompleted: ipAddress " + ipAddress);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e(TAG, "onActivityResult: " + resultCode);
        if (requestCode == REQUEST_CAMERA_CODE) {
            switch (resultCode) {
                case AppCompatActivity.RESULT_OK:
                    if (requestCode == REQUEST_CAMERA_CODE) {

                        if (savedpath != null) {
                            isCropDone = false;
                            //bitmap = BitmapFactory.decodeFile(savedpath.getPath());
                            setImage(savedpath);

                            isPhotoTaken = true;
                            if (imageFile.exists()) {
                                //  Toast.makeText(SignInActivity.this,"This image is stored at"+savedpath.toString(),Toast.LENGTH_LONG).show();
                                // txtpath.setText(savedpath.toString());
                            } else {
                                Toast.makeText(BaseActivity.this, getString(R.string.error_in_saving_image), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    break;


                case RESULT_CANCELED:
                    isPhotoTaken = false;
                    Toast.makeText(BaseActivity.this, getString(R.string.user_canceled_to_capture_image), Toast.LENGTH_SHORT).show();

                    break;
                default:
                    break;


            }
        } else if (requestCode == CropUtils.CROP_PIC) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {

                    Bundle extras = data.getExtras();

                    if (extras != null) {

                        bitmap = extras.getParcelable("data");
                        isCropDone = true;
                        assignImageToView();
                        saveRotatedImage(bitmap);
                        isCropDone = false;
                    }
                }
            }

        } else if (requestCode == SELECT_FILE) {
            if (data != null) {
                onSelectFromGalleryResult(data);
            }
        } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                savedpath = resultUri;
                bitmap = BitmapFactory.decodeFile(resultUri.getPath());
                assignImageToView();
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    protected void assignImageToView() {
        Log.e(TAG, "assignImageToView: ");

    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {

        Bitmap bm = null;
        if (data != null) {
            try {
                Uri filePath = data.getData();
                if (filePath != null) {

                    File file = new File(FileUtils.getPath(BaseActivity.this, filePath));
                    savedpath = Uri.fromFile(file);
                    isFromGallery = true;

                    CropUtils.performCrop(BaseActivity.this, filePath);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onPause() {

        super.onPause();
    }

    @Override
    protected void onResume() {

        super.onResume();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    public void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(BaseActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                //   boolean result = Utility.checkPermission(getActivity());

                if (ContextCompat.checkSelfPermission(BaseActivity.this, Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(BaseActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(BaseActivity.this,
                            new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
                } else {
                    //cameraIntent();
                }

                if (items[item].equals("Take Photo")) {
                    //userChoosenTask = "Take Photo";
                    setTakePhoto();


                } else if (items[item].equals("Choose from Library")) {
                    //userChoosenTask = "Choose from Library";
                    // if (result)
                    galleryIntent();

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }


    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        this.startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }

    public void setTakePhoto() {
        Log.e(TAG, "setTakePhoto:  00");
        if (ContextCompat.checkSelfPermission(BaseActivity.this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(BaseActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(BaseActivity.this,
                    new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
        } else {
            Log.e(TAG, "setTakePhoto: ");
            Intent takepic = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            imageFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "DreamScore");
            Log.e(TAG, "setTakePhoto: image file " + imageFile);
            if (!imageFile.exists()) {
                if (!imageFile.mkdirs()) {

                }
            }

            timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
            File mediafile = new File(imageFile.getAbsolutePath() + File.separator + timeStamp + ".png");

            savedpath = Uri.fromFile(mediafile);
            //savedpath = FileProvider.getUriForFile(this, "in.newtel.anike.utils.documents", mediafile);
            Log.e(TAG, "setTakePhoto: savepath " + savedpath + " mediafile " + mediafile);
            if (Build.VERSION.SDK_INT >= 24) {
                try {
                    Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                    m.invoke(null);
                    Log.e(TAG, "setTakePhoto: strickmode ");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            Log.e(TAG, "setTakePhoto: 22");
            takepic.putExtra(MediaStore.EXTRA_OUTPUT, savedpath);
            Log.e(TAG, "setTakePhoto: 221 ");
            takepic.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
            Log.e(TAG, "setTakePhoto: 222 ");
            this.startActivityForResult(takepic, REQUEST_CAMERA_CODE);
        }
    }

    private void setImage(Uri photoPath) {

        try {

            InputStream inputStream = getContentResolver().openInputStream(photoPath);
            bitmap = BitmapFactory.decodeStream(inputStream);
            androidx.exifinterface.media.ExifInterface ei = new androidx.exifinterface.media.ExifInterface(inputStream);
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_UNDEFINED);

            switch (orientation) {

                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotateImage(90);
                    break;

                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotateImage(180);
                    break;

                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotateImage(270);
                    break;

                case ExifInterface.ORIENTATION_NORMAL:

                    CropUtils.performCrop(BaseActivity.this, savedpath);
                    break;
                default:

                    CropUtils.performCrop(BaseActivity.this, savedpath);
                    break;
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
            byte[] imageBytes = baos.toByteArray();

        } catch (Exception e) {

            Toast.makeText(BaseActivity.this, getString(R.string.error_while_rotatiing_image), Toast.LENGTH_SHORT).show();

        }
    }

    private void rotateImage(int angle) {

        Matrix matrix = new Matrix();
        matrix.postRotate(angle);

        Bitmap correctBmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(),
                matrix, true);
        saveRotatedImage(correctBmp);
    }


    private void saveRotatedImage(Bitmap bmp) {

        try {
            OutputStream fOut = null;
            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Anike");
            if (!file.exists()) {
                if (!file.mkdirs()) {
                    Log.d(TAG, "Oops! Failed create " + imageFile.toString() + " directory");
                }
            }
            timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
            File mediafile = new File(file.getAbsolutePath() + File.separator + timeStamp + ".png");
            fOut = new FileOutputStream(mediafile);

            Bitmap pictureBitmap = bmp;
            pictureBitmap.compress(Bitmap.CompressFormat.JPEG, 85, fOut);
            fOut.flush();
            fOut.close();

            MediaStore.Images.Media.insertImage(getContentResolver(), mediafile.getAbsolutePath(), file.getName(), file.getName());

            savedpath = Uri.fromFile(mediafile);

            if (!isCropDone) {

                CropUtils.performCrop(BaseActivity.this, savedpath);
            }

        } catch (Exception e) {

            //MyLog.d(TAG, "Error while saving rotated image");

        }
    }

    public File CompressImageFile(File file) {
        try {

            // BitmapFactory options to downsize the image
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            o.inSampleSize = 6;
            // factor of downsizing the image

            FileInputStream inputStream = new FileInputStream(file);
            //Bitmap selectedBitmap = null;
            BitmapFactory.decodeStream(inputStream, null, o);
            inputStream.close();

            // The new size we want to scale to
            final int REQUIRED_SIZE = 75;

            // Find the correct scale value. It should be the power of 2.
            int scale = 1;
            while (o.outWidth / scale / 2 >= REQUIRED_SIZE &&
                    o.outHeight / scale / 2 >= REQUIRED_SIZE) {
                scale *= 2;
            }

            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            inputStream = new FileInputStream(file);

            Bitmap selectedBitmap = BitmapFactory.decodeStream(inputStream, null, o2);
            inputStream.close();

            // here i override the original image file
            //    file.mkdirs();
            file.createNewFile();
            FileOutputStream outputStream = new FileOutputStream(file);

            selectedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);

            return file;
        } catch (Exception e) {
            return null;
        }

    }


}
