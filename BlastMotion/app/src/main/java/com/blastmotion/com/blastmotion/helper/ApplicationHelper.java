package com.blastmotion.com.blastmotion.helper;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.blastmotion.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.Window;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;

@SuppressLint("SimpleDateFormat") public class ApplicationHelper {

	private Context mContext;

	public ApplicationHelper(Context context) {
		// TODO Auto-generated constructor stub
		this.mContext = context;
	}


	public Dialog showProgressDialog(Activity activity, boolean isCancelable) {
		Dialog pgbar = new Dialog(activity);
		pgbar.requestWindowFeature(Window.FEATURE_NO_TITLE);
		pgbar.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		pgbar.setCancelable(isCancelable);
		ProgressBar pg = new ProgressBar(activity);
		pg.setIndeterminateDrawable(mContext.getResources().getDrawable(
                R.drawable.loadingprogressbar));
		pgbar.addContentView(pg, new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));
		return pgbar;
	}
	
	public Dialog showMessageDialog(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Information");
        builder.setMessage(msg);
        builder.setIcon(android.R.drawable.ic_dialog_info);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                dialog.dismiss();
            }
        });
        Dialog dialog = builder.create();
        dialog.show();
        return dialog;
    }

    public void noInternetConnection() {
        showMessageDialog("Please check your internet connection or try again later");
    }

    public boolean isInternetAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }

        }
        return false;

    }

	
	public String getCurrentVersion() {
        PackageManager manager = mContext.getPackageManager();
        try {
            PackageInfo info = manager.getPackageInfo(
                    mContext.getPackageName(), 0);
            String version = info.versionName;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return "1.0";
        }

    }

    public String getApplicationName() {
        return mContext.getPackageName();
    }

    public String getformattedDate(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy");
        return format.format(date);
    }

    @SuppressLint("SimpleDateFormat") public Date getDate(String date) {
        SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy");
        try {
            return format.parse(date);
        } catch (Exception e) {
            return new Date();
        }
    }

    public Date getTime(String time) {
        SimpleDateFormat format = new SimpleDateFormat("hh:mm a");
        try {
            return format.parse(time);
        } catch (Exception e) {
            return new Date();
        }
    }

    public String getformattedTime(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("hh:mm a");
        return format.format(date);
    }

    public boolean checkIsGPSOn() {

        LocationManager manager = (LocationManager) mContext
                .getSystemService(Context.LOCATION_SERVICE);
        return manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }


}
