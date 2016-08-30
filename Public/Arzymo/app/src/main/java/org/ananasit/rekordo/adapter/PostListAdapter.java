package org.ananasit.rekordo.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import org.ananasit.rekordo.AppController;
import org.ananasit.rekordo.PostDetailActivity;
import org.ananasit.rekordo.R;
import org.ananasit.rekordo.model.Post;
import org.ananasit.rekordo.util.GlobalVar;
import java.util.List;

public class PostListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    private Context context;
    private List<Post> postItems;
    private boolean isListView;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();


    public PostListAdapter(Context _context, List<Post> _postItems, boolean _isListView) {
        context = _context;
        postItems = _postItems;
        isListView = _isListView;
    }
    public static class PostViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{

        public ProgressBar spin;
        public NetworkImageView thumbnail;
        public TextView  hitcount, date, location, title, price;
        private ItemClickListener clickListener;

        public PostViewHolder(View parent) {
            super(parent);
            spin = (ProgressBar) parent.findViewById(R.id.progressBar1);
            thumbnail = (NetworkImageView) parent.findViewById(R.id.thumbnail);
            title = (TextView) parent.findViewById(R.id.title);
            hitcount = (TextView) parent.findViewById(R.id.hitcount);
            date = (TextView) parent.findViewById(R.id.date);
            location = (TextView) parent.findViewById(R.id.location);
            price = (TextView) parent.findViewById(R.id.price);
            parent.setTag(parent);
            parent.setOnClickListener(this);
            parent.setOnLongClickListener(this);
        }

        public void setClickListener(ItemClickListener itemClickListener) {
            this.clickListener = itemClickListener;
        }
        @Override
        public void onClick(View view) {
            clickListener.onClick(view, getAdapterPosition(), false);
        }
        @Override
        public boolean onLongClick(View view) {
            clickListener.onClick(view, getAdapterPosition(), true);
            return true;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view;
        if(isListView)
           view = LayoutInflater.from(context).inflate(R.layout.post_list_row, parent, false);
        else view = LayoutInflater.from(context).inflate(R.layout.post_grid_row, parent, false);
        PostViewHolder vh = new PostViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();

        PostViewHolder holder = (PostViewHolder) viewHolder;
        Post post = postItems.get(position);
        holder.title.setText(post.getTitle());
        holder.hitcount.setText(post.getHitcount());
        holder.price.setText(post.getPrice());
        holder.location.setText(post.getLocation());
        holder.date.setText(post.getDate_created());

        holder.spin.setVisibility(View.VISIBLE);
        String image_url = post.getThumbnailUrl();
        // thumbnail image
        if(image_url.equals(""))
            holder.thumbnail.setDefaultImageResId(R.drawable.default_img);
        holder.thumbnail.setImageUrl(image_url, imageLoader);
        if(holder.thumbnail.getDrawable() != null)
            holder.spin.setVisibility(View.GONE);

        holder.setClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                if (isLongClick) {
                    //Toast.makeText(context, "#" + position + " - " + " (Long click)", Toast.LENGTH_SHORT).show();
                } else {
                    GlobalVar._Post = postItems.get(position);
                    Intent i = new Intent(context, PostDetailActivity.class);
                    context.startActivity(i);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return postItems == null ? 0 : postItems.size();
    }

    public interface ItemClickListener {
        void onClick(View view, int position, boolean isLongClick);
    }
}
