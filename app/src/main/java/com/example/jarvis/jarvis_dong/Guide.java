package com.example.jarvis.jarvis_dong;

import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.example.jarvis.jarvis_dong.Bean.Tab;
import com.example.jarvis.jarvis_dong.fragment.HomeFragment;
import com.example.jarvis.jarvis_dong.fragment.MapFragment;
import com.example.jarvis.jarvis_dong.fragment.MineFragment;

import java.util.ArrayList;
import java.util.List;



public class Guide extends AppCompatActivity {

    //试试写个注释

    private LayoutInflater minflater;
    private FragmentTabHost mtabhost;
    private List<Tab> mtabs=new ArrayList<Tab>(3);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
//        JPushInterface.setDebugMode(true);
//        JPushInterface.init(this);


        initab();

    }
    //封装好
    private void initab() {
        Tab home=new Tab(HomeFragment.class,R.drawable.home,R.string.home);
        Tab map=new Tab(MapFragment.class,R.drawable.map,R.string.map);
        Tab mine=new Tab(MineFragment.class,R.drawable.mine,R.string.mine);
        mtabs.add(home);
        mtabs.add(map);
        mtabs.add(mine);

        //定义fragmenttabhost
        minflater =LayoutInflater.from(this);
        mtabhost=(FragmentTabHost)findViewById(android.R.id.tabhost);
        mtabhost.setup(this,getSupportFragmentManager(),R.id.realtabcontent);
        for (Tab tab:  mtabs)
        {
            TabHost.TabSpec tabspec=mtabhost.newTabSpec(getString(tab.getTitle()));
            tabspec.setIndicator(buildindicate(tab));
            mtabhost.addTab(tabspec,tab.getFragment(),null);

        }
    }
    public View buildindicate(Tab tab){
        View view= minflater.inflate(R.layout.tab,null);
        ImageView img= (ImageView) view.findViewById(R.id.icon_tab);
        TextView  text= (TextView) view.findViewById(R.id.txt_indicator);
        img.setBackgroundResource(tab.getIcon());
        text.setText(tab.getTitle());
        return  view;
    }

}
