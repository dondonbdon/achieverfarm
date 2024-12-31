package dev.bti.achieverfarm.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import dev.bti.achieverfarm.R;
import dev.bti.achieverfarm.androidsdk.enums.ItemType;
import dev.bti.achieverfarm.androidsdk.enums.SellingType;
import dev.bti.achieverfarm.androidsdk.models.Item;
import dev.bti.achieverfarm.fragments.ItemInfoFragment;


public class SearchItemAdapter extends RecyclerView.Adapter<SearchItemAdapter.ViewHolder> {

    FragmentActivity activity;
    List<Item> items = new ArrayList<>();

    public SearchItemAdapter(FragmentActivity activity) {
        this.activity = activity;
    }

    @NonNull
    @Override
    public SearchItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(activity).inflate(R.layout.format_search_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchItemAdapter.ViewHolder holder, int position) {
        Item item = items.get(position);
        holder.formatSearchItemProductName.setText(item.getItemInformation().getName());
        holder.formatSearchItemPrice.setText(String.format(Locale.getDefault(), "$%.2f", item.getPricingInfo().getUsdPrice()));

        Glide.with(activity)
                .load(item.getItemInformation().getPhotoUrl())
                .transform(new CenterCrop(), new RoundedCorners(20))
                .into(holder.formatSearchItemProductImage);

        holder.formatSearchItemHolder.setOnClickListener(v -> activity.getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.slide_in_bottom, R.anim.slide_out_bottom)
                .replace(R.id.mainFragContainerFull, new ItemInfoFragment(item))
                .addToBackStack(null)
                .commit());

        if (item.getPricingInfo().getSellingType() != SellingType.PER_KG)
            holder.formatSearchItemPerKG.setVisibility(View.GONE);

        if (item.getType() == ItemType.DRY)
            holder.formatSearchItemType.setImageResource(R.drawable.ic_dry_produce2);
        else holder.formatSearchItemType.setImageResource(R.drawable.ic_fresh_produce);

        switch (item.getItemInformation().getAdditionalInformation().getWhereGrown()) {
            case OPEN_FIELD ->
                    holder.formatSearchItemGrowTech.setImageResource(R.drawable.ic_open_field);
            case GREEN_HOUSE ->
                    holder.formatSearchItemGrowTech.setImageResource(R.drawable.ic_green_house);
            default -> holder.formatSearchItemGrowTech.setImageResource(R.drawable.ic_shed_net);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItems(List<Item> newItems) {

        notifyItemRangeRemoved(0, items.size());
        items.clear();
        items.addAll(newItems);
        notifyItemRangeInserted(0, newItems.size());
    }

    public String getLastItemId() {
        return items.get(items.size() - 1).getId();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ConstraintLayout formatSearchItemHolder;
        ImageView formatSearchItemProductImage, formatSearchItemType, formatSearchItemGrowTech;
        TextView formatSearchItemProductName, formatSearchItemPrice, formatSearchItemPerKG;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            init(itemView);
        }

        void init(View itemView) {
            initUi(itemView);
        }

        void initUi(View itemView) {
            formatSearchItemHolder = itemView.findViewById(R.id.formatSearchItemHolder);
            formatSearchItemProductImage = itemView.findViewById(R.id.formatSearchItemProductImage);
            formatSearchItemProductName = itemView.findViewById(R.id.formatSearchItemProductName);
            formatSearchItemPrice = itemView.findViewById(R.id.formatSearchItemPrice);
            formatSearchItemPerKG = itemView.findViewById(R.id.formatSearchItemPerKG);
            formatSearchItemType = itemView.findViewById(R.id.formatSearchItemType);
            formatSearchItemGrowTech = itemView.findViewById(R.id.formatSearchItemGrowTech);
        }
    }
}
