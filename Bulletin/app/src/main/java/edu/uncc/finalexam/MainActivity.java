package edu.uncc.finalexam;
/*
File Name: MainActivity.java
Full Name of author: Krithika Kasaragod
*/
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;

import edu.uncc.finalexam.auth.LoginFragment;
import edu.uncc.finalexam.auth.RegisterFragment;

public class MainActivity extends AppCompatActivity implements LoginFragment.LoginFragmentListener,
        RegisterFragment.RegisterFragmentListener,
        NewsFragment.NewsFragmentListener,
        NewsDetailsFragment.NewsAddListListener, AddToListFragment.NewsAddListInterface, MyListsFragment.NewsListInterface {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //checks whether the user has logged-in or not for the first time
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.contentView, new LoginFragment())
                    .commit();
        } else {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.contentView, new NewsFragment())
                    .commit();
        }
    }

    @Override
    public void gotoRegister() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.contentView, new RegisterFragment())
                .commit();
    }

    @Override
    public void gotoLogin() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.contentView, new LoginFragment())
                .commit();
    }

    @Override
    public void successGotoNewsFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.contentView, new NewsFragment())
                .commit();
    }

    @Override
    public void logout() {
        FirebaseAuth.getInstance().signOut();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.contentView, new LoginFragment())
                .commit();
    }

    @Override
    public void gotoMyListFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.contentView, new MyListsFragment())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void gotoNewsDetailsFragment(News news) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.contentView, NewsDetailsFragment.newInstance(news), "NewsDetailsFragment")
                .addToBackStack(null)
                .commit();
    }


    @Override
    public void gotoAddToListFragment(News news) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.contentView, AddToListFragment.newInstance(news), "NewsDetailsFragment")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void gotoNewsDetailsFragment() {
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void gotoListDetails(NewsList newsList) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.contentView, ListDetailsFragment.newInstance(newsList), "NewsDetailsFragment")
                .addToBackStack(null)
                .commit();
    }
}