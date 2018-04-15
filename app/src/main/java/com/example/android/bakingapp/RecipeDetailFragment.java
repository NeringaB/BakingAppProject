package com.example.android.bakingapp;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.bakingapp.objects.Recipe;
import com.example.android.bakingapp.objects.Step;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * RecipeDetailFragment is a fragment displaying a recipe details
 * It will be displayed in the RecipesDetailActivity
 */
public class RecipeDetailFragment extends Fragment
        implements RecipeDetailAdapter.RecipeStepOnClickHandler {

    @BindView(R.id.recycler_view_recipe_detail)
    RecyclerView recyclerView;

    private Recipe currentRecipe;
    private List<Step> recipeSteps;
    private RecipeDetailAdapter recipeDetailAdapter;

    private OnStepSelectedClickListener clickListener;

    private Parcelable layoutManagerSavedState;

    public RecipeDetailFragment() {
    }

    // Override onAttach to make sure that the container activity has implemented the callback
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // This makes sure that the host activity has implemented the callback interface
        // If not, it throws an exception
        try {
            clickListener = (OnStepSelectedClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnImageClickListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (savedInstanceState != null) {
            // Restores the `state of the LayoutManager
            layoutManagerSavedState = savedInstanceState
                    .getParcelable("RecyclerViewLayoutManager");
        }

        // Inflate the RecipesFragment layout
        View rootView = inflater.inflate(R.layout.fragment_recipe_detail, container, false);
        ButterKnife.bind(this, rootView);

        // Create LinearLayout
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setHasFixedSize(true);

        // Get fragments arguments
        Bundle fragmentArguments = this.getArguments();
        // If fragment arguments are not null
        if (fragmentArguments != null) {
            // Get recipe
            currentRecipe = fragmentArguments.getParcelable("currentRecipe");
            recipeSteps = currentRecipe.getSteps();
        }

        // Instantiate and set adapter on the RecyclerView
        recipeDetailAdapter = new RecipeDetailAdapter(getContext(), currentRecipe,
                recipeSteps, this);
        recyclerView.setAdapter(recipeDetailAdapter);

        recyclerView.getLayoutManager()
                .onRestoreInstanceState(layoutManagerSavedState);

        return rootView;
    }

    @Override
    public void onClick(int stepId) {
        clickListener.onStepSelected(stepId);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable("RecyclerViewLayoutManager",
                recyclerView.getLayoutManager().onSaveInstanceState());
        super.onSaveInstanceState(outState);
    }

    // OnStepSelectedClickListener interface calls a method in the host activity named onImageSelected
    public interface OnStepSelectedClickListener {
        void onStepSelected(int position);
    }
}
