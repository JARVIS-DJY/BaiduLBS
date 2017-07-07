package com.example.jarvis.jarvis_dong.fragment;


import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.example.jarvis.jarvis_dong.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    private SliderLayout msliderlayout;
    private PagerIndicator mindicator;
    private WebView web;
    private Button btn;
    private EditText webaddr;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_home, container, false);
        msliderlayout=(SliderLayout)view.findViewById(R.id.slider);
        mindicator=(PagerIndicator) view.findViewById(R.id.custom_indicator);
        web=(WebView)view.findViewById(R.id.web);
        webaddr=(EditText)view.findViewById(R.id.webaddr);
        view.findViewById(R.id.webbtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(webaddr.getText().toString().equals(""))
                {
                    Toast.makeText(getActivity().getApplicationContext(),"你的网址为空，请输入你要跳转的网址",Toast.LENGTH_SHORT).show();
                }
                else{
                    web.loadUrl("http://"+webaddr.getText().toString());
                }
            }
        });



        web.getSettings().setJavaScriptEnabled(true);
        web.setWebViewClient(new WebViewClient());
        web.loadUrl("http://sina.cn/?wm=3165_0004");
        inislider();
        return view;
    }
    private void inislider(){
        TextSliderView textSliderView = new TextSliderView(getActivity());
        textSliderView
                .description("球哥参加湖人队新秀见面会")
                .image(R.mipmap.pic1)
                .setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                    @Override
                    public void onSliderClick(BaseSliderView slider) {
                       web.loadUrl("http://slide.sports.sina.com.cn/k/slide_2_786_135547.html/d/1#p=1");
                    }
                });
        TextSliderView textSliderView2 = new TextSliderView(getActivity());
        textSliderView2
                .description("NBA众星拍摄人像写真")
                .image(R.mipmap.pic2)
                .setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                    @Override
                    public void onSliderClick(BaseSliderView slider) {
                     web.loadUrl("http://slide.sports.sina.com.cn/k/slide_2_786_135551.html#p=1");
                    }
                });
        TextSliderView textSliderView3 = new TextSliderView(getActivity());
        textSliderView3
                .description("2017年NBA选秀大会")
                .image(R.mipmap.pic3)
                .setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                    @Override
                    public void onSliderClick(BaseSliderView slider) {
                        web.loadUrl("http://slide.sports.sina.com.cn/k/slide_2_786_134923.html/d/1#p=1");
                    }
                });
        TextSliderView textSliderView4 = new TextSliderView(getActivity());
        textSliderView4
                .description("选秀区乐透区结果一览 福尔茨状元")
                .image(R.mipmap.pic4)
                .setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                    @Override
                    public void onSliderClick(BaseSliderView slider) {
                        web.loadUrl("http://slide.sports.sina.com.cn/k/slide_2_786_134932.html#p=1");
                    }
                });
        TextSliderView textSliderView5 = new TextSliderView(getActivity());
        textSliderView5
                .description("贾里奇前妻利马时尚杂志大片")
                .image(R.mipmap.pic5)
                .setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                    @Override
                    public void onSliderClick(BaseSliderView slider) {
                        web.loadUrl("http://slide.sports.sina.com.cn/k/slide_2_730_135585.html#p=1");
                    }
                });
        TextSliderView textSliderView6 = new TextSliderView(getActivity());
        textSliderView6
                .description("威少巴黎时装周深V花衬衫秀胸")
                .image(R.mipmap.pic6)
                .setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                    @Override
                    public void onSliderClick(BaseSliderView slider) {
                        web.loadUrl("http://slide.sports.sina.com.cn/k/slide_2_786_135015.html#p=1");
                    }
                });

        msliderlayout.addSlider(textSliderView);
        msliderlayout.addSlider(textSliderView2);
        msliderlayout.addSlider(textSliderView3);
        msliderlayout.addSlider(textSliderView4);
        msliderlayout.addSlider(textSliderView5);
        msliderlayout.addSlider(textSliderView6);

        msliderlayout.setCustomIndicator(mindicator);
    }

}
