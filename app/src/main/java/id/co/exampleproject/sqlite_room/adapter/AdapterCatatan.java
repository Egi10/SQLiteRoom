package id.co.exampleproject.sqlite_room.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import id.co.exampleproject.sqlite_room.R;
import id.co.exampleproject.sqlite_room.db.entity.Catatan;

public class AdapterCatatan extends RecyclerView.Adapter<AdapterCatatan.ViewHolder> {
    private List<Catatan> listCatatan;
    private OnItemClickListener listener;

    public AdapterCatatan(List<Catatan> listCatatan, OnItemClickListener listener) {
        this.listCatatan = listCatatan;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_list_catatan, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Catatan catatan = listCatatan.get(position);

        holder.catatan.setText(catatan.catatan);
        holder.tanggal.setText(catatan.tanggal);

        holder.onClick(catatan, listener);
    }

    @Override
    public int getItemCount() {
        return listCatatan.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView catatan, tanggal;

        private ViewHolder(@NonNull View itemView) {
            super(itemView);

            catatan = itemView.findViewById(R.id.catatan);
            tanggal = itemView.findViewById(R.id.tanggal);
        }

        public void onClick(final Catatan catatan, final OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onClick(catatan);
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onClick(Catatan catatan);
    }
}
