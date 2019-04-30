package hemath.com.pagination;

import android.content.*;
import android.os.*;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.*;
import android.view.*;
import android.widget.*;

import com.android.volley.*;
import com.android.volley.toolbox.*;

import org.json.*;

import java.util.*;

import hemath.com.pagination.Adapters.*;
import hemath.com.pagination.LoadMoreOnScroll.*;
import hemath.com.pagination.Models.*;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mTemplateContainer;
    private ArrayList<TemplateModel> model;
    private TemplateAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    ProgressBar progressBar;
    private Context mContext;
    public RequestQueue requestQueue;
    private int requestCount = 1;
    private RecyclerViewPositionHelper mRecyclerViewHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;
        mTemplateContainer = (RecyclerView) findViewById(R.id.template_container);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        layoutManager = new LinearLayoutManager(mContext);
        mTemplateContainer.setLayoutManager(layoutManager);

        model = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(mContext);
        mAdapter = new TemplateAdapter(mContext,model);
        mRecyclerViewHelper = RecyclerViewPositionHelper.createHelper(mTemplateContainer);

        getData();
        mTemplateContainer.setAdapter(mAdapter);

        InfiniteScrollProvider infiniteScrollProvider = new InfiniteScrollProvider();
        infiniteScrollProvider.attach(mTemplateContainer,new OnLoadMoreListener() {
            @Override
            public void onLoadMore()
            {
                getData();
            }
        });

        infiniteScrollProvider.attach(mTemplateContainer,new ScrollPosChanged() {
            @Override
            public void ScrollPosChanged(int pos, boolean show)
            {
                int position = mRecyclerViewHelper.findFirstVisibleItemPosition();
//                Log.i("InfiniteScroll", String.valueOf(position));
                if(position <= 1) {
                    if(show == true) {

                    }
                }
                else{
                    if(show == false) {

                    }
                }
            }
        });

    }

    private void getData()
    {
        progressBar.setVisibility(View.VISIBLE);
        requestQueue.add(getDataFromServer(requestCount));
        requestCount++;
    }

    private StringRequest getDataFromServer(int requestCount)
    {

        String Book_URL =Config.DATA_URL + String.valueOf(requestCount)+"&sUserId=0&sDevice=Android";


        StringRequest jsonArrayRequest = new StringRequest(Book_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(final String response)
                    {
                        try
                        {
                            if (response.isEmpty()){
                                Toast.makeText(getBaseContext(), "No more books available", Toast.LENGTH_LONG).show();
                                progressBar.setVisibility(View.GONE);
                            }
                            else{
                                JSONArray Jarray = new JSONArray(response);
                                new DisplayHandler().execute(Jarray);
                            }
                        }
                        catch (Exception e)
                        {
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressBar.setVisibility(View.GONE);
                        String ResError = error.getMessage();
                        Toast.makeText(MainActivity.this, ResError, Toast.LENGTH_SHORT).show();
                    }
                });
        return jsonArrayRequest;
    }

    private class DisplayHandler extends AsyncTask<JSONArray, String, String>
    {
        String msg="";
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
            setProgressBarIndeterminateVisibility(true);
        }

        @Override
        protected String doInBackground(JSONArray... data)
        {
            String  title = "", image_url = "" ;

            try {
                JSONArray array = data[0];
                for (int i = 0; i < array.length(); i++)
                {
                    try {
                        JSONObject json = array.getJSONObject(i);
                        title = json.getString(Config.TAG_TITLE);
                        image_url = json.getString(Config.TAG_IMAGE_URL);
                        image_url = image_url.replace("http://","https://");
                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }
                        model.add(new TemplateModel(image_url,title));
                }
            } catch (Exception e)
            {
                e.printStackTrace();
            }
            return msg;
        }

        @Override
        protected void onPostExecute(String s)
        {
            super.onPostExecute(s);
            mAdapter.notifyDataSetChanged ();
            progressBar.setVisibility(View.GONE);
        }
    }
}
