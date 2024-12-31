package dev.bti.achieverfarm.fragments;

import static dev.bti.achieverfarm.common.Common.applyMultipleColorSpans;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.widget.TextViewCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;

import java.time.temporal.ChronoUnit;
import java.util.Locale;
import java.util.Optional;

import dev.bti.achieverfarm.Application;
import dev.bti.achieverfarm.GlobalCartHelper;
import dev.bti.achieverfarm.MainActivity;
import dev.bti.achieverfarm.R;
import dev.bti.achieverfarm.androidsdk.common.Pair;
import dev.bti.achieverfarm.androidsdk.common.SDKHelpers;
import dev.bti.achieverfarm.androidsdk.common.Triple;
import dev.bti.achieverfarm.androidsdk.enums.SellingType;
import dev.bti.achieverfarm.androidsdk.models.Cart;
import dev.bti.achieverfarm.androidsdk.models.Item;
import dev.bti.achieverfarm.common.Credentials;
import dev.bti.achieverfarm.util.SwipeTouchListener;

public class ItemInfoFragment extends Fragment {

    ImageView itemInfoImage, itemInfoMinus, itemInfoPlus, itemInfoCart, itemInfoDeleteFromCart;
    TextView itemInfoName, itemInfoDescription, itemInfoPrice, itemInfoPerKG, itemInfoQuantity,
            itemInfoRecommended, itemInfoPackaging, itemInfoGrowLocation, itemInfoGrowLength,
            itemInfoShelfLife, itemInfoGrowTech, itemInfoSource;

    ConstraintLayout itemInfoCartHolder;
    Item item;

    Boolean cartNavOpen = false;

    public ItemInfoFragment(Item item) {
        this.item = item;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_item_info, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.setTag(this);
        view.setOnTouchListener(new SwipeTouchListener(view));
        initUi(view);
        initInfo();
        initClicks();
    }

    public void initUi(View rootView) {
        itemInfoImage = rootView.findViewById(R.id.itemInfoImage);
        itemInfoDeleteFromCart = rootView.findViewById(R.id.itemInfoDeleteFromCart);
        itemInfoName = rootView.findViewById(R.id.itemInfoName);
        itemInfoDescription = rootView.findViewById(R.id.itemInfoDescription);
        itemInfoPrice = rootView.findViewById(R.id.itemInfoPrice);
        itemInfoPerKG = rootView.findViewById(R.id.itemInfoPerKG);
        itemInfoQuantity = rootView.findViewById(R.id.itemInfoQuantity);
        itemInfoCart = rootView.findViewById(R.id.itemInfoCart);
        itemInfoCartHolder = rootView.findViewById(R.id.itemInfoCartHolder);
        itemInfoRecommended = rootView.findViewById(R.id.itemInfoRecommended);
        itemInfoPackaging = rootView.findViewById(R.id.itemInfoPackaging);
        itemInfoGrowLocation = rootView.findViewById(R.id.itemInfoGrowLocation);
        itemInfoGrowLength = rootView.findViewById(R.id.itemInfoGrowLength);
        itemInfoShelfLife = rootView.findViewById(R.id.itemInfoShelfLife);
        itemInfoGrowTech = rootView.findViewById(R.id.itemInfoGrowTech);
        itemInfoSource = rootView.findViewById(R.id.itemInfoSource);
        itemInfoMinus = rootView.findViewById(R.id.itemInfoMinus);
        itemInfoPlus = rootView.findViewById(R.id.itemInfoPlus);
    }

    public void initClicks() {
        itemInfoCart.setOnClickListener(v -> cartNavOpen());
        itemInfoDeleteFromCart.setOnClickListener(v -> handleCartAction(0));
        itemInfoMinus.setOnClickListener(v -> handleCartAction(-1));
        itemInfoPlus.setOnClickListener(v -> handleCartAction(1));
    }

    private void handleCartAction(int action) {
        int currentQuantity = Integer.parseInt(itemInfoQuantity.getText().toString());

        switch (action) {
            case 1 -> incrementCartItem();

            case -1 -> {
                if (currentQuantity > 0) {
                    decrementCartItem();
                } else {
                    Toast.makeText(requireContext(), "Cannot decrement, item is already at 0", Toast.LENGTH_SHORT).show();
                }
            }
            case 0 -> {
                if (currentQuantity > 0) {
                    deleteCartItem();
                } else {
                    Toast.makeText(requireContext(), "Item is not in the cart", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void incrementCartItem() {
        updateQuantity(1);
        SDKHelpers.GetInstance(Credentials.GetInstance().TOKEN)
                .getCartHelper()
                .incrementCartItem(Credentials.GetInstance().UID, item.getId(), 1)
                .addOnSuccessListener(response -> updateGlobalCart(response.getData()))
                .execute();
    }

    private void decrementCartItem() {
        updateQuantity(-1);
        SDKHelpers.GetInstance(Credentials.GetInstance().TOKEN)
                .getCartHelper()
                .decrementCartItem(Credentials.GetInstance().UID, item.getId(), 1)
                .addOnSuccessListener(response -> updateGlobalCart(response.getData()))
                .execute();
    }

    private void deleteCartItem() {
        itemInfoQuantity.setText(String.format(Locale.getDefault(), "%d", 0));
        Toast.makeText(requireContext(), "Removed from cart", Toast.LENGTH_SHORT).show();
        cartNavOpen();

        if (Application.getGlobalCartHelper().getGlobalCart().getQuantity() > 0) {
            itemInfoDeleteFromCart.setVisibility(View.VISIBLE);
        } else {
            itemInfoDeleteFromCart.setVisibility(View.GONE);
        }

        SDKHelpers.GetInstance(Credentials.GetInstance().TOKEN)
                .getCartHelper()
                .deleteCartItem(Credentials.GetInstance().UID, item.getId())
                .addOnSuccessListener(response -> updateGlobalCart(response.getData()))
                .execute();
    }

    private void updateGlobalCart(Cart globalCart) {
        Application.getGlobalCartHelper().setGlobalCart(globalCart);
        notifyCartFragment();
    }

    private void notifyCartFragment() {
        MainActivity mainActivity = (MainActivity) requireActivity();
        if (mainActivity.getCartFragment() != null) {
            mainActivity.getCartFragment().onCartUpdated();
        }
    }

    private void updateQuantity(int delta) {
        int current = Integer.parseInt(itemInfoQuantity.getText().toString());
        int updated = Math.max(current + delta, 0);
        itemInfoQuantity.setText(String.format(Locale.getDefault(), "%d", updated));

        if (Application.getGlobalCartHelper().getGlobalCart().getQuantity() > 0) {
            itemInfoDeleteFromCart.setVisibility(View.VISIBLE);
        } else {
            itemInfoDeleteFromCart.setVisibility(View.GONE);
        }
    }

    private void cartNavOpen() {
        if (cartNavOpen) {
            itemInfoCartHolder.setAlpha(0f);
            itemInfoCart.animate().rotation(0).setDuration(200).start();
            itemInfoCartHolder.setVisibility(View.GONE);
        } else {
            itemInfoCartHolder.animate().alpha(1).setDuration(200).start();
            itemInfoCart.animate().rotation(180).setDuration(200).start();
            itemInfoCartHolder.setVisibility(View.VISIBLE);
        }
        cartNavOpen = !cartNavOpen;
    }

    public void initInfo() {
        if (Application.getGlobalCartHelper() == null) {
            Log.d("CART-HELPER", "NULL");
        }

        if (Application.getGlobalCartHelper().getGlobalCart().getQuantity() > 0) {
            itemInfoDeleteFromCart.setVisibility(View.VISIBLE);
        } else {
            itemInfoDeleteFromCart.setVisibility(View.GONE);
        }

        int cartCount = Optional.ofNullable(Application.getGlobalCartHelper())
                .map(GlobalCartHelper::getGlobalCart)
                .map(cart -> cart.getItemCounts().get(item.getId()))
                .orElse(0);

        itemInfoQuantity.setText(String.format(Locale.getDefault(), "%d", cartCount));

        Glide.with(this)
                .load(item.getItemInformation().getPhotoUrl())
                .transform(new CenterCrop())
                .into(itemInfoImage);


        itemInfoName.setText(item.getItemInformation().getName());
        itemInfoDescription.setText(item.getItemInformation().getDescription());
        itemInfoPrice.setText(String.format(Locale.getDefault(), "$%.2f", item.getPricingInfo().getUsdPrice()));

        if (item.getPricingInfo().getSellingType() != SellingType.PER_KG) {
            itemInfoPerKG.setVisibility(View.GONE);
        }

        applyMultipleColorSpans(requireContext(), itemInfoRecommended,
                new Pair<>("Recommended uses are ", false),
                new Pair<>(String.join(", ", item.getItemInformation().getUsesList()), true));

        applyMultipleColorSpans(requireContext(), itemInfoPackaging,
                new Pair<>("Packaged in a ", false),
                new Pair<>(String.join(", ", item.getItemInformation().getPackaging()), true));

        applyMultipleColorSpans(requireContext(), itemInfoGrowLocation,
                new Pair<>("Grown in ", false),
                new Pair<>(item.getItemInformation().getAdditionalInformation().getOrigin().getOne(), true),
                new Pair<>(" by ", false),
                new Pair<>(item.getItemInformation().getAdditionalInformation().getOrigin().getTwo(), true));

        applyMultipleColorSpans(requireContext(), itemInfoGrowLength,
                new Pair<>("Grow time is ", false),
                new Pair<>(tripleJoin("-", item.getItemInformation().getAdditionalInformation().getGrowLength()), true));

        applyMultipleColorSpans(requireContext(), itemInfoShelfLife,
                new Pair<>("Average Shelf Life of ", false),
                new Pair<>(item.getItemInformation().getAdditionalInformation().avgShelfLifeVal().toString() +
                        item.getItemInformation().getAdditionalInformation().getAvgShelfLife().getThree().toString().toLowerCase(), true));

        applyMultipleColorSpans(requireContext(), itemInfoGrowTech,
                new Pair<>("Grown in a ", false),
                new Pair<>(item.getItemInformation().getAdditionalInformation().getWhereGrown().toString(), true));

        applyMultipleColorSpans(requireContext(), itemInfoSource,
                new Pair<>(item.getItemInformation().getAdditionalInformation().getVariety().getOne(), true),
                new Pair<>(" Variety from ", false),
                new Pair<>(item.getItemInformation().getAdditionalInformation().getVariety().getTwo(), true));


        switch (item.getItemInformation().getAdditionalInformation().getWhereGrown()) {
            case OPEN_FIELD -> setDrawable(R.drawable.ic_open_field);
            case GREEN_HOUSE -> setDrawable(R.drawable.ic_green_house);
            default -> setDrawable(R.drawable.ic_shed_net);
        }
    }

    public String tripleJoin(String delim, Triple<Integer, Integer, ChronoUnit> triple) {
        return String.join(delim, triple.getOne().toString(), triple.getTwo().toString()) + " " +
                triple.getThree().toString().toLowerCase();
    }

    public void setDrawable(int drawableId) {
        Drawable drawableStart = ContextCompat.getDrawable(requireContext(), R.drawable.dot);
        Drawable drawableEnd = ContextCompat.getDrawable(requireContext(), drawableId);

        if (drawableEnd != null && drawableStart != null) {
            drawableStart.setBounds(0, 0, drawableStart.getIntrinsicWidth(), drawableStart.getIntrinsicHeight());
            drawableEnd.setBounds(0, 0, drawableEnd.getIntrinsicWidth(), drawableEnd.getIntrinsicHeight());
        }

        TextViewCompat.setCompoundDrawablesRelativeWithIntrinsicBounds(itemInfoGrowTech, drawableStart, null, drawableEnd, null);
    }
}
