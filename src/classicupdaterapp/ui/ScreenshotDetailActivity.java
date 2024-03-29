package classicupdaterapp.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import classicupdaterapp.customExceptions.InvalidPictureException;
import classicupdaterapp.misc.Constants;

public class ScreenshotDetailActivity extends Activity {
    private int mCurrentScreenshotIndex = 0;
    private ImageView imageView;
    private ImageButton nextButton;
    private ImageButton prevButton;
    private int maxIndexSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screenshots_detail);
        imageView = (ImageView) findViewById(R.id.image_view);

        Intent i = getIntent();
        Bundle b = i.getExtras();

        mCurrentScreenshotIndex = b.getInt(Constants.SCREENSHOTS_POSITION, 0);

        maxIndexSize = ScreenshotActivity.getScreenshotSize() - 1;

        nextButton = (ImageButton) findViewById(R.id.next_button);
        prevButton = (ImageButton) findViewById(R.id.previous_button);

        nextButton.setOnClickListener(ButtonOnClickListener);
        prevButton.setOnClickListener(ButtonOnClickListener);
    }

    @Override
    protected void onStart() {
        super.onStart();
        showScreenshot();
    }

    private void showScreenshot() {
        //Reenable the Buttons, they will be removed in the following ifs
        nextButton.setVisibility(View.VISIBLE);
        prevButton.setVisibility(View.VISIBLE);
        if (mCurrentScreenshotIndex >= maxIndexSize) {
            mCurrentScreenshotIndex = maxIndexSize;
            nextButton.setVisibility(View.GONE);
        }
        if (mCurrentScreenshotIndex <= 0) {
            mCurrentScreenshotIndex = 0;
            prevButton.setVisibility(View.GONE);
        }

        try {
            imageView.setImageBitmap((ScreenshotActivity.getItem(mCurrentScreenshotIndex)).getBitmap());
        }
        catch (InvalidPictureException e) {
            imageView.setImageResource(Constants.SCREENSHOTS_FALLBACK_IMAGE);
        }
        //Image not yet loaded
        catch (IndexOutOfBoundsException e) {
            imageView.setImageResource(Constants.SCREENSHOTS_LOADING_IMAGE);
        }
        ((TextView) findViewById(R.id.status_text)).setText(String.format("%d/%d", mCurrentScreenshotIndex + 1, maxIndexSize + 1));
    }

    @Override
    protected void onStop() {
        super.onStop();
        imageView.getDrawable().setCallback(null);
        System.gc();
    }

    private final View.OnClickListener ButtonOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.next_button:
                    mCurrentScreenshotIndex++;
                    break;
                case R.id.previous_button:
                    mCurrentScreenshotIndex--;
                    break;
            }
            showScreenshot();
        }
    };
}
