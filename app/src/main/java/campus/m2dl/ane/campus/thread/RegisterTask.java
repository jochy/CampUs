package campus.m2dl.ane.campus.thread;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
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

/**
 * Created by Nabil on 23/01/16.
 */
public class RegisterTask extends AsyncTask<String, Void, String> {

    String response = "";
    String firstname ;
    String lastname;
    String login ;
    String password1 ;
    Context context ;



    public  RegisterTask(String firstname , String lastname,String login, String password1,
                         Context context)
    {
        this.firstname = firstname ;
        this.lastname = lastname ;
        this.login = login ;
        this.password1  = password1 ;
        this.context = context ;
    }


    @Override
    protected String doInBackground(String... urls) {

        try {

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(urls[0]);

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);
            nameValuePairs.add(new BasicNameValuePair("firstname",firstname ));
            nameValuePairs.add(new BasicNameValuePair("lastname",lastname ));
            nameValuePairs.add(new BasicNameValuePair("username",login));
            nameValuePairs.add(new BasicNameValuePair("password",password1 ));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));

            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            response = httpclient.execute(httppost, responseHandler);

        } catch (Exception e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
            response = "error";
            return "error";
        }

        return null;
    }

    @Override
    protected void onPostExecute(String success) {

        if (!response.trim().equals("error")) {
            Toast.makeText(context, "Inscription valide", Toast.LENGTH_LONG).show();
            ((Activity)context).finish();
        }
        else {
            Toast.makeText(context, "Login déja existant ", Toast.LENGTH_LONG).show();
        }
    }
}
