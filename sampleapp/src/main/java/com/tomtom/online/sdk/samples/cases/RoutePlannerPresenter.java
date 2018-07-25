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
package com.tomtom.online.sdk.samples.cases;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;

import com.google.common.base.Optional;
import com.tomtom.online.sdk.common.location.LatLng;
import com.tomtom.online.sdk.location.Locations;
import com.tomtom.online.sdk.map.CameraPosition;
import com.tomtom.online.sdk.map.Icon;
import com.tomtom.online.sdk.map.MapConstants;
import com.tomtom.online.sdk.map.Route;
import com.tomtom.online.sdk.map.RouteBuilder;
import com.tomtom.online.sdk.map.TomtomMap;
import com.tomtom.online.sdk.map.model.MapModeType;
import com.tomtom.online.sdk.map.rx.RxContext;
import com.tomtom.online.sdk.routing.OnlineRoutingApi;
import com.tomtom.online.sdk.routing.RoutingApi;
import com.tomtom.online.sdk.routing.data.FullRoute;
import com.tomtom.online.sdk.routing.data.RouteQuery;
import com.tomtom.online.sdk.routing.data.RouteResult;
import com.tomtom.online.sdk.samples.R;
import com.tomtom.online.sdk.samples.activities.BaseFunctionalExamplePresenter;
import com.tomtom.online.sdk.samples.fragments.FunctionalExampleFragment;
import com.tomtom.online.sdk.samples.routes.RouteConfigExample;
import com.tomtom.online.sdk.samples.utils.CheckedButtonCleaner;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public abstract class RoutePlannerPresenter extends BaseFunctionalExamplePresenter implements RxContext {

    protected static final int DEFAULT_ZOOM_FOR_EXAMPLE = 10;
    protected static final int DEFAULT_MAP_PADDING = 0;

    protected CheckedButtonCleaner checkedButtonCleaner;
    protected RoutingUiListener viewModel;

    protected RoutingApi routePlannerAPI;

    /**
     * Choose map which keep orders. To match queries with the order.
     */
    protected Map<Long, FullRoute> routesMap = new LinkedHashMap<>();
    protected CompositeDisposable compositeDisposable = new CompositeDisposable();
    private Icon startIcon;
    private Icon endIcon;

    public void setRoutesMap(Map<Long, FullRoute> routesMap) {
        this.routesMap = routesMap;
    }

    public Map<Long, FullRoute> getRoutesMap() {
        return routesMap;
    }

    public RoutePlannerPresenter(RoutingUiListener viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void onResume(Context context) {
        //tag::doc_initialise_routing[]
        routePlannerAPI = OnlineRoutingApi.create(context);
        //end::doc_initialise_routing[]
    }

    @Override
    public void onPause() {
        compositeDisposable.clear();
    }

    @NonNull
    @Override
    public Scheduler getWorkingScheduler() {
        return networkScheduler;
    }

    @NonNull
    @Override
    public Scheduler getResultScheduler() {
        return AndroidSchedulers.mainThread();
    }

    @Override
    public void cleanup() {
        compositeDisposable.clear();
        super.cleanup();
        tomtomMap.clear();
        tomtomMap.setPadding(DEFAULT_MAP_PADDING, DEFAULT_MAP_PADDING,
                DEFAULT_MAP_PADDING, DEFAULT_MAP_PADDING);

    }

    @Override
    public void bind(FunctionalExampleFragment view, TomtomMap map) {
        super.bind(view, map);

        confMapPadding();
        confRouteIcons();

        if (!view.isMapRestored()) {
            tomtomMap.getUiSettings().setMapModeType(MapModeType.MODE_2D);
            centerOnDefaultLocation();
        } else {
            tomtomMap.displayRoutesOverview();
        }

        viewModel.repeatRequestWhenNotFinished();
    }

    protected void confRouteIcons() {
        startIcon = Icon.Factory.fromResources(view.getContext(), R.drawable.ic_map_route_departure);
        endIcon = Icon.Factory.fromResources(view.getContext(), R.drawable.ic_map_route_destination);
    }

    protected void confMapPadding() {
        int offsetBig = view.getContext().getResources().getDimensionPixelSize(R.dimen.offset_super_big);

        int actionBarHeight = view.getContext().getResources().getDimensionPixelSize(
                android.support.v7.appcompat.R.dimen.abc_action_bar_default_height_material);

        int etaPanelHeight = view.getContext().getResources().getDimensionPixelSize(
                R.dimen.eta_panel_height);

        int buttonPadding = view.getContext().getResources().getDimensionPixelSize(R.dimen.button_offset);

        int topPadding = actionBarHeight + etaPanelHeight + offsetBig;
        int bottomPadding = actionBarHeight + buttonPadding;

        tomtomMap.setPadding(topPadding, offsetBig, bottomPadding, offsetBig);
    }

    @SuppressLint("CheckResult")
    public void showRoute(RouteQuery routeQuery) {
        //tag::doc_execute_routing[]
        Disposable subscribe = routePlannerAPI.planRoute(routeQuery).subscribeOn(getWorkingScheduler())
                .observeOn(getResultScheduler())
                .subscribe(new Consumer<RouteResult>() {
                    @Override
                    public void accept(RouteResult routeResult) throws Exception {
                        displayRoutes(routeResult);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        proceedWithError(throwable.getMessage());
                    }
                });
        //end::doc_execute_routing[]
        compositeDisposable.add(subscribe);
    }

    protected Optional<FullRoute> getActiveRoute(List<FullRoute> fullRoutes) {
        if (fullRoutes != null && !fullRoutes.isEmpty()) {
            return Optional.of(fullRoutes.get(0));
        }
        return Optional.absent();
    }

    protected void displayRoutes(RouteResult routeResult) {

        routesMap.clear();

        displayFullRoutes(routeResult);

        tomtomMap.displayRoutesOverview();
    }

    protected void displayFullRoutes(RouteResult routeResult) {
        List<FullRoute> routes = routeResult.getRoutes();
        Optional<FullRoute> activeRoute = getActiveRoute(routes);

        for (FullRoute route : routes) {

            boolean isActiveRoute = activeRoute.isPresent() ? activeRoute.get().equals(route) : false;

            //tag::doc_display_route[]
            RouteBuilder routeBuilder = new RouteBuilder(route.getCoordinates())
                    .isActive(isActiveRoute)
                    .endIcon(endIcon)
                    .startIcon(startIcon);
            final Route mapRoute = tomtomMap.addRoute(routeBuilder);
            //end::doc_display_route[]
            routesMap.put(mapRoute.getId(), route);
            if (isActiveRoute) {
                displayInfoAboutRoute(route);
            }
        }
    }

    protected void displayInfoAboutRoute(FullRoute routeResult) {
        viewModel.hideRoutingInProgressDialog();
        viewModel.routeUpdated(routeResult);
    }

    protected void proceedWithError(String message) {
        viewModel.showError(view.getContext().getString(R.string.msg_error_general_route_processing, message));
        viewModel.hideRoutingInProgressDialog();
    }

    public void centerOnDefaultLocation() {
        tomtomMap.centerOn(CameraPosition.builder(new LatLng(Locations.AMSTERDAM.getLatitude(), Locations.AMSTERDAM.getLongitude()))
                .bearing(MapConstants.ORIENTATION_NORTH)
                .zoom(DEFAULT_ZOOM_FOR_EXAMPLE)
                .build());
    }

    public void setCheckedButtonCleaner(CheckedButtonCleaner checkedButtonCleaner) {
        this.checkedButtonCleaner = checkedButtonCleaner;
    }

    public float getConsumption(FullRoute route) {
        //One of those two items is always 0, so max will select the proper value
        float fuelConsumption = route.getSummary().getFuelConsumptionInLiters();
        float batteryConsumption = route.getSummary().getBatteryConsumptionInkWh();
        return Math.max(fuelConsumption, batteryConsumption);
    }

    @Override
    public void alignCompassButton(FunctionalExampleFragment view, TomtomMap tomtomMap) {
        int compassButtonTopMargin = view.getContext().getResources().getDimensionPixelSize(R.dimen.compass_default_margin_top);
        int compassLeftMargin = view.getContext().getResources().getDimensionPixelSize(R.dimen.compass_default_margin_start);
        tomtomMap.getUiSettings().getCompassView().setMargins(compassLeftMargin, compassButtonTopMargin + getCurrentLocationBottomMarginDelta(view), 0, 0);
    }

    @Override
    public void resetCompassButton(FunctionalExampleFragment view, TomtomMap tomtomMap) {
        int compassLeftMargin = view.getContext().getResources().getDimensionPixelSize(R.dimen.compass_default_margin_start);
        int compassButtonTopMargin = view.getContext().getResources().getDimensionPixelSize(R.dimen.compass_default_margin_top);
        tomtomMap.getUiSettings().getCompassView().setMargins(compassLeftMargin, compassButtonTopMargin, 0, 0);
    }

    public abstract RouteConfigExample getRouteConfig();

}