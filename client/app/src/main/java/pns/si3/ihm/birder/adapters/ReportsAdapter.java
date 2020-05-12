package pns.si3.ihm.birder.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import etudes.fr.demoosm.R;
import pns.si3.ihm.birder.models.Report;

public class ReportsAdapter extends RecyclerView.Adapter<ReportsAdapter.ReportViewHolder> {
	private List<Report> reports;

	public ReportsAdapter() {
		this.reports = new ArrayList<>();
	}

	public void setReports(List<Report> reports) {
		this.reports = reports;
		notifyDataSetChanged();
	}

	@Override
	public ReportsAdapter.ReportViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		Context context = parent.getContext();
		LayoutInflater inflater = LayoutInflater.from(context);

		// Inflate the custom layout
		View reportView = inflater.inflate(R.layout.list_item_report, parent, false);

		// Return a new holder instance
		ReportViewHolder viewHolder = new ReportViewHolder(reportView);
		return viewHolder;
	}

	@Override
	public void onBindViewHolder(ReportsAdapter.ReportViewHolder viewHolder, int position) {
		// Get the data model based on position
		Report report = reports.get(position);

		// Set item views based on your views and data model
		TextView titleText = viewHolder.titleText;
		titleText.setText(report.getSpecies());
		Button button = viewHolder.selectButton;
	}

	// Returns the total count of items in the list
	@Override
	public int getItemCount() {
		return reports.size();
	}

	public class ReportViewHolder extends RecyclerView.ViewHolder {
		public TextView titleText;
		public Button selectButton;

		public ReportViewHolder(View itemView) {
			super(itemView);
			titleText = itemView.findViewById(R.id.text_title);
			selectButton = itemView.findViewById(R.id.button_select);
		}
	}
}