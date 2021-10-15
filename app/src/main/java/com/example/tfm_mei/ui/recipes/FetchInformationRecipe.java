package com.example.tfm_mei.ui.recipes;

import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.example.tfm_mei.R;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;

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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FetchInformationRecipe extends AsyncTask<String, Void, String> {

    private RecipeActivity recipe;

    public FetchInformationRecipe (RecipeActivity recipeActivity){
        this.recipe = recipeActivity;
    }

    @Override
    protected String doInBackground(String... strings) {

        final String URL_BASE="https://www.themealdb.com/api/json/v1/1/";
        String URL_RECIPE;

        if (strings[0].equals("id")){
            URL_RECIPE = URL_BASE + "lookup.php?i=" + strings[1];
        } else if (strings[0].equals("search")) {
            URL_RECIPE = URL_BASE + "search.php?s=" + strings[1];
        }else {
            URL_RECIPE = URL_BASE;
        }

        try {
            URL urlRecipe = new URL(URL_RECIPE);
            HttpURLConnection connection = (HttpURLConnection) urlRecipe.openConnection();
            InputStream inputStream = connection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            StringBuffer json = new StringBuffer(2048);

            String line="";
            while((line=bufferedReader.readLine())!=null)
                json.append(line).append("\n");
            bufferedReader.close();

            return json.toString();

        } catch(Exception e){
            return null;
        }
    }

    @Override
    protected void onPostExecute(String result) {
        try {

            JSONObject jsonResult = new JSONObject(result);
            JSONArray mealsArray  = jsonResult.getJSONArray("meals");
            JSONObject meal = mealsArray.getJSONObject(0);
            String name = meal.getString("strMeal");
            String area = meal.getString("strArea");
            String instructions = meal.getString("strInstructions");
            String urlfoto = meal.getString("strMealThumb");
            String urlvideo = meal.getString("strYoutube");

            List<Ingrediente> ingredientes = new ArrayList<>();

            int i = 1;

            while (meal.getString("strIngredient" + i) != null && !meal.getString("strIngredient" + i).equals("")){
                ingredientes.add(new Ingrediente(meal.getString("strIngredient" + i), meal.getString("strMeasure" + i)));
                i++;
            }

            recipe.nombre = recipe.findViewById(R.id.result_recipe_name);

            recipe.recyclerView.setHasFixedSize(true);
            recipe.recyclerView.setLayoutManager(new LinearLayoutManager(recipe.getApplicationContext()));
            recipe.recyclerView.setAdapter(new RecipeAdapter(ingredientes));

            recipe.area = recipe.findViewById(R.id.result_recipe_area);
            recipe.area.setText(area);

            recipe.instrucciones = recipe.findViewById(R.id.result_recipe_instructions);
            recipe.instrucciones.setText(instructions);

            recipe.nombre = recipe.findViewById(R.id.result_recipe_name);
            recipe.nombre.setText(name);

            recipe.foto = recipe.findViewById(R.id.result_recipe_image);
            Glide.with(recipe.getApplicationContext()).load(urlfoto).into(recipe.foto);

            recipe.video = recipe.findViewById(R.id.result_recipe_video);
            recipe.getLifecycle().addObserver(recipe.video);
            recipe.video.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                @Override
                public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                    String videoid = extractYoutubeId(urlvideo);
                    youTubePlayer.cueVideo(videoid, 0);
                }
            });

            recipe.progressDialog.dismiss();

        } catch (JSONException e) {
            e.printStackTrace();
            recipe.progressDialog.dismiss();
        }
    }

    public static String extractYoutubeId (String videoUrl) {
        String regex = "v=([^\\s&#]*)";
        final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
        final Matcher matcher = pattern.matcher(videoUrl);
        if(matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

}