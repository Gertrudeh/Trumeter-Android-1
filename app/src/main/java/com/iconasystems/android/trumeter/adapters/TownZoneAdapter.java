package com.iconasystems.android.trumeter.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.iconasystems.android.trumeter.R;
import com.iconasystems.android.trumeter.vo.TownZone;

import java.util.List;

/**
 * Created by christoandrew on 10/7/16.
 */

public class TownZoneAdapter extends RecyclerView.Adapter<TownZoneAdapter.ViewHolder> {
    private List<TownZone> townList;
    private OnItemClickListener onItemClickListener;

    public TownZoneAdapter(List<TownZone> townList, OnItemClickListener
            onItemClickListener) {
        this.townList = townList;
        this.onItemClickListener = onItemClickListener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mTownName;
        public TextView mAreaCode;
        public View itemView;
        public TextView mTownId;
        public TextView mZoneName;
        public TextView mZoneId;
        public TextView mZoneCode;

        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            mTownName = (TextView) itemView.findViewById(R.id.town_name);
            mAreaCode = (TextView) itemView.findViewById(R.id.town_area_code);
            mTownId = (TextView) itemView.findViewById(R.id.town_id);
            mZoneCode = (TextView) itemView.findViewById(R.id.zone_code);
            mZoneName = (TextView) itemView.findViewById(R.id.zone_name);
            mZoneId = (TextView) itemView.findViewById(R.id.zone_id);

        }
        public void bind(final TownZone townzone, final OnItemClickListener onItemClickListener){
            mTownName.setText(townzone.getTown_name());
            mAreaCode.setText(townzone.getArea_code());
            mZoneId.setText(String.valueOf(townzone.getZone_id()));
            mZoneCode.setText(townzone.getZone_code());
            mZoneName.setText(townzone.getZone_name());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(townzone);
                }
            });
        }
    }

    @Override
    public TownZoneAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_list_item,parent,false);
        return new TownZoneAdapter.ViewHolder(v);

    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(townList.get(position),onItemClickListener);
    }

    @Override
    public int getItemCount() {
        return townList.size();
    }

    public interface OnItemClickListener {
        void onItemClick(TownZone townZone);
    }


}
