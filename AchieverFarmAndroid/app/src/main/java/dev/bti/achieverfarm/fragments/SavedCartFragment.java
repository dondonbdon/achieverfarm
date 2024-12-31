package dev.bti.achieverfarm.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Objects;

import dev.bti.achieverfarm.Application;
import dev.bti.achieverfarm.CheckoutActivity;
import dev.bti.achieverfarm.MainActivity;
import dev.bti.achieverfarm.R;
import dev.bti.achieverfarm.adapter.CartItemAdapter;
import dev.bti.achieverfarm.androidsdk.common.SDKHelpers;
import dev.bti.achieverfarm.androidsdk.models.Cart;
import dev.bti.achieverfarm.common.Credentials;
import dev.bti.achieverfarm.util.SwipeTouchListener;

public class SavedCartFragment extends Fragment {

    TextView savedCartName, savedCartKeepShopping,
            savedCartCheckoutForItems, savedCartCheckoutAmount, savedCartCheckout, savedCartTimestamp;
    FrameLayout savedCartDelete, savedCartEditName;
    TextView savingCartStatus;
    RecyclerView savedCartRecycler;

    CartItemAdapter cartItemAdapter;

    Cart cart;

    public SavedCartFragment(Cart cart) {
        this.cart = cart;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_saved_cart, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.setTag(this);
        view.setOnTouchListener(new SwipeTouchListener(view));
        initUi(view);
        initClicks();
        initRecycler();
        initData();
        initInfo();
    }

    public void initUi(View rootView) {
        savedCartName = rootView.findViewById(R.id.savedCartName);
        savedCartDelete = rootView.findViewById(R.id.savedCartDelete);
        savedCartEditName = rootView.findViewById(R.id.savedCartEditName);
        savedCartTimestamp = rootView.findViewById(R.id.savedCartTimestamp);
        savedCartRecycler = rootView.findViewById(R.id.savedCartRecycler);
        savedCartKeepShopping = rootView.findViewById(R.id.savedCartKeepShopping);
        savedCartCheckoutForItems = rootView.findViewById(R.id.savedCartCheckoutForItems);
        savedCartCheckoutAmount = rootView.findViewById(R.id.savedCartCheckoutAmount);
        savedCartCheckout = rootView.findViewById(R.id.savedCartCheckout);
    }

    public void initClicks() {
        savedCartDelete.setOnClickListener(v -> showConfirmationDialog());

        savedCartEditName.setOnClickListener(v -> showCartNameDialog());

        savedCartKeepShopping.setOnClickListener(v -> {
            MainActivity mainActivity = (MainActivity) requireActivity();
            mainActivity.getMainBottomNavigationView().setSelectedItemId(R.id.nav_home);

            if (!Application.getCurrCart().equals(cart.getId())) {
                Application.setCurrCart(cart.getId());
                Toast.makeText(requireContext(), String.format(Locale.getDefault(), "Now adding items to %s.", cart.getName()), Toast.LENGTH_SHORT).show();
            }
        });

        savedCartCheckout.setOnClickListener(v -> {
            if (cartItemAdapter.getItemCount() == 0) {
                Toast.makeText(requireActivity(), "Your cart is empty. Do some shopping first!", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent intent = new Intent(requireActivity(), CheckoutActivity.class);
            intent.putExtra("cart", cart);
            requireActivity().startActivity(intent);
        });
    }

    public void initRecycler() {
        cartItemAdapter = new CartItemAdapter(requireActivity());
        savedCartRecycler.setAdapter(cartItemAdapter);
    }

    public void initInfo() {
        savedCartCheckoutAmount.setText(String.format(Locale.getDefault(), "$%.2f", cart.getTotal()));
        savedCartCheckoutForItems.setText(String.format(Locale.getDefault(), "Checking out for %s items in stock",
                cart.getItemCounts().size()));

        savedCartName.setText(cart.getName());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM yyyy", Locale.getDefault())
                .withZone(ZoneId.systemDefault());

        savedCartTimestamp.setText(formatter.format(cart.getLastUpdatedTimestamp()));
    }

    public void initData() {
        cartItemAdapter.clearNotInGlobal(cart);

        cart.getItemCounts().forEach((id, quantity) -> {
            if (Application.getItemCache().containsKey(id)) {
                cartItemAdapter.addItem(Application.getItemCache().get(id), quantity);
            } else {
                SDKHelpers.GetInstance(Credentials.GetInstance().TOKEN).getItemHelper()
                        .getItem(id).addOnSuccessListener(response -> {
                            cartItemAdapter.addItem(Application.getItemCache().get(id), quantity);
                            Application.getItemCache().put(id, response.getData());
                        }).execute();
            }
        });
    }

    private void showCartNameDialog() {
        LayoutInflater inflater = LayoutInflater.from(requireContext());
        View customView = inflater.inflate(R.layout.dialog_cart_name, null);

        TextView dialogCartNameCancel = customView.findViewById(R.id.dialogCartNameCancel);
        TextView dialogCartNameSave = customView.findViewById(R.id.dialogCartNameSave);
        EditText dialogCartNameEdit = customView.findViewById(R.id.dialogCartNameEdit);

        dialogCartNameEdit.setHint(cart.getName());

        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(requireContext())
                .setView(customView)
                .setCancelable(true);

        AlertDialog dialog = builder.create();
        dialog.show();

        dialogCartNameCancel.setOnClickListener(v1 -> dialog.dismiss());
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawableResource(R.drawable.rect_20dp);

        dialogCartNameSave.setOnClickListener(v1 -> {
            String newName = dialogCartNameEdit.getText().toString().trim();
            if (newName.isEmpty()) {
                dialogCartNameEdit.setError("Name can not be empty");
            } else if (Objects.equals(cart.getName(), newName)) {
                dialogCartNameEdit.setError("Name can not be the same");
            } else {
                updateCartName(dialogCartNameEdit.getText().toString().trim());
                dialog.dismiss();
            }
        });
    }

    private void updateCartName(String cartName) {
        SDKHelpers.GetInstance(Credentials.GetInstance().TOKEN).getCartHelper().updateCartName(Credentials.GetInstance().UID,
                cart.getId(), cartName).execute();

        savedCartName.setText(cartName);
        cart.setName(cartName);
    }

    private void saveCart(String cartName) {
        SDKHelpers.GetInstance(Credentials.GetInstance().TOKEN)
                .getCartHelper()
                .saveCart(Credentials.GetInstance().UID, cartName)
                .addOnSuccessListener(result -> {

                    cartItemAdapter.clear();

                    savingCartStatus.setText(R.string.cart_saved);

                    new Handler().postDelayed(() -> {
                        savingCartStatus.setText(R.string.saving_your_cart);
                        savingCartStatus.setVisibility(View.INVISIBLE);
                    }, 10000);
                }).execute();
    }

    private void showConfirmationDialog() {
        new MaterialAlertDialogBuilder(requireContext())
                .setTitle("Delete Your Cart")
                .setMessage("Are you sure you want to delete this cart?")
                .setPositiveButton("Delete", (dialog, which) -> SDKHelpers.GetInstance(Credentials.GetInstance().TOKEN)
                        .getCartHelper().deleteCart(Credentials.GetInstance().UID, cart.getId())
                        .addOnSuccessListener(result -> {
                            Toast.makeText(requireContext(), "Cart deleted", Toast.LENGTH_SHORT).show();
                            requireActivity().getOnBackPressedDispatcher().onBackPressed();
                        }).execute())
                .setNegativeButton("Back", (dialog, which) -> dialog.dismiss())
                .show();
    }
}
