package com.velocityappsdj.subserve.POJOS;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FireBaseUtils {
    public static String getFirebaseId(){
        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        return user.getUid();

    }
}
