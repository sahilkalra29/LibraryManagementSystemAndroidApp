package com.example.ngo2.ui.catalogue;

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

public class CatalogueListAdapter extends ArrayAdapter {

    List list = new ArrayList<>();
    private Context mcontext;

    public CatalogueListAdapter(Context context, int resource) {

        super(context,resource);
        mcontext = context;
    }

    public void add(CatalogueList object){
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
        catalogueHolder catalogueHolder;
        if(row == null)
        {
            LayoutInflater layoutinflator = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutinflator.inflate(R.layout.books_catalogue_row_layout,parent,false);
            catalogueHolder = new catalogueHolder();
            catalogueHolder.tx_bookname = (TextView) row.findViewById(R.id.tx_bookname);
            /*catalogueHolder.tx_author = (TextView) row.findViewById(R.id.tx_author);
            catalogueHolder.tx_category = (TextView) row.findViewById(R.id.tx_category);
            catalogueHolder.tx_isbn = (TextView) row.findViewById(R.id.tx_isbn);
            catalogueHolder.tx_totalquantity = (TextView) row.findViewById(R.id.tx_totalquantity);
            catalogueHolder.tx_issuedquantity = (TextView) row.findViewById(R.id.tx_issuedquantity);*/
            row.setTag(catalogueHolder);

        }
        else
        {
            catalogueHolder = (catalogueHolder) row.getTag();
        }
        CatalogueList catalogueList = (CatalogueList) this.getItem(position);

        catalogueHolder.tx_bookname.setText(catalogueList.getBookname());
        /*catalogueHolder.tx_author.setText(catalogueList.getAuthor());
        catalogueHolder.tx_category.setText(catalogueList.getCategory());
        catalogueHolder.tx_isbn.setText(catalogueList.getIsbn());
        catalogueHolder.tx_totalquantity.setText(catalogueList.getTotal_quantity());
        catalogueHolder.tx_issuedquantity.setText(catalogueList.getIssued_quantity());*/
        row.setOnClickListener(v -> {

                Intent i = new Intent( mcontext.getApplicationContext(), BooksDetails.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra("bookItemName", catalogueList.getBookname());
                i.putExtra("bookAuthor", catalogueList.getAuthor());
                i.putExtra("bookCategory", catalogueList.getCategory());
                i.putExtra("bookIsbn", catalogueList.getIsbn());
                i.putExtra("bookTotalQuantity", catalogueList.getTotal_quantity());
                i.putExtra("bookIssuedQuantity", catalogueList.getIssued_quantity());
                mcontext.startActivity(i);
        });
        return row;
    }

    @Override
    public int getCount(){
        return list.size();
    }

    static class catalogueHolder
    {
        TextView tx_bookname;
    }

}
