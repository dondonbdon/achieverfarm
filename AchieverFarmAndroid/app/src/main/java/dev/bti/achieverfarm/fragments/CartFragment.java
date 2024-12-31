package dev.bti.achieverfarm.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
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
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.progressindicator.CircularProgressIndicator;

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
import dev.bti.achieverfarm.interfaces.CartUpdatedListener;

public class CartFragment extends Fragment implements CartUpdatedListener {

    TextView savedCartName, cartKeepShopping, cartRepeatOrder, cartSaved,
            cartCheckoutForItems, cartCheckoutAmount, cartCheckout, savingCartStatus;
    FrameLayout cartClear, cartSave;
    RecyclerView cartRecycler;
    LinearLayoutCompat savingCartProgressIndicatorHolder;
    CircularProgressIndicator savingCartProgressIndicator;
    CartItemAdapter cartItemAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cart, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initUi(view);
        initClicks();
        initRecycler();
        initData();
        initInfo();
    }

    public void initUi(View rootView) {
        savedCartName = rootView.findViewById(R.id.savedCartName);
        cartClear = rootView.findViewById(R.id.cartClear);
        cartSave = rootView.findViewById(R.id.cartSave);
        cartRecycler = rootView.findViewById(R.id.cartRecycler);
        cartKeepShopping = rootView.findViewById(R.id.cartKeepShopping);
        cartRepeatOrder = rootView.findViewById(R.id.cartRepeatOrder);
        cartSaved = rootView.findViewById(R.id.cartSaved);
        cartCheckoutForItems = rootView.findViewById(R.id.cartCheckoutForItems);
        cartCheckoutAmount = rootView.findViewById(R.id.cartCheckoutAmount);
        cartCheckout = rootView.findViewById(R.id.cartCheckout);
        savingCartProgressIndicatorHolder = rootView.findViewById(R.id.savingCartProgressIndicatorHolder);
        savingCartProgressIndicator = rootView.findViewById(R.id.savingCartProgressIndicator);
        savingCartStatus = rootView.findViewById(R.id.savingCartStatus);
    }

    public void initClicks() {
        cartClear.setOnClickListener(v -> {
            if (cartItemAdapter.getItemCount() > 0) {
                showConfirmationDialog();
            } else {
                Toast.makeText(requireActivity(), "Your cart is empty. Do some shopping first!", Toast.LENGTH_SHORT).show();
            }
        });

        cartSave.setOnClickListener(v -> {
            if (cartItemAdapter.getItemCount() > 0) {
                showCartNameDialog();
            } else {
                Toast.makeText(requireActivity(), "Your cart is empty. Do some shopping first!", Toast.LENGTH_SHORT).show();
            }
        });

        cartKeepShopping.setOnClickListener(v -> {
            MainActivity mainActivity = (MainActivity) requireActivity();
            mainActivity.getMainBottomNavigationView().setSelectedItemId(R.id.nav_home);

            if (!Application.getCurrCart().isEmpty()) {
                Application.setCurrCart("");
                Toast.makeText(requireContext(), "Now adding items to main cart.", Toast.LENGTH_SHORT).show();
            }
        });

        cartSaved.setOnClickListener(v -> SDKHelpers.GetInstance(Credentials.GetInstance().TOKEN)
                .getCartHelper()
                .getCarts(Credentials.GetInstance().UID)
                .addOnSuccessListener(result -> requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(R.anim.slide_in_bottom, R.anim.slide_out_bottom)
                        .replace(R.id.mainFragContainerFull, new SavedCartsListFragment())
                        .addToBackStack("saved_cart")
                        .commit())
                .execute());

        cartCheckout.setOnClickListener(v -> {
            if (cartItemAdapter.getItemCount() == 0) {
                Toast.makeText(requireActivity(), "Your cart is empty. Do some shopping first!", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent intent = new Intent(requireContext(), CheckoutActivity.class);
            requireActivity().startActivity(intent);
        });
    }

    public void initRecycler() {
        cartItemAdapter = new CartItemAdapter(requireActivity());
        cartRecycler.setAdapter(cartItemAdapter);
    }

    public void initInfo() {
        Cart cart = Application.getGlobalCartHelper().getGlobalCart();

        cartCheckoutAmount.setText(String.format(Locale.getDefault(), "$%.2f", cart.getTotal()));
        cartCheckoutForItems.setText(String.format(Locale.getDefault(), "Checking out for %s items in stock", cart.getItemCounts().size()));
    }

    public void initData() {
        Cart cart = Application.getGlobalCartHelper().getGlobalCart();
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

    @Override
    public void onCartUpdated() {
        refreshCart();
        Log.d("CART TO REFRESH", Application.getGlobalCartHelper().getGlobalCart().toString());
    }

    private void refreshCart() {
        initInfo();
        initData();
    }

    private void showCartNameDialog() {
        LayoutInflater inflater = LayoutInflater.from(requireContext());
        View customView = inflater.inflate(R.layout.dialog_cart_name, null);

        TextView dialogCartNameCancel = customView.findViewById(R.id.dialogCartNameCancel);
        TextView dialogCartNameSave = customView.findViewById(R.id.dialogCartNameSave);
        EditText dialogCartNameEdit = customView.findViewById(R.id.dialogCartNameEdit);

        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(requireContext())
                .setView(customView)
                .setCancelable(true);

        AlertDialog dialog = builder.create();
        dialog.show();

        dialogCartNameCancel.setOnClickListener(v1 -> dialog.dismiss());
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawableResource(R.drawable.rect_20dp);

        dialogCartNameSave.setOnClickListener(v1 -> {
            if (dialogCartNameEdit.getText().toString().trim().isEmpty()) {
                dialogCartNameEdit.setError("Name can not be empty");
            }

            saveCart(dialogCartNameEdit.getText().toString().trim());
            progress(true);
            dialog.dismiss();
        });
    }

    private void saveCart(String cartName) {
        SDKHelpers.GetInstance(Credentials.GetInstance().TOKEN)
                .getCartHelper()
                .saveCart(Credentials.GetInstance().UID, cartName)
                .addOnSuccessListener(result -> {

                    cartItemAdapter.clear();
                    initInfo();
                    progress(false);

                    savingCartStatus.setText(R.string.cart_saved);

                    new Handler().postDelayed(() -> {
                        savingCartStatus.setText(R.string.saving_your_cart);
                        savingCartStatus.setVisibility(View.INVISIBLE);
                    }, 10000);
                }).execute();

    }

    private void progress(boolean show) {
        if (show) {
            savingCartProgressIndicator.setIndeterminate(true);
            savingCartStatus.setVisibility(View.VISIBLE);
            savingCartProgressIndicator.setVisibility(View.VISIBLE);
            cartClear.setEnabled(false);
            cartSave.setEnabled(false);
            cartCheckout.setEnabled(false);
        } else {
            savingCartProgressIndicator.setIndeterminate(false);
            savingCartProgressIndicator.setVisibility(View.INVISIBLE);
            cartClear.setEnabled(true);
            cartSave.setEnabled(true);
            cartCheckout.setEnabled(true);

        }
    }


    private void showConfirmationDialog() {
        new MaterialAlertDialogBuilder(requireContext())
                .setTitle("Clear Your Cart")
                .setMessage("Are you sure you want to clear your cart?")
                .setPositiveButton("Clear", (dialog, which) -> SDKHelpers.GetInstance(Credentials.GetInstance().TOKEN)
                        .getCartHelper().clearCart(Credentials.GetInstance().UID)
                        .addOnSuccessListener(result -> {
                            cartItemAdapter.clear();
                            initInfo();
                            Toast.makeText(requireContext(), "Cart cleared", Toast.LENGTH_SHORT).show();
                        }).execute())
                .setNegativeButton("Back", (dialog, which) -> dialog.dismiss())
                .show();
    }
}
