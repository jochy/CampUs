package campus.m2dl.ane.campus.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
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

import campus.m2dl.ane.campus.R;

public class RegisterActivity extends AppCompatActivity {

    EditText firstnameField,lastnameField,loginField,
            password1Field,password2Field ;

    String firstname,lastname,login,password1,password2;

    ArrayList<EditText> fieldList = new ArrayList<EditText>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firstnameField = (EditText)findViewById(R.id.firstnameField);
        fieldList.add(firstnameField);
        lastnameField = (EditText)findViewById(R.id.lastnameField);
        fieldList.add(lastnameField);
        loginField = (EditText)findViewById(R.id.loginField);
        fieldList.add(loginField);
        password1Field = (EditText)findViewById(R.id.password1Field);
        fieldList.add(password1Field);
        password2Field = (EditText)findViewById(R.id.password2Field);
        fieldList.add(password2Field);

    }

    public void registerButton(View view)
    {
        boolean isError = false ;

        password1 = password1Field.getText().toString();
        password2 = password2Field.getText().toString();
        lastname = lastnameField.getText().toString();
        firstname = firstnameField.getText().toString();
        login = loginField.getText().toString();

        for (EditText field : fieldList)
        {
            if (field.getText().toString().equals(""))
            {
                field.setError("Veuillez remplir ce champ");
                field.setFocusable(true);
                isError = true;
            }
        }

        if (!password1.equals(password2))
        {
            password2Field.setError("Mot de passe différent");
            password2Field.setFocusable(true);
            isError = true;
        }

        if(!isError)
        {
            new RegisterTask().execute("http://camp-us.net16.net/script_php/register.php");
            //Check en base si login déja existant
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register, menu);
        return true;
    }


    public void cancelButton(View view)
    {
        this.finish();
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


    public class RegisterTask extends AsyncTask<String, Void, String> {

        String response = "";
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
                Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                response = "error";
                return "error";
            }

            return null;
        }

        @Override
        protected void onPostExecute(String success) {

            if (!response.trim().equals("error")) {
                Toast.makeText(getApplicationContext(), "Inscription valide", Toast.LENGTH_LONG).show();
                finish();
            }
            else {
                Toast.makeText(getApplicationContext(), "Login déja existant ", Toast.LENGTH_LONG).show();
            }
        }
    }

}





