package com.example.ngo2.ui.catalogue;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ngo2.R;

public class BooksDetails extends AppCompatActivity {

    String bookItemName, bookAuthor, bookCategory, bookIsbn, bookTotalQuantity, bookIssuedQuantity ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.books_details_activity);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            bookItemName = bundle.getString("bookItemName");
            bookAuthor = bundle.getString("bookAuthor");
            bookCategory = bundle.getString("bookCategory");
            bookIsbn = bundle.getString("bookIsbn");
            bookTotalQuantity = bundle.getString("bookTotalQuantity");
            bookIssuedQuantity = bundle.getString("bookIssuedQuantity");
        }

        TextView tv_profile_studentid = findViewById(R.id.tv_profile_studentid);
        TextView tv_bookdetails_category = findViewById(R.id.tv_bookdetails_category);
        TextView tv_bookdetails_author = findViewById(R.id.tv_bookdetails_author);
        TextView tv_bookdetails_isbn = findViewById(R.id.tv_bookdetails_isbn);
        TextView tv_bookdetails_total_quantity = findViewById(R.id.tv_bookdetails_total_quantity);
        TextView tv_bookdetails_issued_quantity = findViewById(R.id.tv_bookdetails_issued_quantity);

        tv_profile_studentid.setText(bookItemName);
        tv_bookdetails_author.setText(bookAuthor);
        tv_bookdetails_category.setText(bookCategory);
        tv_bookdetails_isbn.setText(bookIsbn);
        tv_bookdetails_total_quantity.setText(bookTotalQuantity);
        tv_bookdetails_issued_quantity.setText(bookIssuedQuantity);
    }
}
