package com.example.cpitcel.ingredientalert;

//import android.Manifest;
//import android.content.Context;
import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
//import android.os.Environment;
//import android.provider.MediaStore;
//import android.support.annotation.NonNull;
//import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
//import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

//import com.google.android.gms.vision.Frame;
//import com.google.android.gms.vision.barcode.Barcode;
//import com.google.android.gms.vision.barcode.BarcodeDetector;

public class MainActivity extends AppCompatActivity {
    private TextView scanResult;
    private TextView searchResult;
   // private BarcodeDetector detector;
    private Uri imgDetect;
    private String theUPC = "";
    private IntentIntegrator integrator;
    private static final String TAG = "Main";
    //private static final int Req_Write_Permissions = 20;
    //private static final int Req_Take_Photo = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button ScanButton = (Button) findViewById(R.id.scanButton);
        Button SearchButton = (Button) findViewById(R.id.searchButton);
        scanResult = (TextView) findViewById(R.id.result);
        searchResult = (TextView) findViewById(R.id.searchResult);

        if (savedInstanceState != null){
            imgDetect = Uri.parse(savedInstanceState.getString("uri"));
            scanResult.setText(savedInstanceState.getString("result"));
        }

        /**ScanButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, Req_Write_Permissions);
            }
        });

         SearchButton.setOnClickListener(new View.OnClickListener(){
        public void onClick(View v) {
        getSearch(v);
        }
        });

        detector = new BarcodeDetector.Builder(getApplicationContext())
                .setBarcodeFormats(Barcode.UPC_A | Barcode.UPC_E)
                .build();
        if (!detector.isOperational()) {
            scanResult.setText("Could not setup detector");
            return;
        }*/
    }
/**
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (imgDetect != null) {
            outState.putString("uri", imgDetect.toString());
            outState.putString("result", scanResult.getText().toString());
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        super.onRequestPermissionsResult(requestCode, permissions,grantResults);
        switch (requestCode){
            case Req_Write_Permissions:
                if (grantResults.length> 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    getBarcode();
                } else{
                    Toast.makeText(MainActivity.this, "Denied Permissions! WHY!", Toast.LENGTH_LONG).show();
                }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Req_Take_Photo && resultCode == RESULT_OK) {
            launchMediaScanIntent();
            try {
                Bitmap bitImage = decodeBitmapUri(this, imgDetect);
                if (detector.isOperational() && bitImage != null){
                    Frame frame = new Frame.Builder().setBitmap(bitImage).build();
                    SparseArray<Barcode> allResults = detector.detect(frame);
                    if (allResults.size() == 1){
                        scanResult.setText(allResults.valueAt(0).displayValue);
                        getSearchandResults(allResults.valueAt(0).displayValue);
                    } else {
                        scanResult.setText("Multiple or Zero Results, try again");
                    }
                } else {
                    scanResult.setText("Detector Setup Issue");
                }
            } catch (Exception e) {
                Toast.makeText(this, "Image Load Issue", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void launchMediaScanIntent(){
        Intent mediaScan = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        mediaScan.setData(imgDetect);
        this.sendBroadcast(mediaScan);
    }

    private void getBarcode() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File pic = new File(Environment.getExternalStorageDirectory(), "pic.jpg");
        imgDetect = Uri.fromFile(pic);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imgDetect);
        startActivityForResult(intent, Req_Take_Photo);
    }

    private Bitmap decodeBitmapUri(Context ctx, Uri uri) throws FileNotFoundException {
        int targetW = 600;
        int targetH = 600;
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(ctx.getContentResolver().openInputStream(uri), null, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        int scaleFactor = Math.min(photoW / targetW, photoH / targetH);
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;

        return BitmapFactory.decodeStream(ctx.getContentResolver()
                .openInputStream(uri), null, bmOptions);
    }

    public void getSearch(View view){
        Log.i("MainActivity.getSearch", "inside");
        //String testURL = "http://api.nal.usda.gov/ndb/search/?format=json&q=070640034086&max=1&offset=0&api_key=DEMO_KEY";
        //ONION UPC:    041500220208 - french's french fried onions
        //NO ONION UPC: 070640034086
        //Cherry Coke: 49000036756
        GetAPIData apiData = new GetAPIData("735022005008");
        apiData.execute();
        Log.i("MainActivity.getSearch", "Done");
    }

    private void getSearchandResults(String newUPC){
        GetAPIData apiData = new GetAPIData(newUPC);
        apiData.execute();
        Log.i("MA.getSearchandResults", "Done with " + newUPC);
    }
 **/

    public void doScan(View view) {
        Log.i(TAG + "doScanSearch", "starting");
        String newUPC = "";
        //ONION UPC:    041500220208 - french's french fried onions
        //NO ONION UPC: 070640034086
        //Cherry Coke: 49000036756

        //TODO: initialize and setup the barcode scanner
        integrator = new IntentIntegrator(this);
        integrator.setCaptureActivity(AnyOrientationCaptureActivity.class);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
        integrator.setPrompt("Scan a Barcode");
        integrator.setOrientationLocked(false);
        integrator.setBeepEnabled(false);
        integrator.setCameraId(0);
        integrator.initiateScan();

        //Lookup the Data part
        Log.i(TAG + "doScanSearch", "Done scan, start search");
        //GetAPIData apiData = new GetAPIData(newUPC);
        //apiData.execute();
        Log.i(TAG + "doScanSearch", "Done with " + newUPC);
    }

    public void doSearch(View view) {
        //theUPC = scanResult.getText().toString();
        theUPC = "49000036756";
        if (theUPC == null || theUPC == "") {
            searchResult.setText("No UPC to Lookup >" + theUPC + "<");
        } else {
            GetAPIData apiData = new GetAPIData(theUPC);
            apiData.execute();
        }
    }

    public void doScanSearch(View view){
        doScan(view);
        //doSearch();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
                scanResult.setText("Cancelled");
                Log.i(TAG, "Cancelled");
            } else {
                Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
                scanResult.setText(result.getContents());
                Log.i(TAG, "Scan Results: " + result.getContents());
                theUPC = result.getContents();
                theUPC = "00001";
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
            Log.i(TAG, "No Scan Results");
            Toast.makeText(this, "No Results", Toast.LENGTH_LONG).show();
        }
    }

    private class GetAPIData extends AsyncTask<Void, Void, String> {
        private String TAG = "GetAPIData";
        private String theResult;
        private String theBarcode = "";
        //private String theNDBNO = "";
        private String theNutrList = "";
        private String theItemName = "";
        private String deathBy = "ONION";
        private boolean hasDeath = false;
        private boolean hasNoData = false;
        //URL = P1 + Barcode + P3 + APPID + P5 + API_KEY
        private final static String URLP1 = "https://api.nutritionix.com/v1_1/item?upc=";
        private final static String URLP3 = "&appId=";
        private final static String theAppID = BuildConfig.theAppID;
        private final static String URLP5 = "&appKey=";
        private final static String theAPI_KEY = BuildConfig.theAPIKey;
        //private final static String URL1P1 = "http://api.nal.usda.gov/ndb/search/?format=json&q=";
        //private final static String URL1P3 = "&max=1&offset=0&api_key=";
        //private final static String URL2P1 = "https://api.nal.usda.gov/ndb/reports/?ndbno=";
        //private final static String URL2P3 = "&type=f&format=json&api_key=";

        public GetAPIData(String newBarcode){theBarcode = newBarcode;}

        private String GetSearchURL(){
            return URLP1 + theBarcode + URLP3 + theAppID + URLP5 + theAPI_KEY;
        }

        //DEPRECATED
        //private String GetNutrURL() {
        //return URL2P1 + theNDBNO + URL2P3 + theAPI_KEY;
        //}

        public String GetResult(){return theResult;}

        protected void onPreExecute(){
            //Log.i("GetAPIData.onPreExecute", "Starting Exectution with URL: " + theURL);
            searchResult.setText("Getting Data...");
        }

        @Override
        protected String doInBackground(Void... params) {
            String theURL = GetSearchURL();
            String theSearchResult = "";
            String theNutrResult = "";
            if (theURL == "") {
                Log.i(TAG, "NO URL, exiting");
                return "No URL";
            }

            try {
                //TODO: COMPLETE REWRITE
                theSearchResult = GetURLData(theURL);
                if(theSearchResult == null || theSearchResult.isEmpty()) {
                    Log.i(TAG, "No Search Results");
                }

                //TODO: Have the Data, parse out the JSON
                if (getNutrFacts(theSearchResult) == Boolean.FALSE) {
                    Log.i(TAG, "No Nutrition Data");
                    hasNoData = Boolean.TRUE;
                    return "No Nutrition Data";
                }


                //See if any search results from USDA for UPC/Barcode - DEPRECATED
                /*
                if(theSearchResult.isEmpty() || theSearchResult == null) {
                    Log.i(TAG, "No USDA Barcode Found");
                    hasNoData = true;
                    return "No USDA Barcode Value";
                }
                //Should have JSON, see if has NDBNO
                if (getNDBNO(theSearchResult) == false) {
                    Log.i(TAG, "No USDA NDBNO Found");
                    hasNoData = true;
                    return "No USDA NDBNO Found";
                }
                //Have NDBNO, use it to get more data
                String theNutrURL = GetNutrURL();
                theNutrResult = GetURLData(theNutrURL);

                //Have Nutrition JSON, parse it out
                if (getNutrList(theNutrResult) == false) {
                    Log.i(TAG, "No USDA Nutrition Data found");
                    //hasNoUSDAData = true;
                    return "No Nutrition Data";
                }
                */
                if (getHasDeath()){
                    return "!!!ONIONS FOUND!!!";
                }

                return "No Onions";
            } catch (Exception e) {
                Log.e(TAG, "Error with getting Search Result data: ", e);
            }
            return null;
        }

        protected void onPostExecute(String response){
            super.onPostExecute(response);
            Log.i(TAG, "Execution Ended with result: " + response.toString());
            theResult = response.toString();
            searchResult.setText(response.toString());
            scanResult.setText(theItemName);

        }

        private String GetURLData(String newURL) {
            try {
                Log.i(TAG, "Using URL: " + newURL);
                URL theURL = new URL(newURL);
                HttpURLConnection urlConnection = (HttpURLConnection) theURL.openConnection();
                try{
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line);
                        Log.d(TAG, "Line: " + line);
                    }
                    return stringBuilder.toString();
                } finally {
                    urlConnection.disconnect();
                    Log.i(TAG, "Exiting GAD");
                }

            } catch (Exception e) {
                Log.e(TAG, "Issue in GAD.GetURLData: ", e);
            }
            return null;
        }
        /*
        private Boolean getNDBNO(String newJSON){
            if (newJSON != null && !newJSON.isEmpty()) {
                try {
                    JSONObject jsonObject = new JSONObject(newJSON);
                    //Log.i("getNDBNO.jsonObject", jsonObject.toString());
                    JSONArray itemArray = jsonObject.getJSONObject("list").getJSONArray("item");
                    JSONObject itemObject = itemArray.getJSONObject(0);
                    String ndbno = itemObject.getString("ndbno");
                    //Log.i("getNDBNO.ndbno", ndbno);
                    theNDBNO = ndbno;
                    return true;
                } catch(final JSONException e) {
                    Log.e(TAG, "Error with JSON", e);
                }
            }
            return false;
        } */

        private Boolean getNutrFacts (String newJSON){
            try {
                JSONObject jsonObject = new JSONObject((newJSON));
                String strNutrList = jsonObject.getString("nf_ingredient_statement");
                Log.i(TAG, "Nutrition List: " + strNutrList);
                theNutrList = strNutrList;
                String strItemName = jsonObject.getString("item_name");
                Log.i(TAG, "Item Name: " + strItemName);
                theItemName = strItemName;
                return true;
            } catch (JSONException e) {
                Log.e(TAG, "Error with JSON: ", e);
            }
            return false;
        }

        /*
        private Boolean getNutrList (String newJSON){
            //Get the JSON object with ingredients: report, food, img, decs

            try {
                JSONObject jsonObject = new JSONObject(newJSON);
                JSONObject jsonNutrList = jsonObject.getJSONObject("report").getJSONObject("food")
                        .getJSONObject("ing");
                String strNutrList = jsonNutrList.getString("desc");
                Log.i(TAG, "NutritionString: " + strNutrList);
                theNutrList = strNutrList;
                return true;
            } catch (JSONException e ){
                Log.e(TAG, "Error with JSON", e);
            }
            return false;
        } */

        private Boolean getHasDeath(){
            if (theNutrList.toUpperCase().contains("ONION")){
                hasDeath = true;
                return true;
            }

            //TODO: If so, break the description apart into an arraylist
            //TODO: Check the position of the Onion
            //TODO: Check for "Contains" and see if it is before or after "Onion"
            return false;
        }
    }


}
