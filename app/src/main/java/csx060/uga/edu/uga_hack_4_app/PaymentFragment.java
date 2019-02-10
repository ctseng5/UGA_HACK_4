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
import android.widget.TextView;

import org.w3c.dom.Text;


public class PaymentFragment extends Fragment {
    private int totalCount = 0;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_payment, container, false);
        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Button confirmB = (Button) getActivity().findViewById(R.id.button7);
        Button b1 = (Button) getActivity().findViewById(R.id.singleTrip);
        Button b2 = (Button) getActivity().findViewById(R.id.fiveTrips);
        Button b3 = (Button) getActivity().findViewById(R.id.tenTrips);
        b1.setOnClickListener();
        b2.setOnClickListener((View.OnClickListener) this);
        b3.setOnClickListener((View.OnClickListener) this);
        confirmB.setOnClickListener((View.OnClickListener) this);
    }
    public void onClick(final View v) { //check for what button is pressed
        TextView totalStrCount = (TextView) getActivity().findViewById(R.id.textView8);

        switch (v.getId()) {
            case R.id.singleTrip:
                totalCount += 1;
                totalStrCount.setText("Purchase Trip Count: " + totalCount);
                break;
            case R.id.fiveTrips:
                totalCount += 5;
                totalStrCount.setText("Purchase Trip Count: " + totalCount);
                break;
            case R.id.tenTrips:
                totalCount += 10;
                totalStrCount.setText("Purchase Trip Count: " + totalCount);
                break;
            case R.id.button7:
                break;
            default:
                break;
        }
    }
}
