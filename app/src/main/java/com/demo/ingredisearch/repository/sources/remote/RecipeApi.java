package com.demo.ingredisearch.repository.sources.remote;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RecipeApi {

    // https://recipesapi.herokuapp.com/api/search?key=""&q=eggs
    @GET("api/search")
    Call<RecipeSearchResponse> search(
            @Query("key") String key,
            @Query("q") String query
    );

    // https://recipesapi.herokuapp.com/api/get?key=&rId=1af01c
    @GET("api/get")
    Call<RecipeResponse> getRecipe(
            @Query("key") String key,
            @Query("rId") String recipe_id
    );
}
