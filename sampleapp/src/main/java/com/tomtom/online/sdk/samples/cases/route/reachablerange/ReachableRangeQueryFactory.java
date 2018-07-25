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
package com.tomtom.online.sdk.samples.cases.route.reachablerange;

import com.tomtom.online.sdk.data.CommonParams;
import com.tomtom.online.sdk.data.CommonParamsBuilder;
import com.tomtom.online.sdk.data.SpeedToConsumptionMap;
import com.tomtom.online.sdk.data.reachablerange.ReachableRangeParams;
import com.tomtom.online.sdk.data.reachablerange.ReachableRangeParamsBuilder;
import com.tomtom.online.sdk.data.reachablerange.ReachableRangeQuery;
import com.tomtom.online.sdk.data.reachablerange.ReachableRangeQueryBuilder;
import com.tomtom.online.sdk.routing.data.VehicleEngineType;
import com.tomtom.online.sdk.samples.utils.Locations;

public class ReachableRangeQueryFactory {

    public ReachableRangeQuery createReachableRangeQueryForElectric() {
        return new ReachableRangeQueryBuilder(createDefaultCommonParamsForElectric(), createDefaultReachableRangeParamsForElectric()
                .withEnergyBudgetInKWh(5));
    }

    public ReachableRangeQuery createReachableRangeQueryForElectricLimitTo2Hours() {
        return new ReachableRangeQueryBuilder(createDefaultCommonParamsForElectric(), createDefaultReachableRangeParamsForElectric()
                .withTimeBudgetInSeconds(7200));
    }

    public ReachableRangeQuery createReachableRangeQueryForCombustion() {
        //tag::doc_reachable_range_query_combustion[]
        return new ReachableRangeQueryBuilder(
                createDefaultCommonParamsForCombustion(),
                createDefaultReachableRangeParamsForCombustion());
        //end::doc_reachable_range_query_combustion[]
    }

    private CommonParams createDefaultCommonParamsForElectric() {
        SpeedToConsumptionMap consumption = new SpeedToConsumptionMap();
        consumption.addSpeedToConsumptionValue(50, 6.3);

        return new CommonParamsBuilder()
                .withVehicleWeightInKg(1600)
                .withCurrentChargeInKWh(43.0)
                .withMaxChargeInKWh(85.0)
                .withAuxiliaryPowerInKW(1.7)
                .withAccelerationEfficiency(0.33)
                .withDecelerationEfficiency(0.33)
                .withUphillEfficiency(0.33)
                .withDownhillEfficiency(0.33)
                .withConstantSpeedConsumptionInKWhPerHundredKm(consumption)
                .withVehicleEngineType(VehicleEngineType.ELECTRIC);
    }

    private ReachableRangeParams createDefaultReachableRangeParamsForElectric() {
        return new ReachableRangeParamsBuilder(Locations.AMSTERDAM_CENTER_LOCATION);
    }

    private ReachableRangeParams createDefaultReachableRangeParamsForCombustion() {
        //tag::doc_reachable_range_specific_params_combustion[]
        return new ReachableRangeParamsBuilder(Locations.AMSTERDAM_CENTER_LOCATION)
                .withFuelBudgetInLiters(5);
        //end::doc_reachable_range_specific_params_combustion[]
    }

    private CommonParams createDefaultCommonParamsForCombustion() {
        //tag::doc_reachable_range_common_params_combustion[]
        SpeedToConsumptionMap consumption = new SpeedToConsumptionMap();
        consumption.addSpeedToConsumptionValue(50, 6.3);

        return new CommonParamsBuilder()
                .withVehicleWeightInKg(1600)
                .withCurrentFuelInLiters(43.0)
                .withFuelEnergyDensityInMJoulesPerLiter(34.2)
                .withAuxiliaryPowerInLitersPerHour(1.7)
                .withAccelerationEfficiency(0.33)
                .withDecelerationEfficiency(0.33)
                .withUphillEfficiency(0.33)
                .withDownhillEfficiency(0.33)
                .withConstantSpeedConsumptionInLitersPerHundredKm(consumption)
                .withVehicleEngineType(VehicleEngineType.COMBUSTION);
        //tag::doc_reachable_range_common_params_combustion[]
    }
}
