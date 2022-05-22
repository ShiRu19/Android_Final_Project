package t4.sers;

import android.accounts.Account;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.tasks.Task;

import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private final int RC_GET_TOKEN = 9002;
    private GoogleSignInClient signInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_hello);

        String GOOGLE_CLIENT_ID = getString(R.string.google_client_ID);

        validateServerClientID();

        GoogleSignInOptions gso = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestScopes(new Scope(Scopes.PROFILE))
                .requestIdToken(GOOGLE_CLIENT_ID)
                .requestEmail()
                .build();

        signInClient = GoogleSignIn.getClient(this, gso);

        signOut();

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

        new Handler().postDelayed(() -> {
            if(account == null) {
                setContentView(R.layout.activity_login);
            }else{
                wentLobby(account);
            }
        }, 2000);

    }

    public void googleOAuthLogin(View view){
        Intent googleLoginIntent = signInClient.getSignInIntent();
        startActivityForResult(googleLoginIntent, RC_GET_TOKEN);
    }

    private void validateServerClientID() {
        String serverClientId = getString(R.string.google_client_ID);
        String suffix = ".apps.googleusercontent.com";
        if (!serverClientId.trim().endsWith(suffix)) {
            String message = "Invalid server client ID in strings.xml, must end with " + suffix;
            Log.w("Warning", message);
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_GET_TOKEN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try{
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            wentLobby(account);
        }catch (ApiException e){
            System.out.println(e.getStatusCode());
            e.printStackTrace();
        }
    }

    private void wentLobby(@NonNull GoogleSignInAccount account){
        Intent intent = new Intent(MainActivity.this, LobbyActivity.class);
        intent.putExtra("email", account.getEmail());
        intent.putExtra("displayName", account.getDisplayName());
        intent.putExtra("idToken", account.getIdToken());
        startActivity(intent);
        finish();
    }

    private void signOut() {
        signInClient.signOut();
    }
}