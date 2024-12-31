package dev.bti.achieverfarm.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import dev.bti.achieverfarm.R;
import dev.bti.achieverfarm.androidsdk.common.Triple;
import dev.bti.achieverfarm.androidsdk.models.Item;
import lombok.Setter;


public class HomeListAdapter extends RecyclerView.Adapter<HomeListAdapter.ViewHolder> {

    List<Triple<String, List<Item>, HomeItemAdapter>> lists = new ArrayList<>();
    FragmentActivity activity;

    @Setter
    boolean reset = false;

    public HomeListAdapter(FragmentActivity activity) {
        this.activity = activity;
    }

    @NonNull
    @Override
    public HomeListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(activity).inflate(R.layout.format_home_list, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeListAdapter.ViewHolder holder, int position) {
        Triple<String, List<Item>, HomeItemAdapter> current = lists.get(position);

        holder.listHomeName.setText(current.getOne());

        holder.listHomeSellAll.setOnClickListener(v -> {

        });

        holder.homeListRecycler.setAdapter(current.getThree());

    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    public void addNewSection(String name, List<Item> itemList) {
        if (reset) {
            lists.forEach((triple) -> {
                int itemCount = triple.getThree().getItemCount();
                triple.getThree().itemList.clear();
                triple.getThree().notifyItemRangeRemoved(0, itemCount);
            });

            lists.clear();
            notifyItemRangeRemoved(0, getItemCount());
            setReset(false);
        }

        lists.add(Triple.of(name, itemList, new HomeItemAdapter(activity, itemList)));
        notifyItemInserted(lists.size() - 1);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView listHomeName, listHomeSellAll;
        RecyclerView homeListRecycler;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            init(itemView);
        }

        void init(View itemView) {
            initUi(itemView);
        }

        void initUi(View itemView) {
            listHomeName = itemView.findViewById(R.id.listHomeName);
            listHomeSellAll = itemView.findViewById(R.id.listHomeSellAll);
            homeListRecycler = itemView.findViewById(R.id.homeListRecycler);
        }
    }
}
