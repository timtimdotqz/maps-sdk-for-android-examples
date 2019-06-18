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
package com.tomtom.online.sdk.samples.cases.route.modes;

import androidx.annotation.VisibleForTesting;

import com.tomtom.online.sdk.map.TomtomMap;
import com.tomtom.online.sdk.routing.data.RouteQuery;
import com.tomtom.online.sdk.routing.data.TravelMode;
import com.tomtom.online.sdk.samples.activities.FunctionalExampleModel;
import com.tomtom.online.sdk.samples.cases.RoutePlannerPresenter;
import com.tomtom.online.sdk.samples.cases.RoutingUiListener;
import com.tomtom.online.sdk.samples.cases.route.RouteQueryFactory;
import com.tomtom.online.sdk.samples.fragments.FunctionalExampleFragment;
import com.tomtom.online.sdk.samples.routes.AmsterdamToRotterdamRouteConfig;

public class RouteTravelModesPresenter extends RoutePlannerPresenter {

    private TravelMode travelMode;

    public RouteTravelModesPresenter(RoutingUiListener viewModel) {
        super(viewModel);
    }

    @Override
    public void bind(FunctionalExampleFragment view, TomtomMap map) {
        super.bind(view, map);
        if (travelMode != null) {
            displayRoute(travelMode);
        }
    }

    @Override
    public FunctionalExampleModel getModel() {
        return new RouteTravelModesFunctionalExample();
    }

    void displayRoute(TravelMode travelMode) {
        tomtomMap.clearRoute();
        viewModel.showRoutingInProgressDialog();
        showRoute(getRouteQuery(travelMode));
    }

    @VisibleForTesting
    protected RouteQuery getRouteQuery(TravelMode travelMode) {
        return RouteQueryFactory.createRouteTravelModesQuery(travelMode, new AmsterdamToRotterdamRouteConfig());
    }

    public void startTravelModeTruck() {
        displayRoute(TravelMode.TRUCK);
    }

    public void startTravelModePedestrian() {
        displayRoute(TravelMode.PEDESTRIAN);
    }

    public void startTravelModeCar() {
        displayRoute(TravelMode.CAR);
    }

    public TravelMode getTravelMode() {
        return travelMode;
    }

    public void setTravelMode(TravelMode travelMode) {
        this.travelMode = travelMode;
    }

}
