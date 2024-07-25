package com.sum.practice22_1;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class BookListAdapter extends RecyclerView.Adapter<BookListAdapter.ViewHolder> implements OnBookItemClickListener, OnBookItemLongClickListener{
    ArrayList<Book> items = new ArrayList<>();
    OnBookItemClickListener listener;
    OnBookItemLongClickListener longClickListener;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.book_card, parent, false);
        return new ViewHolder(itemView, this, this);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Book item = items.get(position);
        holder.setItem(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public void onItemClick(ViewHolder holder, View view, int position) {
        listener.onItemClick(holder, view, position);
    }

    @Override
    public void onItemLongClick(ViewHolder holder, View view, int position, int itemID) {
        longClickListener.onItemLongClick(holder, view, position, itemID);
    }


    public void addItem(Book item){
        items.add(item);
        notifyItemInserted(items.size()-1); //마지막 인덱스에 추가됨
    }

    public Book getItem(int position){
        return items.get(position);
    }

    public void setItems(ArrayList<Book> items){
        this.items = items;
        notifyDataSetChanged();
    }

    public void setItem(int position, Book item){
        items.set(position, item);
        notifyItemChanged(position);
    }

    public void setOnItemClickListener(OnBookItemClickListener listener){
        this.listener = listener;
    }
    public void setOnItemLongClickListener(OnBookItemLongClickListener longClickListener){
        this.longClickListener = longClickListener;
    }



    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView ivIcon;
        TextView tvTitle, tvAuthor;
        Book item;

        public ViewHolder(@NonNull View itemView, OnBookItemClickListener listener, OnBookItemLongClickListener longClickListener) {
            super(itemView);
            ivIcon = itemView.findViewById(R.id.ivIcon);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvAuthor = itemView.findViewById(R.id.tvAuthor);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) { //클릭을 받은 뷰 객체를 넘겨준다
                    listener.onItemClick(ViewHolder.this, v, getAdapterPosition());
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    longClickListener.onItemLongClick(ViewHolder.this, v, getAdapterPosition(), item.get_id());
                    return true;
                }
            });
        }

        public void setItem(Book item){
            if(item.isPaper()){
                ivIcon.setImageResource(R.drawable.ic_parchment);
            }else{
                ivIcon.setImageResource(R.drawable.ic_book);
            }
            tvTitle.setText(item.getTitle());
            tvAuthor.setText(item.getAuthor());
            this.item = item;
        }
    }
}
