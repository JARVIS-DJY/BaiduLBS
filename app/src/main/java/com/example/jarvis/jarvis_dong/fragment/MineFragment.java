package com.example.jarvis.jarvis_dong.fragment;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jarvis.jarvis_dong.R;

import static android.R.attr.phoneNumber;

/**
 * A simple {@link Fragment} subclass.
 */
public class MineFragment extends Fragment {
    private FrameLayout frame;
    private TextView call;

    public MineFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view=inflater.inflate(R.layout.fragment_mine, container, false);
        frame=(FrameLayout)view.findViewById(R.id.frame);
        call=(TextView)view.findViewById(R.id.call);
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity().getApplicationContext(),"即将打开拨号",Toast.LENGTH_SHORT).show();
                Intent i=new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+698699));
                startActivity(i);
            }
        });
        frame.getBackground().setAlpha(155);

        return view;
    }

}
