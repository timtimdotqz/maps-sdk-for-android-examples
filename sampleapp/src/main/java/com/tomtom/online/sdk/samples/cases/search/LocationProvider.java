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
package com.tomtom.online.sdk.samples.cases.search;

import android.content.Context;
import android.location.Location;

import com.tomtom.online.sdk.common.location.LatLng;
import com.tomtom.online.sdk.location.LocationRequestsFactory;
import com.tomtom.online.sdk.location.LocationSource;
import com.tomtom.online.sdk.location.LocationSourceFactory;
import com.tomtom.online.sdk.location.LocationUpdateListener;
import com.tomtom.online.sdk.location.Locations;

class LocationProvider {

    private LocationSource locationSource;

    LocationProvider(Context context, LocationUpdateListener updateListener) {
        this.locationSource = createLocationSource(context, updateListener);
    }

    LatLng getLastKnownPosition() {
        Location location = locationSource.getLastKnownLocation();
        if (location == null) {
            location = Locations.AMSTERDAM;
        }
        return new LatLng(location);
    }

    void activateLocationSource() {
        locationSource.activate();
    }

    void deactivateLocationSource() {
        locationSource.deactivate();
    }

    private LocationSource createLocationSource(Context context, LocationUpdateListener updateListener) {
        return new LocationSourceFactory().createDefaultLocationSource(context, updateListener,
                LocationRequestsFactory.create().createSearchLocationRequest());
    }
}
