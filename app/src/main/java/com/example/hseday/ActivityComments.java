package com.example.hseday;

import android.os.Bundle;
import android.app.Activity;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.MenuItem;

import com.example.hseday.RecyclerViewAdapters.RecyclerViewAdapterComments;
import com.example.hseday.RecyclerViewAdapters.RecyclerViewAdapterOrganisations;

public class ActivityComments extends AppCompatActivity {
    private String[] commentsTextList;
    private String[] commentsNamesList;

    private RecyclerView mRecyclerView;
    private RecyclerViewAdapterComments mAdapter;
    private StaggeredGridLayoutManager mGridLayoutManager;
    private FloatingActionButton floatingActionButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Комментарии");

        commentsTextList = getResources().getStringArray(R.array.comments_comments);
        commentsNamesList = getResources().getStringArray(R.array.comments_names);

        mAdapter = new RecyclerViewAdapterComments(this, commentsTextList, commentsNamesList);
        mRecyclerView = (RecyclerView) findViewById(R.id.comments_recycler_view);
        floatingActionButton = (FloatingActionButton) findViewById(R.id.comments_floating_button);
        mGridLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(mGridLayoutManager);
        mAdapter.setHasStableIds(true);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy)
            {
                if (floatingActionButton.isShown()) {
                    floatingActionButton.hide();
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState)
            {
                if (newState == RecyclerView.SCROLL_STATE_IDLE)
                {
                    floatingActionButton.show();
                }
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
    }





    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
