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

package com.aurora.corona.viewmodel;

import android.app.Application;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.aurora.corona.Constants;
import com.aurora.corona.model.casetime.Statewise;
import com.aurora.corona.model.item.StateItem;
import com.aurora.corona.util.PrefUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import io.reactivex.Observable;

public class StateWiseReportModel extends AndroidViewModel implements SharedPreferences.OnSharedPreferenceChangeListener {

    private Gson gson = new Gson();
    private MutableLiveData<List<StateItem>> data = new MutableLiveData<>();
    private MutableLiveData<String> error = new MutableLiveData<>();

    public StateWiseReportModel(@NonNull Application application) {
        super(application);
        fetchDataFromPreferences();
    }

    public MutableLiveData<String> getError() {
        return error;
    }

    public LiveData<List<StateItem>> getData() {
        return data;
    }

    public void fetchDataFromPreferences() {
        final String rawStateWiseDate = PrefUtil.getString(getApplication(), Constants.PREFERENCE_STATE_WISE);
        final Type type = new TypeToken<List<Statewise>>() {
        }.getType();

        if (!rawStateWiseDate.isEmpty()) {
            final List<Statewise> statewiseList = gson.fromJson(rawStateWiseDate, type);
            if (!statewiseList.isEmpty()) {
                Observable.fromIterable(statewiseList)
                        .map(StateItem::new)
                        .toList()
                        .doOnSuccess(countryItems -> data.setValue(countryItems))
                        .doOnError(throwable -> error.setValue(throwable.getMessage()))
                        .subscribe();
            }
        }
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(Constants.PREFERENCE_STATE_WISE)) {
            fetchDataFromPreferences();
        }
    }
}
