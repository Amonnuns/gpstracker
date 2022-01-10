package br.unifacs.gpstracker;

import android.location.Location;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Firebase {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Map<String,Object> loc;
    private static ArrayList<Map> locations = new ArrayList<>();
    private static ArrayList<String> references = new ArrayList<>();


    @RequiresApi(api = Build.VERSION_CODES.O)
    public void saveToFirebase(Location location) {

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();

        Map<String, Object> loc = new HashMap<String, Object>();
        loc.put("time", dtf.format(now));
        loc.put("latitude", location.getLatitude());
        loc.put("longitude", location.getLongitude());

        db.collection("localização").add(loc).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                System.out.println("We have done it successfully");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println("We couldn't do because of: " + e.getCause());
            }
        });
    }

    private void locsFromFirebase() {

        db.collection("localização").orderBy("time", Query.Direction.ASCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                locations.clear();
                for(QueryDocumentSnapshot document : task.getResult()){
                    loc = document.getData();
                    references.add(document.getId());
                    locations.add(loc);
                }

            }
        });
    }

    public ArrayList getLocations(){
        locsFromFirebase();
        return locations;
        }

    public void deleteLocs(){
        for(String reference: references){
            db.collection("localização").document(reference).delete();
        }
    }
    }


