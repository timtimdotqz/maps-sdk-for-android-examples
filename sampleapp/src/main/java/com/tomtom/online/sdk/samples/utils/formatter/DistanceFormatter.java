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

import android.annotation.SuppressLint;
import android.support.annotation.VisibleForTesting;

import java.util.Locale;

public final class DistanceFormatter {

    private static final double METERS_IN_ONE_KM = 1000;
    private static final double YARDS_IN_ONE_MILE = 1760.0;
    private static final double YARDS_IN_ONE_M = 1.0936133;

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

    @SuppressLint("DefaultLocale")
    @VisibleForTesting
    static String formatKilometers(int distanceInMeters) {

        if (distanceInMeters < METERS_IN_ONE_KM) {
            return String.format("%d m", distanceInMeters);
        }

        int distanceInKilometers = (int) (distanceInMeters / METERS_IN_ONE_KM);
        int remMeters = (int) (distanceInMeters % METERS_IN_ONE_KM);

        return String.format("%d km %d m", distanceInKilometers, remMeters);
    }

    @SuppressLint("DefaultLocale")
    @VisibleForTesting
    static String formatMiles(int distanceInMeters) {

        double yards = distanceInMeters * YARDS_IN_ONE_M;

        if (yards < YARDS_IN_ONE_MILE) {
            return String.format("%d yd", (int) yards);
        }

        int distanceInMiles = (int) (yards / YARDS_IN_ONE_MILE);
        int remYard = (int) (yards % YARDS_IN_ONE_MILE);

        return String.format(Locale.getDefault(), "%d mi %d yd", distanceInMiles, remYard);
    }

}