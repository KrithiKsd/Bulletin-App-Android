package edu.uncc.finalexam;
/*
File Name: ListDetailsFragment.java
Full Name of author: Krithika Kasaragod
*/
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashSet;

import edu.uncc.finalexam.databinding.FragmentListDetailsBinding;
import edu.uncc.finalexam.databinding.FragmentNewsDetailsBinding;
import edu.uncc.finalexam.databinding.NewsRowItemWithDeleteBinding;


public class ListDetailsFragment extends Fragment {


    private static final String ARG_PARAM1 = "param1";
    private NewsList mParam1;
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    ArrayList<SubNews> listSub = new ArrayList<>();
    LinearLayoutManager linearLayoutManager;
    TrackAdapter adapter;


    FragmentListDetailsBinding binding;

    public ListDetailsFragment() {
        // Required empty public constructor
    }


    public static ListDetailsFragment newInstance(NewsList param1) {
        ListDetailsFragment fragment = new ListDetailsFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = (NewsList) getArguments().getSerializable(ARG_PARAM1);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentListDetailsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("List Details");
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        if (mParam1 != null) {
            binding.textViewListName.setText(mParam1.getListItemName());

            getListFromSub();

            binding.recyclerListItem.setHasFixedSize(true);
            linearLayoutManager = new LinearLayoutManager(getContext());
            binding.recyclerListItem.setLayoutManager(linearLayoutManager);
            adapter = new TrackAdapter(listSub);
            binding.recyclerListItem.getRecycledViewPool().setMaxRecycledViews(0, 0);
            binding.recyclerListItem.setAdapter(adapter);
        }
    }

    private void getListFromSub() {

        db.collection("NewsList").document(mParam1.getDID()).collection("SubList")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        listSub.clear();

                        for (QueryDocumentSnapshot document : value) {

                            SubNews item = document.toObject(SubNews.class);
                            listSub.add(item);


                            Log.d("TAG", "onEvent: image" + listSub);

                        }

                        if (getActivity() != null) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (listSub.size() < 0) {
                                        Toast.makeText(getActivity(), "Sorry no tracks", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                        adapter.notifyDataSetChanged();

                    }
                });
    }

    class TrackAdapter extends RecyclerView.Adapter<TrackAdapter.RecyclerMixiewHolder> {
        ArrayList<SubNews> listAlbum;

        public TrackAdapter(ArrayList<SubNews> sortNameList) {
            this.listAlbum = sortNameList;
        }

        @NonNull
        @Override
        public TrackAdapter.RecyclerMixiewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            NewsRowItemWithDeleteBinding binding = NewsRowItemWithDeleteBinding.inflate(getLayoutInflater(), parent, false);
            TrackAdapter.RecyclerMixiewHolder sortViewHolder = new TrackAdapter.RecyclerMixiewHolder(binding);
            return sortViewHolder;

        }

        @Override
        public int getItemCount() {
            return listAlbum.size();
        }

        @Override
        public void onBindViewHolder(@NonNull TrackAdapter.RecyclerMixiewHolder holder, @SuppressLint("RecyclerView") int position) {

            SubNews item = listAlbum.get(position);
            holder.setUpData(item);
        }

        public class RecyclerMixiewHolder extends RecyclerView.ViewHolder {

            NewsRowItemWithDeleteBinding mBinding;
            SubNews mItem;

            public RecyclerMixiewHolder(NewsRowItemWithDeleteBinding binding) {
                super(binding.getRoot());
                mBinding = binding;
            }

            public void setUpData(SubNews item) {

                mItem = item;
                mBinding.textViewNewsTitle.setText(mItem.getTitle());
                mBinding.textViewNewsAuthor.setText(mItem.getAuthor());
                mBinding.textViewSourceName.setText(mItem.getSource());
                mBinding.textViewPublishedAt.setText(mItem.getPublishedAt());


                Log.d("TAG", "setUpData: " + mItem.getImage());
                Picasso.get()
                        .load(mItem.getImage())
                        .into(mBinding.ivNewsIcon);


                mBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });

                mBinding.imageViewDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        deleteSub(mItem);
                    }
                });
            }

        }
    }

    private void deleteSub(SubNews mItem) {

        db.collection("NewsList").document(mItem.getDocId()).collection("SubList")
                .document(mItem.getSubId())
                .delete()
                .addOnSuccessListener(getActivity(), new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("TAG", "onSuccess: ");
                    }
                });

        db.collection("NewsList").document(mItem.getDocId())
                .update("numberTrack", FieldValue.increment(-1))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("TAG", "onSuccess: ");
                        getListFromSub();
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                            }
                        });

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("TAG", "onFailure: Inside update " + e.getMessage());
            }
        });
    }

}