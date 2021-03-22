package com.example.javabreak;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import java.util.Random;

public class LoopTimer {

    private long mSec;

    public LoopTimer(long mSec) {
        this.mSec = mSec;
    }

    public void setSec(long mSec) {
        this.mSec = mSec;
    }

    public long getSec() {
        return mSec;
    }



}
