package com.example.project001.database;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.junit.Assert;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import static com.google.android.gms.wearable.DataMap.TAG;
import static org.junit.Assert.*;

public class DBConnectionTest {


    DBConnection dbConnection=new DBConnection();
    String firstLineFromFile="";
    String firstLineFromDatabase="";


    @Test
    public void addTheNumbers() {






        BufferedReader reader;

        try {
            final InputStream file = dbConnection.context.getAssets().open("personnumer.csv");
            reader = new BufferedReader(new InputStreamReader(file));
            String line = reader.readLine();
            firstLineFromFile=line;
            while (line != null) {
                personnumer n=new personnumer(line, "Martin", "Zannato", "Östra Boulevarden 34", "291 31", "Kristianstad", "false","real.martin.zannato@gmail.com");
                dbConnection.db.collection("testBase").document(line).set(n).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
                line = reader.readLine();
            }
        } catch (Exception e) {
            System.out.println("please die"+e);
            Log.e("fart", e.getMessage());
        }


        final String finalFirstLineFromFile = firstLineFromFile;
        dbConnection.db.collection("testBase")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (document.getString("number").equals(finalFirstLineFromFile)){
                                    firstLineFromDatabase=document.getString("number");
                                }
                            }

                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });



        Assert.assertEquals(finalFirstLineFromFile, firstLineFromDatabase);


    }
}