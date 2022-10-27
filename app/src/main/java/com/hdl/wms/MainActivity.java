package com.hdl.wms;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// Imports
public class MainActivity extends AppCompatActivity {

    String userId;
    String password;
    Button loginSub;
    EditText et_Email, et_Pass;
    APIInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        apiInterface = APIClient.getClient().create(APIInterface.class);

        loginSub = (Button) findViewById(R.id.loginSub);
        et_Email = (EditText) findViewById(R.id.edtEmail);
        et_Pass = (EditText) findViewById(R.id.edtPass);

        loginSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkValidation()) {

                    if (CommonMethod.isNetworkAvailable(MainActivity.this))
                        loginRetrofit2Api(userId, password);
                    else
                        CommonMethod.showAlert("Internet Connectivity Failure", MainActivity.this);
                }
            }
        });
    }

    private void loginRetrofit2Api(String userId, String password) {
        final LoginResponse login = new LoginResponse(userId, password);
        Call<LoginResponse> call1 = apiInterface.createUser(login);
        call1.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                LoginResponse loginResponse = response.body();

                Log.e("keshav", "loginResponse 1 --> " + loginResponse);
                if (loginResponse != null) {
                    Log.e("keshav", "getUserId          -->  " + loginResponse.getUserId());
                    Log.e("keshav", "getFirstName       -->  " + loginResponse.getFirstName());
                    Log.e("keshav", "getLastName        -->  " + loginResponse.getLastName());
                    Log.e("keshav", "getProfilePicture  -->  " + loginResponse.getProfilePicture());

                    String responseCode = loginResponse.getResponseCode();
                    Log.e("keshav", "getResponseCode  -->  " + loginResponse.getResponseCode());
                    Log.e("keshav", "getResponseMessage  -->  " + loginResponse.getResponseMessage());
                    if (responseCode != null && responseCode.equals("404")) {
                        Toast.makeText(MainActivity.this, "Invalid Login Details \n Please try again", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "Welcome " + loginResponse.getFirstName(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "onFailure called ", Toast.LENGTH_SHORT).show();
                call.cancel();
            }
        });
    }

    public boolean checkValidation() {
        userId = et_Email.getText().toString();
        password = et_Pass.getText().toString();

        Log.e("Keshav", "userId is -> " + userId);
        Log.e("Keshav", "password is -> " + password);

        if (et_Email.getText().toString().trim().equals("")) {
            CommonMethod.showAlert("UserId Cannot be left blank", MainActivity.this);
            return false;
        } else if (et_Pass.getText().toString().trim().equals("")) {
            CommonMethod.showAlert("password Cannot be left blank", MainActivity.this);
            return false;
        }
        return true;




    }
}