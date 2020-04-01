/*
 * Corona Stats
 * Copyright (C) 2020, Rahul Kumar Patel <auroraoss.dev@gmail.com>
 *
 * Aurora Store is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 *  (at your option) any later version.
 *
 * Corona Stats is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Aurora Store.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.aurora.corona.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aurora.corona.Constants;
import com.aurora.corona.R;
import com.aurora.corona.RecyclerDataObserver;
import com.aurora.corona.model.item.StateItem;
import com.aurora.corona.sheet.StateWiseSheet;
import com.aurora.corona.viewmodel.StateWiseReportModel;
import com.google.gson.Gson;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.adapters.ItemAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StateListFragment extends Fragment {

    @BindView(R.id.txt_title)
    AppCompatTextView txtTitle;
    @BindView(R.id.recycler)
    RecyclerView recycler;
    @BindView(R.id.coordinator)
    CoordinatorLayout coordinator;

    private Gson gson = new Gson();
    private StateWiseReportModel model;
    private RecyclerDataObserver dataObserver;
    private FastAdapter<StateItem> fastAdapter;
    private ItemAdapter<StateItem> itemAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_state_wise, container, false);
        ButterKnife.bind(this, root);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupRecycler();

        model = new ViewModelProvider(this).get(StateWiseReportModel.class);
        model.getData().observe(getViewLifecycleOwner(), countryItems -> {
            itemAdapter.add(countryItems);
        });

    }

    private void setupRecycler() {
        fastAdapter = new FastAdapter<>();
        itemAdapter = new ItemAdapter<>();

        fastAdapter.addAdapter(0, itemAdapter);

        fastAdapter.setOnClickListener((view, itemIAdapter, stateItem, position) -> {
            StateWiseSheet stateWiseSheet = new StateWiseSheet();
            Bundle bundle = new Bundle();
            bundle.putString(Constants.STRING_EXTRA, gson.toJson(stateItem.getStatewise()));
            stateWiseSheet.setArguments(bundle);
            stateWiseSheet.show(getChildFragmentManager(), StateWiseSheet.TAG);
            return false;
        });

        //dataObserver = new RecyclerDataObserver(recyclerView, emptyLayout, progressLayout);
        //fastAdapter.registerAdapterDataObserver(dataObserver);

        recycler.setAdapter(fastAdapter);
        recycler.setLayoutManager(new LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false));
    }


}
