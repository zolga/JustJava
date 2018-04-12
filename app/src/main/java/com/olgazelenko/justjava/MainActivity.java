package com.olgazelenko.justjava;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;

public class MainActivity extends AppCompatActivity {
    final int COFFEE_PRICE = 5;
    final int WHIPPED_CREAM_PRICE = 2;
    final int CHOCOLATE_PRICE = 2;
    final String EMAIL_ADDRESS = "order@gmail.com";
    final String EMAIL_SUBJECT = "Coffe Order";
    final String STATE_PRICE = "price";
    final String STATE_QUANTITY = "quantity";
    final String STATE_CHECK_BOX_WHIPPED_CREAM = "whipped";
    final String STATE_CHECK_BOX_CHOCOLATE = "chocolate";
    final String STATE_USER_NAME = "name";
    int quantity = 0;
    int orderPrice;
    private TextView quantityTextView;
    private TextView priceTextView;
    private CheckBox whippedCreamCheckBox;
    private CheckBox chocolateCheckBox;
    private EditText nameEditText;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_PRICE, orderPrice);
        outState.putInt(STATE_QUANTITY, quantity);
        outState.putBoolean(STATE_CHECK_BOX_WHIPPED_CREAM, whippedCreamCheckBox.isChecked());
        outState.putBoolean(STATE_CHECK_BOX_CHOCOLATE, chocolateCheckBox.isChecked());
        outState.putString(STATE_USER_NAME, nameEditText.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        orderPrice = savedInstanceState.getInt(STATE_PRICE);
        quantity = savedInstanceState.getInt(STATE_QUANTITY);
        whippedCreamCheckBox.setChecked(savedInstanceState.getBoolean(STATE_CHECK_BOX_WHIPPED_CREAM));
        chocolateCheckBox.setChecked(savedInstanceState.getBoolean(STATE_CHECK_BOX_CHOCOLATE));
        nameEditText.setText(savedInstanceState.getString(STATE_USER_NAME));
        setQuantityAndPrice();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            quantity = 1;
            orderPrice = COFFEE_PRICE;
        }
        initialStates();
    }

    /**
     * This method is called from the onCreate method and initial all the global variables.
     */
    private void initialStates() {
        quantityTextView = findViewById(R.id.quantity_text_view);
        priceTextView = findViewById(R.id.price_text_view);
        whippedCreamCheckBox = findViewById(R.id.whippedCream);
        chocolateCheckBox = findViewById(R.id.chocolate_checkbox);
        nameEditText = findViewById(R.id.name);
        setQuantityAndPrice();
    }


    private void setQuantityAndPrice()
    {
        displayPrice(orderPrice);
        setQuantity(quantity);
    }
    /**
     * This method displays the given price on the screen.
     */
    private void displayPrice(int number) {
        Log.v("MainActivity", "number: ");
        priceTextView.setText(NumberFormat.getCurrencyInstance().format(number));
    }

    private void setQuantity(int newQuantity) {
        Log.v("MainActivity", "newQuantity: ");
        quantityTextView.setText("" + newQuantity);
    }

    /**
     * This method is called when the + button is clicked and its increase the quantity by 1 and updating the total price order
     */
    public void increaseQuantity(View view) {
        if (quantity == 100) {
            Toast.makeText(this, R.string.validation_increase, Toast.LENGTH_SHORT).show();
            return;
        }
        quantity = quantity + 1;
        setQuantity(quantity);
        calculateTotalPrice();
        displayPrice(orderPrice);
    }

    private void calculateTotalPrice() {
        int total = 0;
        if (whippedCreamCheckBox.isChecked())
            total += WHIPPED_CREAM_PRICE;
        if (chocolateCheckBox.isChecked())
            total += CHOCOLATE_PRICE;
        orderPrice = quantity * (total + COFFEE_PRICE);
        Log.v("MainActivity", "calculateTotalPrice -> new price: ");
    }

    /**
     * This method is called when the - button is clicked and its decrease the quantity by 1 and updating the total price order
     */
    public void decreaseQuantity(View view) {
        if (quantity == 1) {
            Toast.makeText(this, R.string.validation_decrease, Toast.LENGTH_SHORT).show();
            return;
        }
        quantity = quantity - 1;
        setQuantity(quantity);
        calculateTotalPrice();
        displayPrice(orderPrice);
    }

    public void updateTopping(View view) {
        calculateTotalPrice();
        displayPrice(orderPrice);
    }


    /**
     * This method is called when the order button is clicked, its display summary of the order.
     */
    public void submitOrder(View view) {
        String message = createOrderSummary();
        composeEmail(message);
    }

    /**
     * Create summary of the order.
     */
    private String createOrderSummary() {
        String priceMessage = nameEditText.getText().toString();
        priceMessage += "\nAdd whipped cream? " + whippedCreamCheckBox.isChecked();
        priceMessage += "\nAdd chocolate? " + chocolateCheckBox.isChecked();
        priceMessage += "\nQuantity: " + quantity;
        priceMessage += "\nTotal: $" + orderPrice;
        priceMessage += "\nThank you!";
        return priceMessage;
    }

    public void composeEmail(String body) {
        String[] cc_emails = new String[]{"aaa@gmail.com"};
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_EMAIL, EMAIL_ADDRESS);
        intent.putExtra(Intent.EXTRA_SUBJECT, EMAIL_SUBJECT);
        intent.putExtra(Intent.EXTRA_CC, cc_emails);
        intent.putExtra(Intent.EXTRA_TEXT, body);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}



