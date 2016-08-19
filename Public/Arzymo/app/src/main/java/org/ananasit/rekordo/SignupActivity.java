package org.ananasit.rekordo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import org.ananasit.rekordo.model.User;
import org.ananasit.rekordo.util.ApiHelper;
import org.ananasit.rekordo.util.Utils;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class SignupActivity extends AppCompatActivity {
    private static final String TAG = "SignupActivity";

    @InjectView(R.id.input_username) EditText _usernameText;
    @InjectView(R.id.input_email) EditText _emailText;
    @InjectView(R.id.input_password) EditText _passwordText;
    @InjectView(R.id.btn_signup) Button _signupButton;
    @InjectView(R.id.link_login) TextView _loginLink;

    String username;
    String email;
    String password;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.inject(this);

        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                finish();
                Intent in = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(in);
            }
        });
    }

    public void signup() {
        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed();
            return;
        }

        _signupButton.setEnabled(false);

        /*
        final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this,
                R.style.AppBaseTheme);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();
        */
        username = _usernameText.getText().toString();
        email = _emailText.getText().toString();
        password = _passwordText.getText().toString();

        SignupAsyncTask task = new SignupAsyncTask();
        task.execute(ApiHelper.REGISTER_URL);

       /*
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onSignupSuccess or onSignupFailed
                        // depending on success
                        onSignupSuccess();
                        // onSignupFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);
        */
    }


    public void onSignupSuccess() {
        _signupButton.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _signupButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String username = _usernameText.getText().toString();
        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (username.isEmpty() || username.length() < 3) {
            _usernameText.setError("at least 3 characters");
            valid = false;
        } else {
            _usernameText.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }

    private class SignupAsyncTask extends AsyncTask<String, Void, String> {
        private ProgressDialog pDialog;
        private String result = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = ProgressDialog.show(SignupActivity.this, "", "Регистрация...", true);
        }

        @Override
        protected String doInBackground(String... urls) {

            try
            {
                ApiHelper api = new ApiHelper();
                JSONObject response = api.signup(username, email, password);
                if(response.getBoolean("error"))
                {
                    result = response.getString("message");
                }
                else
                {
                    User user = new User();
                    user.setId(response.getString("id"));
                    user.setActivated(false);
                    user.setUserName(response.getString("username"));
                    user.setEmail(response.getString("email"));
                    user.setClient_key(response.getString("api_key"));

                    AppController appcon = AppController.getInstance();
                    appcon.setUser(user);
                    appcon.getPrefManager().saveUser(user);
                }
            }
            catch (Exception ex)
            {
                result = ex.getMessage();
                Log.d("Start activity", "Exception: " + result);
            }

            return result;
        }

        @Override
        protected void onPostExecute(String result)
        {
            pDialog.dismiss();
            if(!result.equals(""))
            {
                Toast.makeText(SignupActivity.this, result, Toast.LENGTH_SHORT).show();
            }
            else
            {
                Intent in = new Intent(SignupActivity.this, MainActivity.class);
                startActivity(in);
                finish();
            }
        }
    }
}