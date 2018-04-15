package com.example.android.bakingapp.data;

import net.simonvt.schematic.annotation.AutoIncrement;
import net.simonvt.schematic.annotation.ConflictResolutionType;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;

/**
 * This class uses the Schematic library to define the
 * columns in a the table of the database
 */
public class RecipeContract {

    @DataType(DataType.Type.INTEGER)
    @PrimaryKey(onConflict = ConflictResolutionType.REPLACE)
    @AutoIncrement
    public static final String COLUMN_ID = "_id";

    // This column will store recipe gson strings
    // It should never be null, because its the only one data that will be stored in the database
    @DataType(DataType.Type.TEXT)
    @NotNull
    public static final String COLUMN_RECIPE_GSON_STRING = "recipeGsonString";

}
