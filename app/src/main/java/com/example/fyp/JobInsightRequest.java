package com.example.fyp;

public class JobInsightRequest {
    private String country;
    private String what;
    private int resultsPerPage;

    public JobInsightRequest(String country, String what, int resultsPerPage) {
        this.country = country;
        this.what = what;
        this.resultsPerPage = resultsPerPage;
    }

    public String getCountry() {
        return country;
    }

    public String getWhat() {
        return what;
    }

    public int getResultsPerPage() {
        return resultsPerPage;
    }
}
