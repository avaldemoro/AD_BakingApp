package co.asterv.ad_bakingapp.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Ingredient implements Parcelable {
    private double ingredientsQuantity;
    private String ingredientsMeasureType;
    private String ingredientsName;

    public Ingredient() { }

    protected Ingredient(Parcel in) {
        ingredientsQuantity = in.readDouble ();
        ingredientsMeasureType = in.readString ();
        ingredientsName = in.readString ();
    }

    public static final Creator<Ingredient> CREATOR = new Creator<Ingredient> () {
        @Override
        public Ingredient createFromParcel(Parcel in) {
            return new Ingredient (in);
        }

        @Override
        public Ingredient[] newArray(int size) {
            return new Ingredient[size];
        }
    };

    /*** SETTER METHODS ***/
    public void setIngredientsQuantity(double ingredientsQuantity) { this.ingredientsQuantity = ingredientsQuantity; }
    public void setIngredientsMeasureType(String ingredientsMeasureType) { this.ingredientsMeasureType = ingredientsMeasureType; }
    public void setIngredientsName(String ingredientsName) { this.ingredientsName = ingredientsName; }

    /*** GETTER METHODS ***/
    public double getIngredientsQuantity() { return ingredientsQuantity; }
    public String getIngredientsMeasureType() { return ingredientsMeasureType; }
    public String getIngredientsName() { return ingredientsName; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble (ingredientsQuantity);
        dest.writeString (ingredientsMeasureType);
        dest.writeString (ingredientsName);
    }
}
