package com.example.android.bakingapp.objects;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * This {@link Ingredient} object contains ingredient
 * information such as ingredient quantity, name and measure
 */
public class Ingredient implements Parcelable {

    // Ingredient quantity
    private double ingredientQuantity;
    // Ingredient name
    private String ingredientName;
    // Ingredient measure
    private String ingredientMeasure;

    /**
     * Constructs a new {@link Recipe} object.
     *
     * @param ingredientQuantity   is the quantity for the ingredient
     * @param ingredientName       is the name of the ingredient
     * @param ingredientMeasure    is the measure for the ingredient
     */
    public Ingredient(double ingredientQuantity, String ingredientName, String ingredientMeasure) {
        this.ingredientQuantity = ingredientQuantity;
        this.ingredientName = ingredientName;
        this.ingredientMeasure = ingredientMeasure;
    }

    protected Ingredient(Parcel in) {
        this.ingredientQuantity = in.readDouble();
        this.ingredientName = in.readString();
        this.ingredientMeasure = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeDouble(this.ingredientQuantity);
        parcel.writeString(this.ingredientName);
        parcel.writeString(this.ingredientMeasure);
    }

    public static final Creator<Ingredient> CREATOR = new Creator<Ingredient>() {
        @Override
        public Ingredient createFromParcel(Parcel source) {
            return new Ingredient(source);
        }

        @Override
        public Ingredient[] newArray(int size) {
            return new Ingredient[size];
        }
    };

    // Returns ingredient quantity.
    public double getIngredientQuantity() {
        return ingredientQuantity;
    }

    // Returns ingredient name.
    public String getIngredientName() {
        return ingredientName;
    }

    // Returns the recipe image.
    public String getIngredientMeasure() {
        return ingredientMeasure;
    }
}
