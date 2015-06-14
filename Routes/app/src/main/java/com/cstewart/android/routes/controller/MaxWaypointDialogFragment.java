package com.cstewart.android.routes.controller;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.cstewart.android.routes.R;

public class MaxWaypointDialogFragment extends DialogFragment {

    public static MaxWaypointDialogFragment newInstance() {
        return new MaxWaypointDialogFragment();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.max_waypoint_title)
                .setMessage(R.string.max_waypoint_message)
                .setPositiveButton(R.string.max_waypoint_positive_button, null)
                .create();
    }
}
