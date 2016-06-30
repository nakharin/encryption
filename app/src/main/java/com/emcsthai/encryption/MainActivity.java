package com.emcsthai.encryption;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;

public class MainActivity extends AppCompatActivity {

    private EditText edtInput;

    private Button btnEncode;
    private Button btnDecode;

    private TextView txtResult;

    private AesCbcWithIntegrity.SecretKeys keys;
    private AesCbcWithIntegrity.CipherTextIvMac cipherTextIvMac;

    private String cipherTextString = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initInstance();

        btnEncode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    cipherTextIvMac = AesCbcWithIntegrity.encrypt(edtInput.getText().toString(), keys);
                } catch (UnsupportedEncodingException | GeneralSecurityException e) {
                    e.printStackTrace();
                }
                //store or send to server
                cipherTextString = cipherTextIvMac.toString();

                txtResult.setText(cipherTextString);
                edtInput.setText("");
            }
        });

        btnDecode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    //Use the constructor to re-create the CipherTextIvMac class from the string:
                    cipherTextIvMac = new AesCbcWithIntegrity.CipherTextIvMac(txtResult.getText().toString());
                    String plainText = AesCbcWithIntegrity.decryptString(cipherTextIvMac, keys);
                    edtInput.setText(plainText);
                } catch (IllegalArgumentException | UnsupportedEncodingException | GeneralSecurityException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void initInstance() {

        try {
            keys = AesCbcWithIntegrity.generateKey();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }

        edtInput = (EditText) findViewById(R.id.edt_input);

        btnEncode = (Button) findViewById(R.id.btn_encode);
        btnDecode = (Button) findViewById(R.id.btn_decode);

        txtResult = (TextView) findViewById(R.id.txt_result);
    }
}
