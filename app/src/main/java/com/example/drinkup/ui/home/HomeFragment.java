package com.example.drinkup.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.drinkup.ActivityDrink;
import com.example.drinkup.ActivityIngredient;
import com.example.drinkup.R;
import com.example.drinkup.RandomDrink;

public class HomeFragment extends Fragment implements View.OnClickListener {

    private HomeViewModel view;
    private ImageButton imagebuttonedrinkingredient;
    private ImageButton imagebuttonedrink;
    private ImageButton imagebuttonIngredient;
    private ImageButton imagebuttonRandom;

    public View onCreateView(@NonNull LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {


        view = new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);



        imagebuttonedrinkingredient = (ImageButton) root.findViewById(R.id.imageButton_DrinkPerIngredient);
        imagebuttonedrinkingredient.setOnClickListener(this);

        imagebuttonedrink = (ImageButton) root.findViewById(R.id.imageButtonDrink);
        imagebuttonedrink.setOnClickListener(this);

        imagebuttonIngredient = (ImageButton) root.findViewById(R.id.imagebutton_Ingredient);
        imagebuttonIngredient.setOnClickListener(this);

        imagebuttonRandom = (ImageButton) root.findViewById(R.id.imagebutton_randomDrink);
        imagebuttonRandom.setOnClickListener(this);

        return root;
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.imageButtonDrink:
               //azione da compiere dopo aver cliccato bottone drink

                Intent intentNomeDrink = new Intent(getActivity(), ActivityDrink.class);
                startActivity(intentNomeDrink);
                break;

            case R.id.imageButton_DrinkPerIngredient:
                //azione da compiere dopo aver cliccato bottone beer
                Toast.makeText(this.getContext(), "ricerca drink per ingredente", Toast.LENGTH_LONG).show();
                break;

            case R.id.imagebutton_Ingredient:
                //azione da compiere dopo aver cliccato bottone drink
                Intent intentIngredient = new Intent(getActivity(), ActivityIngredient.class);
                startActivity(intentIngredient);
                break;

            case R.id.imagebutton_randomDrink:
                //azione da compiere dopo aver cliccato bottone beer
                Intent intentRandom = new Intent(getActivity(), RandomDrink.class);
                startActivity(intentRandom);
                break;
        }


    }
}