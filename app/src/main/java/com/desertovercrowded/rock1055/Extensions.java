package com.desertovercrowded.rock1055;

import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import static com.desertovercrowded.rock1055.Application.CHANNEL_ID;

class Extensions {

    private Extensions() {
        throw new IllegalStateException("Extensions class");
    }

    //Send SMS
    static void sms(Context context) {
        Intent mobileIntent = new Intent();
        mobileIntent.setAction(Intent.ACTION_SENDTO);
        mobileIntent.setData(Uri.parse("smsto:6999241055"));
        context.startActivity(mobileIntent);
    }

    //Send Viber text
    static void viber(Context context) {
        PackageManager pm = context.getPackageManager();
        boolean isInstalled = isPackageInstalled("com.viber.voip", pm);
        if (isInstalled) {
            Intent viberIntent = new Intent();
            viberIntent.setAction(Intent.ACTION_VIEW);
            viberIntent.setPackage("com.viber.voip");
            viberIntent.setData(Uri.parse("tel:6999241055"));
            context.startActivity(viberIntent);
        } else {
            Toast viberToast = Toast.makeText(context.getApplicationContext(), "Το Viber δεν είναι εγκατεστημένο", Toast.LENGTH_SHORT);
            viberToast.show();
        }
    }

    //Checks if a specific app is installed(we need this for viber)
    static boolean isPackageInstalled(String packageName, PackageManager packageManager) {
        try {
            packageManager.getPackageInfo(packageName, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }


    //Open Website
    static void site(Context context) {
        Intent siteintent = new Intent(Intent.ACTION_VIEW);
        siteintent.setData(Uri.parse(
                "http://www.1055rock.gr"));
        context.startActivity(siteintent);
    }

    //Rate the app
    static void rate(Context context) {
        Intent rate = new Intent(Intent.ACTION_VIEW);
        rate.setData(Uri.parse(
                "https://play.google.com/store/apps/details?id=com.desertovercrowded.rock1055"));
        rate.setPackage("com.android.vending");
        Toast.makeText(context, " :) ", Toast.LENGTH_SHORT).show();
        context.startActivity(rate);
    }

    //Share the app
    static void share(Context context) {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, context.getString(R.string.share));
        shareIntent.setType("text/plain");
        context.startActivity(shareIntent);
    }

    //Checks if user has Internet access
    static boolean isOnline(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());

        if (capabilities != null) {
            return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);
        }
        return false;
    }

    static void displayNoInternetAlert(final Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(R.string.isonline)
                .setTitle(R.string.unabletoconnect)
                .setCancelable(false)
                .setPositiveButton(R.string.settings,
                        (dialog, id) -> {
                            Intent intent = new Intent(Settings.ACTION_SETTINGS);
                            context.startActivity(intent);
                        }
                )
                .setNegativeButton(R.string.back,
                        (dialog, id) -> {
                        }
                );
        AlertDialog alert = builder.create();
        alert.show();
    }

    // When the app closes, delete NotificationChannel
    // Google doesn't allow this anymore!
    static void deleteNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager manager = context.getSystemService(NotificationManager.class);
            assert manager != null;
            manager.deleteNotificationChannel(CHANNEL_ID);
        }
    }
}
