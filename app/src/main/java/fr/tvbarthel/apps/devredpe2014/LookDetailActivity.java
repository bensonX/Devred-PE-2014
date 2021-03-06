package fr.tvbarthel.apps.devredpe2014;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.ListView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import fr.tvbarthel.apps.devredpe2014.model.Look;
import fr.tvbarthel.apps.devredpe2014.ui.ActionBarHelper;
import fr.tvbarthel.apps.devredpe2014.ui.LookItemAdapter;
import uk.co.senab.photoview.PhotoViewAttacher;

public class LookDetailActivity extends Activity {

    public static final String EXTRA_LOOK = "fr.tvbarthel.apps.devredpe2014.extra.look";

    private Look mLook;
    private ImageView mImageView;
    private ListView mListView;
    private LookItemAdapter mAdapter;
    private PhotoViewAttacher mAttacher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_look_detail);
        ActionBarHelper.setDevredTitle(this, getActionBar());
        loadLookOrFinish();
    }

    private void loadLookOrFinish() {
        mLook = getIntent().getParcelableExtra(EXTRA_LOOK);
        if (mLook == null) {
            finish();
        } else {
            init();
        }
    }

    private void init() {
        mListView = (ListView) findViewById(R.id.test);
        mImageView = (ImageView) findViewById(R.id.look_detail_activity_look_image);
        mAttacher = new PhotoViewAttacher(mImageView);
        mAdapter = new LookItemAdapter(LookDetailActivity.this, mLook.getLookItems());
        final View headerView = getLayoutInflater().inflate(R.layout.header_look_detail, mListView, false);
        mListView.addHeaderView(headerView);

        loadLookImage();
        initListViewPaddingTop();
    }

    private void loadLookImage() {
        Picasso.with(this).load(mLook.getLookResourceId()).skipMemoryCache().into(mImageView, new Callback() {
            @Override
            public void onSuccess() {
                mAttacher.update();
                mAttacher.zoomTo(1.2f, 0, 0);
            }

            @Override
            public void onError() {
            }
        });
    }

    private void initListViewPaddingTop() {
        final int headerListHeight = getResources().getDimensionPixelSize(R.dimen.header_look_detail_height);
        final View contentView = findViewById(android.R.id.content);
        contentView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                contentView.getViewTreeObserver().removeOnPreDrawListener(this);
                int height = contentView.getMeasuredHeight();
                int paddingTop = height - headerListHeight;
                mListView.setPadding(mListView.getPaddingLeft(), paddingTop, mListView.getPaddingRight(),
                        mListView.getPaddingBottom());
                mListView.setAdapter(mAdapter);
                return true;
            }
        });
    }

}
