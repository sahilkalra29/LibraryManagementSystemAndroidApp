package com.example.ngo2.ui.dashboard;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import com.example.ngo2.R;

import androidx.appcompat.app.AppCompatActivity;

public class IssuedBookDetails extends AppCompatActivity {

    String issuedbookName, issuedbookISBN, issuedbookIssuedDate, issuedbookReturnDate, issuedbookFine ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.issued_book_details_activity);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            issuedbookName = bundle.getString("issuedbookName");
            issuedbookISBN = bundle.getString("issuedbookISBN");
            issuedbookIssuedDate = bundle.getString("issuedbookIssuedDate");
            issuedbookReturnDate = bundle.getString("issuedbookReturnDate");
            issuedbookFine = bundle.getString("issuedbookFine");
        }

        TextView tv_issuedbook_name = findViewById(R.id.tv_issuedbook_name);
        TextView tv_issuedbook_isbn = findViewById(R.id.tv_issuedbook_isbn);
        TextView tv_issuedbook_issueddate = findViewById(R.id.tv_issuedbook_issueddate);
        TextView tv_issuedbook_returndate = findViewById(R.id.tv_issuedbook_returndate);
        TextView tv_issuedbook_fine = findViewById(R.id.tv_issuedbook_fine);

        tv_issuedbook_name.setText(issuedbookName);
        tv_issuedbook_isbn.setText(issuedbookISBN);
        tv_issuedbook_issueddate.setText(issuedbookIssuedDate);
        tv_issuedbook_fine.setText(issuedbookFine);
        tv_issuedbook_returndate.setText(issuedbookReturnDate);

        if(issuedbookReturnDate.equals("Not Returned Yet"))
        {
            tv_issuedbook_returndate.setTextColor(Color.parseColor("#FF0000"));
        }
    }
}
