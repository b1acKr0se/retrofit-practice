package io.wyrmise.retrofitpractice.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import butterknife.Bind;
import butterknife.ButterKnife;
import io.wyrmise.retrofitpractice.R;
import io.wyrmise.retrofitpractice.model.User;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.ViewHolder> implements View.OnClickListener {

    private ArrayList<User> list;
    private OnItemClickListener onItemClickListener;
    private Context context;

    public UserListAdapter(Context c, ArrayList<User> users) {
        context = c;
        list = users;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_card, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        User user = list.get(position);
        holder.name.setText(user.getName());
        holder.follower.setText(user.getFollowers() + " followers");
        holder.following.setText(user.getFollowing() + " following");
        System.out.println("Image: " +user.getAvatar_url());
        Picasso.with(context).load(user.getAvatar_url()).into(holder.image);
        holder.itemView.setTag(user);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onClick(final View v) {
        onItemClickListener.onItemClick(v, (User) v.getTag());
    }

    protected class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.userImage) ImageView image;
        @Bind(R.id.userName) TextView name;
        @Bind(R.id.follower) TextView follower;
        @Bind(R.id.following) TextView following;
        @Bind(R.id.view) Button view;
        @Bind(R.id.bookmark) Button bookmark;

        public ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
            view.setOnClickListener(UserListAdapter.this);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, User user);
    }
}
