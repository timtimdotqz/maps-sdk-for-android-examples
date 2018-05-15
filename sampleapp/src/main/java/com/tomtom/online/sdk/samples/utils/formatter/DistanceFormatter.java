/**
 * Copyright (c) 2015-2018 TomTom N.V. All rights reserved.
 *
 * This software is the proprietary copyright of TomTom N.V. and its subsidiaries and may be used
 * for internal evaluation purposes or commercial use strictly subject to separate licensee
 * agreement between you and TomTom. If you are the licensee, you are only permitted to use
 * this Software in accordance with the terms of your license agreement. If you are not the
 * licensee then you are not authorised to use this software in any manner and should
 * immediately return it to TomTom N.V.
 */
package com.tomtom.online.sdk.samples.utils.formatter;

import java.util.Locale;

public final class DistanceFormatter {

    private static final double METERS_IN_ONE_KM = 1000;
    private static final double METERS_IN_ONE_MILE = 1609.0;

    private DistanceFormatter() {

    }

    public static String format(int distanceInMeters) {
        if (isMiles()) {
            return formatMiles(distanceInMeters);
        } else {
            return formatKilometers(distanceInMeters);
        }
    }

    private static boolean isMiles() {
        Locale locale = Locale.getDefault();
        return locale.equals(Locale.US) || locale.equals(Locale.UK);
    }

    private static String formatKilometers(int distanceInMeters) {
        int distanceInKilometers = (int) (distanceInMeters / METERS_IN_ONE_KM);
        return String.format("%d km", distanceInKilometers);
    }

    private static String formatMiles(int distanceInMeters) {
        int distanceInMiles = (int) (distanceInMeters / METERS_IN_ONE_MILE);
        return String.format(Locale.getDefault(), "%d mi", distanceInMiles);
    }

}