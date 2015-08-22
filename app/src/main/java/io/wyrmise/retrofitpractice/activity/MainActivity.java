package io.wyrmise.retrofitpractice.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.wyrmise.retrofitpractice.R;
import io.wyrmise.retrofitpractice.adapter.UserListAdapter;
import io.wyrmise.retrofitpractice.api.GithubApi;
import io.wyrmise.retrofitpractice.model.User;
import io.wyrmise.retrofitpractice.utils.FabAnimation;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends AppCompatActivity implements UserListAdapter.OnItemClickListener{

    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.recyclerView) RecyclerView recyclerView;
    @Bind(R.id.fab) FloatingActionButton fab;
    @OnClick(R.id.fab)
    public void onClick() {
        onFabClick();
    }

    private ArrayList<User> userList;
    private UserListAdapter adapter;
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

        animation.animateIn(fab);

        setUpProgressDialog();

        setUpAdapter();

        userList = new ArrayList<>();
    }

    private void setUpRecyclerView() {
        Log.d(getClass().getSimpleName(),""+userList.size());
        if(userList.size()>0) {
            adapter = new UserListAdapter(MainActivity.this,userList);
            adapter.setOnItemClickListener(MainActivity.this);
            recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
            recyclerView.setAdapter(adapter);
            recyclerView.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.GONE);
        }
    }

    public void onFabClick(){
        animation.animateOut(fab);
        setUpDialog();
    }

    private void setUpDialog(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        final EditText editText = new EditText(MainActivity.this);
        builder.setTitle("Enter the username");
        builder.setView(editText);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (editText.getText()!=null) {
                    username = editText.getText().toString();
                    getUserInformation(username);
                    dialogInterface.dismiss();
                } else {
                    Toast.makeText(MainActivity.this, "You must enter an username!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                animation.animateIn(fab);
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void setUpAdapter(){
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(BASE_URL).build(); //create retrofit adapter with base url as end point
        api = restAdapter.create(GithubApi.class); //create a service from interface with GET method
    }

    private void setUpProgressDialog(){
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCanceledOnTouchOutside(false);
    }

    private void getUserInformation(String username) {
        progressDialog.show();
        api.getInformation(username, new Callback<User>() {
            @Override
            public void success(User user, Response response) {
                progressDialog.dismiss();
                userList.add(user);
                System.out.println(user.getCompany());
                setUpRecyclerView();
            }

            @Override
            public void failure(RetrofitError error) {
                progressDialog.dismiss();
                Toast.makeText(MainActivity.this, "Failed to retrieve user information", Toast.LENGTH_SHORT).show();
            }
        });
        animation.animateIn(fab);
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

    @Override
    public void onItemClick(View view, User user) {
        Toast.makeText(this,user.getName(),Toast.LENGTH_SHORT).show();
    }
}
