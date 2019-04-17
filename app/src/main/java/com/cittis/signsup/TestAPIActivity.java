package com.cittis.signsup;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.cittis.signsup.actions.FetchDataListener;
import com.cittis.signsup.connection.GETAPIRequest;
import com.cittis.signsup.connection.POSTAPIRequest;
import com.cittis.signsup.connection.RequestQueueService;
import org.json.JSONObject;

public class TestAPIActivity extends AppCompatActivity {
    private TextView resultTextView;
    //Implementing interfaces of FetchDataListener for GET api request
    FetchDataListener fetchGetResultListener = new FetchDataListener() {
        @Override
        public void onFetchComplete(JSONObject data) {
            //Fetch Complete. Now stop progress bar  or loader
            //you started in onFetchStart
            RequestQueueService.cancelProgressDialog();
            try {
                //Now check result sent by our GETAPIRequest class
                if (data != null) {
                    if (data.has("success")) {
                        int success = data.getInt("success");
                        if (success == 1) {
                            JSONObject response = data.getJSONObject("response");
                            if (response != null) {
                                //Display the result
                                //Or, You can do whatever you need to
                                //do with the JSONObject
                                resultTextView.setText(response.toString(4));
                            }
                        } else {
                            RequestQueueService.showAlert("Error! No data fetched", TestAPIActivity.this);
                        }
                    }
                } else {
                    RequestQueueService.showAlert("Error! No data fetched", TestAPIActivity.this);
                }
            } catch (Exception e) {
                RequestQueueService.showAlert("Something went wrong", TestAPIActivity.this);
                e.printStackTrace();
            }
        }

        @Override
        public void onFetchFailure(String msg) {
            RequestQueueService.cancelProgressDialog();
            //Show if any error message is there called from GETAPIRequest class
            RequestQueueService.showAlert(msg, TestAPIActivity.this);
        }

        @Override
        public void onFetchStart() {
            //Start showing progressbar or any loader you have
            RequestQueueService.showProgressDialog(TestAPIActivity.this);
        }
    };
    View.OnClickListener getApiListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            //Call getApiCall() method
            getApiCall();
        }
    };
    //Implementing interfaces of FetchDataListener for POST api request
    FetchDataListener fetchPostResultListener = new FetchDataListener() {
        @Override
        public void onFetchComplete(JSONObject data) {
            //Fetch Complete. Now stop progress bar  or loader
            //you started in onFetchStart
            RequestQueueService.cancelProgressDialog();
            try {
                //Now check result sent by our POSTAPIRequest class
                if (data != null) {
                    if (data.has("success")) {
                        int success = data.getInt("success");
                        if (success == 1) {
                            JSONObject response = data.getJSONObject("response");
                            if (response != null) {
                                //Display the result
                                //Or, You can do whatever you need to
                                //do with the JSONObject
                                resultTextView.setText(response.toString(4));
                            }
                        } else {
                            RequestQueueService.showAlert("Error! No data fetched", TestAPIActivity.this);
                        }
                    }
                } else {
                    RequestQueueService.showAlert("Error! No data fetched", TestAPIActivity.this);
                }
            } catch (Exception e) {
                RequestQueueService.showAlert("Something went wrong", TestAPIActivity.this);
                e.printStackTrace();
            }
        }

        @Override
        public void onFetchFailure(String msg) {
            RequestQueueService.cancelProgressDialog();
            //Show if any error message is there called from POSTAPIRequest class
            RequestQueueService.showAlert(msg, TestAPIActivity.this);
        }

        @Override
        public void onFetchStart() {
            //Start showing progressbar or any loader you have
            RequestQueueService.showProgressDialog(TestAPIActivity.this);
        }
    };
    View.OnClickListener postApiListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            //Call postApiCall() method
            postApiCall();
        }
    };
    private Button getApiBtn, postApiBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_api);
        resultTextView = findViewById(R.id.resultTextView);
        getApiBtn = findViewById(R.id.getApiBtn);
        postApiBtn = findViewById(R.id.postApiBtn);

        //Attaching OnClickListener with Buttons
        getApiBtn.setOnClickListener(getApiListener);
        postApiBtn.setOnClickListener(postApiListener);
    }

    private void getApiCall() {
        try {
            //Create Instance of GETAPIRequest and call it's
            //request() method
            GETAPIRequest getapiRequest = new GETAPIRequest();
            //Attaching only part of URL as base URL is given
            //in our GETAPIRequest(of course that need to be same for all case)
            String url = "webapi.php?userId=1";
            getapiRequest.request(TestAPIActivity.this, fetchGetResultListener, url);
            Toast.makeText(TestAPIActivity.this, "GET API called", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void postApiCall() {
        try {
            //Create Instance of POSTAPIRequest and call it's
            //request() method
            POSTAPIRequest postapiRequest = new POSTAPIRequest();
            //Attaching only part of URL as base URL is given
            //in our POSTAPIRequest(of course that need to be same for all case)
            String url = "webapi.php";
            JSONObject params = new JSONObject();
            try {
                //Creating POST body in JSON format
                //to send in POST request
                params.put("userId", 2);
            } catch (Exception e) {
                e.printStackTrace();
            }
            postapiRequest.request(TestAPIActivity.this, fetchPostResultListener, params, url);
            Toast.makeText(TestAPIActivity.this, "POST API called", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

