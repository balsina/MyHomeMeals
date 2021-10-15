package com.example.tfm_mei.ui.recipelist;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import com.example.tfm_mei.ui.recipes.RecipeActivity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import static com.example.tfm_mei.ui.recipes.FilterActivity.PARAMETER;
import static com.example.tfm_mei.ui.recipes.FilterActivity.TYPE;

public class FetchInformationRecipeList extends AsyncTask<String, Void, String> {

    private RecipeListFragment recipe;

    public FetchInformationRecipeList (RecipeListFragment recipeListFragment) {
        this.recipe = recipeListFragment;
    }

    @Override
    protected String doInBackground(String... strings) {

        final String URL_BASE = "https://www.themealdb.com/api/json/v1/1/filter.php?";
        String URL_RECIPE_LIST;

        if (strings[0] != null) {
            switch (strings[0]) {
                case "category":
                    URL_RECIPE_LIST = URL_BASE + "c=" + strings[1];
                    recipe.title.setText("Search results: ");
                    break;
                case "area":
                    URL_RECIPE_LIST = URL_BASE + "a=" + strings[1];
                    recipe.title.setText("Search results: ");
                    break;
                case "ingredient":
                    URL_RECIPE_LIST = URL_BASE + "i=" + strings[1];
                    recipe.title.setText("Search results: ");
                    break;
                default:
                    URL_RECIPE_LIST = URL_BASE + "c=Miscellaneous";
                    break;
            }
        } else {
            URL_RECIPE_LIST = URL_BASE + "c=Miscellaneous";
        }

        try {

            URL urlRecipeList = new URL(URL_RECIPE_LIST);
            Log.i("URL", urlRecipeList.toString());
            HttpURLConnection connection = (HttpURLConnection) urlRecipeList.openConnection();
            InputStream inputStream = connection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            StringBuffer json = new StringBuffer(2048);

            String line="";
            while((line=bufferedReader.readLine())!=null)
                json.append(line).append("\n");
            bufferedReader.close();

            return json.toString();

        } catch (Exception e) {
            return null;
        }
    }

    @Override
    protected void onPostExecute(String result) {
        try {

            JSONObject jsonResult = new JSONObject(result);

            if (jsonResult.getString("meals").equals("null")) {
                recipe.title.setText("No results have been found for your search");
                recipe.progressDialog.dismiss();
                return;
            }

            JSONArray lista = jsonResult.getJSONArray("meals");
            List<RecipeListItem> l = new ArrayList<>();
            for (int i = 0; i < lista.length(); i++) {
                JSONObject a = lista.getJSONObject(i);
                l.add(new RecipeListItem(a.getString("strMeal"), a.getString("strMealThumb"), a.getString("idMeal")));

            }

            RecipeListAdapter listarecetas = new RecipeListAdapter(l, recipe.getContext());

            listarecetas.setOnItemClickListener((position, v) -> {
                Intent i = new Intent(recipe.getActivity(), RecipeActivity.class);
                RecipeListItem model = l.get(position);
                i.putExtra(TYPE, "id");
                i.putExtra(PARAMETER, model.id);
                recipe.startActivity(i);
            });

            recipe.recyclerView.setAdapter(listarecetas);
            recipe.progressDialog.dismiss();

        } catch (JSONException e) {
            e.printStackTrace();
            recipe.progressDialog.dismiss();
        }
    }
}

