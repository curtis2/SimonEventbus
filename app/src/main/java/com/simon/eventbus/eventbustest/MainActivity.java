package com.simon.eventbus.eventbustest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.simon.eventbus.EventBus;
import com.simon.eventbus.Subscriber;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EventBus.getDefault().register(this);
        EventBus.getDefault().post(new User("android"));
    }

    @Subscriber
    public void setUser(User user){
        System.out.print(user.name);
    }

    @Override
    protected void onDestroy() {
        // Don’t forget to unregister !!
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }


}
