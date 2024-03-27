package com.evans.pillreminder.db;

import static com.evans.pillreminder.helpers.Constants.DB_USER_TABLE_NAME;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
@Entity(tableName = DB_USER_TABLE_NAME)
public class User {
    @PrimaryKey(autoGenerate = true)
    private int userId;
    private boolean synced = false; // Flag to track sync status
    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    private boolean gender;
    private String username;
    private String imageURL;
    private String uid;// Store the UID of the logged in user

    public User(String firstName, String lastName, String phone, String email, boolean gender, String username, String uid, String imageURL) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.email = email;
        this.gender = gender;
        this.username = username;
        this.imageURL = imageURL;
        this.uid = uid;
    }

    public User() {
        // empty constructor for firebase firestore
        // Default constructor required for calls to DataSnapshot.getValue()
    }

    // to create a User Object from a User document Snapshot
    public static User fromSnapshot(DocumentSnapshot snapshot) {
        User user = new User();
        user.setFirstName(snapshot.getString("firstName"));
        user.setLastName(snapshot.getString("lastName"));
        user.setPhone(snapshot.getString("phone"));
        user.setEmail(snapshot.getString("email"));
        user.setGender(snapshot.getBoolean("gender"));
        user.setUsername(snapshot.getString("username"));
        user.setImageURL(snapshot.getString("imageURL"));
        return user;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public boolean isSynced() {
        return synced;
    }

    public void setSynced(boolean synced) {
        this.synced = synced;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("firstName", firstName);
        map.put("lastName", lastName);
        map.put("phone", phone);
        map.put("email", email);
        map.put("gender", gender);
        map.put("username", username);
        map.put("imageURL", imageURL);
        return map;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean getGender() {
        return gender;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }


}
