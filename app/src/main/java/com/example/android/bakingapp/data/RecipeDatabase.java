package com.example.android.bakingapp.data;

/**
 * This class uses the Schematic library
 * to create the database with one table for recipes
 */

import net.simonvt.schematic.annotation.Database;
import net.simonvt.schematic.annotation.Table;

@Database(version = RecipeDatabase.VERSION)
public class RecipeDatabase {

    public static final int VERSION = 1;

    @Table(RecipeContract.class)
    public static final String RECIPES = "recipes";
}
