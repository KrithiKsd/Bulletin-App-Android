package edu.uncc.finalexam;
/*
File Name: AddToListFragment.java
Full Name of author: Krithika Kasaragod
*/
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import edu.uncc.finalexam.databinding.FragmentAddToListBinding;
import edu.uncc.finalexam.databinding.FragmentNewsDetailsBinding;
import edu.uncc.finalexam.databinding.MylistRowItemBinding;


public class AddToListFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private News mNews;
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    ArrayList<NewsList> listNews = new ArrayList<>();
    NewsAddAdapter adapter;
    LinearLayoutManager linearLayoutManager;
    boolean flag = false;
    ArrayList<SubNews> listSub = new ArrayList<>();


    FragmentAddToListBinding binding;

    public AddToListFragment() {
        // Required empty public constructor
    }


    public static AddToListFragment newInstance(News news) {
        AddToListFragment fragment = new AddToListFragment();
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
        binding = FragmentAddToListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Add To List");
        if (mNews != null) {
            mAuth = FirebaseAuth.getInstance();
            db = FirebaseFirestore.getInstance();

            binding.recyclerAddList.setHasFixedSize(true);
            linearLayoutManager = new LinearLayoutManager(getContext());
            binding.recyclerAddList.setLayoutManager(linearLayoutManager);
            adapter = new NewsAddAdapter(listNews, mListener);
            binding.recyclerAddList.getRecycledViewPool().setMaxRecycledViews(0, 0);
            binding.recyclerAddList.setAdapter(adapter);

            getListFromDB();


        }
    }

    private void getListFromSub(ArrayList<NewsList> listNews) {
        for (NewsList item : listNews) {
            db.collection("NewsList").document(item.getDID()).collection("SubList")
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                            if (value != null) {
                                for (QueryDocumentSnapshot document : value) {

                                    SubNews item = document.toObject(SubNews.class);
                                    listSub.add(item);
                                    Log.d("TAG", "onEvent: image" + listSub);

                                }
                            } else {
                                Log.d("TAG", "Current data: null");
                            }
                        }
                    });
        }
    }

    private void getListFromDB() {
        db.collection("NewsList")//.whereEqualTo("uid",mAuth.getCurrentUser().getUid())
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        listNews.clear();
                        for (QueryDocumentSnapshot document : value) {
                            NewsList list = document.toObject(NewsList.class);

                            listNews.add(list);
                            Log.d("TAG", "onEvent: getList1" + listNews);
                        }

                        adapter.notifyDataSetChanged();
                        if (listNews.size() > 0) {
                            getListFromSub(listNews);
                        }
                    }
                });
    }

    class NewsAddAdapter extends RecyclerView.Adapter<NewsAddAdapter.RecyclerMixiewHolder> {
        ArrayList<NewsList> listAlbum;
        NewsAddListInterface mListener;

        public NewsAddAdapter(ArrayList<NewsList> sortNameList, NewsAddListInterface mListener) {
            this.listAlbum = sortNameList;
            this.mListener = mListener;
        }

        @NonNull
        @Override
        public RecyclerMixiewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            MylistRowItemBinding binding = MylistRowItemBinding.inflate(getLayoutInflater(), parent, false);
            NewsAddAdapter.RecyclerMixiewHolder sortViewHolder = new NewsAddAdapter.RecyclerMixiewHolder(binding);
            return sortViewHolder;

        }

        @Override
        public int getItemCount() {
            return listAlbum.size();
        }

        @Override
        public void onBindViewHolder(@NonNull NewsAddAdapter.RecyclerMixiewHolder holder, @SuppressLint("RecyclerView") int position) {

            NewsList item = listAlbum.get(position);
            holder.setUpData(item);
        }

        public class RecyclerMixiewHolder extends RecyclerView.ViewHolder {

            MylistRowItemBinding mBinding;
            NewsList mItem;

            public RecyclerMixiewHolder(MylistRowItemBinding binding) {
                super(binding.getRoot());
                mBinding = binding;
            }

            public void setUpData(NewsList item) {
                mItem = item;
                mBinding.textViewSourceName.setText(mItem.getListItemName());
                mBinding.textViewItemsCount.setText(String.valueOf(mItem.getNumberTrack()));


                mBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.d("TAG", "onClick:******************** "+listSub.size());

                        if (listSub.size() > 0) {
                            for (SubNews item : listSub) {
                                if (item.getTitle().equals(mNews.getTitle()) && mItem.getListItemName().equals(item.getNewsItem())) {
                                    flag = true;
                                }
                            }
                            if (!flag) {
                                Log.d("TAG", "onClick: item is not  there");
                                addToSubCollection(mItem);
                            } else {
                                Log.d("TAG", "item is already there");
                                Toast.makeText(getActivity(), "Item is already there", Toast.LENGTH_SHORT).show();
                                mListener.gotoNewsDetailsFragment();
                            }
                        }
                        addToSubCollection(mItem);
                    }
                });
            }

        }
    }

    private void addToSubCollection(NewsList item) {
        DocumentReference newDoc = db.collection("NewsList").document(item.getDID())
                .collection("SubList").document();
        String subDocID = newDoc.getId();

        newDoc.set(new SubNews(subDocID, item.getDID(),
                item.getUID(), item.listItemName, mNews.getTitle(), mNews.getAuthor(),
                mNews.getSource_name(), mNews.getPublished_at(), mNews.getImage()))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("TAG", "onSuccess: ****");
                        callUpdateCounts(item.getDID());

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

    }

    private void callUpdateCounts(String did) {
        db.collection("NewsList").document(did)
                .update("numberTrack", FieldValue.increment(1))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("TAG", "onSuccess: ");
                        mListener.gotoNewsDetailsFragment();
                       // Toast.makeText(getActivity(), "Added Successfully!", Toast.LENGTH_SHORT).show();

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("TAG", "onFailure: Inside update " + e.getMessage());
            }
        });

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            if (context instanceof NewsAddListInterface) {
                mListener = (NewsAddListInterface) context;
            } else {
                throw new RuntimeException();
            }
        } catch (RuntimeException exception) {

        }
    }

    NewsAddListInterface mListener;

    public interface NewsAddListInterface {
        void gotoNewsDetailsFragment();
    }
}