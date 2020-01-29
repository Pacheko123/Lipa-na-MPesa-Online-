package com.pacheko.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.androidstudy.daraja.Daraja;
import com.androidstudy.daraja.DarajaListener;
import com.androidstudy.daraja.constants.Transtype;
import com.androidstudy.daraja.model.AccessToken;
import com.androidstudy.daraja.model.LNMExpress;
import com.androidstudy.daraja.model.LNMResult;
import com.androidstudy.daraja.util.Env;
import com.androidstudy.daraja.util.TransactionType;



public class MainActivity extends AppCompatActivity {
    Button pay;
    EditText phoneNumber,amount;
    private ProgressBar loader;
    String enter,getCash;
    Daraja daraja;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pay = findViewById(R.id.pay);
        phoneNumber = findViewById(R.id.phone);
        amount = findViewById(R.id.amount);
        loader = findViewById(R.id.toNext);
        daraja = Daraja.with("Uku3wUhDw9z0Otdk2hUAbGZck8ZGILyh", "JDjpQBm5HpYwk38b", new DarajaListener<AccessToken>() {
            @Override
            public void onResult(@NonNull AccessToken accessToken) {
                loader.setVisibility(View.GONE);
                Log.i(MainActivity.this.getClass().getSimpleName(), accessToken.getAccess_token());
                Toast.makeText(MainActivity.this, "TOKEN : " + accessToken.getAccess_token(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error) {
                loader.setVisibility(View.GONE);
                Log.e(MainActivity.this.getClass().getSimpleName(), error);
            }
        });

        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loader.setVisibility(View.VISIBLE);
                //Get Phone Number from User Input
                enter = phoneNumber.getText().toString().trim().replace(" ","");
                getCash = amount.getText().toString().trim().replace(" ","");

                if (TextUtils.isEmpty(enter) || TextUtils.isEmpty(getCash)) {
                    loader.setVisibility(View.GONE);
                    //Toast.makeText(this,"Please input both amount and phone number",Toast.LENGTH_LONG).show();
                    return;
                }



    LNMExpress lnmExpress = new LNMExpress(
            "174379",
            "bfb279f9aa9bdbcf158e97dd71a467cd2e0c893059b10f78e6b72ada1ed2c919",
            TransactionType.CustomerPayBillOnline , // TransactionType.CustomerBuyGoodsOnline, // TransactionType.CustomerPayBillOnline  <- Apply one of these two
            getCash,
            "254742202957",
            "174379",
            enter,
            "http://mycallbackurl.com/checkout.php",
            "001ABC",
            "Pay For Goods"
    );

                daraja.requestMPESAExpress(lnmExpress,
                        new DarajaListener<LNMResult>() {
                            @Override
                            public void onResult(@NonNull LNMResult lnmResult) {
                                Log.i(MainActivity.this.getClass().getSimpleName(), lnmResult.ResponseDescription);
                                loader.setVisibility(View.GONE);
                            }

                            @Override
                            public void onError(String error) {
                                Log.i(MainActivity.this.getClass().getSimpleName(), error);
                                loader.setVisibility(View.GONE);
                            }
                        });
                }
            });
        }
}
