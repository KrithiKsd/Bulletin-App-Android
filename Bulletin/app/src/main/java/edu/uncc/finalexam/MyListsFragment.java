package edu.uncc.finalexam;
/*
File Name: MyListsFragment.java
Full Name of author: Krithika Kasaragod
*/
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import edu.uncc.finalexam.databinding.FragmentMyListsBinding;
import edu.uncc.finalexam.databinding.FragmentNewsDetailsBinding;
import edu.uncc.finalexam.databinding.MylistRowItemBinding;

public class MyListsFragment extends Fragment {

    FragmentMyListsBinding binding;

    int numberOfTrack = 0;
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    ArrayList<NewsList> listNews = new ArrayList<>();
    LinearLayoutManager linearLayoutManager;
    NewsAdapter adapter;


    public MyListsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentMyListsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("My Lists");

        Log.d("TAG", "onViewCreated: called** ");

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        binding.reclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(getContext());
        binding.reclerView.setLayoutManager(linearLayoutManager);
        adapter = new NewsAdapter(listNews, mListener);
        binding.reclerView.getRecycledViewPool().setMaxRecycledViews(0, 0);
        binding.reclerView.setAdapter(adapter);

        binding.buttonAddList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNewNewsList();
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        getNewsList();
    }

    private void getNewsList() {

        db.collection("NewsList")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        listNews.clear();
                        for (QueryDocumentSnapshot document : value) {
                            NewsList list = document.toObject(NewsList.class);

                            listNews.add(list);
                            Log.d("TAG", "onEvent: getListITEMS***" + listNews);
                        }

                        adapter.notifyDataSetChanged();
                    }
                });
    }

    private void createNewNewsList() {
        String listName = binding.editTextListName.getText().toString();
        if (listName.isEmpty()) {
            displayAlert("Please Enter list name");
        } else {

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            DocumentReference newDoc = db.collection("NewsList").document();

            String DID = newDoc.getId();

            newDoc.set(new NewsList(DID, mAuth.getCurrentUser().getUid(),
                    listName, numberOfTrack))
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Log.d("TAG", "onSuccess: ");
                            binding.editTextListName.setText("");
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("TAG", "onFailure: CreateList");
                }
            });

        }


    }

    public void displayAlert(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.label_alert))
                .setMessage(message)
                .setPositiveButton(getString(R.string.label_ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
        builder.show();
    }

    class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.RecyclerMixiewHolder> {
        ArrayList<NewsList> listAlbum;
        NewsListInterface mListener;

        public NewsAdapter(ArrayList<NewsList> sortNameList, NewsListInterface mListener) {
            this.listAlbum = sortNameList;
            this.mListener = mListener;
        }

        @NonNull
        @Override
        public RecyclerMixiewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            MylistRowItemBinding binding = MylistRowItemBinding.inflate(getLayoutInflater(), parent, false);
            RecyclerMixiewHolder sortViewHolder = new RecyclerMixiewHolder(binding);
            return sortViewHolder;

        }

        @Override
        public int getItemCount() {
            return listAlbum.size();
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerMixiewHolder holder, @SuppressLint("RecyclerView") int position) {

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
                        mListener.gotoListDetails(mItem);
                    }
                });
            }

        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            if (context instanceof NewsListInterface) {
                mListener = (NewsListInterface) context;
            } else {
                throw new RuntimeException();
            }
        } catch (RuntimeException exception) {

        }
    }

    NewsListInterface mListener;

    public interface NewsListInterface {
        void gotoListDetails(NewsList newsList);
    }
}