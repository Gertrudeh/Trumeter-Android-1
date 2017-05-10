package com.iconasystems.android.trumeter.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.iconasystems.android.trumeter.R;
import com.iconasystems.android.trumeter.vo.Zone;

import java.util.List;

/**
 * Created by christoandrew on 10/7/16.
 */

public class ZonesAdapter extends RecyclerView.Adapter<ZonesAdapter.ViewHolder> {
    private List<Zone> zoneList;
    private Context _context;
    private OnItemClickListener onItemClickListener;

    public ZonesAdapter(List<Zone> zoneList, Context _context, OnItemClickListener onItemClickListener) {
        this.zoneList = zoneList;
        this._context = _context;
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public ZonesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.zone_list_item,parent,false);
        return new ZonesAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ZonesAdapter.ViewHolder holder, int position) {
        holder.bind(zoneList.get(position),onItemClickListener);
    }

    @Override
    public int getItemCount() {
        return zoneList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mZoneName;
        public TextView mZoneId;
        public View itemView;

        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            mZoneName = (TextView) itemView.findViewById(R.id.zone_name);
            mZoneId = (TextView) itemView.findViewById(R.id.zone_id);
        }
        public void bind(final Zone zone, final OnItemClickListener onItemClickListener){
            mZoneName.setText(zone.getName());
            mZoneId.setText(zone.getZoneId());


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(zone);
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Zone zone);
    }
}
