package com.example.myfridge;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myfridge.Common.KeywordsUtils;
import com.example.myfridge.Models.Usermodel;
import com.example.myfridge.databinding.FragmentUserBinding;
import com.google.gson.Gson;

import java.util.Objects;

public class UserFragment extends Fragment implements View.OnClickListener {

    FragmentUserBinding binding;

    public UserFragment() {
        // Required empty public constructor
    }

    public static UserFragment newInstance() {
        UserFragment fragment = new UserFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentUserBinding.inflate(inflater, container, false);
        binding.btnLogOut.setOnClickListener(this);

        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences(KeywordsUtils.SP_userSession, Context.MODE_PRIVATE);
        String json = sharedPreferences.getString(KeywordsUtils.SP_userName, "");
        // Convert json string to Usermodel object
        Gson gson = new Gson();
        Usermodel usermodel = gson.fromJson(json, Usermodel.class);
        // Set the username in the textview
        binding.txtUsername.setText(Objects.requireNonNull(usermodel).getUserName());
        return binding.getRoot();
    }

    @Override
    public void onClick(View view) {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences(KeywordsUtils.SP_userSession, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        // Go to the login activity
        Intent intent = new Intent(requireActivity(), LoginActivity.class);
        startActivity(intent);
        requireActivity().finish();
    }
}