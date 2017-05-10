package com.iconasystems.android.trumeter.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.iconasystems.android.trumeter.R;
import com.iconasystems.android.trumeter.vo.Route;

import java.util.List;

/**
 * Created by christoandrew on 10/7/16.
 */

public class RoutesAdapter extends RecyclerView.Adapter<RoutesAdapter.ViewHolder> {
    private List<Route> routesList;
    private Context _context;
    private OnItemClickListener onItemClickListener;

    public RoutesAdapter(List<Route> zoneList, Context _context, OnItemClickListener onItemClickListener) {
        this.routesList = zoneList;
        this._context = _context;
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public RoutesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.route_list_item,parent,false);
        return new RoutesAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RoutesAdapter.ViewHolder holder, int position) {
        holder.bind(routesList.get(position),onItemClickListener);
    }

    @Override
    public int getItemCount() {
        return routesList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mZoneName;
        public TextView mZoneId;
        public View itemView;

        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            mZoneName = (TextView) itemView.findViewById(R.id.route_name);
            mZoneId = (TextView) itemView.findViewById(R.id.route_id);
        }
        public void bind(final Route route, final OnItemClickListener onItemClickListener){
            mZoneName.setText(route.getName());
            mZoneId.setText(String.valueOf(route.getId()));


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(route);
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Route zone);
    }
}
