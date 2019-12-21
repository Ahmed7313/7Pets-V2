package com.noon.a7pets.Rgisteration;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.noon.a7pets.MainActivity;
import com.noon.a7pets.R;
import com.noon.a7pets.User;
import com.noon.a7pets.usersession.UserSession;

public class CompleteSignUpData extends Activity {

    EditText editTextPhone;

    private String phoneNumber, username, email, photoURL;

    private UserSession session;

    private Button finishSignInBtn;
    private DatabaseReference deviceTokenRef;
    private FirebaseAuth mAuth;
    private DatabaseReference userRef;
    private String sessionEmail,sessionPass,sessionMobile,sessionName,sessionPhoto;
    String currentUserID;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference mDatabaseReference = database.getReference();
    private User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_sign_up_data);

        mAuth = FirebaseAuth.getInstance();
        session = new UserSession(getApplicationContext());

        Intent intent = getIntent();
        username = intent.getExtras().getString("Username");
        photoURL = intent.getExtras().getString("PhotoURL");
        email = intent.getExtras().getString("Email");

        editTextPhone = findViewById(R.id.editTextPhone_google);
        phoneNumber = editTextPhone.getText().toString().trim();



        finishSignInBtn = findViewById(R.id.finish_ggogle_signUp);
        finishSignInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserInfoToFirebaseDatabase();
            }
        });

    }



    private void saveUserInfoToFirebaseDatabase() {


        final String phoneNumber = editTextPhone.getText().toString().trim();
        String userId = mAuth.getCurrentUser().getUid();
        DatabaseReference currentUserDb = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);
        user = new User(username, phoneNumber, email, photoURL);

        if (photoURL != null) {
            finishSignInBtn.setVisibility(View.GONE);
            String sessionName = user.getName();
            String sessionMail = user.getEmail();
            String sessionMobile = user.getMobile();
            String sessionPhoto = user.getPhoto();
            //create shared preference and store data
            session.createLoginSession(sessionName,sessionMail,sessionMobile,sessionPhoto);

            currentUserDb.setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    finishSignInBtn.setVisibility(View.VISIBLE);
                    //Set the user Device token if he signed in using google
                    FirebaseAuth mAuth = FirebaseAuth.getInstance();
                    String currentUserID = mAuth.getCurrentUser().getUid();
                    deviceTokenRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserID);
                    String deviceToken = FirebaseInstanceId.getInstance().getToken();
                    deviceTokenRef.child("device_token").setValue(deviceToken);

                    Intent intent = new Intent(CompleteSignUpData.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    finishSignInBtn.setVisibility(View.VISIBLE);
                    Toast.makeText(CompleteSignUpData.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        } else {
            finishSignInBtn.setVisibility(View.GONE);
            user = new User(username, phoneNumber, email);
            currentUserDb.setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    finishSignInBtn.setVisibility(View.VISIBLE);
                    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                    currentUser.sendEmailVerification();

                    user = new User(username, phoneNumber, email, null);
                    String sessionName = user.getName();
                    String sessionMail = user.getEmail();
                    String sessionMobile = user.getMobile();
                    String sessionPhoto = user.getPhoto();
                    //create shared preference and store data
                    session.createLoginSession(sessionName,sessionMail,sessionMobile,sessionPhoto);

                    //Set the user Device token if he signed in using google
                    FirebaseAuth mAuth = FirebaseAuth.getInstance();
                    currentUserID = mAuth.getCurrentUser().getUid();
                    deviceTokenRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserID);
                    String deviceToken = FirebaseInstanceId.getInstance().getToken();
                    deviceTokenRef.child("device_token").setValue(deviceToken);

                    Intent intent = new Intent(CompleteSignUpData.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    finishSignInBtn.setVisibility(View.VISIBLE);
                    Toast.makeText(CompleteSignUpData.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private void countFirebaseValues() {

        mDatabaseReference.child(currentUserID).child("cart").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.e(dataSnapshot.getKey(),dataSnapshot.getChildrenCount() + "");
                session.setCartValue((int)dataSnapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mDatabaseReference.child(currentUserID).child("wishlist").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.e(dataSnapshot.getKey(),dataSnapshot.getChildrenCount() + "");
                session.setWishlistValue((int)dataSnapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}