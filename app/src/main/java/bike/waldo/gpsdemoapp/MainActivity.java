package bike.waldo.gpsdemoapp;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import com.google.android.gms.location.LocationListener;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class MainActivity extends Activity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {
    ScrollView mScrollView;
    LinearLayout mLogLinearLayout;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private static String mUserLocation = "";
    private static final int TEXT_SIZE_SP = 14;
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final long GPS_REFRESH = 2000;
    Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mScrollView = (ScrollView) findViewById(R.id.scrollview_log);
        mLogLinearLayout = (LinearLayout) mScrollView.findViewById(R.id.linearlayout_log);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
		//Testing the Slack-Bitbucket integration.
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private void loadLog(String valueToDisplay) {
        TextView logTextView = new TextView(getApplicationContext());
        logTextView.setText(valueToDisplay);
        logTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, TEXT_SIZE_SP);//the text size is automatically scaled to fit the screen's dimensions
        logTextView.setTextColor(Color.BLACK);
        mLogLinearLayout.addView(logTextView);
    }

    @Override
    public void onConnected(Bundle bundle) {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(GPS_REFRESH); // Update the location every 5 seconds
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {
        Log.i(LOG_TAG, "Lat/lng: " + location.getLatitude() + "/" + location.getLongitude());
        DateFormat df = new SimpleDateFormat("HH:mm:ss");
        Date dateobj = new Date();
        mUserLocation = String.valueOf(location.getLatitude() + "/" + location.getLongitude());
        loadLog(mUserLocation + "|" + df.format(dateobj));

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
