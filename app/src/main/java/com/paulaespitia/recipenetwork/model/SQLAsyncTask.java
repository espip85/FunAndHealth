package com.paulaespitia.recipenetwork.model;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.sql.Connection;
import java.sql.SQLException;

@SuppressWarnings("all")
public abstract class SQLAsyncTask<T, S> extends AsyncTask<T, Void, S> {

    private final WeakReference<Activity> weakActivity;
    private boolean sqlException = false;

    public SQLAsyncTask(Activity activity) {
        weakActivity = new WeakReference<>(activity);
    }

    @Override
    protected S doInBackground(T... ts) {
        try (Connection connection = SQLHelper.getHelper().getConnection()) {
            return sqlBackground(connection, ts);
        } catch (SQLException e) {
            sqlException = true;
            e.printStackTrace();
        }
        return null;
    }

    protected abstract S sqlBackground(Connection connection, T... ts) throws SQLException;

    @Override
    protected void onPostExecute(S s) {
        super.onPostExecute(s);
        Activity activity = weakActivity.get();
        if (activity != null && !activity.isFinishing() && !activity.isDestroyed()) {
            if (sqlException) {
                Toast.makeText(activity.getApplicationContext(), "Unable to connect to the server", Toast.LENGTH_SHORT).show();
            } else {
                sqlPostExecute(activity, s);
            }
        }
    }

    protected abstract void sqlPostExecute(Activity activity, S s);
}