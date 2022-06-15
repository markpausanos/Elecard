package com.example.fc;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class DB_Cards {
    private DatabaseReference databaseReferenceUser, databaseReferenceCardSet;
    private FirebaseAuth firebaseAuth;
    public DB_Cards() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReferenceUser = database.getReference("users").child(firebaseAuth.getCurrentUser().getUid());
        databaseReferenceCardSet = database.getReference("users").child(firebaseAuth.getCurrentUser().getUid()).child("cardset");
    }
    //USER
    public Task<Void> updateUserNickName(String name){
        return databaseReferenceUser.child("nickname").setValue(name);
    }
    //CARDSET
    public Class_CardSet createCardSet(Class_CardSet cardSet){
        String ID = databaseReferenceCardSet.push().getKey();
        cardSet.setId(ID);
        databaseReferenceCardSet.child(ID).setValue(cardSet);
        return cardSet;
    }
    public Class_CardSet updateCardSetName(Class_CardSet cardSet, String name){
        databaseReferenceCardSet.child(cardSet.getId()).child("name").setValue(name);
        cardSet.setName(name);
        return cardSet;
    }
    public Class_CardSet updateCardSetSize(Class_CardSet cardSet, int size){
        cardSet.setSize(size);
        databaseReferenceCardSet.child(cardSet.getId()).child("size").setValue(size);
        return cardSet;
    }
    public void deleteCardSet(Class_CardSet cardSet){
        databaseReferenceCardSet.child(cardSet.getId()).removeValue();
    }

    //FLASHCARDS
    public Class_Card createFlashCard(Class_CardSet cardSet, Class_Card card){
        String ID = databaseReferenceCardSet.child(cardSet.getId()).child("flashcards").push().getKey();
        card.setId(ID);
        card.setIdParent(cardSet.getId());
        cardSet.setSize(cardSet.getSize()+1);
        databaseReferenceCardSet.child(cardSet.getId()).child("size").setValue(cardSet.getSize());
        databaseReferenceCardSet.child(cardSet.getId()).child("flashcards").child(ID).setValue(card);
        return card;
    }
    public Task<Void> updateFlashCard(Class_Card card, String question, String answer){
        HashMap<String, Object> cardData = new HashMap<>();
        cardData.put("question", question);
        cardData.put("answer", answer);
        cardData.put("idParent", card.getIdParent());
        cardData.put("id", card.getId());
        return databaseReferenceCardSet.child(card.getIdParent()).child("flashcards").child(card.getId()).updateChildren(cardData);
    }
    public Task<Void> deleteFlashCard(Class_Card card){
        return databaseReferenceCardSet.child(card.getIdParent()).child("flashcards").child(card.getId()).removeValue();
    }
    public Task<Void> deleteFlashCard(Class_CardSet cardSet, Class_Card card, int size){
        databaseReferenceCardSet.child(card.getIdParent()).child("flashcards").child(card.getId()).removeValue();
        return databaseReferenceCardSet.child(card.getIdParent()).child("size").setValue(size);
    }

    public Query getCardSets(){
        return databaseReferenceCardSet.orderByKey();
    }

    public Query getFlashCards(Class_CardSet cardSet){
        return databaseReferenceCardSet.child(cardSet.getId()).child("flashcards").orderByKey();
    }
//    public int getSizeCardSet(Class_CardSet cardSet) {
//        final int[] size = new int[1];
//        databaseReferenceCardSet.child(cardSet.getId()).child("size").addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                for (DataSnapshot data : dataSnapshot.getChildren()) {
//                    size[0] = data.getValue(Integer.class);
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//            }
//        });
//        return size[0];
//    }
}
