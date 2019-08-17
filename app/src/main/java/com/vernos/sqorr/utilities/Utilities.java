package com.vernos.sqorr.utilities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.RectF;
import android.net.ConnectivityManager;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.vernos.sqorr.R;
import com.vernos.sqorr.views.UserLocation;

import me.aflak.libraries.callback.FingerprintCallback;
import me.aflak.libraries.dialog.DialogAnimation;
import me.aflak.libraries.dialog.FingerprintDialog;
import me.aflak.libraries.view.Fingerprint;


public class Utilities {



    public static boolean isNetworkAvailable(Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    public static void showNoInternetAlert(final Context context) {
        // Create custom dialog object
        final Dialog dialog = new Dialog(context);
        // Include dialog.xml file
        dialog.setContentView(R.layout.no_internet);

        Window window = dialog.getWindow();
        // window.setGravity(Gravity.CENTER);
//        window.setGravity(Gravity.BOTTOM);

        WindowManager.LayoutParams param = window.getAttributes();
        window.setLayout(Toolbar.LayoutParams.MATCH_PARENT, Toolbar.LayoutParams.WRAP_CONTENT);
        param.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
        param.y = 110;
        window.setAttributes(param);
        window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        dialog.show();
        dialog.setCancelable(false);


        TextView declineButton = dialog.findViewById(R.id.settings);
        // if decline button is clicked, close the custom dialog
        declineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();


                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.setComponent(new ComponentName("com.android.settings",
                        "com.android.settings.Settings"));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);


            }
        });

    }

    public static void showAlertBox(Context context, String title, String message) {

        // Create custom dialog object
        final Dialog dialog = new Dialog(context);
        // Include dialog.xml file
        dialog.setContentView(R.layout.alerts);


        Window window = dialog.getWindow();

        window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        // window.setGravity(Gravity.CENTER);
//        window.setGravity(Gravity.BOTTOM);


        dialog.show();
        dialog.setCancelable(false);


        TextView alert_title = (TextView) dialog.findViewById(R.id.alert_title);
        TextView alert_msg = (TextView) dialog.findViewById(R.id.alert_msg);

        alert_title.setText(title);
        alert_msg.setText(message);
        TextView alert_ok = (TextView) dialog.findViewById(R.id.alert_ok);
        // if decline button is clicked, close the custom dialog
        alert_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();



            }
        });

    }

    public static void showAlertBoxLoc(final Context context, String title, String message) {

        // Create custom dialog object
        final Dialog dialog = new Dialog(context);
        // Include dialog.xml file
        dialog.setContentView(R.layout.alerts);


        Window window = dialog.getWindow();

        window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        // window.setGravity(Gravity.CENTER);
//        window.setGravity(Gravity.BOTTOM);


        dialog.show();
        dialog.setCancelable(false);


        TextView alert_title = (TextView) dialog.findViewById(R.id.alert_title);
        TextView alert_msg = (TextView) dialog.findViewById(R.id.alert_msg);

        alert_title.setText(title);
        alert_msg.setText(message);
        TextView alert_ok = (TextView) dialog.findViewById(R.id.alert_ok);
        // if decline button is clicked, close the custom dialog
        alert_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.setComponent(new ComponentName("com.android.settings",
                        "com.android.settings.Settings"));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);


            }
        });

    }



    public static void showAlertBoxTwo(Context context, String title, String message) {

        // Create custom dialog object
        final Dialog dialog = new Dialog(context);
        // Include dialog.xml file
        dialog.setContentView(R.layout.alerts_two);


        Window window = dialog.getWindow();

        window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        // window.setGravity(Gravity.CENTER);
//        window.setGravity(Gravity.BOTTOM);


        dialog.show();
        dialog.setCancelable(false);


        TextView alert_title = (TextView) dialog.findViewById(R.id.alert_title);
        TextView alert_msg = (TextView) dialog.findViewById(R.id.alert_msg);

        alert_title.setText(title);
        alert_msg.setText(message);
        TextView alert_ok = (TextView) dialog.findViewById(R.id.alert_ok);
        alert_ok.setText("Add Funds");
        // if decline button is clicked, close the custom dialog
        alert_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
//                Intent aa = new Intent()


            }
        });

        TextView alert_cancel = (TextView) dialog.findViewById(R.id.alert_cancel);
        // if decline button is clicked, close the custom dialog
        alert_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();


            }
        });


//        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
//        alertDialog.setTitle("Alert");
//        alertDialog.setMessage(message);
//
//        alertDialog.setPositiveButton("OK",
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//
//                    }
//                });
//
//        alertDialog.show();

    }



    public static Path resizePath(Path path, float width, float height) {
        RectF bounds = new RectF(0, 0, width, height);
        Path resizedPath = new Path(path);
        RectF src = new RectF();
        resizedPath.computeBounds(src, true);

        Matrix resizeMatrix = new Matrix();
        resizeMatrix.setRectToRect(src, bounds, Matrix.ScaleToFit.CENTER);
        resizedPath.transform(resizeMatrix);

        return resizedPath;
    }

    public static Bitmap convertTParellelogram(Bitmap src, String type, Context context) {
        Bitmap typex;
        if (type.equals("pare")) {
            typex = BitmapUtils.getCroppedBitmap(src, getParellelogramPath(src, type, context));
        } else {
            typex = BitmapUtils.getCroppedBitmap(src, getParellelogramPath(src, type, context));
        }
        return typex;
    }


    public static Path getParellelogramPath(Bitmap src, String type, Context context) {
        Path path = null;
        //  Context context;
        if (type.equals("pare")) {
            path = resizePath(PathParser.createPathFromPathData(context.getString(R.string.pare)),
                    src.getWidth(), src.getHeight());
        } else {
            path = resizePath(PathParser.createPathFromPathData(context.getString(R.string.square)),
                    src.getWidth(), src.getHeight());
        }

        return path;
    }


    public static String getMonthName(int month) {

        switch (month) {
            case 1:
                return "January";
            case 2:
                return "February";
            case 3:
                return "March";
            case 4:
                return "April";
            case 5:
                return "May";
            case 6:
                return "June";
            case 7:
                return "July";
            case 8:
                return "August";
            case 9:
                return "September";
            case 10:
                return "October";
            case 11:
                return "November";
            case 12:
                return "December";
            default:
                break;
        }
        return null;
    }


    public static void showToast(Context applicationContext, String msg) {
        try {
            // Toast.makeText(applicationContext, msg, Toast.LENGTH_LONG).show();

            Toast toast = Toast.makeText(applicationContext, msg, Toast.LENGTH_SHORT);
            LinearLayout toastLayout = (LinearLayout) toast.getView();
            TextView toastTV = (TextView) toastLayout.getChildAt(0);
            //  Typeface typeface = Typeface.createFromAsset(applicationContext.getAssets(),"fonts/exo_regular.ttf");
            //  toastTV.setTypeface(typeface);
            toastTV.setTextSize(TypedValue.COMPLEX_UNIT_PX, applicationContext.getResources().getDimension(R.dimen.normal_size));
            toast.show();
        } catch (Exception e) {
            Log.e("Globals", e.toString());
        }
    }


    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    /*public static boolean checkLocationPermission(Context applicationContext, UserLocation userLocation) {
        if (ContextCompat.checkSelfPermission(applicationContext, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(userLocation, android.Manifest.permission.ACCESS_FINE_LOCATION) ||
                    ActivityCompat.shouldShowRequestPermissionRationale(userLocation, android.Manifest.permission.ACCESS_COARSE_LOCATION)) {

                ActivityCompat.requestPermissions(userLocation, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            } else {
                ActivityCompat.requestPermissions(userLocation, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }*/

  /*  public static int  getColorCodes(String gameName){
        int colorCode = 0;

        switch (gameName){
            case "soccer":
                colorCode=R.color.acc_hint_color;
                break;
            default:break;
        }
        return colorCode;

    }
*/

//    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    public static boolean checkLocationPermission(Context applicationContext, Activity atc) {
        if (ContextCompat.checkSelfPermission(applicationContext, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(atc, android.Manifest.permission.ACCESS_FINE_LOCATION) ||
                    ActivityCompat.shouldShowRequestPermissionRationale(atc, android.Manifest.permission.ACCESS_COARSE_LOCATION)) {

                ActivityCompat.requestPermissions(atc, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            } else {
                ActivityCompat.requestPermissions(atc, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }



}
