package com.demo.ingredisearch.util;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

public class ViewHelper {
    private LinearLayout mLoadingContainer;
    private LinearLayout mErrorContainer;
    private LinearLayout mNoResultsContainer;
    private RecyclerView mRecyclerView;
    private ScrollView mScrollView;

    public ViewHelper(View view,
                      LinearLayout loadingContainer,
                      LinearLayout errorContainer,
                      LinearLayout noResultsContainer) {
        if (view instanceof RecyclerView) {
            mRecyclerView = (RecyclerView) view;
        } else {
            mScrollView = (ScrollView) view;
        }
        mLoadingContainer = loadingContainer;
        mErrorContainer = errorContainer;
        mNoResultsContainer = noResultsContainer;
    }

    public void hideOthers() {
        if (mRecyclerView != null) {
            mRecyclerView.setVisibility(View.VISIBLE);
        } else {
            mScrollView.setVisibility(View.VISIBLE);
        }
        mLoadingContainer.setVisibility(View.GONE);
        mErrorContainer.setVisibility(View.GONE);
        mNoResultsContainer.setVisibility(View.GONE);
    }

    public void showError() {
        if (mRecyclerView != null) {
            mRecyclerView.setVisibility(View.GONE);
        } else {
            mScrollView.setVisibility(View.GONE);
        }
        mLoadingContainer.setVisibility(View.GONE);
        mErrorContainer.setVisibility(View.VISIBLE);
        mNoResultsContainer.setVisibility(View.GONE);
    }

    public void showLoading() {
        if (mRecyclerView != null) {
            mRecyclerView.setVisibility(View.GONE);
        } else {
            mScrollView.setVisibility(View.GONE);
        }
        mLoadingContainer.setVisibility(View.VISIBLE);
        mErrorContainer.setVisibility(View.GONE);
        mNoResultsContainer.setVisibility(View.GONE);
    }

    public void showNoResults() {
        if (mRecyclerView != null) {
            mRecyclerView.setVisibility(View.GONE);
        } else {
            mScrollView.setVisibility(View.GONE);
        }
        mLoadingContainer.setVisibility(View.GONE);
        mErrorContainer.setVisibility(View.GONE);
        mNoResultsContainer.setVisibility(View.VISIBLE);
    }

    public static void hideKeyboard(Fragment context) {
        View view = context.requireActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm =
                    (InputMethodManager) context.requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null)
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static void showSubtitle(Fragment context, String subtitle) {
        if (context.requireActivity() instanceof AppCompatActivity) {
            ActionBar supportActionBar = ((AppCompatActivity) context.requireActivity()).getSupportActionBar();
            if (supportActionBar != null)
                supportActionBar.setSubtitle(subtitle);
        }
    }
}
