package com.vernos.sqorr.views;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.gson.JsonObject;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.single.PermissionListener;
import com.vernos.sqorr.R;
import com.vernos.sqorr.pojos.MyCardsPojo;
import com.vernos.sqorr.utilities.APIs;
import com.vernos.sqorr.utilities.FileUtils;
import com.vernos.sqorr.utilities.LocationTrack;
import com.vernos.sqorr.utilities.LocationTracker;
import com.vernos.sqorr.utilities.LocationUtil;
import com.vernos.sqorr.utilities.Utilities;
import com.vernos.sqorr.utilities.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class ProfileEdit extends BaseActivity implements View.OnClickListener {

    private static final String TAG = ProfileEdit.class.getSimpleName();
    LinearLayout camera_img;


    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private String userChoosenTask;

    public static final String CAMERA_PREF = "camera_pref";
    public static final String ALLOW_KEY = "ALLOWED";
    public static final int MY_PERMISSIONS_REQUEST_CAMERA = 10000;

    ImageView profile_image;

    EditText edt_address, pwd_edt, edt_etFullName, et_dob, et_ph_no;
    TextView edt_submit;

    private LocationTracker gpst;
    private double gpslatitude, gpslongitude;


    private int RESULT_LOAD_IMAGE = 101;

    String city_txt = "";
    String state_txt = "";
    String country_txt = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ResourcesCompat.getColor(getResources(), R.color.second_status_bar_color, null));
        }
        init();
    }

    private void init() {

        gpst = new LocationTracker(this);

        TextView toolbar_title_x = findViewById(R.id.toolbar_title_x);
        toolbar_title_x.setText(getString(R.string._profile));

        toolbar_title_x.setOnClickListener(this);

        pwd_edt = findViewById(R.id.etPassword);
        edt_address = findViewById(R.id.etLocation);


        camera_img = findViewById(R.id.camera_img);
        profile_image = findViewById(R.id.profile_image);
        edt_etFullName = findViewById(R.id.etFullName);
        et_dob = findViewById(R.id.et_dob);
        et_ph_no = findViewById(R.id.et_ph_no);
        edt_submit = findViewById(R.id.edt_submit);

        pwd_edt.setOnClickListener(this);
        camera_img.setOnClickListener(this);
        edt_address.setOnClickListener(this);
        edt_submit.setOnClickListener(this);
        et_dob.setOnClickListener(this);

        edt_etFullName.setText(Dashboard.ACCNAME);


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toolbar_title_x:
//                overridePendingTransition(  R.anim.slide_down,R.anim.slide_up );
                finish();
                break;
            case R.id.etPassword:
                Intent changePwdIntent = new Intent(ProfileEdit.this, ChangePassword.class);
                changePwdIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(changePwdIntent);
                break;
            case R.id.et_dob:
                showCalendar();

                break;
            case R.id.edt_submit:

                JSONObject metrics_json = new JSONObject();
                try {
                    metrics_json.put("fullName", edt_etFullName.getText().toString().trim());
                    metrics_json.put("dob", et_dob.getText().toString().trim());
                    metrics_json.put("city", city_txt);
                    metrics_json.put("state", state_txt);
                    metrics_json.put("phoneNumber", et_ph_no.getText().toString().trim());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                apiCallEditProfile(metrics_json);

                break;
            case R.id.etLocation:

                try {
                    Dexter.withActivity(ProfileEdit.this)
                            .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                            .withListener(new PermissionListener() {
                                @Override
                                public void onPermissionGranted(PermissionGrantedResponse response) {


                                    //  if (Helper.isGPSEnabled(MainActivity.this)) {
                                    LocationTrack locationTrack = new LocationTrack(ProfileEdit.this);
                                    if (locationTrack.canGetLocation) {
                                        double lat = locationTrack.getLatitude();
                                        double lon = locationTrack.getLongitude();

                                        try {
                                            Geocoder gcd = new Geocoder(ProfileEdit.this, Locale.getDefault());
                                            List<Address> addresses = gcd.getFromLocation(lat,
                                                    lon, 1);

                                            if (addresses.size() > 0) {

                                                state_txt = addresses.get(0).getAdminArea();
                                                city_txt = addresses.get(0).getLocality();
                                                country_txt = addresses.get(0).getCountryName();
                                                edt_address.setText(state_txt + "," + country_txt);
                                            }
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                      /*  } else {
                                            Toast.makeText(MainActivity.this, "Location service not enabled", Toast.LENGTH_LONG).show();
                                        }*/


                                }

                                @Override
                                public void onPermissionDenied(PermissionDeniedResponse response) {
                                    // check for permanent denial of permission
                                    if (response.isPermanentlyDenied()) {
                                        // navigate user to app settings
                                        Utilities.showAlertBoxLoc(ProfileEdit.this, getResources().getString(R.string.enable_location_title), getResources().getString(R.string.enable_location_msg));
                                    }
                                }

                                @Override
                                public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                                    token.continuePermissionRequest();
                                }


                            }).check();


                    // finish();
                } catch (Exception e) {

                }
                break;
            case R.id.camera_img:
                // showPictureialog();
                selectImage();
                break;
        }
    }

    @Override
    protected void assignImageToView() {
        super.assignImageToView();
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss", Locale.ENGLISH).format(new Date());


        try {
            String path = FileUtils.getPath(this, savedpath);
            Log.e(TAG, "assignImageToView: path " + path);
            // saveImageToServer(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
        File file = new File(savedpath.getPath());
        Log.e(TAG, "assignImageToView: file " + file);
        //File file = CompressImageFile(FileUtils.getFile(this, fileUri));
        //saveImagesToServer(savedpath);


    }


    private void showCalendar() {
        Calendar calendar1 = Calendar.getInstance();

        DatePickerDialog datePickerDialog = new DatePickerDialog(ProfileEdit.this, R.style.MyDatePickerDialogTheme, listener, calendar1.get(Calendar.YEAR), calendar1.get(Calendar.MONTH), calendar1.get(Calendar.DAY_OF_MONTH));
        //    datePickerDialog.getDatePicker().setMinDate(calendar1.getTimeInMillis());
        datePickerDialog.getDatePicker().setMaxDate(calendar1.getTimeInMillis());
        datePickerDialog.show();
    }

    DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {

            et_dob.setText((Utilities.getMonthName((monthOfYear + 1)) + " " + dayOfMonth + ", " + year));

        }
    };

    private void showPictureialog() {
        final Dialog dialog = new Dialog(this, R.style.CustomDialogTheme2
                /*android.R.style.Theme_Translucent_NoTitleBar*/);

        // Setting dialogview
        Window window = dialog.getWindow();
        // window.setGravity(Gravity.CENTER);
        window.setGravity(Gravity.BOTTOM);
        window.setLayout(Toolbar.LayoutParams.MATCH_PARENT, Toolbar.LayoutParams.WRAP_CONTENT);
        dialog.setTitle(null);
        dialog.setContentView(R.layout.activity_bottomdialog);

        TextView mGallery = (TextView) dialog.findViewById(R.id.pickGallery);
        TextView mCamera = (TextView) dialog.findViewById(R.id.takePhoto);
        TextView mRemove = (TextView) dialog.findViewById(R.id.removePhoto);
        mRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // profileImageview.setImageDrawable(null);
                profile_image.setImageResource(R.drawable.adam_scott);
            }
        });

        mCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Dexter.withActivity(ProfileEdit.this)
                            .withPermissions(
                                    Manifest.permission.READ_EXTERNAL_STORAGE,

                                    Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            .withListener(new MultiplePermissionsListener() {
                                @Override
                                public void onPermissionsChecked(MultiplePermissionsReport report) {
                                    // check if all permissions are granted
                                    if (report.areAllPermissionsGranted()) {
                                        // do you work now

                                        dialog.dismiss();

                                        openCameraIntent();
                                    }

                                    // check for permanent denial of any permission
                                    if (report.isAnyPermissionPermanentlyDenied()) {
                                        // permission is denied permenantly, navigate user to app settings
                                    }
                                }

                                @Override
                                public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                                    token.continuePermissionRequest();
                                }
                            })
                            .onSameThread()
                            .check();


                } catch (Exception e) {

                }
            }
        });

        mGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                openGallery();
            }
        });
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);

        dialog.show();
    }

    private void openCameraIntent() {

        startActivityForResult(getPickImageChooserIntent(), 200);
    }

    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_GET_CONTENT);
        gallery.setType("image/*");
        startActivityForResult(gallery, RESULT_LOAD_IMAGE);
    }

    Uri picUri;
    Bitmap myBitmap;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        Bitmap bitmap;
        if (resultCode == Activity.RESULT_OK && requestCode == 200) {


            //   ImageView imageView = (ImageView) findViewById(R.id.imageView);

            if (getPickImageResultUri(data) != null) {
                picUri = getPickImageResultUri(data);

                try {
                    myBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), picUri);
                    myBitmap = rotateImageIfRequired(myBitmap, picUri);
                    myBitmap = getResizedBitmap(myBitmap, 500);

                    //CircleImageView croppedImageView = (CircleImageView) findViewById(R.id.img_profile);
                    //croppedImageView.setImageBitmap(myBitmap);
                    profile_image.setImageBitmap(myBitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                }


            } else {


                bitmap = (Bitmap) data.getExtras().get("data");

                myBitmap = bitmap;
                //   CircleImageView croppedImageView = (CircleImageView) findViewById(R.id.img_profile);
                if (profile_image != null) {
                    profile_image.setImageBitmap(myBitmap);
                }

                profile_image.setImageBitmap(myBitmap);

            }

        } else if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK) {
            Uri imageUri = data.getData();
            profile_image.setImageURI(imageUri);

          /*  if (data != null && data.getExtras() != null) {
                Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");
                profileImageview.setImageBitmap(imageBitmap);
            }*/

        }

    }


    public Intent getPickImageChooserIntent() {

        // Determine Uri of camera image to save.
        Uri outputFileUri = getCaptureImageOutputUri();


        List<Intent> allIntents = new ArrayList<>();
        PackageManager packageManager = getPackageManager();

        // collect all camera intents
        Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
        for (ResolveInfo res : listCam) {
            Intent intent = new Intent(captureIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(res.activityInfo.packageName);
            if (outputFileUri != null) {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            }
            allIntents.add(intent);
        }

        Intent mainIntent = allIntents.get(allIntents.size() - 1);
        for (Intent intent : allIntents) {
            if (intent.getComponent().getClassName().equals("com.android.documentsui.DocumentsActivity")) {
                mainIntent = intent;
                break;
            }
        }
        allIntents.remove(mainIntent);

        // Create a chooser from the main intent
        Intent chooserIntent = Intent.createChooser(mainIntent, "Select source");

        // Add all other intents
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, allIntents.toArray(new Parcelable[allIntents.size()]));

        return chooserIntent;
    }

    private Uri getCaptureImageOutputUri() {
        Uri outputFileUri = null;
        File getImage = getExternalCacheDir();
        if (getImage != null) {
            outputFileUri = Uri.fromFile(new File(getImage.getPath(), "profile.png"));
        }
        return outputFileUri;
    }

    private static Bitmap rotateImageIfRequired(Bitmap img, Uri selectedImage) throws IOException {

        ExifInterface ei = new ExifInterface(selectedImage.getPath());
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return rotateImage(img, 90);
            case ExifInterface.ORIENTATION_ROTATE_180:
                return rotateImage(img, 180);
            case ExifInterface.ORIENTATION_ROTATE_270:
                return rotateImage(img, 270);
            default:
                return img;
        }
    }

    private static Bitmap rotateImage(Bitmap img, int degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap rotatedImg = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
        img.recycle();
        return rotatedImg;
    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 0) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }


    public Uri getPickImageResultUri(Intent data) {
        boolean isCamera = true;
        if (data != null) {
            String action = data.getAction();
            isCamera = action != null && action.equals(MediaStore.ACTION_IMAGE_CAPTURE);
        }


        return isCamera ? getCaptureImageOutputUri() : data.getData();
    }

    private void apiCallEditProfile(JSONObject metrics_json) {

        AndroidNetworking.put(APIs.ACCOUNT_UPDATE_URL)
                .setPriority(Priority.HIGH)
                .addHeaders("sessionToken", Dashboard.SESSIONTOKEN)
                .addBodyParameter(metrics_json)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e(":EDIT PROFILE:", response.toString());
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });

    }

}


