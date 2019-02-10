package csx060.uga.edu.uga_hack_4_app;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.WriterException;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class PaymentFragment extends Fragment implements View.OnClickListener{
    private FirebaseAuth auth;
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    //Reference to Information table
    DatabaseReference ref = database.getReference("Information");
    EditText totalStrCount;
    Button button1;
    Button button2;
    Button button3;
    Button submit;
    int totalCount = 0;
    String num;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_payment, container, false);
        // Inflate the layout for this fragment
        button1 = (Button) view.findViewById(R.id.singleTrip);
        button1.setOnClickListener(this);
        button2 = (Button) view.findViewById(R.id.fiveTrips);
        button2.setOnClickListener(this);
        button3 = (Button) view.findViewById(R.id.tenTrips);
        button3.setOnClickListener(this);
        submit = (Button) view.findViewById(R.id.button7);
        submit.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        EditText totalStrCount = (EditText) getActivity().findViewById(R.id.editText);
        //Get Firebase Auth instance
        auth = FirebaseAuth.getInstance();

        switch (v.getId()){
            case R.id.singleTrip:
                totalCount += 1;
                num = Integer.toString(totalCount);
                totalStrCount.setText("Purchase Trip Count: " + num);
                break;
            case R.id.fiveTrips:
                totalCount += 5;
                num = Integer.toString(totalCount);
                totalStrCount.setText("Purchase Trip Count: " + num);
                break;
            case R.id.tenTrips:
                totalCount += 10;
                num = Integer.toString(totalCount);
                totalStrCount.setText("Purchase Trip Count: " + num);
                break;
            case R.id.button7:
                auth.getUid();
                DatabaseReference RelationRef = ref.child("users");

                if(totalCount != 0) {
                    //Fetch the users from the DB
                    RelationRef.child(auth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                long count = 0;

                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    String key = snapshot.getKey().toString();

                                    if (key.equalsIgnoreCase("tripCount")) {
                                        count = (long) snapshot.getValue();
                                    }
                                }
                                totalCount += count;
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    HashMap<String, Object> userMap = new HashMap<>();
                    userMap.put("tripCount", totalCount);
                    RelationRef.child(auth.getUid()).updateChildren(userMap);

                    totalCount = 0;

                    num = Integer.toString(totalCount);
                    totalStrCount.setText("Purchase Trip Count: " + num);
                }
                break;
            default:
                break;
        }
    }
}
