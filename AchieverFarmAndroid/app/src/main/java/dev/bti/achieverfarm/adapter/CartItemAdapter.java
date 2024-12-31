package dev.bti.achieverfarm.adapter;

import static dev.bti.achieverfarm.common.Common.applyMultipleColorSpans;

import android.util.Log;
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

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import dev.bti.achieverfarm.R;
import dev.bti.achieverfarm.androidsdk.common.Pair;
import dev.bti.achieverfarm.androidsdk.enums.ItemType;
import dev.bti.achieverfarm.androidsdk.enums.SellingType;
import dev.bti.achieverfarm.androidsdk.models.Cart;
import dev.bti.achieverfarm.androidsdk.models.Item;
import dev.bti.achieverfarm.fragments.ItemInfoFragment;
import lombok.Getter;

public class CartItemAdapter extends RecyclerView.Adapter<CartItemAdapter.ViewHolder> {

    public static final int MAX = -1;
    @Getter
    List<Pair<Item, Integer>> cart = new ArrayList<>();
    FragmentActivity activity;
    Integer limit = 0;

    public CartItemAdapter(FragmentActivity activity) {
        this.activity = activity;
    }

    @NonNull
    @Override
    public CartItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(activity).inflate(R.layout.format_cart_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CartItemAdapter.ViewHolder holder, int position) {

        Item item = cart.get(position).getOne();
        int quantity = cart.get(position).getTwo();

        Glide.with(activity)
                .load(item.getItemInformation().getPhotoUrl())
                .transform(new CenterCrop(), new RoundedCorners(20))
                .into(holder.formatCartItemProductImage);

        if (item.getPricingInfo().getSellingType() == SellingType.PER_KG) {
            holder.formatCartItemName.setText(String.format(Locale.getDefault(), "%s - $%.2f/kg",
                    item.getItemInformation().getName(),
                    item.getPricingInfo().getUsdPrice().setScale(2, RoundingMode.UP)));
        } else {
            holder.formatCartItemName.setText(String.format(Locale.getDefault(), "%s - $%.2f",
                    item.getItemInformation().getName(),
                    item.getPricingInfo().getUsdPrice().setScale(2, RoundingMode.UP)));
        }

        applyMultipleColorSpans(activity, holder.formatCartItemDetails,
                Pair.of(String.format(Locale.getDefault(),
                                "$%.2f",
                                item.getPricingInfo().getUsdPrice().multiply(BigDecimal.valueOf(quantity))),
                        true),
                Pair.of("  ", false),
                Pair.of(String.format(Locale.getDefault(), "%d %s", quantity,
                                item.getPricingInfo().getSellingType() == SellingType.PER_KG ? "kg" : "item(s)"),
                        false));

        if (item.getType() == ItemType.DRY)
            holder.formatCartItemType.setImageResource(R.drawable.ic_dry_produce2);
        else holder.formatCartItemType.setImageResource(R.drawable.ic_fresh_produce);

        switch (item.getItemInformation().getAdditionalInformation().getWhereGrown()) {
            case OPEN_FIELD ->
                    holder.formatCartItemGrowTech.setImageResource(R.drawable.ic_open_field);
            case GREEN_HOUSE ->
                    holder.formatCartItemGrowTech.setImageResource(R.drawable.ic_green_house);
            default -> holder.formatCartItemGrowTech.setImageResource(R.drawable.ic_shed_net);
        }

        holder.formatCartHolder.setOnClickListener(v -> activity.getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.slide_in_bottom, R.anim.slide_out_bottom)
                .replace(R.id.mainFragContainerFull, new ItemInfoFragment(item))
                .addToBackStack(null)
                .commit());
    }

    @Override
    public int getItemCount() {
        return limit;
    }

    public void addItem(Item item, int quantity) {
        Integer pos = contains(item);
        if (pos != null) {
            cart.get(pos).setTwo(quantity);
            notifyItemChanged(pos);
        } else {
            cart.add(Pair.of(item, quantity));
            limit++;
            notifyItemInserted(cart.size() - 1);
        }
    }

    public void addItems(List<Pair<Item, Integer>> items) {
        for (Pair<Item, Integer> item : items) {
            addItem(item.getOne(), item.getTwo());
        }
    }

    public void removeItem(Item item) {
        Integer pos = contains(item);
        if (pos != null) {
            notifyItemRemoved(pos);
            cart.remove(pos.intValue());
            limit--;
        }
    }

    private Integer contains(Item item) {
        int count = 0;
        for (Pair<Item, Integer> pair : cart) {
            if (pair.getOne() == item) {
                return count;
            }

            count++;
        }
        return null;
    }

    public void clear() {
        limit = 0;
        notifyItemRangeRemoved(0, cart.size());
        cart.clear();
    }

    public void clearNotInGlobal(Cart globalCart) {
        for (Pair<Item, Integer> pair : cart) {
            if (globalCart.getItemCounts().get(pair.getOne().getId()) == null) {
                removeItem(pair.getOne());
            }
        }
    }

    public void updateLimit(int newLimit) {

        Log.d("LIMIT", "HERE: " + cart.size());

        if (newLimit == MAX) {
            newLimit = cart.size();

            Log.d("LIMIT", "HERE 2: " + cart.size());
        }

        if (newLimit > limit) {
            notifyItemRangeInserted(limit, newLimit - limit);
        } else if (newLimit < limit) {
            notifyItemRangeRemoved(newLimit, limit - newLimit);
        }

        this.limit = newLimit;

        if (limit > cart.size()) {
            this.limit = cart.size();
            notifyItemRangeRemoved(limit, getItemCount() - limit);
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ConstraintLayout formatCartHolder;
        TextView formatCartItemName, formatCartItemDetails;
        ImageView formatCartItemType, formatCartItemGrowTech;
        ImageView formatCartItemProductImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            init(itemView);
        }

        void init(View itemView) {
            initUi(itemView);
        }

        void initUi(View itemView) {
            formatCartHolder = itemView.findViewById(R.id.formatCartHolder);
            formatCartItemName = itemView.findViewById(R.id.formatCartItemName);
            formatCartItemDetails = itemView.findViewById(R.id.formatCartItemDetails);
            formatCartItemType = itemView.findViewById(R.id.formatCartItemType);
            formatCartItemGrowTech = itemView.findViewById(R.id.formatCartItemGrowTech);
            formatCartItemProductImage = itemView.findViewById(R.id.formatCartItemProductImage);
        }
    }

}
