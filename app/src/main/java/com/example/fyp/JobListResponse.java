// JobListResponse.java - Updated to match Adzuna API response
package com.example.fyp;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class JobListResponse {
    @SerializedName("results")
    private List<JobInsightResponse> results;

    @SerializedName("count")
    private int count;

    @SerializedName("mean")
    private double mean;

    public List<JobInsightResponse> getResults() { return results; }
    public int getCount() { return count; }
    public double getMean() { return mean; }

    public void setResults(List<JobInsightResponse> results) { this.results = results; }
    public void setCount(int count) { this.count = count; }
    public void setMean(double mean) { this.mean = mean; }
}