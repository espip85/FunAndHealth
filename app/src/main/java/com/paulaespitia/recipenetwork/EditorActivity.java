package com.paulaespitia.recipenetwork;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.paulaespitia.recipenetwork.model.Ingredient;
import com.paulaespitia.recipenetwork.model.Recipe;
import com.paulaespitia.recipenetwork.model.RecipeDetailed;
import com.paulaespitia.recipenetwork.model.SQLAsyncTask;
import com.paulaespitia.recipenetwork.model.Step;
import com.paulaespitia.recipenetwork.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;

public class EditorActivity extends Activity {

    private TableLayout ingredientsTableLayout;
    private TableLayout stepsTableLayout;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        ingredientsTableLayout = (TableLayout) findViewById(R.id.editorTableIngredients);
        stepsTableLayout = (TableLayout) findViewById(R.id.editorTableSteps);
    }

    public void addIngredient(View v) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.editor_ingredient, null);
        ingredientsTableLayout.addView(rowView, ingredientsTableLayout.indexOfChild((View) v.getParent()) + 1);
    }

    public void removeIngredient(View v) {
        if (ingredientsTableLayout.getChildCount() > 2) {
            ingredientsTableLayout.removeView((View) v.getParent());
        } else {
            TableRow tableRow = (TableRow) ingredientsTableLayout.getChildAt(1);
            EditText quantityText = (EditText) tableRow.getChildAt(0);
            EditText unitsText = (EditText) tableRow.getChildAt(1);
            EditText ingredientText = (EditText) tableRow.getChildAt(2);
            quantityText.setText("");
            unitsText.setText("");
            ingredientText.setText("");
        }
    }

    public void addStep(View v){

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.editor_steps, null);
        stepsTableLayout.addView(rowView, stepsTableLayout.indexOfChild((View) v.getParent()) + 1);
    }

    public void removeStep (View v){

        if (stepsTableLayout.getChildCount() > 2){
            stepsTableLayout.removeView( (View) v.getParent());
        } else {
            TableRow tableRow = (TableRow) stepsTableLayout.getChildAt(1);
            EditText stepNumber = (EditText) tableRow.getChildAt(0);
            EditText stepDescription = (EditText) tableRow.getChildAt(1);
            stepNumber.setText("");
            stepDescription.setText("");
        }
    }

    public void saveRecipe(View view) {

        Button saveButton = findViewById(R.id.editorSave);
        saveButton.setEnabled(false);

        ArrayList<Ingredient> ingredients = new ArrayList<>();
        for (int i = 1; i < ingredientsTableLayout.getChildCount(); i++) {
            TableRow row = (TableRow) ingredientsTableLayout.getChildAt(i);
            try {
                double quantity = Double.parseDouble(((EditText) row.getChildAt(0)).getText().toString());
                String units = ((EditText) row.getChildAt(1)).getText().toString();
                String ingredient = ((EditText) row.getChildAt(2)).getText().toString();
                if (quantity > 0.0 && units.length() > 0 && ingredient.length() > 0) {
                    ingredients.add(new Ingredient(ingredient, quantity, units));
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        ArrayList<Step> steps = new ArrayList<>();
        for (int i = 1; i < stepsTableLayout.getChildCount(); i++) {
            TableRow row = (TableRow) stepsTableLayout.getChildAt(i);
            try {
                String stepText = ((EditText) row.getChildAt(1)).getText().toString();
                if (stepText.length() > 0) {
                    steps.add(new Step(stepText));
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        String name = ((EditText) findViewById(R.id.editorRecipeName)).getText().toString();
        String description = ((EditText) findViewById(R.id.editorDescription)).getText().toString();
        Double estimatedTime = Double.parseDouble(((EditText) findViewById(R.id.editorEstimatedTime)).getText().toString());

        RecipeDetailed recipe = new RecipeDetailed(0, name, User.currentUser.username, description, estimatedTime, ingredients, steps);
        SaveRecipeTask saveRecipeTask = new SaveRecipeTask(this);
        saveRecipeTask.execute(recipe);
    }


    private static class SaveRecipeTask extends SQLAsyncTask<RecipeDetailed, Void> {
        public SaveRecipeTask(Activity activity) {
            super(activity);
        }

        @Override
        protected Void sqlBackground(Connection connection, RecipeDetailed... recipes) throws SQLException {
            final RecipeDetailed recipe = recipes[0];
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Recipes (idUsers, name, description, estimatedTime) VALUES(?,?,?,?)");
            preparedStatement.setInt(1, User.currentUser.userId);
            preparedStatement.setString(2, recipe.name); // name
            preparedStatement.setString(3, recipe.description); // description
            preparedStatement.setDouble(4, recipe.estimatedTime); // estimatedTime
            preparedStatement.executeUpdate();

            preparedStatement = connection.prepareStatement("SELECT idRecipes FROM Recipes WHERE idUsers = ? AND name = ?");
            preparedStatement.setInt(1, User.currentUser.userId);
            preparedStatement.setString(2, recipe.name);
            ResultSet rs = preparedStatement.executeQuery();
            if (!rs.next()) {
                return null;
            }
            int recipeID = rs.getInt("idRecipes");

            for (Ingredient ingredient : recipe.ingredients) {
                preparedStatement = connection.prepareStatement("INSERT INTO Ingredients (idRecipes, name, quantity, unit) VALUES(?,?,?,?)");
                preparedStatement.setInt(1, recipeID);
                preparedStatement.setString(2, ingredient.name);
                preparedStatement.setDouble(3, ingredient.quantity);
                preparedStatement.setString(4, ingredient.units);
                preparedStatement.executeUpdate();
            }

            for (int i = 0; i < recipe.steps.size(); i++) {
                preparedStatement = connection.prepareStatement("INSERT INTO Steps (idRecipes, stepNum, stepText) VALUES(?,?,?)");
                preparedStatement.setInt(1, recipeID);
                preparedStatement.setInt(2, i);
                preparedStatement.setString(3, recipe.steps.get(i).instruction);
                preparedStatement.executeUpdate();
            }
            return null;
        }

        @Override
        protected void sqlPostExecute(Activity activity, Void aVoid) {
            activity.finish();
        }
    }


}
