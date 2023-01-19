package edu.uncc.finalexam;
/*
File Name: NewsFragment.java
Full Name of author: Krithika Kasaragod
*/
import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import edu.uncc.finalexam.databinding.FragmentNewsBinding;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class NewsFragment extends Fragment {

    FragmentNewsBinding binding;
    final String TAG = "demo";

    private final OkHttpClient client = new OkHttpClient();
    ArrayList<News> newsList = new ArrayList<>();
    ArrayAdapter<News> listAdapter;


    public NewsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentNewsBinding.inflate(inflater, container, false);
        getActivity().setTitle("News");
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("News");
        setHasOptionsMenu(true);

        getNewsList();

    }

    private void getNewsList() {

        HttpUrl url = HttpUrl.parse("https://www.theappsdr.com/news_api.json").newBuilder()
                .build();

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.d("TAG", "onFailure:********* ");
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        newsList.clear();

                        JSONObject jsonObject = new JSONObject(response.body().string());
                        JSONArray articlesJsonArray = jsonObject.getJSONArray("articles");
                        for (int i = 0; i < articlesJsonArray.length(); i++) {
                            JSONObject jsonData = articlesJsonArray.getJSONObject(i);

                            News news = new News();
                            JSONObject sourceJson = jsonData.getJSONObject("source");
                            news.setSource_name(sourceJson.getString("name"));

                            news.setAuthor(jsonData.getString("author"));
                            news.setTitle(jsonData.getString("title"));
                            news.setImage(jsonData.getString("urlToImage"));
                            news.setPublished_at(jsonData.getString("publishedAt"));
                            news.setUrl(jsonData.getString("url"));

                            newsList.add(news);
                        }

                        if (getActivity() != null) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    Log.d(TAG, "run: albumList************" + newsList);
                                    listAdapter = new NewsAdapter(getContext(), R.layout.news_row_item, newsList);
                                    binding.listViewNews.setAdapter(listAdapter);

                                    binding.listViewNews.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                                            Log.d(TAG, "onItemClick: listAdapter" + (News) listAdapter.getItem(position));
                                            mListener.gotoNewsDetailsFragment((News) listAdapter.getItem(position));
                                        }
                                    });
                                }
                            });

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    Log.d("TAG", "onResponse: failed" + response.body());
                }
            }
        });


    }

    NewsFragmentListener mListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mListener = (NewsFragmentListener) context;
    }

    public interface NewsFragmentListener {
        void logout();

        void gotoMyListFragment();

        void gotoNewsDetailsFragment(News news);
    }


    public class NewsAdapter extends ArrayAdapter<News> {

        public NewsAdapter(@NonNull Context context, int resource, @NonNull List<News> objects) {
            super(context, resource, objects);
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext())
                        .inflate(R.layout.news_row_item, parent, false);
                AdapterViewHolder viewHolder = new AdapterViewHolder();
                viewHolder.tvTitle = convertView.findViewById(R.id.textViewNewsTitle);
                viewHolder.tvAuthor = convertView.findViewById(R.id.textViewNewsAuthor);
                viewHolder.tvSourceName = convertView.findViewById(R.id.textViewSourceName);
                viewHolder.tvPublished = convertView.findViewById(R.id.textViewPublishedAt);
                viewHolder.image = convertView.findViewById(R.id.imageViewNewsIcon);

                convertView.setTag(viewHolder);

            }

            News news = getItem(position);
            AdapterViewHolder viewHolder = (AdapterViewHolder) convertView.getTag();
            viewHolder.tvTitle.setText(news.getTitle());
            viewHolder.tvAuthor.setText(news.getAuthor());
            viewHolder.tvSourceName.setText(news.getSource_name());
            viewHolder.tvPublished.setText(news.getPublished_at());


            Picasso.get()
                    .load(news.getImage())
                    .into(viewHolder.image);
            return convertView;
        }

        public class AdapterViewHolder {

            TextView tvTitle, tvAuthor, tvSourceName, tvPublished;
            ImageView image;

        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menu_logout:
                mListener.logout();
                return true;
            case R.id.menu_listItem:
                mListener.gotoMyListFragment();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}