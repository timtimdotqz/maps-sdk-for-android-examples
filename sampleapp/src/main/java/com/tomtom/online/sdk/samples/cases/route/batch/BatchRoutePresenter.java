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
package com.tomtom.online.sdk.samples.cases.route.batch;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.google.common.base.Optional;
import com.tomtom.online.sdk.map.Route;
import com.tomtom.online.sdk.map.TomtomMap;
import com.tomtom.online.sdk.map.TomtomMapCallback;
import com.tomtom.online.sdk.routing.data.Avoid;
import com.tomtom.online.sdk.routing.data.FullRoute;
import com.tomtom.online.sdk.routing.data.RouteQueryBuilder;
import com.tomtom.online.sdk.routing.data.RouteResult;
import com.tomtom.online.sdk.routing.data.RouteType;
import com.tomtom.online.sdk.routing.data.TravelMode;
import com.tomtom.online.sdk.routing.data.batch.BatchRoutingQuery;
import com.tomtom.online.sdk.routing.data.batch.BatchRoutingQueryBuilder;
import com.tomtom.online.sdk.routing.data.batch.BatchRoutingResponse;
import com.tomtom.online.sdk.samples.R;
import com.tomtom.online.sdk.samples.activities.FunctionalExampleModel;
import com.tomtom.online.sdk.samples.cases.RoutePlannerPresenter;
import com.tomtom.online.sdk.samples.cases.RoutingUiListener;
import com.tomtom.online.sdk.samples.fragments.FunctionalExampleFragment;
import com.tomtom.online.sdk.samples.routes.AmsterdamToRotterdamRouteConfig;
import com.tomtom.online.sdk.samples.routes.RouteConfigExample;

import java.util.List;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class BatchRoutePresenter extends RoutePlannerPresenter {

    public static int[] routesDescription = new int[]{R.string.empty, R.string.empty};

    public BatchRoutePresenter(RoutingUiListener viewModel) {
        super(viewModel);
    }

    @Override
    public void bind(FunctionalExampleFragment view, TomtomMap map) {
        super.bind(view, map);
        tomtomMap.addOnRouteClickListener(onRouteClickListener);
    }

    @Override
    public void cleanup() {
        super.cleanup();
        activeRouteSet = false;
        tomtomMap.removeOnRouteClickListener(onRouteClickListener);
    }

    @Override
    public FunctionalExampleModel getModel() {
        return new BatchRouteFunctionalExample();
    }

    public void startRoutingDependsOnTravelMode() {
        planRoute(getTravelModeBatchRouteQuery());
    }

    public void startRoutingDependsOnRouteType() {
        planRoute(getRouteTypeBatchRouteQuery());
    }

    public void startRoutingDependsOnAvoids() {
        planRoute(getAvoidsBatchRouteQuery());
    }

    private void planRoute(BatchRoutingQuery query) {
        clearRouteSelection();
        viewModel.showRoutingInProgressDialog();

        Disposable subscribe = routePlannerAPI.planBatchRoute(query)

                .subscribeOn(getWorkingScheduler())
                .observeOn(getResultScheduler())
                .subscribe(new Consumer<BatchRoutingResponse>() {
                    @Override
                    public void accept(BatchRoutingResponse batchRoutingResponse) throws Exception {
                        displayRouteAndSetDescription(batchRoutingResponse);
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

    private void clearRouteSelection() {
        tomtomMap.clearRoute();
        routesMap.clear();
        activeRouteSet = false;
    }

    private void displayRouteAndSetDescription(BatchRoutingResponse batchRoutingResponse) {
        tomtomMap.clearRoute();
        int i = 0;
        for (RouteResult routeResult : batchRoutingResponse.getRouteRoutingResponses()) {
            for (FullRoute fullRoute : routeResult.getRoutes()) {
                fullRoute.setTag(getContext().getString(routesDescription[i++]));
            }
            displayFullRoutes(routeResult);
        }
        tomtomMap.displayRoutesOverview();
    }

    boolean activeRouteSet = false;

    /**
     * Choose first route as a active.
     * @param fullRoutes
     * @return
     */
    @Override
    protected Optional<FullRoute> getActiveRoute(List<FullRoute> fullRoutes) {
        if (!activeRouteSet) {
            activeRouteSet = true;
            return Optional.fromNullable(fullRoutes.get(0));
        } else {
            return Optional.absent();
        }
    }

    @VisibleForTesting
    protected BatchRoutingQuery getTravelModeBatchRouteQuery() {
        routesDescription = new int[]{R.string.batch_travel_mode_car_text, R.string.batch_travel_mode_pedestrian_text};
        return new BatchRoutingQueryBuilder()
                .withRouteQuery(new RouteQueryBuilder(getRouteConfig().getOrigin(), getRouteConfig().getDestination()).withTravelMode(TravelMode.CAR))
                .withRouteQuery(new RouteQueryBuilder(getRouteConfig().getOrigin(), getRouteConfig().getDestination()).withTravelMode(TravelMode.PEDESTRIAN))
                ;
    }

    @VisibleForTesting
    protected BatchRoutingQuery getRouteTypeBatchRouteQuery() {
        routesDescription = new int[]{R.string.batch_route_type_fastest, R.string.batch_route_type_shortest};
        return new BatchRoutingQueryBuilder()
                .withRouteQuery(new RouteQueryBuilder(getRouteConfig().getOrigin(), getRouteConfig().getDestination()).withRouteType(RouteType.FASTEST))
                .withRouteQuery(new RouteQueryBuilder(getRouteConfig().getOrigin(), getRouteConfig().getDestination()).withRouteType(RouteType.SHORTEST))
                ;
    }

    @VisibleForTesting
    protected BatchRoutingQuery getAvoidsBatchRouteQuery() {
        routesDescription = new int[]{R.string.batch_avoid_motorways, R.string.batch_avoid_ferries};
        return new BatchRoutingQueryBuilder()
                .withRouteQuery(new RouteQueryBuilder(getRouteConfig().getOrigin(), getRouteConfig().getDestination()).withAvoidType(Avoid.MOTORWAYS))
                .withRouteQuery(new RouteQueryBuilder(getRouteConfig().getOrigin(), getRouteConfig().getDestination()).withAvoidType(Avoid.FERRIES))
                ;
    }

    @Override
    public RouteConfigExample getRouteConfig() {
        return new AmsterdamToRotterdamRouteConfig();
    }

    private TomtomMapCallback.OnRouteClickListener onRouteClickListener = new TomtomMapCallback.OnRouteClickListener() {
        @Override
        public void onRouteClick(@NonNull Route route) {
            long routeId = route.getId();
            tomtomMap.getRouteSettings().setRoutesInactive();
            tomtomMap.getRouteSettings().setRouteActive(routeId);
            FullRoute fullRoute = routesMap.get(routeId);
            displayInfoAboutRoute(fullRoute);
        }
    };

}
