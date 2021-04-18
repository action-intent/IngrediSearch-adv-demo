package com.demo.ingredisearch.features.search;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.demo.ingredisearch.util.Event;

public class SearchViewModel extends ViewModel {

    public void search(String query) {
        if (query == null || query.isEmpty()) {
            mIsEmpty.setValue(new Event<>(new Object()));
        } else {
            mNavToSearchResults.setValue(new Event<>(query));
        }
    }

    private final MutableLiveData<Event<String>> mNavToSearchResults = new MutableLiveData<>();

    public LiveData<Event<String>> navToSearchResults() {
        return mNavToSearchResults;
    }

    private final MutableLiveData<Event<Object>> mIsEmpty = new MutableLiveData<>();

    public LiveData<Event<Object>> isEmpty() {
        return mIsEmpty;
    }
}
