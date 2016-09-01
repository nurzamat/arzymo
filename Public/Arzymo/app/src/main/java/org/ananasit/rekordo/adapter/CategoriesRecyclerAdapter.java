package org.ananasit.rekordo.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import org.ananasit.rekordo.PostsActivity;
import org.ananasit.rekordo.R;
import org.ananasit.rekordo.SubcatsActivity;
import org.ananasit.rekordo.model.Category;
import org.ananasit.rekordo.util.Constants;
import org.ananasit.rekordo.util.GlobalVar;
import java.util.List;

/**
 * Created by nurzamat on 8/12/15.
 */
public class CategoriesRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Category> mItemList;
    private Context context;


    public CategoriesRecyclerAdapter(Context _context, List<Category> itemList) {
        mItemList = itemList;
        context = _context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_row, parent, false);
        CategoryViewHolder vh = new CategoryViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

        CategoryViewHolder holder = (CategoryViewHolder) viewHolder;
        Category category = mItemList.get(position);
        holder.title.setText(category.getName());
        if(position == 0)
        holder.icon.setImageResource(R.drawable.ic_swap_vertical);//1
        if(position == 1)
            holder.icon.setImageResource(R.drawable.ic_nature_people);  //2
        if(position == 2)
            holder.icon.setImageResource(R.drawable.ic_home_outline); //3
        if(position == 3)
            holder.icon.setImageResource(R.drawable.ic_calendar_clock); //4
        if(position == 4)
            holder.icon.setImageResource(R.drawable.ic_tennis);   //5
        if(position == 5)
            holder.icon.setImageResource(R.drawable.ic_counter);   //6
        if(position == 6)
            holder.icon.setImageResource(R.drawable.ic_currency_usd); //7
        if(position == 7)
            holder.icon.setImageResource(R.drawable.ic_human_male_female);

        holder.setClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                if (isLongClick) {
                    //Toast.makeText(context, "#" + position + " - " + " (Long click)", Toast.LENGTH_SHORT).show();
                } else {
                    //Toast.makeText(context, "#" + position + " - ", Toast.LENGTH_SHORT).show();
                    ClickWork(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mItemList == null ? 0 : mItemList.size();
    }

    public interface ItemClickListener {
        void onClick(View view, int position, boolean isLongClick);
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{

        public TextView title;
        public ImageView icon;
        private ItemClickListener clickListener;

        public CategoryViewHolder(View parent) {
            super(parent);
            title = (TextView) parent.findViewById(R.id.title);
            icon = (ImageView) parent.findViewById(R.id.left_icon);
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

    public void ClickWork(int position)
    {
        GlobalVar.Category = GlobalVar._categories.get(position);
        Intent in;
        if (GlobalVar.Category.getSubcats() != null && GlobalVar.Category.getSubcats().size() > 0)
        {
            in = new Intent(context, SubcatsActivity.class);
            in.putExtra("mode", Constants.POSTS_MODE);
        }
        else
        {
            in = new Intent(context, PostsActivity.class);
        }
        context.startActivity(in);
    }
}
