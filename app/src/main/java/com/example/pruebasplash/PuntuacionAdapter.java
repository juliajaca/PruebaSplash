package com.example.pruebasplash;

import android.content.Context;
import android.graphics.HardwareRenderer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;

public class PuntuacionAdapter extends RecyclerView.Adapter<PuntuacionAdapter.ViewHolder> {
    private static final int HEADER_VIEW = 1;
    private ArrayList<PuntuacionModel> mScoresData;
    private Context mContext;

    PuntuacionAdapter(Context context, ArrayList<PuntuacionModel> sportsData) {
        this.mScoresData = sportsData;
        this.mContext = context;
    }

    @Override
    public PuntuacionAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;

        if (viewType == HEADER_VIEW) {
            v = LayoutInflater.from(mContext).inflate(R.layout.list_header, parent, false);
            HeaderViewHolder vh = new HeaderViewHolder(v);
            return vh;
        }

        v = LayoutInflater.from(mContext).inflate(R.layout.list_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(PuntuacionAdapter.ViewHolder holder, int position) {
        try {
            if (holder instanceof ViewHolder) {
                ViewHolder vh = (ViewHolder) holder;
                // Get current item -1 because of the header.
                PuntuacionModel currentSport = mScoresData.get(position - 1);
                // Populate the textviews with data.
                holder.bindTo(currentSport);
            } else if (holder instanceof HeaderViewHolder) {
                HeaderViewHolder vh = (HeaderViewHolder) holder;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        Log.d("con", "hay " + mScoresData.size());
        if (mScoresData == null) {
            return 0;
        }

        if (mScoresData.size() == 0) {
            //Return 1 here to show nothing
            return 1;
        }

        // Add extra view to show the footer view
        return mScoresData.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return HEADER_VIEW;
        }

        return super.getItemViewType(position);
    }

    public class HeaderViewHolder extends ViewHolder {
        private TextView mTitleText2;
        private TextView mInfoText2;
        private TextView mTimeText2;
        private TextView mlevelText2;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            mTitleText2 = itemView.findViewById(R.id.userHeader);
            mInfoText2 = itemView.findViewById(R.id.scoreHeader);
            mTimeText2 = itemView.findViewById(R.id.timeHeader);
            mlevelText2 = itemView.findViewById(R.id.levelHeader);
            mTitleText2.setText("USER");
            mInfoText2.setText("SCORE");
            mTimeText2.setText("TIME");
            mlevelText2.setText("LEVEL");
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        // Member Variables for the TextViews
        private TextView mTitleText;
        private TextView mInfoText;
        private TextView mTimeText;
        private TextView mLevelText;

        ViewHolder(View itemView) {
            super(itemView);
            // Initialize the views.
            mTitleText = itemView.findViewById(R.id.user);
            mInfoText = itemView.findViewById(R.id.score);
            mTimeText = itemView.findViewById(R.id.time);
            mLevelText = itemView.findViewById(R.id.level);
        }

        void bindTo(PuntuacionModel currentSport) {
            // Populate the textviews with data.
            mTitleText.setText(currentSport.getNombre());
            mInfoText.setText(String.valueOf(currentSport.getPuntos()));
            mTimeText.setText(currentSport.getTiempo());
            mLevelText.setText(currentSport.getNivel());
        }
    }
}
