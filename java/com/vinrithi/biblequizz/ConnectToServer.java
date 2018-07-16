package com.vinrithi.biblequizz;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by vinri on 1/4/2018.
 */

public class ConnectToServer extends AsyncTask<String,Void,String> {

    private Context mContext;
    private ProgressDialog dialog;
    private String typeofActivity;
    private BibleQuizController controller;
    private SwipeRefreshLayout refreshLayout;
    private AlertDialog alert;

    ConnectToServer(Context otx,String typeofActivity) {
        this.mContext = otx;
        this.typeofActivity = typeofActivity;
        controller = (BibleQuizController)otx;
        dialog = controller.getProgressDialog();
        refreshLayout = controller.getRefresher();
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage("Conncetion could not be established")
                .setCancelable(true)
                .setPositiveButton("Close",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        alert = builder.create();
    }

    @Override
    protected void onPreExecute() {

    }

    @Override
    protected String doInBackground(String... params) {
        try {
            String fetch_data_url = params[0];
            String postedJson = params[1];
            URL url = new URL(fetch_data_url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            OutputStream OS = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));

            String data = URLEncoder.encode("postedData", "UTF-8") + "=" + URLEncoder.encode(postedJson, "UTF-8") + "&" +
                    URLEncoder.encode("typeOfActivity", "UTF-8") + "=" + URLEncoder.encode(typeofActivity, "UTF-8");
            bufferedWriter.write(data);
            bufferedWriter.flush();
            OS.close();
            InputStream IS = httpURLConnection.getInputStream();
            String result = "";
            String line = "";

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(IS, "iso-8859-1"));
            while ((line = bufferedReader.readLine()) != null) {
                result += line;
            }
            bufferedReader.close();
            IS.close();
            httpURLConnection.disconnect();
            return result;
        } catch (Exception e){
            e.printStackTrace();
            if(dialog!=null)
                dialog.dismiss();
            Activity activity =(Activity)mContext;
            activity.runOnUiThread(new Runnable(){
                public void run() {
                    alert.show();
                }
            });
        }

        dialog.dismiss();
        return null;
    }

    @Override
    protected void onProgressUpdate(Void... params) {
        super.onProgressUpdate();
    }

    @Override
    protected void onPostExecute(String result) {
        switch(typeofActivity)
        {
            case "doLogin":
            case "signup":
            case "retrieveQuestions":
            case "saveResults":
            case "getResults":
            case "sendfeedback":
                try {
                    controller.retrieveData(result);
                } catch (JSONException e){
                    e.printStackTrace();
                }
                break;
            default:
                dialog.dismiss();
                break;
        }
    }
}

