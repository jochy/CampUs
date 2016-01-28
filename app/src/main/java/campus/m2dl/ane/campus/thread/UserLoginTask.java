package campus.m2dl.ane.campus.thread;

import android.app.Activity;
import android.content.Context;
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

import campus.m2dl.ane.campus.AppConfiguration;
import campus.m2dl.ane.campus.activity.CampUsActivity;
import campus.m2dl.ane.campus.model.User;
import campus.m2dl.ane.campus.service.MessageService;

import static campus.m2dl.ane.campus.activity.components.ProgressBar.showProgress;

/**
 * Created by Nabil on 23/01/16.
 */
public class UserLoginTask extends AsyncTask<String, Void, String> {

    private String response = "";
    private String username, password;
    private Context context;
    private EditText mPasswordView;
    private View mProgressView;

    public UserLoginTask(String username, String password, Context context, EditText mPasswordView,
                         View mProgressView) {
        this.username = username;
        this.password = password;
        this.context = context;
        this.mPasswordView = mPasswordView;
        this.mProgressView = mProgressView;
    }

    @Override
    protected String doInBackground(String... urls) {

        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(AppConfiguration.URL_LOGIN);

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("username", username));
            nameValuePairs.add(new BasicNameValuePair("password", password));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));

            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            response = httpclient.execute(httppost, responseHandler);
        } catch (Exception e) {
            //Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
            response = "error";
            return "error";
        }

        return null;
    }

    @Override
    protected void onPostExecute(String success) {
        if (!response.trim().equals("error")) {
            MessageService.currentUser = new User(username, null, null, null);

            Toast.makeText(context, "Bienvenue " + username + " !", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(context, CampUsActivity.class);
            context.startActivity(intent);
            ((Activity) context).finish();
        }
        else {
            Toast.makeText(context, "Login ou mot de passe incorrect !", Toast.LENGTH_LONG).show();
            mPasswordView.setError("Mot de passe incorrect");
            mPasswordView.requestFocus();
        }

        showProgress(context.getResources(), mProgressView, false);
    }

}
