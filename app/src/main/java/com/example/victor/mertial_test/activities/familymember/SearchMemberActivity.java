package com.example.victor.mertial_test.activities.familymember;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.victor.mertial_test.R;
import com.example.victor.mertial_test.apapters.MembersAdapter;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class SearchMemberActivity extends ActionBarActivity {
    private Toolbar toolbar;
    private ArrayList<String> listMembers = new ArrayList<>();
    private RecyclerView list_Members;
    private MembersAdapter memberAdapter;
    private ImageButton clickMember;
    private SharedPreferences family;
    private SharedPreferences.Editor family_edit;
    findMember find_them = new findMember() ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        family=getSharedPreferences("family123", MODE_PRIVATE);
        family_edit=family.edit();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_member);
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        clickMember = (ImageButton)findViewById(R.id.click_member);
        final EditText Search = (EditText)findViewById(R.id.member_name);

        clickMember.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String Strname = Search.getText().toString();
                final ParseQuery<ParseObject> query = ParseQuery.getQuery("Member");
                try {
                    List<ParseObject> pList = query.find();
                    int k = query.count();
                    for(int i =0;i<k;i++){
                        if(pList.get(i).getString("account").equals(Strname)){
                            listMembers = find_them.finded_Member(Strname);
                            memberAdapter.setMemberList(listMembers);
                            memberAdapter.notifyDataSetChanged();
                        }
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }


            }
        });
        list_Members = (RecyclerView)findViewById(R.id.listMember);
        list_Members.setLayoutManager(new LinearLayoutManager(this));
        memberAdapter = new MembersAdapter(this);
        list_Members.setAdapter(memberAdapter);
        list_Members.addOnItemTouchListener(new RecyclerTouchListener(this, list_Members, new ClickListener() {
            @Override
            public void onClick(View view, int position) {

                AlertDialog.Builder alert = new AlertDialog.Builder(SearchMemberActivity.this);
                alert.setTitle("輸入權限密碼");
// Set an EditText view to get user input
                final EditText input = new EditText(SearchMemberActivity.this);
                alert.setView(input);
                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        String Searchaccount = Search.getText().toString();
                        Editable Strname = input.getText();
                        String password = Strname.toString();

                        final ParseQuery<ParseObject> query = ParseQuery.getQuery("Member");
                        try {
                            query.whereEqualTo("account",Searchaccount);
                            List<ParseObject> pList = query.find();

                                if (pList.get(0).getString("share_password").equals(password)) {
                                    AlertDialog.Builder alert = new AlertDialog.Builder(SearchMemberActivity.this);
                                    alert.setTitle("連結成功");
                                    alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            String Searchaccount = Search.getText().toString();
                                            SharedPreferences pref = getSharedPreferences("wecare_member", MODE_PRIVATE);
                                            String account = pref.getString("account", null);
                                            ParseObject addrelation = new ParseObject("Relation");
                                            addrelation.put("account",account);
                                            addrelation.put("familymember",Searchaccount);
                                            addrelation.put("nickname",Searchaccount);
                                            addrelation.put("pin","N");
                                            addrelation.saveInBackground();
                                            finish();

                                        }
                                    });
                                    alert.show();

                                } else {
                                    AlertDialog.Builder alert = new AlertDialog.Builder(SearchMemberActivity.this);
                                    alert.setTitle("權限密碼錯誤");
                                    alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                        }
                                    });
                                    alert.show();
                                }

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
// setTitle(value.toString());


                });
                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
// Canceled.
                    }

                });
                alert.show();
            }
            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search_member, menu);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        family_edit.putBoolean("lock2", true).commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(id == android.R.id.home){
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    class RecyclerTouchListener implements RecyclerView.OnItemTouchListener{

        private GestureDetector gestureDetector;
        private ClickListener clickListener;
        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener){
            Log.i("TAG", "constructor invoked ");
            this.clickListener=clickListener;
            gestureDetector = new GestureDetector(context,new GestureDetector.SimpleOnGestureListener(){

                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    Log.i("TAG","onSingleTapUp "+e);
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(),e.getY());
                    if(child!=null && clickListener!=null){
                        Log.i("TAG","onLongPress "+e);
                        clickListener.onLongClick(child,recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View child = rv.findChildViewUnder(e.getX(),e.getY());
            if(child!=null && clickListener!=null && gestureDetector.onTouchEvent(e)){
                clickListener.onClick(child,rv.getChildPosition(child));
            }
            //  Log.i("TAG","onInterceptTouchEvent "+e);
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
            Log.i("TAG","onTouchEvent "+gestureDetector.onTouchEvent(e)+" "+e);
        }
    }

    public static interface ClickListener{
        public void onClick(View view, int position);
        public void onLongClick(View view, int position);
    }


}

