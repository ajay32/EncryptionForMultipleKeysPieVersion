package com.ajay.encryptdecryptstring;


import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;




public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String SAMPLE_ALIAS = "MYALIAS";
    MyActivityPresenter mm;
    String s1,s2,s3,s4;
    SharedPreferences pref = null;




    private EnCryptor encryptor;
    private DeCryptor decryptor;
    byte[] ffb = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         pref = this.getSharedPreferences("MyPref", 0); // 0 - for priva

        encryptor = new EnCryptor(this);

        try {
            decryptor = new DeCryptor();
        } catch (CertificateException | NoSuchAlgorithmException | KeyStoreException |
                IOException e) {
            e.printStackTrace();
        }

  mm = new MyActivityPresenter();

      mm.encryptedData(
              "I am string to encrypt",
             "123456" ,this, "s1");

        mm.encryptedData(
                "I am string to encrypt is my name is coll or not you tell firsot ",
                "123456", this, "s2");

       mm.encryptedData(
                "I am string to encrypt mogamo klhush hua tum aksie",
                "123456", this, "s3");

       mm.encryptedData(
                "I am string to encrypt wshat is ltif eoe4t lelatr",
                "123456", this, "s4");

    }



    public void decryptoa(View view) {
        mm.decryptedData("123456" , pref.getString("s1",""  ) , "s1");
        mm.decryptedData("123456" ,  pref.getString("s2", ""), "s2");
        mm.decryptedData("123456",  pref.getString("s3", "") , "s3" );
        mm.decryptedData("123456" ,  pref.getString("s4", "") , "s4");
    }
}
