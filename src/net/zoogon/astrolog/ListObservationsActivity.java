package net.zoogon.astrolog;

import java.text.SimpleDateFormat;
import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class ListObservationsActivity extends Activity {

	private SessionsDAO sessionsDataSource;
	private ObservationsDAO observationsDataSource;
	private List<Observation> values;
	private Session session;
	private long session_id;
	
	//request codes for onActivityResult
	public static final int ADD_OBSERVATION_REQUEST = 1;
	public static final int EDIT_OBSERVATION_REQUEST = 2; 
	public static final int EDIT_SESSION_REQUEST = 3;

	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_observations);

		// TODO
		// log
		Log.w(ACTIVITY_SERVICE, "ListObservationsActivity starting");

		// get session data and load values
		session_id = getIntent().getExtras().getLong("session_id");
		sessionsDataSource = new SessionsDAO(this);
		observationsDataSource = new ObservationsDAO(this);

		updateSession();

		// get session observations and load on viewlist
		updateObservationList();
		// close data sources
	}

	//TODO check if menu is necessary
	/*
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_list_observations, menu);
		return true;
	}
	*/
	
	private void updateSession() {
		sessionsDataSource.open();

		session = sessionsDataSource.getSession(session_id);
		TextView session_title = (TextView) findViewById(R.id.tv_title);
		session_title.setText(session.getTitle());
		TextView session_date = (TextView) findViewById(R.id.tv_date);
		String date_st = SimpleDateFormat.getDateInstance(
				SimpleDateFormat.SHORT).format(session.getDate());
		session_date.setText(date_st);
		TextView session_location = (TextView) findViewById(R.id.tv_location);
		session_location.setText(session.getLocation());
		TextView session_notes = (TextView) findViewById(R.id.tv_notes);
		session_notes.setText(session.getNotes());
		sessionsDataSource.close();

	}
	
	private void updateObservationList() {
		observationsDataSource.open();

	
		// filling the viewList
		ListView listView = (ListView) findViewById(R.id.vl_observations);
	
		values = observationsDataSource.getObservationsForSession(session_id);
	
		//TODO show message if there is no sessions (invite to create some)
		
		ArrayAdapter<Observation> adapter = new ArrayAdapter<Observation>(this,
				android.R.layout.simple_list_item_1, values);
		
		listView.setAdapter(adapter);
		
		// add a event to each row
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
				int position, long id) {
				editObservation(values.get(position).getId());
			}
		}); 
		// TODO add long click listener to delete sessions
		
		// TODO fill stats counter
		observationsDataSource.close();

		
	}
	public void editSession(View view) {
		Intent intent = new Intent(this, EditSessionActivity.class);
		intent.putExtra("request_code",
				EditSessionActivity.EDIT_SESSION_REQUEST);
		intent.putExtra("session_id", session_id);
		startActivityForResult(intent, EDIT_SESSION_REQUEST);
	}

	/**
	 * Launch editObservation activity in creation mode
	 * 
	 * @param view
	 */
	public void addObservation(View view) {
		Intent intent = new Intent(this, EditObservationActivity.class);
		intent.putExtra("session_id", session_id);
		intent.putExtra("request_code",
				EditObservationActivity.ADD_OBSERVATION_REQUEST);
		startActivityForResult(intent, ADD_OBSERVATION_REQUEST);
	}

	
	public void editObservation(long observation_id) {
		Intent intent = new Intent(this, EditObservationActivity.class);
		intent.putExtra("request_code",
				EditObservationActivity.EDIT_OBSERVATION_REQUEST);
		intent.putExtra("observation_id", observation_id);
		startActivityForResult(intent, EDIT_OBSERVATION_REQUEST);
	}

	/**
	 * Launched after activity called with startActivityForResult finishes
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		switch (requestCode) {
		case EDIT_SESSION_REQUEST:
			switch (resultCode) {
			case Activity.RESULT_OK:
				session_id = data.getExtras().getLong("session_id");
				updateSession();
				break;

			case Activity.RESULT_CANCELED:
				popUp(R.string.message_canceled);
				break;
			}
			break;
		case ADD_OBSERVATION_REQUEST:
		case EDIT_OBSERVATION_REQUEST:
			switch (resultCode) {
			case Activity.RESULT_OK:
				updateObservationList();
				break;

			case Activity.RESULT_CANCELED:
				popUp(R.string.message_canceled);
				break;
			}
			break;
		default:
			popUp(R.string.message_unknow_requestCode);
			break;
		}
	}

	/**
	 * Just a code snippet for toast To use in with resources class
	 * (ex:R.string.message_done) or a String object
	 * 
	 * @param message
	 */
	private void popUp(int message) {
		Toast.makeText(this, message, Toast.LENGTH_LONG).show();
	}

	private void popUp(CharSequence message) {
		Toast.makeText(this, message, Toast.LENGTH_LONG).show();
	}

}