package com.vinrithi.biblequizz;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class AllResults extends ToolNavSet implements BibleQuizController {
    ListView lstAllResults;
    String username;
    ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_results);
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        m_toolbar = (Toolbar)findViewById(R.id.include_toolbar);
        tvToolbarTitle = (TextView)m_toolbar.findViewById(R.id.tvToolbarTitle);
        expandableListView = (ExpandableListView) findViewById(R.id.expandable_listview);
        lstAllResults = (ListView) findViewById(R.id.lstAllResults);
        setToolbar();
        setupDrawer();
        populateNavDrawer();
        username = mPreferences.getString(PreferenceConstants.USERNAME,"");

        dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        dialog.setMessage("Getting results...");

        JSONObject postData = new JSONObject();
        try {
            postData.put("username", username);

            String url = "http://192.168.137.1/quiz4bible/questions_answers/getresults.php";
            dialog.show();
            new ConnectToServer(this,"getResults").execute(url,postData.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }


        //lstAllResults.setAdapter(new CustomAdapter());
    }

    @Override
    public ProgressDialog getProgressDialog() {
        return dialog;
    }

    @Override
    public void resetPreferences() {

    }

    @Override
    public void retrieveData(String result) throws JSONException {
        //lstAllResults.setAdapter(new CustomAdapter(this,WebpagesDb.getSavedpages2()));
        dialog.dismiss();
        if(result!=null)
        {
            JSONArray jsonArray = new JSONArray(result);
            List<AllresultItems> itemsList = new ArrayList<>();
            for(int i=0;i<jsonArray.length();i++)
            {
                JSONObject object = jsonArray.getJSONObject(i);
                AllresultItems items = new AllresultItems(
                        object.getInt("TestScore"),
                        object.getInt("NumberOfQuestions"),
                        object.getString("Book"),
                        object.getString("Level"),
                        object.getString("DateOfQuiz"),
                        object.getLong("TimeTaken")
                        );
                itemsList.add(items);
            }
            lstAllResults.setAdapter(new CustomAdapter(AllResults.this,itemsList));
        }
    }

    @Override
    public SwipeRefreshLayout getRefresher() {
        return null;
    }


    class CustomAdapter extends BaseAdapter {
        List<AllresultItems> itemsList;
        Context mContext;
        private boolean isLongClicked = false;

        CustomAdapter(Context context, List<AllresultItems> items) {
            this.mContext = context;
            this.itemsList = items;
        }

        @Override
        public int getCount() {
            return this.itemsList.size();
        }

        @Override
        public Object getItem(int position) {
            return this.itemsList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final AllresultItems allresultItems = (AllresultItems) getItem(position);

            if (convertView == null) {
                LayoutInflater layoutInflater = (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = layoutInflater.inflate(R.layout.allresults_items, null);
            }

            final TextView tvNo = (TextView) convertView.findViewById(R.id.tvNo);
            final TextView tvScore = (TextView) convertView.findViewById(R.id.tvScore);
            final TextView tvBook = (TextView) convertView.findViewById(R.id.tvBook);
            final TextView tvLevel = (TextView) convertView.findViewById(R.id.tvLevel);
            ImageView imgIndicator = (ImageView) convertView.findViewById(R.id.imgIndicator);

            String score = String.valueOf(allresultItems.getScore());
            String numberOfQuestion = String.valueOf(allresultItems.getNoOfQuestions());
            String book = allresultItems.getBook();
            String level = allresultItems.getLevel();

            /*int imgResource;
             if(position%2==0)
            {
               tvNo.setTextColor(Color.parseColor("#4B508B"));
                tvScore.setTextColor(Color.parseColor("#4B508B"));
                tvBook.setTextColor(Color.parseColor("#4B508B"));
                tvLevel.setTextColor(Color.parseColor("#4B508B"));
                imgResource = R.drawable.purple_more_icon;
                convertView.setBackgroundColor(Color.parseColor("#ffffff"));
            }
            else
            {
                tvNo.setTextColor(Color.parseColor("#ffffff"));
                tvScore.setTextColor(Color.parseColor("#ffffff"));
                tvBook.setTextColor(Color.parseColor("#ffffff"));
                tvLevel.setTextColor(Color.parseColor("#ffffff"));
                imgResource = R.drawable.white_more_icon;
                convertView.setBackgroundColor(Color.parseColor("#4B508B"));
            }*/

             if(position%2==0)
            {
                convertView.setBackgroundColor(Color.parseColor("#ffffff"));
            }
            else
            {
                convertView.setBackgroundColor(Color.parseColor("#E8E8E8"));
            }

            tvNo.setText(String.valueOf(position+1));
            tvScore.setText(score);
            tvBook.setText(book);
            tvLevel.setText(level);

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showFullResults(allresultItems);
                }
            });

            return convertView;
        }

        public void showFullResults(AllresultItems items)
        {
            final Dialog dialog = new Dialog(AllResults.this,R.style.mDialog);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCanceledOnTouchOutside(true);
            dialog.setContentView(R.layout.test_scores_dialog);
            //dialog.getWindow().setBackgroundDrawable(mActivity.getResources().getDrawable(R.drawable.dialogbgwhole));
            Button close = (Button) dialog.findViewById(R.id.btClose);
            TextView tvScore = (TextView) dialog.findViewById(R.id.tvScore);
            TextView tvBook = (TextView) dialog.findViewById(R.id.tvBook);
            TextView tvNumberOfQuestions = (TextView) dialog.findViewById(R.id.tvNumberOfQuestions);
            TextView tvDateTaken = (TextView) dialog.findViewById(R.id.tvDateTaken);
            TextView tvTimeTaken = (TextView) dialog.findViewById(R.id.tvTimeTaken);
            TextView tvLevel = (TextView) dialog.findViewById(R.id.tvLevel);

            tvBook.setText(items.getBook());
            tvDateTaken.setText(items.getDateTaken());
            tvTimeTaken.setText(items.getTimeTaken());
            tvLevel.setText(items.getLevel());
            tvNumberOfQuestions.setText(String.valueOf(items.getNoOfQuestions()));
            tvScore.setText(String.valueOf(items.getScore())+"%");

            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.cancel();
                }
            });
            dialog.show();
        }
    }
}
