package com.example.gigpig;

import android.os.Bundle;
import android.renderscript.Sampler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;


import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Home view fragment
 */
public class HomeFragment extends Fragment implements AdapterView.OnItemSelectedListener,
                            ValueEventListener,
                            View.OnKeyListener {
    private ArrayList<Job> jobsList;
    private ArrayList<Job> displayJobsList;

    private RecyclerView recyclerView;
    private JobAdapter mAdapter;

    private Spinner sortSelection;
    private EditText searchBar;

    private SortingStrategy sortingStrategy;

    private String searchText;
    private static final String TAGS = "Sort by my tags";
    private static final String ALPHABETICALLY = "Sort alphabetically";
    private static final String DATE = "Sort by date";
    private static final String LOWEST_PRICE = "Sort by lowest price";
    private static final String HIGHEST_PRICE = "Sort by highest price";




    /**
     * Default android function that gets called when a view is created
     * @return An instantiation of the home screen view
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_home, container, false);

        this.jobsList = new ArrayList<Job>();
        this.sortingStrategy = new SortByDateStrategy();

        searchText = "";

        DatabaseReference dataRef = FirebaseDatabase.getInstance().getReference("jobs");

        dataRef.addValueEventListener(this);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sortSelection = getView().findViewById(R.id.sortSelection);

        List<String> sortMethods = new ArrayList<>();
        sortMethods.add(TAGS);
        sortMethods.add(ALPHABETICALLY);
        sortMethods.add(DATE);
        sortMethods.add(LOWEST_PRICE);
        sortMethods.add(HIGHEST_PRICE);


        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, sortMethods);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        sortSelection.setAdapter(dataAdapter);
        sortSelection.setSelection(2);

        sortSelection.setOnItemSelectedListener(this);

        mAdapter = new JobAdapter(jobsList);

        mAdapter.notifyDataSetChanged();

        recyclerView = getView().findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        searchBar = getView().findViewById(R.id.search_bar);
        searchBar.setOnKeyListener(this);
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        // Get Job object and use the values to update the UI

        this.jobsList.clear();


        // Use this line to get jobs to display just for user. Not functional yet cause database doesnt
        // look like its supposed to
//        jobsList = DatabaseHelper.getPostedJobsForUser();


        //In final implementation, comment out this loop
        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
            Job job = snapshot.getValue(Job.class);

            if (this.searchText.equals(""))
                this.jobsList.add(job);
            else if (job.getJobTitle().contains(this.searchText)
                    || job.getTags().contains(this.searchText))
                this.jobsList.add(job);

        }


        for (Job job : this.jobsList)
            if (job.getCreationDate() == null || job.getJobTitle() == null)
                return;
        this.mAdapter.updateContents(this.sortingStrategy.sort(this.jobsList));
        this.mAdapter.notifyDataSetChanged();

        // ...
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        // Getting Post failed, log a message
        Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
        // ...
    }

    @Override
    public boolean onKey(View view, int keyCode, KeyEvent event) {
        if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                (keyCode == KeyEvent.KEYCODE_ENTER)) {
            // Perform action on key press

            this.searchText = this.searchBar.getText().toString();

            ArrayList<Job> tempJobList = new ArrayList<>();

            for (Job job : jobsList) {
                if (job.getJobTitle().toLowerCase().contains(this.searchText.toLowerCase())
                        || job.getTags().contains(this.searchText))
                    tempJobList.add(job);
            }

            if (this.searchText.equals(""))
                this.mAdapter.updateContents(this.sortingStrategy.sort(this.jobsList));
            else
                this.mAdapter.updateContents(this.sortingStrategy.sort(tempJobList));

            this.mAdapter.notifyDataSetChanged();

            return true;
        }
        return false;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String item = adapterView.getItemAtPosition(i).toString();

        // placeholder
        ArrayList<String> tags = new ArrayList<>();

        // tags to sort by! will replace with actual logged in user's intrests
        tags.add("space");
        tags.add("earth");
        tags.add("astrophysics");
        tags.add("physics");
        tags.add("big");


        switch (item) {
            case TAGS:
                this.sortingStrategy = new SortByTagsStrategy(tags);
                break;
            case ALPHABETICALLY:
                this.sortingStrategy = new SortByAlphabeticalOrder();
                break;
            case DATE:
                this.sortingStrategy = new SortByDateStrategy();
                break;
            case LOWEST_PRICE:
                this.sortingStrategy = new SortByLowestPrice();
                break;
            case HIGHEST_PRICE:
                this.sortingStrategy = new SortByHighestPrice();
                break;

        }

        this.displayJobsList = this.sortingStrategy.sort(this.jobsList);

        mAdapter.updateContents(this.displayJobsList);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
