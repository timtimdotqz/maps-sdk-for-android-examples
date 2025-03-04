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

import androidx.annotation.NonNull;

import com.tomtom.online.sdk.common.rx.RxContext;
import com.tomtom.online.sdk.search.data.fuzzy.FuzzySearchQuery;
import com.tomtom.online.sdk.search.data.fuzzy.FuzzySearchResponse;
import com.tomtom.online.sdk.search.extensions.SearchService;
import com.tomtom.online.sdk.search.extensions.SearchServiceConnectionCallback;

import java.util.concurrent.Executors;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class SearchFragmentService implements RxContext, SearchServiceConnectionCallback {

    private static final int NETWORK_THREADS_NUMBER = 4;

    private SearchService searchService;

    SearchFragmentService() {
    }

    public void performSearch(FuzzySearchQuery query, DisposableSingleObserver<FuzzySearchResponse> observer) {
        //tag::doc_perform_search[]
        searchService.search(query)
                .subscribeOn(getWorkingScheduler())
                .observeOn(getResultScheduler())
                .subscribe(observer);
        //end::doc_perform_search[]
    }

    void cancelSearchIfRunning() {
        //tag::doc_cancel_search[]
        searchService.cancelSearchIfRunning();
        //end::doc_cancel_search[]
    }

    @NonNull
    @Override
    public Scheduler getWorkingScheduler() {
        return Schedulers.from(Executors.newFixedThreadPool(NETWORK_THREADS_NUMBER));
    }

    @NonNull
    @Override
    public Scheduler getResultScheduler() {
        return AndroidSchedulers.mainThread();
    }

    //tag::doc_search_service_connection_callback[]
    @Override
    public void onBindSearchService(SearchService service) {
        searchService = service;
    }
    //end::doc_search_service_connection_callback[]

    SearchService getSearchService() {
        return searchService;
    }

}
