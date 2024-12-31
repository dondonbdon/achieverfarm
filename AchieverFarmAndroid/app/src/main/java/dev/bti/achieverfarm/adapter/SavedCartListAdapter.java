package dev.bti.achieverfarm.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

import dev.bti.achieverfarm.R;
import dev.bti.achieverfarm.androidsdk.models.Cart;
import dev.bti.achieverfarm.fragments.SavedCartFragment;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class SavedCartListAdapter extends RecyclerView.Adapter<SavedCartListAdapter.ViewHolder> {

    FragmentActivity activity;
    List<Cart> carts;

    @NonNull
    @Override
    public SavedCartListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(activity).inflate(R.layout.format_order_list, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SavedCartListAdapter.ViewHolder holder, int position) {
        Cart cart = carts.get(position);


        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM yyyy", Locale.getDefault())
                .withZone(ZoneId.systemDefault());

        holder.listName.setText(cart.getName());
        holder.listDate.setText(formatter.format(cart.getCreatedTimestamp()));
        holder.listAmount.setText(String.format(Locale.getDefault(), "$%.2f", cart.getTotal()));
        holder.listQuantity.setText(String.format(Locale.getDefault(), "%d items", cart.getQuantity()));


        holder.listHolder.setOnClickListener(v -> activity.getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.slide_in_bottom, R.anim.slide_out_bottom)
                .replace(R.id.mainFragContainerFull, new SavedCartFragment(cart))
                .addToBackStack("saved_cart")
                .commit());
    }

    @Override
    public int getItemCount() {
        return carts.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView listName, listAmount, listDate, listQuantity;
        LinearLayoutCompat listHolder;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            init(itemView);
        }

        void init(View itemView) {
            initUi(itemView);
        }

        void initUi(View itemView) {
            listHolder = itemView.findViewById(R.id.listHolder);
            listName = itemView.findViewById(R.id.listName);
            listAmount = itemView.findViewById(R.id.listAmount);
            listDate = itemView.findViewById(R.id.listDate);
            listQuantity = itemView.findViewById(R.id.listQuantity);
        }

    }


}
