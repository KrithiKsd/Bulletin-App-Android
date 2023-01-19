package edu.uncc.finalexam;
/*
File Name: NewsDetailsFragment.java
Full Name of author: Krithika Kasaragod
*/
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import edu.uncc.finalexam.databinding.FragmentNewsBinding;
import edu.uncc.finalexam.databinding.FragmentNewsDetailsBinding;


public class NewsDetailsFragment extends Fragment {


    private static final String ARG_PARAM1 = "param1";
    FragmentNewsDetailsBinding binding;
    NewsAddListListener mListener;

    private News mNews;


    public NewsDetailsFragment() {
        // Required empty public constructor
    }


    public static NewsDetailsFragment newInstance(News news) {
        NewsDetailsFragment fragment = new NewsDetailsFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, news);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mNews = (News) getArguments().getSerializable(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentNewsDetailsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("News Details");

        if (mNews != null) {
            binding.textViewNewsTitle.setText(mNews.getTitle());
            binding.textViewNewsAuthor.setText(mNews.getAuthor());
            binding.textViewSourceName.setText(mNews.getSource_name());

            Picasso.get()
                    .load(mNews.getImage())
                    .into(binding.imageViewNewsIcon);

        }

        binding.imageViewBrowser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openNewsUrl(mNews.getUrl());
            }
        });


        binding.imageViewAddToList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.gotoAddToListFragment(mNews);
            }
        });
    }

    //this is the code required to open the news page news url
    public void openNewsUrl(String url) {
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl(getActivity(), Uri.parse(url));

    }

    public interface NewsAddListListener {
        void gotoAddToListFragment(News news);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mListener = (NewsAddListListener) context;
    }
}