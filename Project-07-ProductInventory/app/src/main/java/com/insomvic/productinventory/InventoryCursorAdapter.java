package com.insomvic.productinventory;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.insomvic.productinventory.data.InventoryContract.InventoryEntry;

import java.util.Locale;

public class InventoryCursorAdapter extends CursorAdapter {

    public InventoryCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // Find all TextView's for product information
        TextView productNameTextView = view.findViewById(R.id.product_name);
        TextView priceTextView = view.findViewById(R.id.price);
        TextView quantityTextView = view.findViewById(R.id.quantity);
        TextView supplierNameTextView = view.findViewById(R.id.supplier_name);
        TextView supplierPhoneTextView = view.findViewById(R.id.supplier_phone);
        // Get column index for the product
        int idColumnIndex = cursor.getColumnIndex(InventoryEntry._ID);
        int productNameColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_PRODUCT_NAME);
        int priceColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_PRICE);
        int quantityColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_QUANTITY);
        int supplierNameColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_SUPPLIER_NAME);
        int supplierPhoneColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_SUPPLIER_PHONE);
        // Retrieve data as correct data type from column index
        String id = cursor.getString(idColumnIndex);
        String productName = cursor.getString(productNameColumnIndex);
        Float price = cursor.getFloat(priceColumnIndex);
        Integer quantity = cursor.getInt(quantityColumnIndex);
        String supplierName = cursor.getString(supplierNameColumnIndex);
        String supplierPhone = cursor.getString(supplierPhoneColumnIndex);
        // If null or empty, set specific details to "Unknown" value
        if (productName == null || productName.equals("")) {
            productName = context.getString(R.string.unknown);
        }
        if (supplierName == null || supplierName.equals("")) {
            supplierName = context.getString(R.string.unknown);
        }
        if (supplierPhone == null || supplierPhone.equals("")) {
            supplierPhone = context.getString(R.string.unknown);
        }
        // Set text using placeholders from strings.xml
        productNameTextView.setText(productName);
        priceTextView.setText(String.format(context.getString(R.string.main_price), dollarFormat(price)));
        quantityTextView.setText(String.format(context.getString(R.string.main_quantity), Integer.toString(quantity)));
        supplierNameTextView.setText(String.format(context.getString(R.string.main_supplier_name), supplierName));
        supplierPhoneTextView.setText(String.format(context.getString(R.string.main_supplier_phone), supplierPhone));
        // Set onClick for sale button
        saleOnClick(view, id, productName, price, quantity);
    }

    // Logic for sale. Does not work if there is a quantity of zero
    private void saleOnClick(View view, final String id, final String productName, final float price, final int quantity) {
        TextView textView = view.findViewById(R.id.sale);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                // Check there is quantity for product
                if (quantity > 0) {
                    adjustQuantity(context, id, quantity, -1);
                    Toast.makeText(context, String.format(context.getString(R.string.sale_success), productName, dollarFormat(price)), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, String.format(context.getString(R.string.sale_fail), productName), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Update new quantity to db based on id
    private void adjustQuantity(Context context, String id, int quantity, int amount) {
        ContentValues values = new ContentValues();
        Integer updatedQuantity = quantity + amount;
        values.put(InventoryEntry.COLUMN_QUANTITY, updatedQuantity);
        context.getContentResolver().update(InventoryEntry.CONTENT_URI, values, "_id="+id, null);
    }

    // Format to #.##
    private String dollarFormat(Float price) {
        return String.format(Locale.US, "%.02f", price);
    }

}