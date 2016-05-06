package com.example.eliane.moore;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eliane.moore.R;
import com.example.eliane.moore.adapter.ListProductAdapter;
import com.example.eliane.moore.database.DatabaseHelper;
import com.example.eliane.moore.database.WebRequest;
import com.example.eliane.moore.model.Product;
import com.example.eliane.moore.status.AppStatus;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    Context context = this;
    MediaPlayer mp;

    private ListView lvProduct;
    private ListProductAdapter adapter;
    private List<Product> mProductList;
    private DatabaseHelper mDBHelper;
    DatabaseHelper myDb;
    //Button
    Button bsearch;
    TextView edtSeach2;
    EditText inputSearch;
    String etSearch1;
    private ListView lv ;
    public ImageButton imgbt;
    private Button bt;
    // URL to get contacts JSON
    // private static String url1 = "http://elianebirba.org/Tutorial/getmooredata.php";
    private static String url1="http://elianebirba.org/Tutorial/getmooredata.php?etSearch=";
    private static String url="";
    private static String req="";
    // JSON Node names
    private static final String TAG_STUDENTINFO = "data";
    private static final String TAG_ID = "id";
    private static final String TAG_FRANCAIS = "francais";
    private static final String TAG_MOORE = "moore";
    private static final String TAG_SUCCESS = "success";
    //private static final String TAG_GENDER = "gender";
    private static final String TAG_PHONE = "etSearch";
   /* private static final String TAG_PHONE_MOBILE = "mobile";
    private static final String TAG_PHONE_HOME = "home";*/

    public void   getdata() {

        adapter = new ListProductAdapter(this, mProductList);
        //Set adapter for listview
        lv.setAdapter(adapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myDb= new DatabaseHelper(this);
        //bsearch=(Button)findViewById(R.id.bsearch);
        lv=(ListView)findViewById(R.id.listview_product);
        edtSeach2=(TextView)findViewById(R.id.text2);
        inputSearch = (EditText) findViewById(R.id.edtSeach);
        req="?etSearch=" ;
        //url1+=req;
        url=url1;
        if (AppStatus.getInstance(this).isOnline()) {


            inputSearch.addTextChangedListener(new TextWatcher() {

                @Override
                public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
url=url1;

                    url+=cs;

                    new GetData().execute();

                }

                @Override
                public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                              int arg3) {


                }

                @Override
                public void afterTextChanged(Editable arg0) {

                }
            });

        } else {
            mDBHelper = new DatabaseHelper(this);

            //Check exists database
            File database = getApplicationContext().getDatabasePath(DatabaseHelper.DBNAME);
            if(false == database.exists()) {
                mDBHelper.getReadableDatabase();
                //Copy db
                if(copyDatabase(this)) {
                    Toast.makeText(this, "Copy database succes", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Copy data error", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            inputSearch.addTextChangedListener(new TextWatcher() {

                @Override
                public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {

                    mProductList = mDBHelper.getListProduct(cs);
                    getdata();

                }

                @Override
                public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                              int arg3) {

                }

                @Override
                public void afterTextChanged(Editable arg0) {

                }
            });

        }


    }
    /**
     * Async task class to get json by making HTTP call
     */
    private class GetData extends AsyncTask<Void, Void, Void> {
        private MediaPlayer mediaPlayer;
        private boolean playFirst = true;
        // Hashmap for ListView
        ArrayList<HashMap<String, String>> contenairList;
        //ProgressDialog pDialog;

        //  @Override
        /*protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }*/

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            WebRequest webreq = new WebRequest();
            HashMap<String, String> params=new HashMap<String, String>();
            params.put(TAG_PHONE,etSearch1);
            // Making a request to url and getting response
            // url+=req;
            String jsonStr = webreq.makeWebServiceCall(url, WebRequest.GET);


            Log.d("Response: ", "> " + jsonStr);

            contenairList = ParseJSON(jsonStr);

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            /*if (pDialog.isShowing())
                pDialog.dismiss();
            /**
             * Updating parsed JSON data into ListView
             * */

            ListAdapter adapter = new SimpleAdapter(
                    MainActivity.this, contenairList,
                    R.layout.item_listview, new String[]{TAG_FRANCAIS, TAG_MOORE
            }, new int[]{R.id.tv_product_name,
                    R.id.tv_product_description});
            View v = View.inflate(context, R.layout.item_listview, null);
            ImageButton imgbt=(ImageButton)v.findViewById(R.id.imbt);
          //  String m=mProductList.get(position).getMusic();
            mediaPlayer = MediaPlayer.create(context,R.raw.kalimba);
            Resources res = context.getResources();
            int soundId = res.getIdentifier("music1", "raw", context.getPackageName());
            // playSound(mediaPlayer, soundId);
            mediaPlayer = MediaPlayer.create(context,soundId);
            // ImageButton imbt =(ImageButton)viewg.findViewById(R.id.imbt);
            imgbt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(true == playFirst) {
                        edtSeach2.setText("eliane");
                        //mediaPlayer.start();
                        playFirst=false;
                    }
                    else {
                       // mediaPlayer.stop();
                        // mediaPlayer.release();
                        playFirst=true;
                    }
                }

            });

          //  adapter = new ListProductAdapter(this, contenairList);
            lv.setAdapter(adapter);

        }


    }

    private ArrayList<HashMap<String, String>> ParseJSON(String json) {
        if (json != null) {
            try {


                // Hashmap for ListView
                ArrayList<HashMap<String, String>> studentList = new ArrayList<HashMap<String, String>>();

                JSONObject jsonObj = new JSONObject(json);
                int success = jsonObj.getInt(TAG_SUCCESS);
                if(success==1){
                    // Getting JSON Array node
                    JSONArray students = jsonObj.getJSONArray(TAG_STUDENTINFO);

                    // looping through All Students
                    for (int i = 0; i < students.length(); i++) {
                        JSONObject c = students.getJSONObject(i);

                        String id = c.getString(TAG_ID);
                        String name = c.getString(TAG_FRANCAIS);
                        String email = c.getString(TAG_MOORE);
                    /*String address = c.getString(TAG_ADDRESS);
                    String gender = c.getString(TAG_GENDER);
                    String mobile = c.getString(TAG_PHONE_MOBILE);
                    String home = c.getString(TAG_PHONE_HOME);*/
                        // Phone node is JSON Object
                        // JSONObject phone = c.getJSONObject(TAG_PHONE);
                        // String mobile = phone.getString(TAG_PHONE_MOBILE);
                        // String home = phone.getString(TAG_PHONE_HOME);

                        // tmp hashmap for single student
                        HashMap<String, String> student = new HashMap<String, String>();

                        // adding each child node to HashMap key => value
                        student.put(TAG_ID, id);
                        student.put(TAG_FRANCAIS, name);
                        student.put(TAG_MOORE, email);
                        // student.put(TAG_PHONE_MOBILE, mobile);

                        // adding student to students list
                        studentList.add(student);}
                }
                else{

                }
                return studentList;
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            Log.e("ServiceHandler", "Couldn't get any data from the url");
            return null;
        }
    }
    private boolean copyDatabase(Context context) {
        try {

            InputStream inputStream = context.getAssets().open(DatabaseHelper.DBNAME);
            String outFileName = DatabaseHelper.DBLOCATION + DatabaseHelper.DBNAME;
            OutputStream outputStream = new FileOutputStream(outFileName);
            byte[]buff = new byte[1024];
            int length = 0;
            while ((length = inputStream.read(buff)) > 0) {
                outputStream.write(buff, 0, length);
            }
            outputStream.flush();
            outputStream.close();
            Log.w("MainActivity","DB copied");
            return true;
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


}