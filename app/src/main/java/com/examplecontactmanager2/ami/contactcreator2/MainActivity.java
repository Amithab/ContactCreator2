package com.examplecontactmanager2.ami.contactcreator2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    EditText nameTxt/*, phoneTxt, emailTxt, addressTxt*/;
    List<OneProd> produces = new ArrayList<OneProd>();
    ArrayAdapter adapter; // ArrayAdapter if problems occur
    FirebaseDatabase database;
    String name;
    int global;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle info = getIntent().getExtras();
        name = info.getString("name");
        setContentView(R.layout.activity_main);
        adapter = new ProduceListAdapter();
        ListView listview = (ListView) findViewById(R.id.listView);
        listview.setAdapter(adapter);
        nameTxt = (EditText) findViewById(R.id.txtName);
        /*phoneTxt = (EditText) findViewById(R.id.txtPhone);
        emailTxt = (EditText) findViewById(R.id.txtEmail);
        addressTxt = (EditText) findViewById(R.id.txtAddress);*/
        TabHost tabHost1 = (TabHost) findViewById(R.id.tabHost);

        tabHost1.setup();

        TabHost.TabSpec tabSpec = tabHost1.newTabSpec("speak");
        tabSpec.setContent(R.id.Speak);
        tabSpec.setIndicator("Speak");
        tabHost1.addTab(tabSpec);

        TabHost.TabSpec tabSpec2 = tabHost1.newTabSpec("type");
        tabSpec2.setContent(R.id.Type);
        tabSpec2.setIndicator("Type");
        tabHost1.addTab(tabSpec2);

        TabHost.TabSpec tabSpec3 = tabHost1.newTabSpec("list");
        tabSpec3.setContent(R.id.linearLayout3);
        tabSpec3.setIndicator("ListRecipe");
        tabHost1.addTab(tabSpec3);







        final Button addBtn = (Button) findViewById(R.id.btnAdd2);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addContact(nameTxt.getText().toString());
                Toast.makeText(getApplicationContext(), nameTxt.getText().toString() + " has been added to the grocery list!", Toast.LENGTH_SHORT).show();
                nameTxt.setText("");
            }

        });

        nameTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                addBtn.setEnabled(!nameTxt.getText().toString().trim().isEmpty());
            }

            @Override
            public void afterTextChanged(Editable editable)
            {

            }
        });


        database = FirebaseDatabase.getInstance();
        System.out.println(database);
        listener();
    }

    private void addContact(String name)
    {
        produces.add(new OneProd(name));
        adapter.notifyDataSetChanged();
    }

    private class ProduceListAdapter extends ArrayAdapter<OneProd>{
        public ProduceListAdapter()
        {
            super(MainActivity.this, R.layout.listview_item, produces);
        }


        @Override
        public View getView(int position, View view, ViewGroup parent)
        {
            if(view == null)
                view = getLayoutInflater().inflate(R.layout.listview_item, parent, false);

            OneProd currentOneProd = produces.get(position);

            TextView name = (TextView) view.findViewById(R.id.produceName);
            name.setText(currentOneProd.getProduce());

            return view;
        }

    }

    public void listener() {
        database.getReference(name + "/ingredients").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                ArrayList<String> value = (ArrayList<String>)(dataSnapshot.getValue());
                System.out.println(value);
//                database.getReference(name + "/count").addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        setInt(dataSnapshot.getValue(Integer.class));
//                        System.out.println(dataSnapshot.getValue(Integer.class));
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//
//                    }
//                });
                int length = global - 1;
                System.out.println(value.get(0));
                addContact(value.get(0));
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                System.out.println("did not work :(");
            }
        });
    }

    public void setInt(int i) {
        global = i;
    }

}
