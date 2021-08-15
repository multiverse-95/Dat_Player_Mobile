package com.example.datplayer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class PlaylistActivity extends AppCompatActivity {

    ArrayList<String> phones = new ArrayList();
    ArrayAdapter<String> adapter;

    ArrayList<String> selectedPhones = new ArrayList();
    ListView phonesList;
    EditText editText;
    Button add,del;
    MenuItem myMenuItem;
    boolean flag=false;
    boolean flag2=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);

        phones.add("iPhone 7");
        phones.add("Samsung Galaxy S7");
        phones.add("Google Pixel");
        phones.add("Huawei P10");
        phones.add("HP Elite z3");

        editText=(EditText) findViewById(R.id.phone);
        add=(Button)findViewById(R.id.add);
        del=(Button)findViewById(R.id.del);
        phonesList = (ListView) findViewById(R.id.phonesList);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, phones);
        phonesList.setAdapter(adapter);

        // обработка установки и снятия отметки в списке
        phonesList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id)
            {
                Intent intent = new Intent(PlaylistActivity.this, PlaylistAddActivity.class);
                startActivity(intent);

            }
        });


    }

    public void add(View view){

        EditText phoneEditText = (EditText) findViewById(R.id.phone);
        String phone = phoneEditText.getText().toString();
        if(!phone.isEmpty() && phones.contains(phone)==false){
            adapter.add(phone);
            phoneEditText.setText("");
            adapter.notifyDataSetChanged();
        }
    }
    public void remove(View view){
        // получаем и удаляем выделенные элементы
        for(int i=0; i< selectedPhones.size();i++){
            adapter.remove(selectedPhones.get(i));
        }
        // снимаем все ранее установленные отметки
        phonesList.clearChoices();
        // очищаем массив выбраных объектов
        selectedPhones.clear();

        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_second, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_pls_add:
                if(flag==false)
                {
                    del.setVisibility(View.GONE);
                    editText.setVisibility(View.VISIBLE);
                    add.setVisibility(View.VISIBLE);
                    flag=true;
                    flag2=false;
                    adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, phones);
                    phonesList.setAdapter(adapter);

                    // обработка установки и снятия отметки в списке
                    phonesList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                        @Override
                        public void onItemClick(AdapterView<?> parent, View v, int position, long id)
                        {

                            Intent intent = new Intent(PlaylistActivity.this, InPlayListActivity.class);
                            startActivity(intent);
                        }
                    });
                    // myMenuItem.setTitle("Скрыть поиск");
                } else {
                    del.setVisibility(View.GONE);
                    editText.setVisibility(View.GONE);
                    add.setVisibility(View.GONE);
                    flag=false;
                    flag2=false;
                    // myMenuItem.setTitle("Поиск");

                }
                return true;
            case R.id.action_pls_del:
                if(flag2==false)
                {
                    editText.setVisibility(View.GONE);
                    add.setVisibility(View.GONE);
                    //editText.setVisibility(View.VISIBLE);
                    del.setVisibility(View.VISIBLE);
                    flag2=true;
                    flag=false;

                    //phonesList = (ListView) findViewById(R.id.phonesList);
                    adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, phones);
                    phonesList.setAdapter(adapter);

                    // обработка установки и снятия отметки в списке
                    phonesList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                        @Override
                        public void onItemClick(AdapterView<?> parent, View v, int position, long id)
                        {

                            // получаем нажатый элемент
                            String phone = adapter.getItem(position);
                            if(phonesList.isItemChecked(position)==true){
                                selectedPhones.add(phone);
                            }
                            else{

                                selectedPhones.remove(phone);
                            }
                        }
                    });
                    // myMenuItem.setTitle("Скрыть поиск");
                } else {
                    editText.setVisibility(View.GONE);
                    add.setVisibility(View.GONE);
                    //editText.setVisibility(View.GONE);
                    del.setVisibility(View.GONE);
                    flag2=false;
                    flag=false;
                    // myMenuItem.setTitle("Поиск");

                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }
}
