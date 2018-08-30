package zimmermann.larissa.moveitcollector.fragments;


import android.app.AlertDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import zimmermann.larissa.moveitcollector.MainActivity;
import zimmermann.larissa.moveitcollector.R;
import zimmermann.larissa.moveitcollector.database.Researcher;
import zimmermann.larissa.moveitcollector.repository.LoginActivityViewModel;
import zimmermann.larissa.moveitcollector.utils.ConnectionUtils;
import zimmermann.larissa.moveitcollector.utils.Gender;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {

    private static final String TAG = LoginFragment.class.getSimpleName();
    private static final int RC_SIGN_IN = 9001;

    private LoginActivityViewModel mLoginActivityViewModel;

    private GoogleSignInClient mGoogleSignInClient;

    @BindView(R.id.progress_bar)
    public ProgressBar mProgressBar;
    @BindView(R.id.sign_in_button)
    public SignInButton mSignInButton;
    @BindView(R.id.logo)
    ImageView ivLogo;

    private Unbinder unbinder;

    public LoginFragment() {
    }

    @Override
    public void onStart() {
        super.onStart();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getActivity());
        updateUI(account);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        unbinder = ButterKnife.bind(this, view);

        mLoginActivityViewModel = ViewModelProviders.of(getActivity()).get(LoginActivityViewModel.class);

        mSignInButton.setSize(SignInButton.SIZE_STANDARD);
        mProgressBar.setVisibility(View.GONE);

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .requestProfile()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), googleSignInOptions);

        if(!ConnectionUtils.isConnected(getActivity())) {
            hideProgressBar();
            mSignInButton.setVisibility(View.GONE);
            errorDialog(R.string.network_connection_error);
        }

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if(requestCode == RC_SIGN_IN) {
            hideProgressBar();
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
        else {
            Log.e(TAG, "onActivityResult: NOT SIGN IN PROCEDURE");
        }
    }

    @OnClick(R.id.sign_in_button)
    public void signIn() {
        showProgressBar();
        Intent intent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(intent, RC_SIGN_IN);
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            updateUIAndInsertInformation(account);
        } catch (ApiException e) {
            Log.w(TAG, "signInResult:: failed code=" + e.getStatusCode());
            updateUIAndInsertInformation(null);
        }
    }

    private void startFragment(GoogleSignInAccount account) {
        if(account != null) {
            Researcher researcher = new Researcher(
                    account.getGivenName() + " " + account.getFamilyName(),
                    new Date("14/12/1991"),
                    account.getEmail(),
                    "",
                    Gender.F,
                    account.getPhotoUrl().toString()
            );

            mLoginActivityViewModel = new LoginActivityViewModel(getActivity().getApplication());
            mLoginActivityViewModel.insert(researcher);

            FragmentManager manager = getActivity().getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.addSharedElement(ivLogo, ivLogo.getTransitionName());
            transaction.replace(R.id.flContainer, new ResearcherRegisterFragment(), LoginFragment.class.getName());
            transaction.addToBackStack(null);
            transaction.commit();

            //Intent intent = new Intent(this, MainActivity.class);
            //startActivity(intent);
            //finish();
        }
    }

    private void startApp(GoogleSignInAccount account) {
        if(account != null) {
            Intent intent = new Intent(getContext(), MainActivity.class);
            getActivity().startActivity(intent);
            getActivity().finish();
        }
    }

    private void errorDialog(final int messageId) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.Theme_AppCompat_DayNight_Dialog_Alert);
                builder.setTitle(R.string.connection_error);
                builder.setMessage(getResources().getString(messageId));
                builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                        Snackbar.make(getActivity().findViewById(android.R.id.content), getString(R.string.check_network_connection), Snackbar.LENGTH_LONG)
                                .setActionTextColor(Color.GRAY)
                                .show();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    private void showProgressBar() {
        mProgressBar.setVisibility(View.VISIBLE);
        mSignInButton.setVisibility(View.GONE);
    }

    private void hideProgressBar() {
        mProgressBar.setVisibility(View.GONE);
        mSignInButton.setVisibility(View.VISIBLE);
    }

    private void updateUIAndInsertInformation(GoogleSignInAccount account) {
        if(account != null) {
            saveAccountInstance(account);
            hideProgressBar();
            startFragment(account);
        }
    }

    private void updateUI(GoogleSignInAccount account) {
        if(account != null) {
            saveAccountInstance(account);
            hideProgressBar();
            startApp(account);
        }
    }

    private void saveAccountInstance(GoogleSignInAccount account) {
        SharedPreferences sharedPref = getActivity().getSharedPreferences(MainActivity.RESEARCHER_INFO_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(MainActivity.RESEARCHER_EMAIL, account.getEmail());
        editor.putString(MainActivity.RESEARCHER_PROFILE_URL, account.getPhotoUrl().toString());
        editor.commit();
        editor.apply();
    }
}
