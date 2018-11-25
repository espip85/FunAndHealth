package com.paulaespitia.recipenetwork;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.paulaespitia.recipenetwork.model.Comment;
import com.paulaespitia.recipenetwork.model.Ingredient;
import com.paulaespitia.recipenetwork.model.RecipeDetailed;
import com.paulaespitia.recipenetwork.model.SQLAsyncTask;
import com.paulaespitia.recipenetwork.model.Step;
import com.paulaespitia.recipenetwork.model.User;

import java.lang.ref.WeakReference;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Locale;

public class RecipeActivity extends MenuActivity {

    RecipeDetailed recipe;

    Button favoriteRecipeButton;

    TextView recipeNameTextView;
    TextView recipeAuthorTextView;
    TextView estimatedTimeTextView;
    TextView recipeDescriptionTextView;
    TableLayout ingredientsTable;
    TableLayout stepsTable;
    LinearLayout newComment;
    EditText newCommentText;
    EditText newCommentRating;
    LinearLayout commentsList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_view);

        if (RecipeDetailed.recipeToView == null) {
            Toast.makeText(getApplicationContext(), "Unable to view recipe", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            recipe = RecipeDetailed.recipeToView;

            favoriteRecipeButton = findViewById(R.id.recipeViewFavorite);

            recipeNameTextView = findViewById(R.id.recipeViewName);
            recipeAuthorTextView = findViewById(R.id.recipeViewAuthor);
            estimatedTimeTextView = findViewById(R.id.recipeViewEstimatedTime);
            recipeDescriptionTextView = findViewById(R.id.recipeViewDescription);
            ingredientsTable = findViewById(R.id.recipeViewIngredientTable);
            stepsTable = findViewById(R.id.recipeViewStepsTable);
            newComment = findViewById(R.id.recipeViewNewComment);
            newCommentText = findViewById(R.id.recipeViewNewCommentText);
            newCommentRating = findViewById(R.id.recipeViewNewCommentRating);
            commentsList = findViewById(R.id.recipeViewCommentsList);

            recipeNameTextView.setText(recipe.name);
            recipeAuthorTextView.setText(String.format("Created by %s", recipe.author));
            estimatedTimeTextView.setText(String.format(Locale.US, "Estimated Time: %.2f minutes", recipe.estimatedTime));
            recipeDescriptionTextView.setText(recipe.description);

            CheckFavoriteTask checkFavoriteTask = new CheckFavoriteTask(this, favoriteRecipeButton);
            checkFavoriteTask.execute(recipe.id);

            GetIngredientsTask getIngredientsTask = new GetIngredientsTask(this, ingredientsTable);
            getIngredientsTask.execute(recipe.id);

            GetStepsTask getStepsTask = new GetStepsTask(this, stepsTable);
            getStepsTask.execute(recipe.id);

            GetCommentsTask getCommentsTask = new GetCommentsTask(this, commentsList);
            getCommentsTask.execute(recipe.id);
        }
    }

    public void submitComment(View view) {
        String idUsers = String.valueOf(User.currentUser.userId);
        String idRecipes = String.valueOf(recipe.id);
        String commentText = newCommentText.getText().toString();
        String commentRating = newCommentRating.getText().toString();
        SubmitCommentTask submitCommentTask = new SubmitCommentTask(this, newComment);
        submitCommentTask.execute(idUsers, idRecipes, commentText, commentRating);
    }

    public void viewAuthor(View view) {
        ViewAuthorTask viewAuthorTask = new ViewAuthorTask(this);
        viewAuthorTask.execute(recipeAuthorTextView.getText().toString().split(" ")[2]);
    }

    public void favorite(View view) {
        FavoriteTask favoriteTask = new FavoriteTask(this, favoriteRecipeButton);
        favoriteTask.execute(recipe.id);
    }

    private static class ViewAuthorTask extends SQLAsyncTask<String, Integer> {

        public ViewAuthorTask(Activity activity) {
            super(activity);
        }

        @Override
        protected Integer sqlBackground(Connection connection, String... strings) throws SQLException {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT idUsers FROM Users WHERE username=?");
            preparedStatement.setString(1, strings[0]); // author's username
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("idUsers");
            }
            return null;
        }

        @Override
        protected void sqlPostExecute(Activity activity, Integer integer) {
            Intent viewAuthorIntent = new Intent(activity, UserActivity.class);
            viewAuthorIntent.putExtra("idUsers", integer);
            activity.startActivity(viewAuthorIntent);
        }
    }

    private static class SubmitCommentTask extends SQLAsyncTask<String, Void> {

        WeakReference<LinearLayout> weakNewComment;

        public SubmitCommentTask(Activity activity, LinearLayout newCommment) {
            super(activity);
            weakNewComment = new WeakReference<>(newCommment);
        }

        @Override
        protected Void sqlBackground(Connection connection, String... strings) throws SQLException {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Comments(idUsers,idRecipes,comment,score) VALUES(?,?,?,?)");
            preparedStatement.setInt(1, Integer.parseInt(strings[0]));
            preparedStatement.setInt(2, Integer.parseInt(strings[1]));
            preparedStatement.setString(3, strings[2]);
            preparedStatement.setDouble(4, Double.parseDouble(strings[3]));
            preparedStatement.executeUpdate();
            return null;
        }

        @Override
        protected void sqlPostExecute(Activity activity, Void aVoid) {
            if (weakNewComment.get() != null) {
                weakNewComment.get().setVisibility(View.GONE);
            }
        }
    }

    private static class CheckFavoriteTask extends SQLAsyncTask<Integer, Boolean> {

        WeakReference<Button> weakFavoriteButton;

        public CheckFavoriteTask(Activity activity, Button favoriteButton) {
            super(activity);
            weakFavoriteButton = new WeakReference<>(favoriteButton);
        }

        @Override
        protected Boolean sqlBackground(Connection connection, Integer... integers) throws SQLException {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM FavoriteRecipes WHERE idUsers=? AND idRecipes=?");
            preparedStatement.setInt(1, User.currentUser.userId);
            preparedStatement.setInt(2, integers[0]);   // recipeID
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return true;
            }
            return false;
        }

        @Override
        protected void sqlPostExecute(Activity activity, Boolean aBoolean) {
            if (weakFavoriteButton.get() != null) {
                if (aBoolean) {
                    weakFavoriteButton.get().setEnabled(false);
                }
            }
        }
    }

    private static class FavoriteTask extends SQLAsyncTask<Integer, Void> {

        WeakReference<Button> weakFavoriteButton;

        public FavoriteTask(Activity activity, Button favoriteButton) {
            super(activity);
            weakFavoriteButton = new WeakReference<>(favoriteButton);
        }

        @Override
        protected Void sqlBackground(Connection connection, Integer... integers) throws SQLException {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO FavoriteRecipes(idUsers,idRecipes) VALUES (?,?)");
            preparedStatement.setInt(1, User.currentUser.userId);
            preparedStatement.setInt(2, integers[0]);   // recipeID
            preparedStatement.executeUpdate();
            return null;
        }

        @Override
        protected void sqlPostExecute(Activity activity, Void aVoid) {
            if (weakFavoriteButton.get() != null) {
                weakFavoriteButton.get().setEnabled(false);
            }
        }
    }

    private static class GetIngredientsTask extends SQLAsyncTask<Integer, ArrayList<Ingredient>> {

        WeakReference<TableLayout> weakIngredientsTable;

        public GetIngredientsTask(Activity activity, TableLayout ingredientsTable) {
            super(activity);
            weakIngredientsTable = new WeakReference<>(ingredientsTable);
        }

        @Override
        protected ArrayList<Ingredient> sqlBackground(Connection connection, Integer... integers) throws SQLException {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT name,quantity,unit FROM Ingredients WHERE idRecipes=?");
            preparedStatement.setInt(1, integers[0]);   // recipeID
            ResultSet resultSet = preparedStatement.executeQuery();
            ArrayList<Ingredient> ingredients = new ArrayList<>();
            while (resultSet.next()) {
                String name = resultSet.getString("name");
                Double quantity = resultSet.getDouble("quantity");
                String unit = resultSet.getString("unit");
                ingredients.add(new Ingredient(name, quantity, unit));
            }
            return ingredients;
        }

        @Override
        protected void sqlPostExecute(Activity activity, ArrayList<Ingredient> ingredients) {
            if (weakIngredientsTable.get() != null) {
                LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                for (Ingredient ingredient : ingredients) {
                    final View ingredientRow = inflater.inflate(R.layout.recipe_view_ingredient, null);

                    TextView recipeViewIngredientQuantity = ingredientRow.findViewById(R.id.recipeViewIngredientQuantity);
                    recipeViewIngredientQuantity.setText(String.format(Locale.US, "%.2f", ingredient.quantity));

                    TextView recipeViewIngredientUnits = ingredientRow.findViewById(R.id.recipeViewIngredientUnits);
                    recipeViewIngredientUnits.setText(ingredient.units);

                    TextView recipeViewIngredientName = ingredientRow.findViewById(R.id.recipeViewIngredientName);
                    recipeViewIngredientName.setText(ingredient.name);

                    weakIngredientsTable.get().addView(ingredientRow);
                }
            }
        }
    }

    private static class GetStepsTask extends SQLAsyncTask<Integer, ArrayList<Step>> {

        WeakReference<TableLayout> weakStepsTable;

        public GetStepsTask(Activity activity, TableLayout stepsTable) {
            super(activity);
            weakStepsTable = new WeakReference<>(stepsTable);
        }

        @Override
        protected ArrayList<Step> sqlBackground(Connection connection, Integer... integers) throws SQLException {
            PreparedStatement preparedStatement = connection.prepareStatement("Select stepNum,stepText FROM Steps WHERE idRecipes=? ORDER BY stepNum");
            preparedStatement.setInt(1, integers[0]);   // recipeID
            ResultSet resultSet = preparedStatement.executeQuery();
            ArrayList<Step> steps = new ArrayList<>();
            while (resultSet.next()) {
                String instruction = resultSet.getString("stepText");
                steps.add(new Step(instruction));
            }
            return steps;
        }

        @Override
        protected void sqlPostExecute(Activity activity, ArrayList<Step> steps) {
            if (weakStepsTable.get() != null) {
                LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                for (int i = 0 ; i < steps.size(); i++) {
                    final View stepRow = inflater.inflate(R.layout.recipe_view_step, null);

                    TextView recipeViewStepNumber = stepRow.findViewById(R.id.recipeViewStepNumber);
                    recipeViewStepNumber.setText(String.format(Locale.US, "%d) ", i+1));

                    TextView recipeViewStepInstruction = stepRow.findViewById(R.id.recipeViewStepInstruction);
                    recipeViewStepInstruction.setText(steps.get(i).instruction);

                    weakStepsTable.get().addView(stepRow);
                }
            }
        }
    }

    private static class GetCommentsTask extends SQLAsyncTask<Integer, ArrayList<Comment>> {

        WeakReference<LinearLayout> weakCommentsList;

        public GetCommentsTask(Activity activity, LinearLayout commentsList) {
            super(activity);
            weakCommentsList = new WeakReference<>(commentsList);
        }

        @Override
        protected ArrayList<Comment> sqlBackground(Connection connection, Integer... integers) throws SQLException {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT Users.username,Comments.comment,Comments.score FROM Users,Comments WHERE Users.idUsers=Comments.idUsers AND idRecipes=?");
            preparedStatement.setInt(1, integers[0]);   // recipeID
            ResultSet resultSet = preparedStatement.executeQuery();
            ArrayList<Comment> comments = new ArrayList<>();
            while (resultSet.next()) {
                String username = resultSet.getString("username");
                String comment = resultSet.getString("comment");
                Double score = resultSet.getDouble("score");
                comments.add(new Comment(username, comment, score));
            }
            return comments;
        }

        @Override
        protected void sqlPostExecute(Activity activity, ArrayList<Comment> comments) {
            if (weakCommentsList.get() != null) {
                LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                for (Comment comment : comments) {
                    final View commentView = inflater.inflate(R.layout.recipe_view_comment, null);

                    TextView recipeViewCommentUsername = commentView.findViewById(R.id.recipeViewCommentUsername);
                    recipeViewCommentUsername.setText(comment.username);

                    TextView recipeViewCommentRating = commentView.findViewById(R.id.recipeViewCommentRating);
                    recipeViewCommentRating.setText(String.format(Locale.US, "%.1f", comment.score));

                    TextView recipeViewCommentText = commentView.findViewById(R.id.recipeViewCommentText);
                    recipeViewCommentText.setText(comment.text);

                    weakCommentsList.get().addView(commentView);
                }
            }
        }
    }
}
