package campus.m2dl.ane.campus.thread;

import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

import campus.m2dl.ane.campus.activity.CampUsActivity;
import campus.m2dl.ane.campus.activity.LoginActivity;

import static campus.m2dl.ane.campus.service.ProgressBar.showProgress;

/**
 * Created by edouard on 23/01/16.
 */
public class UserLoginTask extends AsyncTask<String, Void, Boolean> {
    private String response = "", url, username, password;
    EditText mPasswordView;
    View mProgressBar;
    private LoginActivity mActivity;

    public UserLoginTask(LoginActivity activity) {
        mActivity = activity;
        mPasswordView = mActivity.mPasswordView;
        mProgressBar = mActivity.mProgressView;
    }

    @Override
    protected Boolean doInBackground(String... params) {
        url = params[0];
        username = params[1];
        password = params[2];

        showProgress(mActivity.getResources(), mPasswordView , true);
        try {

            HttpClient httpclient = new DefaultHttpClient();
            /*"http://camp-us.net16.net/script_php/get_user.php"*/
            HttpPost httppost = new HttpPost(url);

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("username",username ));
            nameValuePairs.add(new BasicNameValuePair("password", password));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));

            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            response = httpclient.execute(httppost, responseHandler);

        } catch (Exception e) {
            e.printStackTrace();
            response = "error";
            return false;
        }

        if (!response.trim().equals("error")) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean success) {

        if (success) {
            Toast.makeText(mActivity.getApplicationContext(), "Bienvenue " + username + " !", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(mActivity.getApplicationContext(), CampUsActivity.class);
            mActivity.startActivity(intent);
            mActivity.finish();
        }
        else {
            //Toast.makeText(getApplicationContext(), "Login ou mot de passe incorrect !", Toast.LENGTH_LONG).show();
            mActivity.mPasswordView.setError("Mot de passe incorrect");
            mActivity.mPasswordView.requestFocus();
        }

        showProgress(mActivity.getResources(), mProgressBar, false
        );
    }

}
