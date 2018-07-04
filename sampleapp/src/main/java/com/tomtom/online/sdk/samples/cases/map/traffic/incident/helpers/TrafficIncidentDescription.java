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
package com.tomtom.online.sdk.samples.cases.map.traffic.incident.helpers;

import android.support.annotation.DrawableRes;

import com.tomtom.online.sdk.samples.R;
import com.tomtom.online.sdk.traffic.incidents.response.IncidentIconCategory;

public enum TrafficIncidentDescription {

    UNKNOWN(IncidentIconCategory.UNKNOWN, R.drawable.ic_map_traffic_incident),
    ACCIDENT(IncidentIconCategory.ACCIDENT, R.drawable.ic_map_traffic_accident),
    FOG(IncidentIconCategory.FOG, R.drawable.ic_map_traffic_fog),
    DANGEROUS_CONDITIONS(IncidentIconCategory.DANGEROUS_CONDITIONS, R.drawable.ic_map_traffic_dangerous_conditions),
    RAIN(IncidentIconCategory.RAIN, R.drawable.ic_map_traffic_rain),
    ICE(IncidentIconCategory.ICE, R.drawable.ic_map_traffic_ice),
    JAM(IncidentIconCategory.JAM, R.drawable.ic_map_traffic_jam),
    LANE_CLOSED(IncidentIconCategory.LANE_CLOSED, R.drawable.ic_map_traffic_lane_closed),
    ROAD_CLOSED(IncidentIconCategory.ROAD_CLOSED, R.drawable.ic_map_traffic_road_closed),
    ROAD_WORKS(IncidentIconCategory.ROAD_WORKS, R.drawable.ic_map_traffic_road_work),
    WIND(IncidentIconCategory.WIND, R.drawable.ic_map_traffic_wind),
    FLOODING(IncidentIconCategory.FLOODING, R.drawable.ic_map_traffic_sliproad),
    DETOUR(IncidentIconCategory.DETOUR, R.drawable.ic_map_traffic_incident),
    CLUSTER(IncidentIconCategory.CLUSTER, R.drawable.ic_map_traffic_cluster);

    private final IncidentIconCategory category;
    private final int iconDrawable;

    TrafficIncidentDescription(IncidentIconCategory category, @DrawableRes int iconDrawable) {
        this.category = category;
        this.iconDrawable = iconDrawable;
    }

    public static int getIconDrawableByCategory(IncidentIconCategory category) {
        return TrafficIncidentDescription.valueOf(category.name()).iconDrawable;
    }
}
