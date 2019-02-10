package csx060.uga.edu.uga_hack_4_app;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;


public class PaymentFragment extends Fragment{
    private int totalCount = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_payment, container, false);
        TextView numRide = getView().findViewById(R.id.textView8);
        numRide.setText("Puchase Trip Count: " + Integer.toString(totalCount));
        // Inflate the layout for this fragment
        return view;
    }
    public void singleAdd(View v){
        totalCount++;
    }
}
