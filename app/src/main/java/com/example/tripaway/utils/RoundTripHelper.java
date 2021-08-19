//package com.example.tripaway.utils;
//
//import android.util.Log;
//
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.OnSuccessListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.firestore.DocumentReference;
//import com.google.firebase.firestore.FirebaseFirestore;
//import com.google.firebase.firestore.QueryDocumentSnapshot;
//import com.google.firebase.firestore.QuerySnapshot;
//
//import java.sql.Timestamp;
//import java.util.Map;
//
//public class RoundTripHelper {
//
//    private FirebaseAuth mAuth;
//    private FirebaseFirestore dbFireStore;
//
//   // private Map<String , Object> roundTripMap ;
//    private Map<String , Object> firstTripMap ;
//    private Map<String , Object> secondTripMap ;
//
//    public  RoundTripHelper(Map<String , Object> firstTripMap, Map<String , Object> secondTripMap){
//
//        this.firstTripMap = firstTripMap;
//        this.secondTripMap = secondTripMap;
//        mAuth = FirebaseAuth.getInstance();
//        dbFireStore = FirebaseFirestore.getInstance();
//
//
//
//    }
//    public RoundTripHelper(){
//
//
//        mAuth = FirebaseAuth.getInstance();
//        dbFireStore = FirebaseFirestore.getInstance();
//        addRoundTrip();
//
//
//
//    }
//
//    public void addRoundTrip()
//    {
//
////        this.tripName = tripName;
////        this.startPoint = startPoint;
////        this.endPoint = endPoint;
////        this.date = date;
////        this.time = time;
////        this.isOneDirection = isOneDirection;
////        this.repeat = repeat;
////        this.notes = notes;
////        this.timestamp = timestamp;
//
//       // Map<String, Object> firstMap;
////        firstTripMap = new RoundTripModel("firstTrip", "start", "end" ,
////                "Friday, Aug 20, 2021","5:24", false, 1, null,
////                new Timestamp( System.currentTimeMillis()), false,null).getRoundTripsMap();
////       // Map<String, Object> secondMap;
////        secondTripMap = new RoundTripModel("Second", "start", "end" ,
////                "Friday, Aug 20, 2021","5:24", false, 1, null,
////               null, true,null).getRoundTripsMap();
//
//
//
//
//        //adding map to fireStore
//
////        roundTripMap.put("firstTrip", firstMap);
////        roundTripMap.put("secondTrip", secondMap);
//
//        sendRoundTripToFireStore();
//
//
//    }
//
//    private void sendRoundTripToFireStore() {
//
//       // firstTripMap.put("isLocked", false);
//     //   secondTripMap.put("firstTripDocumentId", null);
//
//
//        dbFireStore.collection("users")
//                .document(mAuth.getUid())
//                .collection("upcoming")
//                .add(firstTripMap)
//                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                    @Override
//                    public void onSuccess(DocumentReference documentReference) {
//
//                        String firstTripDocumentId = documentReference.getId();
//                        sendSecondTripToFirStore(firstTripDocumentId);
//
//
//
//
//
//                    }
//                });
//
//
//    }
//
//    private void sendSecondTripToFirStore(String firstTripDocumentId) {
//
//        secondTripMap.put("timestamp",  new Timestamp( System.currentTimeMillis()));
//        secondTripMap.put("isLocked", true);
//        secondTripMap.put("firstTripDocumentId", firstTripDocumentId);
//
//        dbFireStore.collection("users")
//                .document(mAuth.getUid())
//                .collection("upcoming")
//                .add(secondTripMap)
//                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                    @Override
//                    public void onSuccess(DocumentReference documentReference) {
//
//
//
//
//                    }
//                });
//
//    }
//
//
//    public  void startRoundTrip(String selectedDocumentId, boolean done)
//    {
//
//        //start the first trip
//        FireStoreHelper.sendDataFromUpcomingToHistory(selectedDocumentId, done);
//
//        //To check if second trip is locked exit
//        dbFireStore.collection("users")
//                .document(mAuth.getUid())
//                .collection("upcoming")
//                .whereEqualTo("firstTripDocumentId", selectedDocumentId)
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete( Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            for (QueryDocumentSnapshot document : task.getResult()) {
//
//                              String  documentId = document.getId();
//
//                                Log.d("WEAAM", document.getId() + " => " + document.getData());
//
//                                //update isLocked
//                                dbFireStore.collection("users")
//                                        .document(mAuth.getUid())
//                                        .collection("upcoming")
//                                        .document(documentId)
//                                        .update("isLocked", false);
//
//
//
//
//
//
//                            }
//
//
//
//
//
//
//
//                        } else {
//                            Log.d("WEAAM", "Error getting documents: ", task.getException());
//                        }
//                    }
//                })
//        ;
//
//    }
//
//
//
//}
