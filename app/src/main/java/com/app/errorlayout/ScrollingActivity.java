package com.app.errorlayout;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ScrollingActivity extends AppCompatActivity {

    private AppBarErrorLayout appbar;
    private RelativeLayout toolbarError; // error layout container
    private TextView titleError; // error title
    private TextView textError; // error message
    private Button buttonError; // button "retry"
    private ProgressBar progressError; // progressbar when retry
    private FloatingActionButton fab;

    // access and find the views on error layout
    private void findViews() {
        appbar = (AppBarErrorLayout) findViewById(R.id.app_bar_layout);
        toolbarError = (RelativeLayout) findViewById(R.id.toolbar_error);
        titleError = (TextView) findViewById(R.id.toolbar_error_title);
        textError = (TextView) findViewById(R.id.toolbar_error_textview);
        buttonError = (Button) findViewById(R.id.toolbar_error_refresh);
        progressError = (ProgressBar) findViewById(R.id.toolbar_error_progress);
        fab = (FloatingActionButton) findViewById(R.id.fab_scrolling);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_widget);
        setSupportActionBar(toolbar);

        findViews();
        setEvents();

        // setting the error elements
        appbar.initErrorViews(toolbar, toolbarError);
    }

    private void setEvents() {
        // in my example, the FAB call the error layout to be displayed
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // display the error message (string examples)
                setErrorTexts(
                        getResources().getString(R.string.err_title_bad),
                        getResources().getString(R.string.err_message)
                );
                // show the error layout
                appbar.showErrorLayout();
            }
        });

        // create the retry request when the error button is clicked
        buttonError.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // display another title when retrying
                setErrorTexts(getResources().getString(R.string.err_title_retry), "");

                // show the error progressbar
                progressError.setVisibility(View.VISIBLE);
                // TEST: after 2sec, the request is successful, hide the
                // error layout
                appbar.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // hide progressbar for future usage
                        progressError.setVisibility(View.GONE);
                        // hide error layout
                        appbar.hideErrorLayout();
                    }
                }, 2000);
            }
        });
    }

    // showing the error messages when forcing to collapse
    private void setErrorTexts(String title, String msg) {
        titleError.setText(title);
        textError.setText(msg);
    }

    @Override
    public void onBackPressed() {
        // collapse and reset the toolbar if the error is shown
        if (appbar.getErrorVisibleState()) {
            appbar.hideErrorLayout();
            return;
        }
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_scrolling, menu);
        return true;
    }
}
