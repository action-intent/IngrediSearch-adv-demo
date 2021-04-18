package com.demo.ingredisearch.features;

import com.demo.ingredisearch.repository.RecipeRepository;
import com.demo.ingredisearch.repository.sources.favorites.FakeFavoritesSource;
import com.demo.ingredisearch.repository.sources.remote.FakeRemoteDataSource;
import com.demo.ingredisearch.util.SingleExecutors;

import org.junit.After;
import org.junit.Before;

public class BaseUnitTest {
    protected FakeFavoritesSource mFavoritesSource;
    protected FakeRemoteDataSource mRemoteDataSource;
    protected RecipeRepository mRecipeRepository;

    @Before
    public void init() {
        mRemoteDataSource = new FakeRemoteDataSource(new SingleExecutors());
        mFavoritesSource = new FakeFavoritesSource();
        mRecipeRepository = RecipeRepository.getInstance(mRemoteDataSource, mFavoritesSource);
    }

    @After
    public void tearDown() {
        mRecipeRepository.destroy();
    }
}
