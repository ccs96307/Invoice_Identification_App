package com.example.clay.a20181220;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.clay.a20181220.Account;

import java.util.List;
import java.util.Locale;

import com.example.clay.a20181220.AccountDao;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.w3c.dom.Text;

public class MainActivity extends Activity {

    private List<Account> list; // data list
    private AccountDao dao; // the class of increase or decrease action
    private EditText datetimeET; // enter datetime edit text
    private EditText infoET; // enter information edit text
    private EditText valueET; // enter value edit text
    private MyAdapter adapter;
    private ListView accountLV; // ListView

    private Button scan_btn; // scan QR Code Button

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // ----------------------------------------------------------electronic invoice
        scan_btn = (Button) findViewById(R.id.scan_btn);
        final Activity activity = this;
        scan_btn.setOnClickListener(new View.OnClickListener()
                                    {
                                        @Override
                                        public void onClick(View view)
                                        {
                                            IntentIntegrator integrator = new IntentIntegrator(activity);
                                            integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                                            integrator.setPrompt("Scan");
                                            integrator.setCameraId(0);
                                            integrator.setBeepEnabled(false);
                                            integrator.setBarcodeImageEnabled(false);
                                            integrator.initiateScan();
                                        }
                                    });

        // ----------------------------------------------------------Accounting
        // initialize
        initView();
        dao = new AccountDao(this);

        // query all data from database
        list = dao.queryAll();
        adapter = new MyAdapter();
        accountLV.setAdapter(adapter);
    }

    // ----------------------------------------------------------traditional invoice
    public void startCamera(View view)
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivity(intent);
    }

    // electronic invoice
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if (result != null)
        {
            if (result.getContents() == null)
            {
                Toast.makeText(this, "You cancel the scanning", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(this, result.getContents(), Toast.LENGTH_LONG).show();
                scan_add(result.getContents());
            }
        }
        else
        {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    // initialize
    private void initView()
    {
        accountLV = (ListView) findViewById(R.id.accountLV);
        datetimeET = (EditText) findViewById(R.id.datetimeET);
        infoET = (EditText) findViewById(R.id.infoET);
        valueET = (EditText) findViewById(R.id.valueET);

        accountLV.setOnItemClickListener(new MyOnItemClickListener()); // set listener
    }

    // activity_main.xml: the click event of ImageView
    public void add(View v)
    {
        String datetime = datetimeET.getText().toString().trim();
        String info = infoET.getText().toString().trim();
        String value = valueET.getText().toString().trim();

        if("".equals(datetime)||"".equals(info)||"".equals(value))
        {
            Toast.makeText(getApplicationContext(),"該項目不能為空",Toast.LENGTH_LONG).show();
            return;
        }

        // if value is not equal to null, transfer value type
        Account a = new Account(datetime, info, value.equals("")?0:Integer.parseInt(value));
        dao.insert(a); // insert database
        list.add(a); // insert list
        adapter.notifyDataSetChanged(); // refresh interface
        accountLV.setSelection(accountLV.getCount()-1); // select the last one
        datetimeET.setText("");
        infoET.setText("");
        valueET.setText("");
    }

    public void scan_add(String scan_info)
    {
        String datetime = "12/20";
        String info = scan_info;
        int value = 500;

        Account a = new Account(datetime, info, value);
        dao.insert(a); // insert database
        list.add(a); // insert list
        adapter.notifyDataSetChanged(); // refresh interface
        accountLV.setSelection(accountLV.getCount()-1); // select the last one
        datetimeET.setText("");
        infoET.setText("");
        valueET.setText("");
    }

    // define Adapter, put data in ListView
    private class MyAdapter extends BaseAdapter
    {
        public int getCount()
        {
            return list.size(); // get the numbers of list data
        }

        public Object getItem(int position)
        {
            return  list.get(position); // get the position
        }

        public long getItemId(int position)
        {
            return  position; // get the id by position
        }

        // list visualization
        public View getView(int position, View convertView, ViewGroup parent)
        {

            View item = convertView!=null?convertView:View.inflate(getApplicationContext(),R.layout.item,null); // reuse ConvertView

            // get TextView
            TextView idTV = (TextView) item.findViewById(R.id.idTV);
            TextView datetimeTV = (TextView) item.findViewById(R.id.datetimeTV);
            TextView infoTV = (TextView) item.findViewById(R.id.infoTV);
            TextView valueTV = (TextView) item.findViewById(R.id.valueTV);

            final Account a = list.get(position); // get Account by position

            // put Account data in TextView
            idTV.setText(a.getId()+"");
            datetimeTV.setText(a.getDatetime());
            infoTV.setText(a.getInfo());
            valueTV.setText(a.getValue()+"");
            ImageView upTV = (ImageView) item.findViewById(R.id.upIV);
            ImageView downTV = (ImageView) item.findViewById(R.id.downIV);
            ImageView deleteTV = (ImageView) item.findViewById(R.id.deleteIV);

            // the up arrow event
            upTV.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    a.setValue(a.getValue()+1);
                    notifyDataSetChanged();
                    dao.update(a);
                }
            });

            // the down arrow event
            downTV.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    a.setValue(a.getValue()-1);
                    notifyDataSetChanged();
                    dao.update(a);
                }
            });

            // delete key event
            deleteTV.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v) {

                    // check dialog pop before delete data
                    android.content.DialogInterface.OnClickListener listener =
                            new android.content.DialogInterface.OnClickListener()
                            {
                                public void onClick(DialogInterface dialog, int which){
                                    list.remove(a); // remove from list
                                    dao.delete(a.getId()); // delete from database
                                    notifyDataSetChanged(); // refresh interface
                                }
                            };

                    // build dialog
                    Builder builder = new Builder(MainActivity.this);
                    builder.setTitle("確定要刪除此項目？");

                    // set check button text and listener
                    builder.setPositiveButton("YES",listener);
                    builder.setNegativeButton("NO",null);
                    builder.show();
                }
            });

            return item;
        }
    }

    // Listview item click button
    private class MyOnItemClickListener implements OnItemClickListener
    {
        public void onItemClick(AdapterView<?> parent,View view,int position,long id)
        {
            // get the click position information
            Account a = (Account) parent.getItemAtPosition(position);
            Toast.makeText(getApplicationContext(), a.toString(), Toast.LENGTH_SHORT).show();
        }
    }
}
