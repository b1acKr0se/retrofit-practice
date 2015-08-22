package io.wyrmise.retrofitpractice.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.wyrmise.retrofitpractice.R;
import io.wyrmise.retrofitpractice.api.GithubApi;
import io.wyrmise.retrofitpractice.model.User;
import io.wyrmise.retrofitpractice.utils.FabAnimation;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends AppCompatActivity{

    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.recyclerView) RecyclerView recyclerView;
    @Bind(R.id.fab) FloatingActionButton fab;

    private GithubApi api;
    private ProgressDialog progressDialog;
    private FabAnimation animation;
    private String username;
    private String BASE_URL = "https://api.github.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        animation = new FabAnimation();

        setUpProgressDialog();

        setUpAdapter();


    }

    public void onFabClick(){
        animation.animateOut(fab);


    }

    private void setUpDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final EditText editText = new EditText(this);
        builder.setTitle("Enter the username");
        builder.setView(editText);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                username = editText.getText().toString();
            }
        });
    }

    private void setUpAdapter(){
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(BASE_URL).build(); //create retrofit adapter with base url as end point
        api = restAdapter.create(GithubApi.class); //create a service from interface with GET method
    }

    private void setUpProgressDialog(){
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait");
        progressDialog.setCanceledOnTouchOutside(false);
    }

    private void getUserInformation(String username) {
        progressDialog.show();
        api.getInformation(username, new Callback<User>() {
            @Override
            public void success(User user, Response response) {
                progressDialog.dismiss();
                
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
