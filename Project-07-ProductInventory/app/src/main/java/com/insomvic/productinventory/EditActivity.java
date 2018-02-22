package com.insomvic.productinventory;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.insomvic.productinventory.data.InventoryContract.InventoryEntry;

public class EditActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    private static final int EXISTING_INVENTORY_LOADER = 0;
    private Uri currentInventoryUri;
    private EditText productNameEditText;
    private EditText priceEditText;
    private EditText quantityEditText;
    private EditText supplierNameEditText;
    private EditText supplierPhoneEditText;
    private ImageButton quantityIncreaseImageButton;
    private ImageButton quantityDecreaseImageButton;
    private Button callSupplierButton;
    private boolean inventoryHasChanged = false;
    private View.OnTouchListener touchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            inventoryHasChanged = true;
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        // Get intent information (id number) from previous activity
        Intent intent = getIntent();
        currentInventoryUri = intent.getData();
        // If URI doesn't exist, we will allow the creation of one
        if (currentInventoryUri == null) {
            setTitle(getString(R.string.title_add_product));
            invalidateOptionsMenu();
        // If URI exists, we can edit the existing db information
        } else {
            setTitle(getString(R.string.title_edit_product));
            getLoaderManager().initLoader(EXISTING_INVENTORY_LOADER, null, this);
        }
        // Find views from layout
        productNameEditText = findViewById(R.id.edit_product_name);
        priceEditText = findViewById(R.id.edit_price);
        quantityEditText = findViewById(R.id.edit_quantity);
        supplierNameEditText = findViewById(R.id.edit_supplier_name);
        supplierPhoneEditText = findViewById(R.id.edit_supplier_phone);
        quantityIncreaseImageButton = findViewById(R.id.increase_quantity);
        quantityDecreaseImageButton = findViewById(R.id.decrease_quantity);
        callSupplierButton = findViewById(R.id.call_supplier);
        // Set listener for user input
        productNameEditText.setOnTouchListener(touchListener);
        priceEditText.setOnTouchListener(touchListener);
        quantityEditText.setOnTouchListener(touchListener);
        supplierNameEditText.setOnTouchListener(touchListener);
        supplierPhoneEditText.setOnTouchListener(touchListener);
        quantityIncreaseImageButton.setOnTouchListener(touchListener);
        quantityDecreaseImageButton.setOnTouchListener(touchListener);
        // Set onClick for quantity increase
        quantityIncreaseImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adjustQuantity(1);
            }
        });
        // Set onClick for quantity decrease
        quantityDecreaseImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adjustQuantity(-1);
            }
        });
        // Set onClick for calling supplier
        callSupplierButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callSupplier();
            }
        });
    }

    private void saveInventory() {
        // Get string from input fields
        String productNameString = productNameEditText.getText().toString().trim();
        String priceString = priceEditText.getText().toString().trim();
        String quantityString = quantityEditText.getText().toString().trim();
        String supplierNameString = supplierNameEditText.getText().toString().trim();
        String supplierPhoneString = supplierPhoneEditText.getText().toString().trim();
        // If a row is missing data, let user know all fields need to be inputted
        if (TextUtils.isEmpty(productNameString) || TextUtils.isEmpty(priceString) ||
                TextUtils.isEmpty(quantityString) || TextUtils.isEmpty(supplierNameString) ||
                TextUtils.isEmpty(supplierPhoneString)) {
            Toast.makeText(this, getString(R.string.missing_data), Toast.LENGTH_LONG).show();
            return;
        }
        // Put the custom strings into ContentValue's
        ContentValues values = new ContentValues();
        values.put(InventoryEntry.COLUMN_PRODUCT_NAME, productNameString);
        values.put(InventoryEntry.COLUMN_PRICE, priceString);
        values.put(InventoryEntry.COLUMN_QUANTITY, quantityString);
        values.put(InventoryEntry.COLUMN_SUPPLIER_NAME, supplierNameString);
        values.put(InventoryEntry.COLUMN_SUPPLIER_PHONE, supplierPhoneString);
        // Error handling
        if (currentInventoryUri == null) {
            // New item
            Uri newUri = getContentResolver().insert(InventoryEntry.CONTENT_URI, values);
            if (newUri == null) {
                Toast.makeText(this, getString(R.string.new_item_error), Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, getString(R.string.new_item_success), Toast.LENGTH_SHORT).show();
            }
        } else {
            // Edit item
            int rowsAffected = getContentResolver().update(currentInventoryUri, values, null, null);
            if (rowsAffected == 0) {
                Toast.makeText(this, getString(R.string.edit_item_error), Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, getString(R.string.edit_item_success), Toast.LENGTH_SHORT).show();
            }
        }
        // Return to main activity
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        // Delete menu item doesn't exist if input is new
        if (currentInventoryUri == null) {
            MenuItem menuItem = menu.findItem(R.id.delete);
            menuItem.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to check icon
            case R.id.save:
                saveInventory();
                return true;
            // Respond to delete icon
            case R.id.delete:
                showDeleteConfirmationDialog();
                return true;
            // Respond to back button on app bar or physical button
            case android.R.id.home:
                if (!inventoryHasChanged) {
                    NavUtils.navigateUpFromSameTask(EditActivity.this);
                    return true;
                }
                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                NavUtils.navigateUpFromSameTask(EditActivity.this);
                            }
                        };
                showUnsavedChangesDialog(discardButtonClickListener);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // If back button is pressed, make sure user has the option to save or discard changes, if any
    @Override
    public void onBackPressed() {
        if (!inventoryHasChanged) {
            super.onBackPressed();
            return;
        }
        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                };
        showUnsavedChangesDialog(discardButtonClickListener);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = {InventoryEntry._ID, InventoryEntry.COLUMN_PRODUCT_NAME, InventoryEntry.COLUMN_PRICE,
                InventoryEntry.COLUMN_QUANTITY, InventoryEntry.COLUMN_SUPPLIER_NAME, InventoryEntry.COLUMN_SUPPLIER_PHONE };
        return new CursorLoader(this, currentInventoryUri, projection, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        // If cursor is invalid, exit loader
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }
        if (cursor.moveToFirst()) {
            // Get column index for each item
            int productNameColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_PRODUCT_NAME);
            int priceColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_PRICE);
            int quantityColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_QUANTITY);
            int supplierNameColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_SUPPLIER_NAME);
            int supplierPhoneColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_SUPPLIER_PHONE);
            // Get appropriate data type for each column index
            String productName = cursor.getString(productNameColumnIndex);
            Float price = cursor.getFloat(priceColumnIndex);
            Integer quantity = cursor.getInt(quantityColumnIndex);
            String supplierName = cursor.getString(supplierNameColumnIndex);
            String supplierPhone = cursor.getString(supplierPhoneColumnIndex);
            // Set db text on EditText
            productNameEditText.setText(productName);
            priceEditText.setText(Float.toString(price));
            quantityEditText.setText(Integer.toString(quantity));
            supplierNameEditText.setText(supplierName);
            supplierPhoneEditText.setText(supplierPhone);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // Ensures each text field is blank on reset
        productNameEditText.setText("");
        priceEditText.setText("");
        quantityEditText.setText("");
        supplierNameEditText.setText("");
        supplierPhoneEditText.setText("");
    }

    // Dialog box to allow user to continue editing or discard changes
    private void showUnsavedChangesDialog(
            DialogInterface.OnClickListener discardButtonClickListener) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(getString(R.string.discard_changes));
                builder.setPositiveButton(getString(R.string.discard), discardButtonClickListener);
                builder.setNegativeButton(getString(R.string.continue_editing), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (dialog != null) {
                            dialog.dismiss();
                        }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    // Dialog box to allow user to delete or cancel deletion
    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.delete_product));
        builder.setPositiveButton(getString(R.string.delete), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                deleteInventory();
            }
        });
        builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    // Product deletion process (after user has confirmed)
    private void deleteInventory() {
        if (currentInventoryUri != null) {
            int rowsDeleted = getContentResolver().delete(currentInventoryUri, null, null);
            if (rowsDeleted == 0) {
                Toast.makeText(this, getString(R.string.delete_error), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.delete_success), Toast.LENGTH_SHORT).show();
            }
        }
        finish();
    }

    private void adjustQuantity(int amount) {
        EditText quantity = findViewById(R.id.edit_quantity);
        // Check if quantity text is empty
        if (!isEmptyEditText(quantity)) {
            Integer currentQuantity = Integer.parseInt(quantity.getText().toString());
            Integer newQuantity = currentQuantity + amount;
            // Quantity can't be set to a negative
            if (newQuantity >= 0) {
                quantity.setText(String.valueOf(newQuantity));
            }
        } else {
            // Set value of zero if no text
            quantity.setText(String.valueOf(0));
        }
    }

    private void callSupplier() {
        EditText callSupplier = findViewById(R.id.edit_supplier_phone);
        // Check if supplier phone number text is empty
        if (!isEmptyEditText(callSupplier)) {
            String phoneNumber = callSupplier.getText().toString();
            // Set new implicit intent to call the inserted number
            startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneNumber, null)));
        } else {
            // Let user know to input valid phone number
            Toast.makeText(this, getString(R.string.failed_to_call), Toast.LENGTH_SHORT).show();
        }
    }

    // Used to check if any EditText is empty
    private boolean isEmptyEditText(EditText editText) {
        return editText.getText().toString().trim().length() == 0;
    }

}
