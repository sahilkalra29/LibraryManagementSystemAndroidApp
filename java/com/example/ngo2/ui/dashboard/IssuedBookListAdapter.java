package com.example.ngo2.ui.dashboard;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.ngo2.R;

import java.util.ArrayList;
import java.util.List;

public class IssuedBookListAdapter extends ArrayAdapter {

    List list = new ArrayList<>();
    private Context mcontext;

    public IssuedBookListAdapter(Context context, int resource) {

        super(context,resource);
        mcontext = context;
    }

    public void add(IssuedBookList object){
        super.add(object);
        list.add(object);
    }

    @Nullable
    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row;
        row = convertView;
        issuedbookHolder issuedbookHolder;
        if(row == null)
        {
            LayoutInflater layoutinflator = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutinflator.inflate(R.layout.issued_books_row_layout,parent,false);
            issuedbookHolder = new issuedbookHolder();
            issuedbookHolder.tx_issuedbookname = (TextView) row.findViewById(R.id.tx_issued_bookname);
            row.setTag(issuedbookHolder);
        }
        else
        {
            issuedbookHolder = (issuedbookHolder) row.getTag();
        }
        IssuedBookList issuedBookList = (IssuedBookList) this.getItem(position);

        issuedbookHolder.tx_issuedbookname.setText(issuedBookList.getBookname());

        row.setOnClickListener(v -> {

                Intent i = new Intent( mcontext.getApplicationContext(), IssuedBookDetails.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra("issuedbookName", issuedBookList.getBookname());
                i.putExtra("issuedbookISBN", issuedBookList.getIsbn());
                i.putExtra("issuedbookIssuedDate", issuedBookList.getIssued_date());
                i.putExtra("issuedbookReturnDate", issuedBookList.getReturn_date());
                i.putExtra("issuedbookFine", issuedBookList.getFine());
                mcontext.startActivity(i);
        });
        return row;
    }

    @Override
    public int getCount(){
        return list.size();
    }

    static class issuedbookHolder
    {
        TextView tx_issuedbookname;
    }

}
