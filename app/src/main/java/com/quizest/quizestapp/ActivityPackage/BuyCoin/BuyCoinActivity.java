package com.quizest.quizestapp.ActivityPackage.BuyCoin;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.braintreepayments.api.BraintreeFragment;
import com.braintreepayments.api.Card;
import com.braintreepayments.api.PayPal;
import com.braintreepayments.api.exceptions.BraintreeError;
import com.braintreepayments.api.exceptions.ErrorWithResponse;
import com.braintreepayments.api.exceptions.InvalidArgumentException;
import com.braintreepayments.api.interfaces.BraintreeErrorListener;
import com.braintreepayments.api.interfaces.PaymentMethodNonceCreatedListener;
import com.braintreepayments.api.models.CardBuilder;
import com.braintreepayments.api.models.PayPalAccountNonce;
import com.braintreepayments.api.models.PayPalRequest;
import com.braintreepayments.api.models.PaymentMethodNonce;
import com.quizest.quizestapp.ActivityPackage.SettingActivity;
import com.quizest.quizestapp.AdapterPackage.SelectPaymentMethodAdapter;
import com.quizest.quizestapp.ModelPackage.AvaiableCoin;
import com.quizest.quizestapp.ModelPackage.PaymentMethodOption;
import com.quizest.quizestapp.R;
import com.quizest.quizestapp.UtilPackge.Util;

import java.util.List;

public class BuyCoinActivity extends AppCompatActivity implements BuyCoinView {

    ImageButton btnBack, btnSetting;
    AppCompatButton btnBuyCoin;
    TextView tvInitialCoinAmount, tvInitialCoinPrice;
    TextView tvCoinCurrency;
    BraintreeFragment braintreeFragment;
    EditText etCoinAmount, etCoinPrice;
    RecyclerView recyclerViewPaymentMethod;
    BuyCoinPresenter buyCoinPresenter;
    ImageView btnPayWithPaypal;
    ConstraintLayout layoutCardInfo, layoutPaypalInfo;
    Double coinPrice = 0.0;
    Integer coinId;
    public static Integer paymentId;
    String paymentNonce;
    Integer coinValue;
    /*card input section*/
    String clientToken;
    EditText etAccountHolderName,
            etCardNumber,
            etCvv,
            etExpireMonth,
            etExpireYear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_coin);
        buyCoinPresenter = new BuyCoinPresenter(this);
        initViews();
        initializeRecyclerView();
        buyCoinPresenter.getPaymentMethodInfo(this);
        buyCoinPresenter.getAvaibaleCoinInfo(this);
        initializeBrainTree();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(
                        BuyCoinActivity.this, SettingActivity.class
                ));
            }
        });

        braintreeFragment.addListener(new BraintreeErrorListener() {
            @Override
            public void onError(Exception error) {
                if (error instanceof ErrorWithResponse) {
                    ErrorWithResponse errorWithResponse = (ErrorWithResponse) error;
                    BraintreeError cardErrors = errorWithResponse.errorFor("creditCard");
                    if (cardErrors != null) {
                        // There is an issue with the credit card.
                        BraintreeError expirationMonthError = cardErrors.errorFor("expirationMonth");
                        if (expirationMonthError != null) {
                            // There is an issue with the expiration month.
                            Toast.makeText(BuyCoinActivity.this, expirationMonthError.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });

        braintreeFragment.addListener(new PaymentMethodNonceCreatedListener() {
            @Override
            public void onPaymentMethodNonceCreated(PaymentMethodNonce paymentMethodNonce) {
                if (paymentMethodNonce instanceof PayPalAccountNonce) {
                    //here paypal call
                    paymentNonce = paymentMethodNonce.getNonce();
                    try {
                        if (paymentNonce != null) {
                            buyCoinPresenter.buyCoin(
                                    BuyCoinActivity.this,
                                    coinId,
                                    paymentId,
                                    Integer.parseInt(etCoinAmount.getText().toString()
                                    ),
                                    paymentNonce
                            );
                        } else {
                            Toast.makeText(BuyCoinActivity.this, "Not ready for payment!", Toast.LENGTH_SHORT).show();
                        }
                    } catch (NumberFormatException e) {
                        Toast.makeText(BuyCoinActivity.this, "Coin amount can't be less than 1!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    //card call
                    paymentNonce = paymentMethodNonce.getNonce();
                    try {
                        if (paymentNonce != null) {
                            buyCoinPresenter.buyCoin(
                                    BuyCoinActivity.this,
                                    coinId,
                                    paymentId,
                                    Integer.parseInt(etCoinAmount.getText().toString()
                                    ),
                                    paymentNonce
                            );
                        } else {
                            Toast.makeText(BuyCoinActivity.this, "Not ready for payment!", Toast.LENGTH_SHORT).show();
                        }
                    } catch (NumberFormatException e) {
                        Toast.makeText(BuyCoinActivity.this, "Coin amount can't be less than 1!", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
        btnBuyCoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etAccountHolderName.getText().toString().trim().length() != 0
                        && etCardNumber.getText().toString().trim().length() != 0
                        && etExpireMonth.getText().toString().trim().length() != 0
                        && etExpireYear.getText().toString().trim().length() != 0
                        && etCvv.getText().toString().trim().length() != 0
                        && etCoinAmount.getText().toString().trim().length() != 0
                ) {
                    validateCard();
                } else {
                    Toast.makeText(BuyCoinActivity.this, "All field's are required!", Toast.LENGTH_SHORT).show();
                }

            }
        });

        btnPayWithPaypal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PayPalRequest request = new PayPalRequest(
                        etCoinPrice.getText().toString().replace("$",
                                ""))
                        .currencyCode("USD")
                        .intent(PayPalRequest.INTENT_AUTHORIZE);
                PayPal.requestOneTimePayment(braintreeFragment, request);
            }
        });

        coinPriceListener();
    }

    private void initializeRecyclerView() {
        recyclerViewPaymentMethod.setHasFixedSize(true);
        recyclerViewPaymentMethod.setLayoutManager(new LinearLayoutManager(
                this, LinearLayoutManager.HORIZONTAL, false
        ));
    }


    private void coinPriceListener() {
        etCoinAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @SuppressLint("DefaultLocale")
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    etCoinPrice.setText(String.format("$%.2f",
                            (Double.parseDouble(String.valueOf(s)) * coinPrice)));
                } catch (NumberFormatException e) {
                    etCoinPrice.setText("$0");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void initViews() {
        btnBack = findViewById(R.id.btn_back);
        btnSetting = findViewById(R.id.btn_setting);
        layoutCardInfo = findViewById(R.id.layout_card_info);
        layoutPaypalInfo = findViewById(R.id.layout_paypal_info);
        etAccountHolderName = findViewById(R.id.edit_text_card_holder_name);
        etCardNumber = findViewById(R.id.edit_text_card_number);
        etExpireMonth = findViewById(R.id.et_month);
        etExpireYear = findViewById(R.id.et_year);
        etCvv = findViewById(R.id.et_cvc);
        btnPayWithPaypal = findViewById(R.id.btn_pay_with_paypal);
        btnBuyCoin = findViewById(R.id.btn_buy_coin);
        etCoinAmount = findViewById(R.id.et_coin_amount);
        etCoinPrice = findViewById(R.id.et_coin_price);
        tvCoinCurrency = findViewById(R.id.tv_coin_name);
        tvInitialCoinAmount = findViewById(R.id.tv_inital_coin);
        tvInitialCoinPrice = findViewById(R.id.tv_initial_coin_price);
        recyclerViewPaymentMethod = findViewById(R.id.recycler_view_payment_method);
    }

    private void initializeBrainTree() {
        try {
            braintreeFragment = BraintreeFragment.newInstance(this, "sandbox_7bz56rh9_jhnyhpkzt8df28xc");
            // mBraintreeFragment is ready to use!
        } catch (InvalidArgumentException e) {
            // There was an issue with your authorization string.
        }
    }

    private void validateCard() {
        CardBuilder cardBuilder = new CardBuilder()
                .cardNumber(etCardNumber.getText().toString())
                .cardholderName(etAccountHolderName.getText().toString())
                .expirationMonth(etExpireMonth.getText().toString())
                .expirationYear(etExpireYear.getText().toString())
                .cvv(etCvv.getText().toString());
        Card.tokenize(
                braintreeFragment, cardBuilder
        );
    }

    @Override
    public void onSuccess(List<PaymentMethodOption> paymentMethodOptionList) {
        if (paymentMethodOptionList.get(0)
                .getPaymentName().contains("Paypal")) {
            layoutCardInfo.setVisibility(View.GONE);
            layoutPaypalInfo.setVisibility(View.VISIBLE);
            btnBuyCoin.setVisibility(View.GONE);
        } else if (paymentMethodOptionList.get(0)
                .getPaymentName().contains("Card")) {
            layoutCardInfo.setVisibility(View.VISIBLE);
            layoutPaypalInfo.setVisibility(View.GONE);
            btnBuyCoin.setVisibility(View.VISIBLE);
        }
        paymentId = paymentMethodOptionList.get(0).getId();
        SelectPaymentMethodAdapter selectPaymentMethodAdapter = new
                SelectPaymentMethodAdapter(paymentMethodOptionList, this, layoutPaypalInfo, layoutCardInfo, btnBuyCoin);
        recyclerViewPaymentMethod.setAdapter(selectPaymentMethodAdapter);
    }

    @Override
    public void onError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onCoinInfoGotSuccess(AvaiableCoin avaiableCoin) {
   /*     tvInitialCoinAmount.setText(String.format("%.0f Coin",
                Double.parseDouble(
                        avaiableCoin.getCoin().getAmount()
                )
        ));*/

        // coinValue = (int) Double.parseDouble(avaiableCoin.getCoin().getAmount());

        tvInitialCoinPrice.setText(
                String.format("$%s",
                        avaiableCoin.getCoin().getPrice()
                )
        );
        tvCoinCurrency.setText(
                avaiableCoin.getCoin().getName()
        );

        try {
            coinPrice = Double.parseDouble(avaiableCoin.getCoin().getPrice());
        } catch (NumberFormatException e) {
            Toast.makeText(this,"Coin price retrieve failed!", Toast.LENGTH_SHORT).show();
        }

        coinId = avaiableCoin.getCoin().getCoinId();
    }

    @Override
    public void onCoinInfoGotError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void buyCoinSuccess(String message, ProgressDialog progressDialog) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        finish();
    }

    @Override
    public void buyCoinError(String message, ProgressDialog progressDialog) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void onClientSettingError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClientSettingSuccess(String client_token) {
        clientToken = client_token;
    }

}
