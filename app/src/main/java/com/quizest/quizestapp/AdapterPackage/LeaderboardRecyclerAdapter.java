package com.quizest.quizestapp.AdapterPackage;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.quizest.quizestapp.ModelPackage.LeaderBoard;
import com.quizest.quizestapp.R;
import com.quizest.quizestapp.UtilPackge.GlideApp;

import java.util.List;


public class LeaderboardRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    //    global field instances
    private List<LeaderBoard.LeaderListItem> leaderBoardList;
    private Activity activity;

    private int HEADER = 1;
    private int REGULAR = 2;

    /*this is the constructor for the leaderboard */
    public LeaderboardRecyclerAdapter(List<LeaderBoard.LeaderListItem> leaderBoardList, Activity activity) {
        this.leaderBoardList = leaderBoardList;
        leaderBoardList.add(0, null);
        this.activity = activity;
    }


    /*this is the function where every row layout inflate for the recycler view */

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == HEADER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.leader_board_header, parent, false);
            return new LeaderBoardHeader(view);
        } else if (viewType == REGULAR) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_leaderboard_recycler_row, parent, false);
            return new LeaderBoardHolder(view);

        }

        return null;
    }


    /*this is the place for data binding for recycler view */
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof LeaderBoardHolder) {
            LeaderBoardHolder leaderBoardHolder = (LeaderBoardHolder) holder;
            GlideApp.with(activity).load(leaderBoardList.get(position).getPhoto()).placeholder(R.drawable.avater).into(leaderBoardHolder.imgPersonImage);
            leaderBoardHolder.tvPersonName.setText(leaderBoardList.get(position).getName());
            leaderBoardHolder.tvPersonRank.setText(String.valueOf(leaderBoardList.get(position).getRanking()));
            leaderBoardHolder.tvPersonRank.setTextColor(activity.getResources().getColor(R.color.color_blue));
            leaderBoardHolder.tvPersonPoing.setText(leaderBoardList.get(position).getScore());
        }


    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return HEADER;
        } else {
            return REGULAR;
        }
    }

    @Override
    public int getItemCount() {
        return leaderBoardList.size();
    }

    /*this is the custom view holder class of leader board*/
    class LeaderBoardHolder extends RecyclerView.ViewHolder {
        ImageView imgPersonImage;
        TextView tvPersonName, tvPersonPoing, tvPersonRank;

        public LeaderBoardHolder(View itemView) {
            super(itemView);
            imgPersonImage = itemView.findViewById(R.id.img_leaderboard_person);
            tvPersonName = itemView.findViewById(R.id.tv_leaderboard_name);
            tvPersonPoing = itemView.findViewById(R.id.tv_leaderboard_point);
            tvPersonRank = itemView.findViewById(R.id.tv_leaderboard_rank);
        }
    }

    class LeaderBoardHeader extends RecyclerView.ViewHolder {

        public LeaderBoardHeader(View itemView) {
            super(itemView);
        }
    }

}
