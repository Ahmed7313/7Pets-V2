package com.noon.a7pets.Rgisteration;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.noon.a7pets.Common;
import com.noon.a7pets.MainActivity;
import com.noon.a7pets.R;
import com.noon.a7pets.networksync.CheckInternetConnection;
import com.noon.a7pets.usersession.UserSession;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;


public class SignInActivity extends AppCompatActivity {

    TextView signUpText, pls_verify_email, resend_verify_email, forget_password;
    Button signInButton, retryBtnSignIn;
    EditText editTextEmail, editTextPassword;
    private FirebaseAuth mAuth;
    private DatabaseReference userRef;
    private FirebaseFirestore mFireStore;
    private FirebaseUser user;
    private LinearLayout mainView, splashScreen, noConnectionViewSignIn;
    //private CallbackManager mCallbackManager,faceBookCallbackManager;
    private String TAG = "SignInActivity";
    private Button login_button;
    private ProgressDialog progressDialog;
    public static int signInNumber = 0;
    private DatabaseReference deviceTokenRef;
    String userId;
    private boolean haseAUser ;
    private ImageView signInGoogle, buttonFacebookLogin;
    private TextView appname,forgotpass,registernow;
    private LottieAnimationView googleSignInAnimation;
    boolean isAnimated=false;
    ProgressDialog p;

    SignInButton googleBtn;
    GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 9001;
    private String email,pass,sessionmobile;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference mDatabaseReference = database.getReference();

    private UserSession session;
    private String sessionEmail,sessionPass,sessionMobile,sessionName,sessionPhoto;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        //check Internet Connection
        new CheckInternetConnection(this).checkConnection();

        Typeface typeface = ResourcesCompat.getFont(this, R.font.blacklist);
        appname = findViewById(R.id.appname);
        appname.setTypeface(typeface);

        session = new UserSession(getApplicationContext());
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.facebook.samples.loginhowto",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }


        progressDialog = new ProgressDialog(SignInActivity.this);
        googleSignInAnimation = findViewById(R.id.google_btn_loading_animation);
        googleSignInAnimation.setVisibility(View.INVISIBLE);
//        FacebookSdk.sdkInitialize(getApplicationContext());
//        AppEventsLogger.activateApp(this);

        signInGoogle = findViewById(R.id.signIn_google_btn);
//        signInGoogle.setSize(SignInButton.SIZE_STANDARD);
//        buttonFacebookLogin = findViewById(R.id.buttonFacebookLogin);
//        faceBookCallbackManager = CallbackManager.Factory.create();

        mAuth = FirebaseAuth.getInstance();
        userRef = FirebaseDatabase.getInstance().getReference().child("Users");
        mFireStore = FirebaseFirestore.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();

//        DoHeavyOperations doHeavyOperations  = new DoHeavyOperations();
//        doHeavyOperations.execute(haseAUser);


// Configure sign-in to request the user's ID, email address, and basic
// profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        signInGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInGoogle.setVisibility(View.INVISIBLE);
                googleSignInAnimation.setVisibility(View.VISIBLE);
                if (!isAnimated){
                    googleSignInAnimation.playAnimation();
                    googleSignInAnimation.setSpeed(3f);
                    isAnimated = true;
                }
                signInGoogle();
            }
        });

//        signInGoogle.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                p = new ProgressDialog(SignInActivity.this);
//                p.setMessage("Please wait...");
//                p.setIndeterminate(false);
//                p.setCancelable(false);
//                p.show();
//                signInGoogle();
//            }
//        });


//        faceBookCallbackManager = CallbackManager.Factory.create();
//
//        buttonFacebookLogin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                LoginManager.getInstance().logInWithReadPermissions(SignInActivity.this, Arrays.asList("email", "public_profile"));
//                LoginManager.getInstance().registerCallback(faceBookCallbackManager, new FacebookCallback<LoginResult>() {
//                    @Override
//                    public void onSuccess(LoginResult loginResult) {
//                        Log.d(TAG, "facebook:onSuccess:" + loginResult);
//                        handleFacebookAccessToken(loginResult.getAccessToken());
//                    }
//
//                    @Override
//                    public void onCancel() {
//                        Log.d(TAG, "facebook:onCancel");
//                        // ...
//                    }
//
//                    @Override
//                    public void onError(FacebookException error) {
//                        Log.d(TAG, "facebook:onError", error);
//                        // ...
//                    }
//                });
//            }
//        });


        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        //pls_verify_email = (TextView) findViewById(R.id.pls_verify_email);
        forget_password = (TextView)findViewById(R.id.forget_password);

        forget_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //     Intent forgetPassword = new Intent(SignInActivity.this, ResetPasswordActivity.class);
                //   startActivity(forgetPassword);
            }
        });

        mainView = (LinearLayout) findViewById(R.id.mainView);
        //splashScreen = (LinearLayout) findViewById(R.id.splashScreen);
        //noConnectionViewSignIn = (LinearLayout) findViewById(R.id.noConnectionViewSignIn);

        signUpText = (TextView) findViewById(R.id.signUpText);
        signUpText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignInActivity.this, SignUpActivity.class));
            }
        });


        signInButton = (Button) findViewById(R.id.buttonSignIn);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

    }




    private void signInGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // ...
            }
        }
    }



    private void firebaseAuthWithGoogle(final GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            final FirebaseUser user = mAuth.getCurrentUser();

                            final String userId = mAuth.getCurrentUser().getUid();
                            DatabaseReference currentUserDb = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);

                            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.hasChild(userId)){
                                        Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        FirebaseAuth mAuth = FirebaseAuth.getInstance();
                                        String currentUserID = mAuth.getCurrentUser().getUid();
                                        deviceTokenRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserID);
                                        String deviceToken = FirebaseInstanceId.getInstance().getToken();
                                        deviceTokenRef.child("device_token").setValue(deviceToken);
                                        startActivity(intent);

                                        haseAUser = true;
                                    } else {
                                        Intent intent = new Intent(SignInActivity.this, CompleteSignUpData.class);
                                        intent.putExtra("Username", account.getDisplayName());
                                        if (account.getPhotoUrl().toString() !=null){
                                            intent.putExtra("PhotoURL", account.getPhotoUrl().toString());
                                        }
                                        intent.putExtra("Email", user.getEmail());
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }

                            });

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                        }

                        // ...
                    }
                });
    }
    private void signIn() {
        logIn();
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check for existing Google Sign In account, if the user is already signed in
// the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

        //if user is already logged in then ShiftSwapFragment will open instead of SignInActivity
        if (Common.isNetworkAvailable(SignInActivity.this) || Common.isWifiAvailable(SignInActivity.this)) {
            new CheckInternetConnection(this).checkConnection();
            //splashScreen.setVisibility(View.VISIBLE);
            mainView.setVisibility(View.GONE);
            //noConnectionViewSignIn.setVisibility(View.GONE);
            final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                Task<Void> userTask = user.reload();
                userTask.addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
//                    if (user != null) {
                        boolean isEmailVerified = user.isEmailVerified();
                        if (isEmailVerified) {
                            Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        } else {
                            Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        }
//                    }
                    }
                });
            } else {
                //splashScreen.setVisibility(View.GONE);
                mainView.setVisibility(View.VISIBLE);
            }
        } else {
            mainView.setVisibility(View.GONE);
            noConnectionViewSignIn.setVisibility(View.VISIBLE);
        }

//        if (user != null) {
//            boolean isEmailVerified = user.isEmailVerified();
//            if (isEmailVerified) {
//                Intent intent = new Intent(SignInActivity.this, NavDrawerActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                startActivity(intent);
//                finish();
//            } else {
//                Intent intent = new Intent(SignInActivity.this, VerifyActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                startActivity(intent);
//                finish();
//            }
//        }

    }


    private void logIn() {

        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (email.isEmpty()) {
            editTextEmail.setError("Please enter your email");
            editTextEmail.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Please enter a valid email");
            editTextEmail.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            editTextPassword.setError("Please enter your password");
            editTextPassword.requestFocus();
            return;
        }
        if (password.length() < 6) {
            editTextPassword.setError("Minimum length of password is 6");
            editTextPassword.requestFocus();
            return;
        }

        signInButton.setVisibility(View.GONE);

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                signInButton.setVisibility(View.VISIBLE);
                if (task.isSuccessful()) {
                    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

//                    if (user != null) {
                    Task<Void> userTask = user.reload();
                    userTask.addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            boolean isEmailVerified = user.isEmailVerified();
                            if (isEmailVerified) {
                                Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            } else {
                                editTextEmail.setError("Your email is not verified.");
                                editTextEmail.requestFocus();
                                pls_verify_email.setVisibility(View.VISIBLE);
//                                    resend_verify_email.setVisibility(View.VISIBLE);
                                signInButton.setVisibility(View.VISIBLE);
                                FirebaseAuth.getInstance().signOut();


                            }

                            String currentUserID = mAuth.getCurrentUser().getUid();
                            String deviceToken = FirebaseInstanceId.getInstance().getToken();

                            userRef.child(currentUserID).child("device_token")
                                    .setValue(deviceToken)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {

                                            }
                                        }
                                    });
                        }



                    });
                } else {
                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        });
    }



    private class DoHeavyOperations extends AsyncTask {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChild(userId)){
//                        Intent intent = new Intent(SignInActivity.this, NavDrawerActivity.class);
////                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
////
////                        FirebaseAuth mAuth = FirebaseAuth.getInstance();
////                        String currentUserID = mAuth.getCurrentUser().getUid();
////                        deviceTokenRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserID);
////                        String deviceToken = FirebaseInstanceId.getInstance().getToken();
////                        deviceTokenRef.child("device_token").setValue(deviceToken);
////
////                        startActivity(intent);
                        haseAUser = true;
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            return haseAUser;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
        }
    }

}