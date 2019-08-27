package com.desertovercrowded.rock1055;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import static com.desertovercrowded.rock1055.Application.CHANNEL_ID;

class Extensions {
    //Open Website
    static void site(Context context) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(
                "http://www.1055rock.gr"));
        context.startActivity(intent);
    }

    //Share the app
    static void share(Context context) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, context.getString(R.string.share));
        sendIntent.setType("text/plain");
        context.startActivity(sendIntent);
    }

    //Rate the app
    static void rate(Context context) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(
                "https://play.google.com/store/apps/details?id=com.desertovercrowded.rock1055"));
        intent.setPackage("com.android.vending");
        Toast.makeText(context, " :) ", Toast.LENGTH_SHORT).show();
        context.startActivity(intent);
    }

    //Checks if user has Internet access
    static boolean isOnline(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    static void displayNoInternetAlert(final Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(R.string.isonline)
                .setTitle(R.string.unabletoconnect)
                .setCancelable(false)
                .setPositiveButton(R.string.settings,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent intent = new Intent(Settings.ACTION_SETTINGS);
                                //context.finish();
                                context.startActivity(intent);
                            }
                        }
                )
                .setNegativeButton(R.string.exit,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //finish();
                            }
                        }
                );
        AlertDialog alert = builder.create();
        alert.show();
    }

    //When the app closes, delete NotificationChannel
    static void deleteNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager manager = context.getSystemService(NotificationManager.class);
            assert manager != null;
            manager.deleteNotificationChannel(CHANNEL_ID);
        }
    }

}
