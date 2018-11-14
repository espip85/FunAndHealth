package com.paulaespitia.recipenetwork;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.mysql.cj.xdevapi.Table;

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
}
