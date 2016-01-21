package campus.m2dl.ane.campus;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class RegisterActivity extends AppCompatActivity {

    EditText firstnameField,lastnameField,loginField,
            password1Field,password2Field ;

    String firstname,lastname,login,password1,password2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firstnameField = (EditText)findViewById(R.id.firstnameField);
        lastnameField = (EditText)findViewById(R.id.firstnameField);
        loginField = (EditText)findViewById(R.id.firstnameField);
        password1Field = (EditText)findViewById(R.id.firstnameField);
        password2Field = (EditText)findViewById(R.id.firstnameField);

    }

    public void registerButton(View view)
    {

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
}
