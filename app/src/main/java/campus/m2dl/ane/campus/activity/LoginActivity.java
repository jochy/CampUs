package campus.m2dl.ane.campus.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import campus.m2dl.ane.campus.R;
import campus.m2dl.ane.campus.thread.UserLoginTask;

public class LoginActivity extends AppCompatActivity {

    EditText mLoginView;
    EditText mPasswordView;
    View mProgressView;
    String username,password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mLoginView = (EditText) findViewById(R.id.usernameLogin);
        mPasswordView = (EditText) findViewById(R.id.password1Login);
        mProgressView = (View) findViewById(R.id.progressBar);
        showProgress(false);

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
        username = mLoginView.getText().toString();
        password = mPasswordView.getText().toString();
        /*BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap b = BitmapFactory.decodeFile(AppConfiguration.URI_CACHE+"Pic.png", options);
        //CacheService.getInstance().saveCacheFile("Pic.png", AppConfiguration.URI_CACHE,b);
        //Bitmap b= CacheService.getInstance().getCacheFile(AppConfiguration.URI_CACHE+"Pic.png");

        POI poi = new POI();

        poi.sender = new User("edsolat", "edsolat", "edsolat", "edsolat");
        poi.description = "test";
        poi.date = new Date();

        poi.image = b;
        poi.tagImg = TagImg.WATER;
        poi.position = new LatLng(43.566994, 1.470158);
        List<String> s = new ArrayList<String>();
        s.add("testTag");
        poi.tags = s;

        new SendPoiToDBTask(this).execute(poi);*/


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

                showProgress(true);

                new UserLoginTask(username,password,this,mPasswordView,mProgressView)
                        .execute("http://camp-us.net16.net/script_php/get_user.php");
            }
        }
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }





}
