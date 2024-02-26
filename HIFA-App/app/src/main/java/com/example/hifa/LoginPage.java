package com.example.hifa;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginPage#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginPage extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Button signUpButton;
    private Button loginButton;
    private EditText email;
    private EditText password;
    public LoginPage() {
        // Required empty public constructor
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LoginPage.
     */
    // TODO: Rename and change types and number of parameters
    public static LoginPage newInstance(String param1, String param2) {
        LoginPage fragment = new LoginPage();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        signUpButton = getView().findViewById(R.id.signupbutton2);
        loginButton = getView().findViewById(R.id.LoginButton);
        email = getView().findViewById(R.id.emailAddressEditText);
        password = getView().findViewById(R.id.passwordeditText);
        String emailString = email.getText().toString();
        String passwordString = password.getText().toString();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 boolean logininfofilled = false;
                if(emailString.isEmpty()){
                    email.setError("Email is required");
                    logininfofilled = true;
                }
                if(passwordString.isEmpty()){
                    password.setError("Password is required");
                    logininfofilled = true;
                }

            }
        });
        verifyUser(emailString, passwordString);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login_page, container, false);

    }

    private void verifyUser(String emailString, String passwordString) {
        DatabaseFirestore.verifyUser(emailString, passwordString, new DatabaseFirestore.CallBackverifyUser() {
            @Override
            public void onCallBack(User user) {
                if(user!=null){
                    //TODO: Use intents to pass the user object to the Home
                }
                else{
                    TextView txtView = (TextView)getView().findViewById(R.id.errorLoginTextView);
                    txtView.setVisibility(View.VISIBLE);
                }

            }
        });

    }
}