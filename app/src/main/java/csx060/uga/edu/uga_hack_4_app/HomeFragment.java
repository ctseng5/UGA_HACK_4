package csx060.uga.edu.uga_hack_4_app;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class HomeFragment extends Fragment {
    //Reference to Firebase Database
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    //Reference to Information table
    DatabaseReference ref = database.getReference("Information");
    private FirebaseAuth auth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        final ImageView imvQrCode = (ImageView) view.findViewById(R.id.imageView2);
        TextView numRide = (TextView) view.findViewById(R.id.textView);

        auth = FirebaseAuth.getInstance();
        DatabaseReference RelationRef = ref.child("users");

        //Fetch the users from the DB
        RelationRef.child(auth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    String email = "";

                    for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String key = snapshot.getKey().toString();

                        //If the user is a friend, show "remove" instead of "add" button
                        if (key.equalsIgnoreCase("email")) {
                            email = (String) snapshot.getValue();
                            System.out.println(email);
                            break;
                        }
                    }

                    Bitmap bitmap = null;
                    try {
                        bitmap = textToImage(email, 500, 500);
                    } catch (WriterException e) {
                        e.printStackTrace();
                    }

                    imvQrCode.setImageBitmap(bitmap);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        // Inflate the layout for this fragment
        return view;
    }

    private Bitmap textToImage(String text, int width, int height) throws WriterException, NullPointerException{

        BitMatrix bitMatrix;
        try {
            bitMatrix = new MultiFormatWriter().encode(text, BarcodeFormat.DATA_MATRIX.QR_CODE, width, height, null);
        }
        catch(IllegalArgumentException Illegalargumentexception){
            return null;
        }
        int bitMatrixWidth = bitMatrix.getWidth();
        int bitMatrixHeight = bitMatrix.getHeight();
        int[] pixels = new int[bitMatrixWidth*bitMatrixHeight];

        for (int y=0; y<bitMatrixHeight; y++) {
            int offset = y * bitMatrixWidth;

            for (int x = 0; x < bitMatrixWidth; x++) {
                pixels[offset + x] = bitMatrix.get(x,y) ? R.color.black : R.color.white;
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_8888);

        bitmap.setPixels(pixels, 0, width, 0, 0, bitMatrixWidth, bitMatrixHeight);
        return bitmap;
    }

}
