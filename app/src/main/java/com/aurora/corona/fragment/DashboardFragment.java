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

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.aurora.corona.Constants;
import com.aurora.corona.R;
import com.aurora.corona.model.casetime.Statewise;
import com.aurora.corona.util.PrefUtil;
import com.aurora.corona.util.Util;
import com.google.gson.Gson;

import org.apache.commons.lang3.StringUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DashboardFragment extends Fragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    @BindView(R.id.txt_recovered)
    TextView txtRecovered;
    @BindView(R.id.txt_new_cases)
    TextView txtNewCases;
    @BindView(R.id.txt_deaths)
    TextView txtDeaths;
    @BindView(R.id.txt_all_total)
    TextView txtAllTotal;
    @BindView(R.id.txt_all_active)
    TextView txtAllActive;
    @BindView(R.id.txt_all_cured)
    TextView txtAllCured;
    @BindView(R.id.txt_all_deaths)
    TextView txtAllDeaths;
    @BindView(R.id.layout_bottom)
    ConstraintLayout layoutBottom;
    @BindView(R.id.txt_today_last_updated)
    AppCompatTextView txtTodayLastUpdated;
    @BindView(R.id.txt_all_last_updated)
    AppCompatTextView txtAllLastUpdated;

    private Gson gson = new Gson();
    private SharedPreferences sharedPreferences;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = Util.getPrefs(requireContext());
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        updateDailyStatus();
        updateOverallDetails();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        switch (key) {
            case Constants.PREFERENCE_KEY_VALUES:
                updateDailyStatus();
                break;
            case Constants.PREFERENCE_OVERALL_DATA:
                updateOverallDetails();
                break;
        }
    }

    @Override
    public void onDestroy() {
        try {
            sharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
        } catch (Exception ignored) {

        }
        super.onDestroy();
    }

    private void updateDailyStatus() {
        final String rawTimeSeries = PrefUtil.getString(requireContext(), Constants.PREFERENCE_KEY_VALUES);
        final Statewise key_values = gson.fromJson(rawTimeSeries, Statewise.class);

        if (key_values != null) {
            txtRecovered.setText(key_values.getDeltarecovered());
            txtNewCases.setText(key_values.getDeltaconfirmed());
            txtDeaths.setText(key_values.getDeltadeaths());
            txtTodayLastUpdated.setText(StringUtils.joinWith(" : ", "Last updated", key_values.getLastupdatedtime()));
        }
    }

    private void updateOverallDetails() {
        final String rawOverAll = PrefUtil.getString(requireContext(), Constants.PREFERENCE_OVERALL_DATA);
        final Statewise statewise = gson.fromJson(rawOverAll, Statewise.class);

        if (statewise != null) {
            txtAllTotal.setText(statewise.getConfirmed());
            txtAllActive.setText(statewise.getActive());
            txtAllCured.setText(statewise.getRecovered());
            txtAllDeaths.setText(statewise.getDeaths());
            txtAllLastUpdated.setText(StringUtils.joinWith(" : ", "Last updated", statewise.getLastupdatedtime()));
        }
    }
}
