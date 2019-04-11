package com.example.gigpig;

import java.util.ArrayList;
import java.util.*;
import com.google.common.collect.*;


public class SortByAlphabeticalOrder implements SortingStrategy {
    public class SortByName implements Comparator<Job> {
        public int compare(Job job, Job job2) {
            return job.getTitle().compareTo(job2.getTitle());
        }
    }
    public ArrayList<Job> sort(ArrayList<Job> jobs) {
        Collections.sort(jobs, new SortByName());
        return jobs;
    }

}

