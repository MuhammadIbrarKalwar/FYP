package com.example.fyp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class JobAdapter extends RecyclerView.Adapter<JobAdapter.JobViewHolder> {

    private List<JobInsightResponse> jobList;
    private Context context;

    public JobAdapter(Context context, List<JobInsightResponse> jobList) {
        this.context = context;
        this.jobList = jobList;
    }

    @NonNull
    @Override
    public JobViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.job_item, parent, false);
        return new JobViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull JobViewHolder holder, int position) {
        JobInsightResponse job = jobList.get(position);

        // Set job title
        holder.textJobTitle.setText(job.title != null ? job.title : "Software Developer");

        // Set company name
        String companyName = "Unknown Company";
        if (job.company.display_name != null && job.company.display_name != null) {
            companyName = job.company.display_name;
        }
        holder.textCompanyName.setText("🏢 " + companyName);

        // Set location
        String location = "Location not specified";
        if (job.location.display_name != null) {
            location = job.location.display_name;
        }
        holder.textLocation.setText("📍 " + location);

        // Set job type
        String jobType = job.adref != null ? job.adref : "Full-time";
        holder.textJobType.setText("⏰ " + jobType);

        // Set salary
        String salary = formatSalary(job.salary_min, job.salary_max);
        holder.textSalary.setText("💰 " + salary);

        // Set description
        String description = job.description != null ?
                stripHtml(job.description) : "No description available";
        holder.textDescription.setText(description);

        // Set click listener for view job button
        holder.btnViewJob.setOnClickListener(v -> {
            if (job.redirect_url != null && !job.redirect_url.isEmpty()) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(job.redirect_url));
                context.startActivity(browserIntent);
            } else {
                Toast.makeText(context, "Job details not available", Toast.LENGTH_SHORT).show();
            }
        });

        // Set click listener for entire card
        holder.itemView.setOnClickListener(v -> {
            if (job.redirect_url != null && !job.redirect_url.isEmpty()) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(job.redirect_url));
                context.startActivity(browserIntent);
            } else {
                Toast.makeText(context, "Job details not available", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return jobList != null ? jobList.size() : 0;
    }

    public void updateJobs(List<JobInsightResponse> newJobs) {
        this.jobList = newJobs;
        notifyDataSetChanged();
    }

    private String formatSalary(double salaryMin, double salaryMax) {
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.UK);

        if (salaryMin > 0 && salaryMax > 0) {
            return currencyFormat.format(salaryMin) + " - " + currencyFormat.format(salaryMax);
        } else if (salaryMin > 0) {
            return "From " + currencyFormat.format(salaryMin);
        } else if (salaryMax > 0) {
            return "Up to " + currencyFormat.format(salaryMax);
        } else {
            return "Salary not specified";
        }
    }

    private String stripHtml(String html) {
        if (html == null) return "";

        // Simple HTML tag removal
        String text = html.replaceAll("<[^>]*>", "");

        // Replace common HTML entities
        text = text.replace("&amp;", "&");
        text = text.replace("&lt;", "<");
        text = text.replace("&gt;", ">");
        text = text.replace("&quot;", "\"");
        text = text.replace("&#39;", "'");
        text = text.replace("&nbsp;", " ");

        // Trim and limit length
        text = text.trim();
        if (text.length() > 200) {
            text = text.substring(0, 200) + "...";
        }

        return text;
    }

    static class JobViewHolder extends RecyclerView.ViewHolder {
        TextView textJobTitle, textCompanyName, textLocation, textJobType, textSalary, textDescription;
        Button btnViewJob;

        public JobViewHolder(@NonNull View itemView) {
            super(itemView);
            textJobTitle = itemView.findViewById(R.id.textJobTitle);
            textCompanyName = itemView.findViewById(R.id.textCompanyName);
            textLocation = itemView.findViewById(R.id.textLocation);
            textJobType = itemView.findViewById(R.id.textJobType);
            textSalary = itemView.findViewById(R.id.textSalary);
            textDescription = itemView.findViewById(R.id.textDescription);
            btnViewJob = itemView.findViewById(R.id.btnViewJob);
        }
    }
}