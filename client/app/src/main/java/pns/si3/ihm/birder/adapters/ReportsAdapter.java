package pns.si3.ihm.birder.adapters;

import android.content.Context;
import android.content.Intent;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import etudes.fr.demoosm.R;
import pns.si3.ihm.birder.models.Report;
import pns.si3.ihm.birder.views.InformationActivity;
import pns.si3.ihm.birder.views.reports.ReportActivity;

/**
 * Reports adapter.
 *
 * Manages a list of reports inside a recycler view.
 */
public class ReportsAdapter extends RecyclerView.Adapter<ReportsAdapter.ReportViewHolder> {

	private Context context;
	/**
	 * The list of reports.
	 */
	private List<Report> reports;

	/**
	 * Constructs a reports adapter.
	 */
	public ReportsAdapter(Context context) {
		this.reports = new ArrayList<>();
		this.context = context;
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
		String elapsedTime = getElapsedTime(report.getDate());
		dateText.setText(elapsedTime);
		Button button = viewHolder.selectButton;
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, InformationActivity.class);
				intent.putExtra("id", report.getId());
				context.startActivity(intent);
			}
		});

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