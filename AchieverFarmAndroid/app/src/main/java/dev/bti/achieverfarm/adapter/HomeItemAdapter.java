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

import java.util.List;
import java.util.Locale;

import dev.bti.achieverfarm.R;
import dev.bti.achieverfarm.androidsdk.enums.ItemType;
import dev.bti.achieverfarm.androidsdk.enums.SellingType;
import dev.bti.achieverfarm.androidsdk.models.Item;
import dev.bti.achieverfarm.fragments.ItemInfoFragment;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class HomeItemAdapter extends RecyclerView.Adapter<HomeItemAdapter.ViewHolder> {

    FragmentActivity activity;
    List<Item> itemList;

    @NonNull
    @Override
    public HomeItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(activity).inflate(R.layout.format_home_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeItemAdapter.ViewHolder holder, int position) {
        Item item = itemList.get(position);
        holder.formatHomeItemProductName.setText(item.getItemInformation().getName());
        holder.formatHomeItemPrice.setText(String.format(Locale.getDefault(), "$%.2f", item.getPricingInfo().getUsdPrice()));

        Glide.with(activity)
                .load(item.getItemInformation().getPhotoUrl())
                .transform(new CenterCrop(), new RoundedCorners(20))
                .into(holder.formatHomeItemProductImage);

        holder.formatHomeItemHolder.setOnClickListener(v -> activity.getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.slide_in_bottom, R.anim.slide_out_bottom)
                .replace(R.id.mainFragContainerFull, new ItemInfoFragment(item))
                .addToBackStack(null)
                .commit()
        );

        if (item.getPricingInfo().getSellingType() != SellingType.PER_KG)
            holder.formatHomeItemPerKG.setVisibility(View.GONE);

        if (item.getType() == ItemType.DRY)
            holder.formatHomeItemType.setImageResource(R.drawable.ic_dry_produce2);
        else holder.formatHomeItemType.setImageResource(R.drawable.ic_fresh_produce);


        switch (item.getItemInformation().getAdditionalInformation().getWhereGrown()) {
            case OPEN_FIELD ->
                    holder.formatHomeItemGrowTech.setImageResource(R.drawable.ic_open_field);
            case GREEN_HOUSE ->
                    holder.formatHomeItemGrowTech.setImageResource(R.drawable.ic_green_house);
            default -> holder.formatHomeItemGrowTech.setImageResource(R.drawable.ic_shed_net);
        }
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ConstraintLayout formatHomeItemHolder;
        ImageView formatHomeItemProductImage, formatHomeItemType, formatHomeItemGrowTech;
        TextView formatHomeItemProductName, formatHomeItemPrice, formatHomeItemPerKG;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            init(itemView);
        }

        void init(View itemView) {
            initUi(itemView);
        }

        void initUi(View itemView) {
            formatHomeItemHolder = itemView.findViewById(R.id.formatHomeItemHolder);
            formatHomeItemProductImage = itemView.findViewById(R.id.formatHomeItemProductImage);
            formatHomeItemProductName = itemView.findViewById(R.id.formatHomeItemProductName);
            formatHomeItemPrice = itemView.findViewById(R.id.formatHomeItemPrice);
            formatHomeItemPerKG = itemView.findViewById(R.id.formatHomeItemPerKG);
            formatHomeItemType = itemView.findViewById(R.id.formatHomeItemType);
            formatHomeItemGrowTech = itemView.findViewById(R.id.formatHomeItemGrowTech);
        }

    }
}
