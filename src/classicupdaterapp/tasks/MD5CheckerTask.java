package classicupdaterapp.tasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;
import classicupdaterapp.customTypes.UpdateInfo;
import classicupdaterapp.misc.Constants;
import classicupdaterapp.misc.Log;
import classicupdaterapp.ui.ApplyUpdateActivity;
import classicupdaterapp.ui.MainActivity;
import classicupdaterapp.ui.R;
import classicupdaterapp.utils.MD5;

import java.io.*;

public class MD5CheckerTask extends AsyncTask<File, Void, Boolean> {
    private static final String TAG = "MD5CheckerTask";

    private Boolean showDebugOutput = false;

    private final ProgressDialog mDialog;
    private final String mFilename;
    private boolean mreturnvalue;
    private final Context mCtx;

    public MD5CheckerTask(Context ctx, ProgressDialog dialog, String filename, Boolean _showDebugOutput) {
        mDialog = dialog;
        mFilename = filename;
        mCtx = ctx;
        showDebugOutput = _showDebugOutput;
    }

    @Override
    public Boolean doInBackground(File... params) {
        boolean MD5exists = false;
        try {
            File MD5file = new File(params[0] + ".md5sum");
            if (MD5file.exists() && MD5file.canRead())
                MD5exists = true;
            if (params[0].exists() && params[0].canRead()) {
                //If MD5 File exists, check it
                if (MD5exists) {
                    //Calculate MD5 of Existing Update
                    String calculatedMD5 = MD5.calculateMD5(params[0]);
                    //Read the existing MD5SUM
                    FileReader input = new FileReader(MD5file);
                    BufferedReader bufRead = new BufferedReader(input);
                    String firstLine = bufRead.readLine();
                    bufRead.close();
                    input.close();
                    //If the content of the File is not empty, compare it
                    if (firstLine != null) {
                        String[] SplittedString = firstLine.split("  ");
                        if (SplittedString[0].equalsIgnoreCase(calculatedMD5))
                            mreturnvalue = true;
                    } else
                        mreturnvalue = false;
                } else {
                    return true;
                }
            }
        }
        catch (IOException e) {
            Log.e(TAG, "IOEx while checking MD5 sum", e);
            mreturnvalue = false;
        }
        return mreturnvalue;
    }

    @Override
    public void onPostExecute(Boolean result) {
        UpdateInfo ui = new UpdateInfo();
        String[] temp = mFilename.split("\\\\");
        ui.setName(temp[temp.length - 1]);
        ui.setFileName(mFilename);
        if (result) {
            Intent i = new Intent(mCtx, ApplyUpdateActivity.class)
                    .putExtra(Constants.KEY_UPDATE_INFO, (Serializable) ui);
            mCtx.startActivity(i);
        } else {
            Toast.makeText(mCtx, R.string.apply_existing_update_md5error_message, Toast.LENGTH_LONG).show();
        }

        //Is null when no MD5SUM is present
        if (mDialog != null)
            mDialog.dismiss();
    }

    @Override
    public void onCancelled() {
        if (showDebugOutput) Log.d(TAG, "MD5Checker Task cancelled");
        Toast.makeText(mCtx, R.string.md5_check_cancelled, Toast.LENGTH_LONG).show();
        Intent i = new Intent(mCtx, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mCtx.startActivity(i);
    }
}
