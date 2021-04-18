package com.demo.ingredisearch.repository.sources.remote;

import com.demo.ingredisearch.models.Recipe;
import com.demo.ingredisearch.repository.sources.RemoteDataSource;
import com.demo.ingredisearch.repository.sources.ResponseCallback;
import com.demo.ingredisearch.util.AppExecutors;
import com.demo.ingredisearch.util.Resource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class FakeRemoteDataSource implements RemoteDataSource {
    public static long FAKE_NETWORK_DELAY = 0L;

    public enum DataStatus {Success, NetworkError, HTTPError, AuthError}

    private final Map<String, Recipe> mRecipes = new LinkedHashMap<>();

    private DataStatus mDataStatus;

    private AppExecutors mAppExecutors;

    public FakeRemoteDataSource(AppExecutors appExecutors) {
        this.mAppExecutors = appExecutors;
        this.mDataStatus = DataStatus.Success;
    }

    @Override
    public void searchRecipes(String query, ResponseCallback<List<Recipe>> callback) {
        mAppExecutors.networkIO().execute(() -> {
            try {
                Thread.sleep(FAKE_NETWORK_DELAY);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            switch (mDataStatus) {
                case Success:
                    callback.onDataAvailable(Resource.success(new ArrayList<>(mRecipes.values())));
                    break;
                case NetworkError:
                    callback.onError(Resource.error("Network Error", null));
                    break;
                case HTTPError:
                    callback.onError(Resource.error("HTTP Error", null));
                    break;
                case AuthError:
                    callback.onError(Resource.error("401 Unauthorized. Token may be invalid", null));
                    break;
                default:
                    throw new IllegalStateException();
            }
        });

    }

    @Override
    public void searchRecipe(String recipeId, ResponseCallback<Recipe> callback) {
        mAppExecutors.networkIO().execute(() -> {
            try {
                Thread.sleep(FAKE_NETWORK_DELAY);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            switch (mDataStatus) {
                case Success:
                    callback.onDataAvailable(Resource.success(mRecipes.get(recipeId)));
                    break;
                case NetworkError:
                    callback.onError(Resource.error("Network Error", null));
                    break;
                case HTTPError:
                    callback.onError(Resource.error("HTTP Error", null));
                    break;
                case AuthError:
                    callback.onError(Resource.error("401 Unauthorized. Token may be invalid", null));
                    break;
                default:
                    throw new IllegalStateException();
            }
        });
    }

    public void setDataStatus(DataStatus dataStatus) {
        this.mDataStatus = dataStatus;
    }

    public void addRecipes(List<Recipe> recipes) {
        recipes.forEach(recipe -> mRecipes.put(recipe.getRecipeId(), new Recipe(recipe)));
    }

    public void addRecipes(Recipe...recipes) {
        addRecipes(Arrays.asList(recipes));
    }

}
