package com.example.android.bakingapp.utils;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.objects.Ingredient;

import java.util.List;

/**
 * FormattingUtils contains formatting methods so they would not repeat in the code
 */
public class FormattingUtils {

    // This method returns formatted ingredient measure
    // It takes ingredient measure received from the server and
    // ingredient quantity
    // Returns formatted ingredient measure taking into account singular and plural
    public static String getFormattedRecipeMeasure
    (double ingredientQuantity, String ingredientMeasure) {

        int quantity = ((int) ingredientQuantity);

        String formattedIngredientMeasure = "";

        if (quantity == 0 || quantity == 1) {
            switch (ingredientMeasure) {
                case "CUP":
                    formattedIngredientMeasure = "cup";
                    break;
                case "TBLSP":
                    formattedIngredientMeasure = "tablespoon";
                    break;
                case "TSP":
                    formattedIngredientMeasure = "teaspoon";
                    break;
                case "K":
                    formattedIngredientMeasure = "kilogram";
                    break;
                case "OZ":
                    formattedIngredientMeasure = "ounce";
                    break;
                case "G":
                    formattedIngredientMeasure = "gram";
                    break;
                case "UNIT":
                    formattedIngredientMeasure = "unit";
                    break;
            }
        } else {

            switch (ingredientMeasure) {
                case "CUP":
                    formattedIngredientMeasure = "cups";
                    break;
                case "TBLSP":
                    formattedIngredientMeasure = "tablespoons";
                    break;
                case "TSP":
                    formattedIngredientMeasure = "teaspoons";
                    break;
                case "K":
                    formattedIngredientMeasure = "kilograms";
                    break;
                case "OZ":
                    formattedIngredientMeasure = "ounces";
                    break;
                case "G":
                    formattedIngredientMeasure = "grams";
                    break;
                case "UNIT":
                    formattedIngredientMeasure = "units";
                    break;
            }

        }

        return formattedIngredientMeasure;
    }

    // This method takes adapter position and
    // returns recipe image resource id when the server does not provide any image
    public static int getRecipeImageResource(int position) {

        int recipeImageResource = 0;

        switch (position) {
            case 0:
                recipeImageResource = R.drawable.drawable_nutella_pie;
                break;
            case 1:
                recipeImageResource = R.drawable.drawable_brownies;
                break;
            case 2:
                recipeImageResource = R.drawable.drawable_yellow_cake;
                break;
            case 3:
                recipeImageResource = R.drawable.drawable_cheesecake;
                break;
        }

        return recipeImageResource;
    }

    // This method takes ingredients list and
    // returns formatted ingredient list
    public static String getFormattedIngredientList(List<Ingredient> recipeIngredients) {

        StringBuilder stringBuilder = new StringBuilder();

        for (Ingredient ingredient : recipeIngredients) {

            stringBuilder.append("* ");

            double ingredientQuantity = ingredient.getIngredientQuantity();
            if ((ingredientQuantity == Math.floor(ingredientQuantity))
                    && !Double.isInfinite(ingredientQuantity)) {
                stringBuilder.append((int) ingredientQuantity);
            } else {
                stringBuilder.append(ingredientQuantity);
            }

            stringBuilder.append(" ");
            String ingredientMeasure = ingredient.getIngredientMeasure();
            stringBuilder.append(getFormattedRecipeMeasure(ingredientQuantity, ingredientMeasure));
            stringBuilder.append(" ");
            String ingredientName = ingredient.getIngredientName();
            stringBuilder.append(ingredientName);
            stringBuilder.append("\n");
        }

        return stringBuilder.toString();
    }
}
