package com.androidbasics.apoukar.sportnewsapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {

    private List<News> mValues;
    private Context mContext;

    public NewsAdapter(List<News> items, Context context) {
        mValues = items;
        mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.mItem = mValues.get(position);
        holder.mTitleView.setText(mValues.get(position).getTitle());
        holder.mDateView.setText(mValues.get(position).getPublished());
        String author = mValues.get(position).getAuthor();
        if (!author.equals("")) {
            holder.mAuthorView.setText(author);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent openBrowser = new Intent(Intent.ACTION_VIEW);
                openBrowser.setData(Uri.parse(mValues.get(position).getUrl()));
                mContext.startActivity(openBrowser);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public final View mView;
        public final TextView mTitleView;
        public final TextView mDateView;
        public final TextView mAuthorView;
        public News mItem;

        public ViewHolder(View itemView) {
            super(itemView);

            mView = itemView;
            mTitleView = (TextView) itemView.findViewById(R.id.article_title);
            mDateView = (TextView) itemView.findViewById(R.id.article_date);
            mAuthorView = (TextView) itemView.findViewById(R.id.article_author);
        }
    }
}
