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
package com.tomtom.online.sdk.samples.cases.search.adp;

import com.tomtom.online.sdk.common.geojson.Feature;
import com.tomtom.online.sdk.common.geojson.FeatureCollection;
import com.tomtom.online.sdk.common.geojson.GeoJsonObjectVisitor;
import com.tomtom.online.sdk.common.geojson.geometry.GeometryCollection;
import com.tomtom.online.sdk.common.geojson.geometry.LineString;
import com.tomtom.online.sdk.common.geojson.geometry.MultiLineString;
import com.tomtom.online.sdk.common.geojson.geometry.MultiPoint;
import com.tomtom.online.sdk.common.geojson.geometry.MultiPolygon;
import com.tomtom.online.sdk.common.geojson.geometry.Point;
import com.tomtom.online.sdk.common.geojson.geometry.Polygon;

import timber.log.Timber;

//Empty GeoJsonObjectVisitor so that user does not have to
//override methods that are not used.
public class GeoJsonObjectVisitorAdapter implements GeoJsonObjectVisitor {

    @Override
    public void visit(Feature feature) {
        Timber.d("empty geojson object visitor: feature");
    }

    @Override
    public void visit(FeatureCollection featureCollection) {
        Timber.d("empty geojson object visitor: featureCollection");
    }

    @Override
    public void visit(GeometryCollection geometryCollection) {
        Timber.d("empty geojson object visitor: geometryCollection");
    }

    @Override
    public void visit(LineString lineString) {
        Timber.d("empty geojson object visitor: lineString");
    }

    @Override
    public void visit(MultiLineString multiLineString) {
        Timber.d("empty geojson object visitor: multiLineString");
    }

    @Override
    public void visit(MultiPoint multiPoint) {
        Timber.d("empty geojson object visitor: multiPoint");
    }

    @Override
    public void visit(MultiPolygon multiPolygon) {
        Timber.d("empty geojson object visitor: multiPolygon");
    }

    @Override
    public void visit(Point point) {
        Timber.d("empty geojson object visitor: point");
    }

    @Override
    public void visit(Polygon polygon) {
        Timber.d("empty geojson object visitor: polygon");
    }
}
