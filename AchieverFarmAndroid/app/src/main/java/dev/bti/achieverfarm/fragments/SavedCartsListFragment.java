package dev.bti.achieverfarm.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import dev.bti.achieverfarm.R;
import dev.bti.achieverfarm.adapter.SavedCartListAdapter;
import dev.bti.achieverfarm.androidsdk.common.SDKHelpers;
import dev.bti.achieverfarm.common.Credentials;
import dev.bti.achieverfarm.util.SwipeTouchListener;

@SuppressWarnings("unchecked")
public class SavedCartsListFragment extends Fragment {

    RecyclerView savedCartsRecycler;

    SavedCartListAdapter savedCartListAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_saved_carts, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.setTag(this);
        view.setOnTouchListener(new SwipeTouchListener(view));
        initUi(view);
        initData();
    }

    public void initUi(View rootView) {
        savedCartsRecycler = rootView.findViewById(R.id.savedCartsRecycler);
    }

    public void initData() {
        SDKHelpers.GetInstance(Credentials.GetInstance().TOKEN)
                .getCartHelper()
                .getCarts(Credentials.GetInstance().UID).addOnSuccessListener(result -> {

                    savedCartListAdapter = new SavedCartListAdapter(requireActivity(), result.getData());
                    savedCartsRecycler.setAdapter(savedCartListAdapter);
                }).execute();
    }
}