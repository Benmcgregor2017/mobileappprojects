/*
    OVERVIEW OF REGISTER_ACTIVITY.JAVA

    This activity is used when one wants to make an account with Google Firebase. There are 2 editText fields, 2 buttons, and 1 textview. The
    email and password editText views are used to create both an email and password for the user. Clicking the create button after these fields are filled out
    creates an account for the user. The user can then login with these credentials. The cancel button simply takes the user back to the login screen.



 */
package com.example.movieapplication;

//Imports used in this activity
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

public class RegisterActivity extends Activity {

    //View Objects used in this activity
    EditText email, password;
    Button create,cancel;

    //Firebase object
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Get instance of firebase database
        mAuth = FirebaseAuth.getInstance();

        //Initialize view objects
        email = (EditText)findViewById(R.id.email);
        password = (EditText)findViewById(R.id.password);
        create = (Button)findViewById((R.id.login));
        cancel = (Button)findViewById((R.id.register));

        //Create account button listener. Clicking after inputting text into the email and password editText fields will
        //successfully create the account for the user.
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Converting email and password results into string variables
                String user_email = email.getText().toString();
                String user_password = password.getText().toString();

                //Account creation happens here.
                mAuth.createUserWithEmailAndPassword(user_email, user_password).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d("EmailPassword", "createUserWithEmail:success");
                                    Toast.makeText(RegisterActivity.this, "Account Created.",
                                            Toast.LENGTH_SHORT).show();
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    updateUI(user);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w("EmailPassword", "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                    updateUI(null);
                                }

                                // ...
                            }
                        });
            }
        });

        //Cancel button onClickListener. Sends user back to MainActivity
        cancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }

    //Similar to MainActivity updateUI. Sends user to HomeActivity
    public void updateUI(FirebaseUser user){
        if(user != null){
            Intent intent = new Intent(RegisterActivity.this, HomeActivity.class);
            startActivity(intent);
        }else{

        }
    }
}
