package co.asterv.ad_bakingapp.model;

import android.os.Parcelable;
import android.os.Parcel;

public class Recipe implements Parcelable {
    private String recipeName;
    private int recipeId;
    private double ingredientsQuantity;
    private String ingredientsMeasureType;
    private String ingredientsName;
    private String steps;
    private int servings;
    private String imagePath;

    public Recipe() { }

    protected Recipe(Parcel in) {
        recipeId = in.readInt ();
        recipeName = in.readString ();
        ingredientsQuantity = in.readDouble ();
        ingredientsMeasureType = in.readString ();
        ingredientsName = in.readString ();
        steps = in.readString ();
        servings = in.readInt ();
        imagePath = in.readString ();
    }

    public static final Creator<Recipe> CREATOR = new Creator<Recipe> () {
        @Override
        public Recipe createFromParcel(Parcel in) { return new Recipe (in); }

        @Override
        public Recipe[] newArray(int size) { return new Recipe[size]; }
    };

    /*** SETTER METHODS ***/
    public void setRecipeName(String recipeName) { this.recipeName = recipeName; }
    public void setRecipeId(int recipeId) { this.recipeId = recipeId; }
    public void setIngredientsQuantity(double ingredientsQuantity) { this.ingredientsQuantity = ingredientsQuantity; }
    public void setIngredientsMeasureType(String ingredientsMeasureType) { this.ingredientsMeasureType = ingredientsMeasureType; }
    public void setIngredientsName(String ingredientsName) { this.ingredientsName = ingredientsName; }
    public void setSteps(String steps) { this.steps = steps; }
    public void setServings(int servings) { this.servings = servings; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }

    /*** GETTER METHODS ***/
    public String getRecipeName() { return recipeName; }
    public int getRecipeId() { return recipeId; }
    public double getIngredientsQuantity() { return ingredientsQuantity; }
    public String getIngredientsMeasureType() { return ingredientsMeasureType; }
    public String getIngredientsName() { return ingredientsName; }
    public String getSteps() { return steps; }
    public int getServings() { return servings; }
    public String getImagePath() { return imagePath; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(recipeName);
        dest.writeInt (recipeId);
        dest.writeDouble (ingredientsQuantity);
        dest.writeString (ingredientsMeasureType);
        dest.writeString (ingredientsName);
        dest.writeString (steps);
        dest.writeInt (servings);
        dest.writeString (imagePath);
    }
}
