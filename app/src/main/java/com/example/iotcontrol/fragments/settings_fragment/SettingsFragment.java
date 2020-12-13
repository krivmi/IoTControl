package com.example.iotcontrol.fragments.settings_fragment;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;

import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.iotcontrol.MainActivity;
import com.example.iotcontrol.R;

import java.util.concurrent.atomic.AtomicBoolean;

public class SettingsFragment extends Fragment {

    private static final String TAG = "APP";
    LinearLayout settings_fragment;
    TextView about_text;
    Switch notify_switch;
    Switch night_switch;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_settings, container, false);

        about_text = rootView.findViewById(R.id.about_text);
        notify_switch = rootView.findViewById(R.id.notification_switch);
        night_switch = rootView.findViewById(R.id.night_switch);

        SharedPreferences sh = this.getActivity().getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);

        if(sh.getBoolean("night", false)){ setNightModeColors(rootView); }

        SetSwitch(notify_switch, "notification", sh.getBoolean("notification", false));
        SetSwitch(night_switch, "night", sh.getBoolean("night", false));

        notify_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) { StoreSwitch("notification", true); }
                else { StoreSwitch("notification", false); }
            }
        });

        night_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(night_switch.isPressed()) {
                    if (isChecked) {
                        popupMessage();
                    } else {
                        popupMessage();
                    }
                }
            }
        });

        return rootView;
    }
    public void SetSwitch(Switch s, String key, boolean value){
        if(value){
            s.setChecked(true);
            StoreSwitch(key, true);
        }
        else{
            s.setChecked(false);
            StoreSwitch(key, false);
        }
    }
    public void StoreSwitch(String key, boolean value){
        SharedPreferences sh = this.getActivity().getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor myEdit = sh.edit();
        myEdit.putBoolean(key, value);
        myEdit.commit();
    }
    public void popupMessage(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setTitle("Set night mode");
        alertDialogBuilder.setMessage("This action will restart the app!");
        alertDialogBuilder.setNegativeButton("cancel", new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(night_switch.isChecked()){
                    night_switch.setChecked(false);
                }
                else {
                    night_switch.setChecked(true);
                }
            }
        });
        alertDialogBuilder.setPositiveButton("ok", new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(night_switch.isChecked()){
                    //night_switch.setChecked(false);
                    StoreSwitch("night", true);
                }
                else {
                    //night_switch.setChecked(true);
                    StoreSwitch("night", false);
                }
                doRestart(getContext());
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
    private void setNightModeColors(ViewGroup rootView){
        int layout_color = getResources().getColor(R.color.colorLayout);
        int text_color = getResources().getColor(R.color.colorAqua);

        settings_fragment = rootView.findViewById(R.id.settings_fragment);
        settings_fragment.setBackgroundColor(getResources().getColor(R.color.colorBlack));

        TextView _not = (TextView) rootView.findViewById(R.id.notification_switch);
        TextView _night = (TextView) rootView.findViewById(R.id.night_switch);
        TextView _aboutHeading = (TextView) rootView.findViewById(R.id.about_heading);
        TextView _contact = (TextView) rootView.findViewById(R.id.contact_heading);
        TextView _contactText = (TextView) rootView.findViewById(R.id.contact_text);
        TextView _versionText = (TextView) rootView.findViewById(R.id.version_text);
        TextView _versionHeading = (TextView) rootView.findViewById(R.id.version_title);

        _not.setBackgroundColor(layout_color);
        _night.setBackgroundColor(layout_color);
        _aboutHeading.setBackgroundColor(layout_color);
        _contact.setBackgroundColor(layout_color);
        _contactText.setBackgroundColor(layout_color);
        _versionText.setBackgroundColor(layout_color);
        _versionHeading.setBackgroundColor(layout_color);
        about_text.setBackgroundColor(layout_color);

        _not.setTextColor(text_color);
        _night.setTextColor(text_color);
        _aboutHeading.setTextColor(text_color);
        _contact.setTextColor(text_color);
        _contactText.setTextColor(text_color);
        _versionText.setTextColor(text_color);
        _versionHeading.setTextColor(text_color);
        about_text.setTextColor(text_color);

        _not.setTypeface(null, Typeface.BOLD);
        _night.setTypeface(null, Typeface.BOLD);
        _aboutHeading.setTypeface(null, Typeface.BOLD);
        _contact.setTypeface(null, Typeface.BOLD);
        _versionHeading.setTypeface(null, Typeface.BOLD);
    }
    public static void doRestart(Context c) {
        try {
            //check if the context is given
            if (c != null) {
                //fetch the packagemanager so we can get the default launch activity
                // (you can replace this intent with any other activity if you want
                PackageManager pm = c.getPackageManager();
                //check if we got the PackageManager
                if (pm != null) {
                    //create the intent with the default start activity for your application
                    Intent mStartActivity = pm.getLaunchIntentForPackage(
                            c.getPackageName()
                    );
                    if (mStartActivity != null) {
                        mStartActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        //create a pending intent so the application is restarted after System.exit(0) was called.
                        // We use an AlarmManager to call this intent in 100ms
                        int mPendingIntentId = 223344;
                        PendingIntent mPendingIntent = PendingIntent
                                .getActivity(c, mPendingIntentId, mStartActivity,
                                        PendingIntent.FLAG_CANCEL_CURRENT);
                        AlarmManager mgr = (AlarmManager) c.getSystemService(Context.ALARM_SERVICE);
                        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent);
                        //kill the application
                        System.exit(0);
                    } else {
                        Log.e(TAG, "Was not able to restart application, mStartActivity null");
                    }
                } else {
                    Log.e(TAG, "Was not able to restart application, PM null");
                }
            } else {
                Log.e(TAG, "Was not able to restart application, Context null");
            }
        } catch (Exception ex) {
            Log.e(TAG, "Was not able to restart application");
        }
    }
}