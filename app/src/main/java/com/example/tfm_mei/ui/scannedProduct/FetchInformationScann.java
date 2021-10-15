package com.example.tfm_mei.ui.scannedProduct;

import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.example.tfm_mei.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.example.tfm_mei.R.drawable.ic_baseline_circle_24;

public class FetchInformationScann extends AsyncTask<String, Void, String> {

    private ScannedProduct scannedProduct;

    String name, brand, score, ingredients, energy_kcal, fat, salt, saturated_fat, sugar, url;

    public FetchInformationScann (ScannedProduct scannedProduct){
        this.scannedProduct = scannedProduct;
    }

    @Override
    protected String doInBackground(String... strings) {

        final String URL_SCANN = "https://world.openfoodfacts.org/api/v0/product/";
        String URL_SCANN_PRODUCT = URL_SCANN + strings[0];

        try {
            URL urlProductsAPI = new URL(URL_SCANN_PRODUCT);
            HttpURLConnection connection = (HttpURLConnection) urlProductsAPI.openConnection();
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
            Log.i("FetchData", result);

            JSONObject jsonResult = new JSONObject(result);
            JSONObject infogen  = jsonResult.getJSONObject("product");
            if (!infogen.has("product_name") ||
                    infogen.getString("product_name") == null ||
                    infogen.getString("product_name").equals("")){
                name = "Unknown";
            } else { name = infogen.getString("product_name"); }

            if (!infogen.has("brands") || infogen.getString("brands").equals(null) || infogen.getString("brands").equals("")){
                brand = "Unknown";
            } else { brand = infogen.getString("brands"); }

            if (!infogen.has("nutriscore_score") || infogen.getString("nutriscore_score").equals(null) || infogen.getString("nutriscore_score").equals("")){
                score = "Unknown";
            } else { score = infogen.getString("nutriscore_score"); }

            if (!infogen.has("ingredients_text_es") || infogen.getString("ingredients_text_es").equals(null) || infogen.getString("ingredients_text_es").equals("") ){
                ingredients = "Unknown";
            } else { ingredients = infogen.getString("ingredients_text_en"); }

            JSONObject energy = infogen.getJSONObject("nutriments");

            if ( !energy.has("energy-kcal") || energy.getString("energy-kcal").equals(null) || energy.getString("energy-kcal").equals("")){
                energy_kcal = "Unknown";
            } else { energy_kcal = energy.getString("energy-kcal");}

            JSONObject nutrients = infogen.getJSONObject("nutrient_levels");

            if (!nutrients.has("fat") || nutrients.getString("fat").equals(null) || nutrients.getString("fat").equals("")){
                fat = "Unknown";
            } else { fat = nutrients.getString("fat");}

            if (!nutrients.has("salt") || nutrients.getString("salt").equals(null) || nutrients.getString("salt").equals("")){
                salt = "Unknown";
            } else { salt = nutrients.getString("salt");}

            if (!nutrients.has("saturated-fat") || nutrients.getString("saturated-fat").equals(null) || nutrients.getString("saturated-fat").equals("")){
                saturated_fat = "Unknown";
            } else { saturated_fat = nutrients.getString("saturated-fat");}

            if (!nutrients.has("sugars") || nutrients.getString("sugars").equals(null) || nutrients.getString("sugars").equals("")){
                sugar = "Unknown";
            } else { sugar = nutrients.getString("sugars");}

            if (!infogen.has("image_front_small_url") || infogen.getString("image_front_small_url").equals(null) || infogen.getString("image_front_small_url").equals("")){
                url = "Unknown";
            } else { url = infogen.getString("image_front_small_url");}

            if (!score.equals("Unknown")){
                int fscore = Integer.parseInt(score);

                if (fscore<=4){
                    scannedProduct.sicon = scannedProduct.findViewById(R.id.product_score_icon);
                    scannedProduct.sicon.setImageResource(ic_baseline_circle_24);
                    scannedProduct.sicon.setColorFilter(Color.parseColor("#F44336"));
                } else if (4<fscore && fscore<=6){
                    scannedProduct.sicon = scannedProduct.findViewById(R.id.product_score_icon);
                    scannedProduct.sicon.setImageResource(ic_baseline_circle_24);
                    scannedProduct.sicon.setColorFilter(Color.parseColor("#FF9800"));
                } else if (fscore>6){
                    scannedProduct.sicon = scannedProduct.findViewById(R.id.product_score_icon);
                    scannedProduct.sicon.setImageResource(ic_baseline_circle_24);
                    scannedProduct.sicon.setColorFilter(Color.parseColor("#8BC34A"));
                }
            } else {
                scannedProduct.sicon = scannedProduct.findViewById(R.id.product_score_icon);
                scannedProduct.sicon.setImageResource(ic_baseline_circle_24);
                scannedProduct.sicon.setColorFilter(Color.parseColor("#FFFFFFFF"));
            }

            Log.i("FetchData", name);

            scannedProduct.sproduct = scannedProduct.findViewById(R.id.product_name);
            scannedProduct.sproduct.setText(name);

            scannedProduct.smarca = scannedProduct.findViewById(R.id.product_brand);
            scannedProduct.smarca.setText(brand);

            scannedProduct.snota = scannedProduct.findViewById(R.id.product_grade);
            scannedProduct.snota.setText(score + "/10");

            scannedProduct.singredientes = scannedProduct.findViewById(R.id.product_ingredients);
            scannedProduct.singredientes.setText(ingredients);

            scannedProduct.sgrasa = scannedProduct.findViewById(R.id.product_fat);
            scannedProduct.sgrasa.setText(fat);

            scannedProduct.ssal = scannedProduct.findViewById(R.id.product_salt);
            scannedProduct.ssal.setText(salt);

            scannedProduct.sgrasasat = scannedProduct.findViewById(R.id.product_satfat);
            scannedProduct.sgrasasat.setText(saturated_fat);

            scannedProduct.sazucar = scannedProduct.findViewById(R.id.product_sugar);
            scannedProduct.sazucar.setText(sugar);

            scannedProduct.senergy = scannedProduct.findViewById(R.id.product_energetic_val);
            scannedProduct.senergy.setText(energy_kcal + " kcal");

            scannedProduct.simagen = scannedProduct.findViewById(R.id.product_image);
            Glide.with(scannedProduct.getApplicationContext()).load(url).into(scannedProduct.simagen);

            scannedProduct.progressDialog.dismiss();

        } catch (JSONException e) {
            e.printStackTrace();
            scannedProduct.progressDialog.dismiss();

        }
    }
}
