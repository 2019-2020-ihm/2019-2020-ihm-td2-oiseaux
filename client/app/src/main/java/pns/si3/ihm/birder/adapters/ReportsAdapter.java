package pns.si3.ihm.birder.adapters;

import android.app.Activity;
import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import etudes.fr.demoosm.R;
import pns.si3.ihm.birder.models.Report;

/**
 * Reports adapter.
 *
 * Manages a list of reports inside a recycler view.
 */
public class ReportsAdapter extends RecyclerView.Adapter<ReportsAdapter.ReportViewHolder> {
	/**
	 * The list of reports.
	 */
	private List<Report> reports;

	/**
	 * Constructs a reports adapter.
	 */
	public ReportsAdapter() {
		this.reports = new ArrayList<>();
	}

	/**
	 * Updates the list of reports.
	 * @param reports The new list of reports.
	 */
	public void setReports(List<Report> reports) {
		this.reports = reports;
		notifyDataSetChanged();
	}

	/**
	 * Returns the time elapsed since a date.
	 * @param date The start date.
	 * @return The time elapsed since the start date.
	 */
	private String getElapsedTime(Date date) {
		Date now = new Date();
		return DateUtils.getRelativeTimeSpanString(
			date.getTime(),
			now.getTime(),
			DateUtils.MINUTE_IN_MILLIS
		).toString();
	}

	@Override
	public ReportsAdapter.ReportViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		Context context = parent.getContext();
		LayoutInflater inflater = LayoutInflater.from(context);

		// Inflate the custom layout.
		View reportView = inflater.inflate(R.layout.list_item_report, parent, false);

		// Return a new holder instance.
		return new ReportViewHolder(reportView);
	}

	@Override
	public void onBindViewHolder(ReportsAdapter.ReportViewHolder viewHolder, int position) {
		// Get the report in the list.
		Report report = reports.get(position);

		// Species.
		TextView titleText = viewHolder.titleText;
		titleText.setText(report.getSpecies());

		// Elapsed time.
		TextView dateText = viewHolder.dateText;
		String timeAgo = getElapsedTime(report.getDate());
		dateText.setText(timeAgo);
		Button button = viewHolder.selectButton;
	}

	/**
	 * Returns the number of reports.
	 * @return The number of reports.
	 */
	@Override
	public int getItemCount() {
		return reports.size();
	}

	static class ReportViewHolder extends RecyclerView.ViewHolder {
		private TextView titleText;
		private TextView dateText;
		private Button selectButton;

		ReportViewHolder(View itemView) {
			super(itemView);
			titleText = itemView.findViewById(R.id.text_title);
			dateText = itemView.findViewById(R.id.text_date);
			selectButton = itemView.findViewById(R.id.button_select);
		}
	}
}