package com.paulaespitia.recipenetwork;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.paulaespitia.recipenetwork.model.Recipe;
import com.paulaespitia.recipenetwork.model.RecipeID;
import com.paulaespitia.recipenetwork.model.SQLAsyncTask;
import com.paulaespitia.recipenetwork.model.SQLHelper;
import com.paulaespitia.recipenetwork.model.User;

import java.lang.ref.WeakReference;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MyRecipesActivity extends Activity {

    LinearLayout myRecipesListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_recipes);

        myRecipesListView = findViewById(R.id.myRecipesList);

        GetMyRecipesTask getMyRecipesTask = new GetMyRecipesTask(this, myRecipesListView);
        getMyRecipesTask.execute();
    }

    public void createNewRecipe(View view) {
        Intent editorIntent = new Intent(this, EditorActivity.class);
        startActivity(editorIntent);
    }

    private static class GetMyRecipesTask extends SQLAsyncTask<Void, List<RecipeID>> {

        final WeakReference<LinearLayout> weakMyRecipesList;

        public GetMyRecipesTask(Activity activity, LinearLayout myRecipesList) {
            super(activity);
            weakMyRecipesList = new WeakReference<>(myRecipesList);
        }

        @Override
        protected List<RecipeID> sqlBackground(Connection connection, Void... voids) throws SQLException {
            List<RecipeID> recipeIDs = new LinkedList<>();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT idRecipes,name FROM Recipes WHERE idUsers=?");
            preparedStatement.setInt(1, User.currentUser.userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                recipeIDs.add(new RecipeID(resultSet.getInt("idRecipes"), resultSet.getString("name"), ""));
            }
            return recipeIDs;
        }

        @Override
        protected void sqlPostExecute(Activity activity, List<RecipeID> recipeIDs) {
            if (weakMyRecipesList.get() != null) {
                LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                for (RecipeID recipeID : recipeIDs) {
                    final View recipeView = inflater.inflate(R.layout.my_recipes_list_item, null);
                    recipeView.setTag(recipeID.recipeID);

                    TextView nameTextView = recipeView.findViewById(R.id.myRecipesListItemName);
                    nameTextView.setText(recipeID.name);

                    Button modifyButton = recipeView.findViewById(R.id.myRecipesListItemModify);
                    modifyButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // TODO: Implement this
                        }
                    });

                    Button deleteButton = recipeView.findViewById(R.id.myRecipesListItemDelete);
                    deleteButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // TODO: Implement this
                        }
                    });

                    weakMyRecipesList.get().addView(recipeView);
                }
            }
        }
    }
}
