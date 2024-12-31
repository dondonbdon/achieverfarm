package dev.bti.achieverfarm.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import dev.bti.achieverfarm.R;
import dev.bti.achieverfarm.adapter.SearchItemAdapter;
import dev.bti.achieverfarm.androidsdk.common.SDKHelpers;
import dev.bti.achieverfarm.androidsdk.models.ItemOptions;
import dev.bti.achieverfarm.common.Credentials;

public class SearchFragment extends Fragment {

    EditText searchEditText;
    RecyclerView searchRecycler;

    SearchItemAdapter searchItemAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initUi(view);
        initLayoutManager();
        initRecycler();
        initListeners();
    }

    public void initUi(View view) {
        searchEditText = view.findViewById(R.id.searchEditText);
        searchRecycler = view.findViewById(R.id.searchRecycler);
    }

    public void initLayoutManager() {
        GridLayoutManager layoutManager = new GridLayoutManager(requireActivity(), 2);
        searchRecycler.setLayoutManager(layoutManager);
    }

    public void initRecycler() {
        searchItemAdapter = new SearchItemAdapter(requireActivity());
        searchRecycler.setAdapter(searchItemAdapter);

        ItemOptions.ItemOptionsBuilder itemOptions = ItemOptions.builder()
                .count(20);

        SDKHelpers.GetInstance(Credentials.GetInstance().TOKEN)
                .getItemHelper()
                .getItemsWithOptions(itemOptions.build())
                .addOnSuccessListener(response -> searchItemAdapter.addItems(response.getData())).execute();
    }

    public void initListeners() {
        searchRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (dy > 0) {
                    GridLayoutManager layoutManager = (GridLayoutManager) recyclerView.getLayoutManager();

                    if (layoutManager != null) {
                        int visibleItemCount = layoutManager.getChildCount();
                        int totalItemCount = layoutManager.getItemCount();
                        int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

                        if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                                && firstVisibleItemPosition >= 0) {

                            loadMoreData(searchEditText.getText().toString().trim());
                        }
                    }
                }
            }
        });

        searchEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                searchItems(v.getText().toString().trim());
                return true;
            }

            return false;
        });
    }

    private void searchItems(String searchTerm) {
        ItemOptions.ItemOptionsBuilder itemOptions = ItemOptions.builder()
                .count(10)
                .searchTerm(searchTerm);

        SDKHelpers.GetInstance(Credentials.GetInstance().TOKEN)
                .getItemHelper()
                .getItemsWithOptions(itemOptions.build())
                .addOnSuccessListener(response -> searchItemAdapter.addItems(response.getData())).execute();
    }

    public void loadMoreData(String searchTerm) {
        ItemOptions.ItemOptionsBuilder itemOptions = ItemOptions.builder()
                .count(10)
                .lastId(searchItemAdapter.getLastItemId())
                .searchTerm(searchTerm);

        SDKHelpers.GetInstance(Credentials.GetInstance().TOKEN)
                .getItemHelper()
                .getItemsWithOptions(itemOptions.build()).addOnSuccessListener(response -> searchItemAdapter.addItems(response.getData())).execute();
    }
}