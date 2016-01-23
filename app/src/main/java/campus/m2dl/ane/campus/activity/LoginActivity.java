package campus.m2dl.ane.campus.activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import campus.m2dl.ane.campus.R;
import campus.m2dl.ane.campus.thread.UserLoginTask;

import static campus.m2dl.ane.campus.service.ProgressBar.showProgress;

public class LoginActivity extends AppCompatActivity {

    EditText mLoginView;
    public EditText mPasswordView;
    public View mProgressView;
    String username,password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mLoginView = (EditText) findViewById(R.id.usernameLogin);
        mPasswordView = (EditText) findViewById(R.id.password1Login);
        mProgressView = (View) findViewById(R.id.progressBar);
        showProgress(getResources(), mProgressView, false);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
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

    public void registerButton(View view)
    {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    public void validateButton(View view)
    {
        UserLoginTask login = new UserLoginTask(this);
        username = mLoginView.getText().toString();
        password = mPasswordView.getText().toString();
        String params[] = {"http://camp-us.net16.net/script_php/get_user.php",username, password};

        if (!isOnline()) {
            Toast.makeText(getApplicationContext(), "Pas de réseau Internet", Toast.LENGTH_LONG).show();
        }
        else {
            if (username.length() == 0) {
                mLoginView.setError("Il me faut votre login !");
                mLoginView.requestFocus();
            } else if (password.length() == 0) {
                mPasswordView.setError("Il me faut votre mot de passe !");
                mPasswordView.requestFocus();
            } else {

                showProgress(getResources(), mProgressView , true);
                login.execute(params);
            }
        }
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

}
