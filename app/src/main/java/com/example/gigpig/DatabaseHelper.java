package com.example.gigpig;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DatabaseHelper {

    public static void writeNewJob(Job job) {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        database.child("jobs").push().setValue(job);
    }

    public static void prepareDatabasePostings() {
        // placeholder
        User user = new User();
        ArrayList<String> tags = new ArrayList<>();
        tags.add("goat");
        tags.add("clean");

        Job job = new Job("Goats",
                "database: Need someone who is experienced with goat care to work one day cleaning stalls" +
                        ", feeding etc.",
                50, user, tags, null);

        DatabaseHelper.writeNewJob(job);

        tags = new ArrayList<>();
        tags.add("line");
        tags.add("long");

        job = new Job("Long job",
                "database: This is to text a long job with multiple lines\n" +
                        "Here's another line\n" +
                        "another one\n" +
                        "the idea is that the cells will adjust their size automatically based on the" +
                        "size of the job posting",
                12, user, tags, null);

        DatabaseHelper.writeNewJob(job);

        tags = new ArrayList<>();
        tags.add("find");
        tags.add("cats");
        tags.add("lost");

        job = new Job("Lost cat",
                "database: REWARD: Need someone to find my cat. Lost somewhere in monument creek." +
                        "He responds to Jimmy",
                100, user, tags, null);

        DatabaseHelper.writeNewJob(job);

        tags = new ArrayList<>();
        tags.add("an");
        tags.add("nothing");

        job = new Job("A nothing thing",
                "database: Test sort alphabetically",
                0, user, tags, null);

        DatabaseHelper.writeNewJob(job);

        tags = new ArrayList<>();
        tags.add("clean");
        tags.add("windex");

        job = new Job("Clean with my cleaning supplies",
                "database: Need someone with a lot of cleaning experience to clean my kitchen floor" +
                        "\n I will supply cleaning materials",
                20, user, tags, null);

        DatabaseHelper.writeNewJob(job);
    }

}