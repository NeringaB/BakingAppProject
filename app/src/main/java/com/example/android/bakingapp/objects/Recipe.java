package com.example.android.bakingapp.objects;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * This {@link Recipe} object contains recipe information: recipe id, name, image,
 * number of servings, a list of {@link Ingredient} and a list of {@link Step}
 */
public class Recipe implements Parcelable {

    // Recipe id
    private int recipeId;
    // Recipe name
    private String recipeName;
    // Recipe image
    private String recipeImage;
    // A number of servings for the recipe
    private int recipeServings;
    // List of ingredients
    private List<Ingredient> ingredients;
    // List of ingredients
    private List<Step> steps;

    /**
     * Constructs a new {@link Recipe} object.
     *
     * @param recipeId      is the id for the recipe
     * @param recipeName    is the name of the recipe
     * @param recipeImage   is the image for the recipe
     * @param ingredients   is the list of ingredients for the recipe
     * @param steps         is the list of steps for the recipe
     */
    public Recipe(int recipeId, String recipeName, String recipeImage, int recipeServings,
                  List<Ingredient> ingredients, List<Step> steps) {
        this.recipeId = recipeId;
        this.recipeName = recipeName;
        this.recipeImage = recipeImage;
        this.recipeServings = recipeServings;
        this.ingredients = ingredients;
        this.steps = steps;
    }

    protected Recipe(Parcel in) {
        this.recipeId = in.readInt();
        this.recipeName = in.readString();
        this.recipeImage = in.readString();
        this.recipeServings = in.readInt();
        this.ingredients = in.createTypedArrayList(Ingredient.CREATOR);
        this.steps = in.createTypedArrayList(Step.CREATOR);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.recipeId);
        parcel.writeString(this.recipeName);
        parcel.writeString(this.recipeImage);
        parcel.writeInt(this.recipeServings);
        parcel.writeTypedList(this.ingredients);
        parcel.writeTypedList(this.steps);
    }

    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel source) {
            return new Recipe(source);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

    // Returns the recipe id.
    public int getRecipeId() {
        return recipeId;
    }

    // Returns the recipe name.
    public String getRecipeName() {
        return recipeName;
    }

    // Returns the recipe image.
    public String getRecipeImage() {
        return recipeImage;
    }

    // Returns the recipe number of servings.
    public int getRecipeServings() {
        return recipeServings;
    }

    // Returns recipe list of Ingredient objects
    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    // Returns recipe list of Step objects
    public List<Step> getSteps() {
        return steps;
    }
}
