package io.wyrmise.retrofitpractice.api;


import io.wyrmise.retrofitpractice.model.User;
import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Created by Thanh on 8/22/2015.
 */
public interface GithubApi {
    //base url https://api.github.com/users/username
    @GET("/users/{user}") //convert the url to a format that retrofit can understand
    void getInformation(@Path("user") String username, Callback<User> response);
    //username is from the EditText/dialog input by user
    //response is returned from the server and converted to the User pojo
}
