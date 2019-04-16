package com.example.gigpig;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class SignUpActivity extends Activity {


    private FirebaseAuth mAuth;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_signup);
    }

    public void backButtonPressed(View view) {
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
    }

    public void signUpButtonPressed(View view) {
        final String firstname = ((EditText)findViewById(R.id.firstname)).getText().toString();
        System.out.println(firstname);
        final String lastname = ((EditText)findViewById(R.id.lastname)).getText().toString();
        System.out.println(lastname);
        final String email = ((EditText)findViewById(R.id.email)).getText().toString();
        System.out.println(email);
        final String password = ((EditText)findViewById(R.id.password)).getText().toString();
        System.out.println(password);
        final String number = ((EditText)findViewById(R.id.number)).getText().toString();
        System.out.println(number);
        final String username = ((EditText)findViewById(R.id.username)).getText().toString();
        System.out.println(username);
        String tags = ((EditText)findViewById(R.id.email)).getText().toString();
        System.out.println(tags);
        final ArrayList<String> tagsArray = new ArrayList<String>(Arrays.asList(tags.split(" ")));
        final String bio = ((EditText)findViewById(R.id.bio)).getText().toString();
        final TextView message = (TextView) findViewById(R.id.message);

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            DatabaseHelper.writeNewUser(new User(user.getUid(), firstname, lastname, number, username, tagsArray, bio));
                            message.setText("Success!");
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            message.setText("Failed. Try again.");
                            updateUI(null);
                        }
                    }
                });
    }

    public void updateUI(FirebaseUser user) {
        if(user != null) {
            Intent i = new Intent(getApplicationContext(), NavigationActivity.class);
            startActivity(i);
        }
    }
}
