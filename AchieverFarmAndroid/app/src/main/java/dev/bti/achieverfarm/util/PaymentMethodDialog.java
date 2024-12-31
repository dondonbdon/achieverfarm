package dev.bti.achieverfarm.util;

import android.content.Context;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.List;

public class PaymentMethodDialog {

    private final Context context;
    private final String title;
    private final List<String> paymentMethods;
    private final List<String> preSelectedMethods;
    private final OnSelectionDoneListener listener;

    public PaymentMethodDialog(Context context, String title, List<String> paymentMethods,
                               List<String> preSelectedMethods, OnSelectionDoneListener listener) {
        this.context = context;
        this.title = title;
        this.paymentMethods = paymentMethods;
        this.preSelectedMethods = preSelectedMethods;
        this.listener = listener;
    }

    public void show() {
        boolean[] selected = new boolean[paymentMethods.size()];
        for (int i = 0; i < paymentMethods.size(); i++) {
            selected[i] = preSelectedMethods.contains(paymentMethods.get(i));
        }

        new MaterialAlertDialogBuilder(context)
                .setTitle(title)
                .setMultiChoiceItems(paymentMethods.toArray(new String[0]), selected,
                        (dialog, which, isChecked) -> selected[which] = isChecked)
                .setPositiveButton("OK", (dialog, which) -> {
                    List<String> selectedItems = new ArrayList<>();
                    for (int i = 0; i < paymentMethods.size(); i++) {
                        if (selected[i]) {
                            selectedItems.add(paymentMethods.get(i));
                        }
                    }
                    listener.onSelectionDone(selectedItems);
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    public interface OnSelectionDoneListener {
        void onSelectionDone(List<String> selectedMethods);
    }
}

