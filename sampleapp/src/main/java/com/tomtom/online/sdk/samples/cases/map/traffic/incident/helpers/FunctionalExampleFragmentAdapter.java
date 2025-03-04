/**
 * Copyright (c) 2015-2019 TomTom N.V. All rights reserved.
 *
 * This software is the proprietary copyright of TomTom N.V. and its subsidiaries and may be used
 * for internal evaluation purposes or commercial use strictly subject to separate licensee
 * agreement between you and TomTom. If you are the licensee, you are only permitted to use
 * this Software in accordance with the terms of your license agreement. If you are not the
 * licensee then you are not authorised to use this software in any manner and should
 * immediately return it to TomTom N.V.
 */
package com.tomtom.online.sdk.samples.cases.map.traffic.incident.helpers;

import androidx.fragment.app.Fragment;

import com.tomtom.online.sdk.samples.fragments.FunctionalExampleFragment;

public class FunctionalExampleFragmentAdapter extends Fragment implements FunctionalExampleFragment {

    @Override
    public void onMenuItemSelected() {
    }

    @Override
    public boolean onBackPressed() {
        return true;
    }

    @Override
    public boolean isMapRestored() {
        return false;
    }

    @Override
    public String getFragmentTag() {
        return null;
    }

    @Override
    public void setActionBarTitle(int titleId) {
    }

    @Override
    public void setActionBarSubtitle(int subtitleId) {

    }

    @Override
    public void showInfoText(String text, long duration) {

    }

    @Override
    public void showInfoText(int text, long duration) {

    }

    @Override
    public void enableOptionsView() {

    }

    @Override
    public void disableOptionsView() {

    }
}
