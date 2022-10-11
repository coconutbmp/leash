package com.coconutbmp.leash;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class EditProfileDialog extends Dialog {
    Button accept, cancel;
    EditText name, surname, email, pass;
    String currName, currSurname, currEmail, currPass;
    Context context;
    SharedPreferences prefs;
    public EditProfileDialog(Activity owner, @NonNull Context context, String currName, String currSurname, String currEmail, String currPass) {
        super(context);
        setContentView(R.layout.dialog_edit_profile);
        this.context = context;
        this.currName = currName;
        this.currSurname = currSurname;
        this.currEmail = currEmail;
        this.currPass = currPass;
        this.setOwnerActivity(owner);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        accept = findViewById(R.id.btnAcceptEdit);
        cancel = findViewById(R.id.btnCancelEdit);
        name = findViewById(R.id.edtEditName);
        surname = findViewById(R.id.edtEditSurname);
        email = findViewById(R.id.edtEditEmail);
        pass = findViewById(R.id.edtEditPass);
        prefs = context.getSharedPreferences("UserPrefs", MODE_PRIVATE);

        accept.setOnClickListener(view -> {
            completeEdit(name.getText().toString(), surname.getText().toString(), email.getText().toString(), pass.getText().toString());
        });

        cancel.setOnClickListener(view -> {
            this.dismiss();
        });
    }

    /**
     * @param Name
     * @param Surname
     * @param Email
     * @param Pass
     * If empty email, don't change anything
     * else if not valid, prompt correction
     * else change details
     */
    public void completeEdit(String Name, String Surname, String Email, String Pass){
        SharedPreferences.Editor editor = prefs.edit();
        InternetRequest ir = new InternetRequest();
        JSONObject params = new JSONObject();
        String myName, mySurname, myEmail, myPass;

        if(Name.equals("")){
            myName = currName;
        }
        else{
            myName = Name;
            if(!loginUtils.validateName(myName)){
                Toast.makeText(context, "Invalid Name", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        if(Surname.equals("")){
            mySurname = currSurname;
        }
        else{
            mySurname = Surname;
            if(!loginUtils.validateName(mySurname)){
                Toast.makeText(context, "Invalid Surname", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        if(Email.equals("")){
            myEmail = currEmail;
        }
        else{
            myEmail = Email;
            if(!loginUtils.validateEmail(myEmail)){
                Toast.makeText(context, "Invalid Email", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        if(Pass.equals("")){
            myPass = currPass;
        }
        else{
            myPass = currPass;
            if(!loginUtils.validatePass(Pass, null)){
                Toast.makeText(context, "Password must be at least 8 characters and contain A-Z, a-z, 0-9", Toast.LENGTH_SHORT).show();
                return;
            }
            else{
                try {
                    myPass = loginUtils.Hash(Pass);
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (InvalidKeySpecException e) {
                    e.printStackTrace();
                }
            }
        }
        try{
            params.put("userID", Data.getUserID());
            params.put("userFirstName", myName);
            params.put("userLastName", mySurname);
            params.put("userEmail", myEmail);
            params.put("userPass", myPass);
            String finalMyPass = myPass;
            ir.doRequest(InternetRequest.std_url + "update_userdetails.php", getOwnerActivity(), params, new RequestHandler() {
                @Override
                public void processResponse(String response) {
                    if(response.equals("success")){
                        getOwnerActivity().runOnUiThread(()->{
                            editor.putString("email", myEmail);
                            editor.putString("pass", finalMyPass);
                            editor.commit();
                            Toast.makeText(context, response, Toast.LENGTH_SHORT).show();
                            dismiss();
                        });
                    }
                    else{
                        Toast.makeText(context, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
            });
        }
        catch (JSONException e){
            Toast.makeText(context, "Something Went Wrong", Toast.LENGTH_SHORT).show();
            return;
        }
    }
}
