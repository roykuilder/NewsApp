package com.example.android.newsapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class NewsItemAdapter extends ArrayAdapter<NewsItem> {

    /**
     * ViewHolder class is used to link the Object to the Views.
     */

    class ViewHolder {
        private TextView titleView;
        private TextView authorView;
        private TextView sectionView;

        public ViewHolder(View view) {
            this.titleView = view.findViewById(R.id.headline_view);
            this.authorView = view.findViewById(R.id.byline_view);
            this.sectionView = view.findViewById(R.id.section_view);
        }
    }

    /**
     * Constructor for a new NewsItemAdapter.
     */
    public NewsItemAdapter(Context context, List<NewsItem> list) {
        super(context, 0, list);
    }

    ViewHolder views;

    /**
     * Make a new ListItem View and set the data from the corresponding NewsItem Object.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if there is an existing list item view (called convertView) that we can reuse,
        // otherwise, if convertView is null, then inflate a new list item layout.
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.news_list_item, parent, false);
            // Use a ViewHolder to reverence the View for the listItem.
            views = new ViewHolder(convertView);
            convertView.setTag(views);
        } else {
            views = (ViewHolder) convertView.getTag();
        }

        // Find the current News Item
        final NewsItem currentNewsItem = getItem(position);

        // Set the text from the current News item tot the title View.
        views.titleView.setText(currentNewsItem.getHeadLine());

        // Set the description from the current Bok item to the description View.
        views.sectionView.setText(currentNewsItem.getSection());

        // Make a String from the authors ArrayList and set it to the authors View.
        views.authorView.setText(currentNewsItem.getAuthor());

        // Set an OnClickListener on the ListItem and get the link from the current NewsItem.
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String infoLink = currentNewsItem.getInfoLink();
                Intent goToWebPage = new Intent(Intent.ACTION_VIEW);
                goToWebPage.setData(Uri.parse(infoLink));
                getContext().startActivity(goToWebPage);
            }
        });

        // Return the list item view that is now showing the appropriate data
        return convertView;
    }
}
