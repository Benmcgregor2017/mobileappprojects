/*

    OVERVIEW OF MAIN_ACTIVITY.JAVA

    This activity is where the user can log into the application. They do this by entering their email address and respective password
    if they have already created an account. Once the values are filled into the editText views and the Log in button is pressed, the
    authentication process begins. If it passes, an intent is used to switch to the HomeActivity. If it fails a toast message appears on
    the screen telling the user the authentication failed. The Register Button below the Login button sends the user to the RegisterActivity
    If the user is already logged in, the Onstart method sends the user to the HomeActivity.

 */
package com.example.movieapplication;

//Imports used
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends Activity  {
    //Declaring the view objects used in this Activity
    Button b1,b2;
    EditText email_main,password_main;

    //TextView tx1;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Grab a FireBase instance to be used for authentication
        mAuth = FirebaseAuth.getInstance();

        //Login and Register buttons initialized
        b1 = (Button)findViewById(R.id.login);
        b2 = (Button)findViewById(R.id.register);

        //Email and Password fields initialized.
        email_main = (EditText)findViewById(R.id.email);
        password_main = (EditText)findViewById(R.id.password);


        //tx1 = (TextView)findViewById(R.id.textView3);
        //tx1.setVisibility(View.GONE);

        //OnClickListener used in the Log in button.
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String user_email = email_main.getText().toString();
                final String user_password = password_main.getText().toString();

                //Authentication Process
                mAuth.signInWithEmailAndPassword(user_email, user_password)
                        .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d("EmailPassword", "signInWithEmail:success");
                                    Toast.makeText(MainActivity.this, "Authentication Passed.",
                                            Toast.LENGTH_SHORT).show();
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    updateUI(user);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w("EmailPassword", "signInWithEmail:failure", task.getException());
                                    Toast.makeText(MainActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                    updateUI(null);
                                    // ...
                                }

                                // ...
                            }
                        });
            }
        });

        //OnClick Listener for the Register button
       b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Go to RegisterActivity
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });




    }

    //Function is called when App starts up. Checks to see if the user is logged in and then calls the UpdateUI function
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    //Called when the a Firebaseuser variable is changed or When one needs to check if a user is logged in. Sends user to HomeActivity if not null.
    public void updateUI(FirebaseUser User){
        if(User!=null){
            Intent home_intent = new Intent(MainActivity.this, HomeActivity.class);
            startActivity(home_intent);
        }else{
        }
    }
}