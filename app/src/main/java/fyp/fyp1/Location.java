package fyp.fyp1;

import java.util.ArrayList;



public class Location {
   String email,uid,lat,lng;
   int status;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    ArrayList<Item> itemArrayList= new ArrayList<>();

    public Location(String email, String uid, String lat, String lng, ArrayList<Item> itemArrayList,int status) {
        this.email = email;
        this.uid = uid;
        this.lat = lat;
        this.lng = lng;
        this.itemArrayList = itemArrayList;
        this.status=status;
    }

    public Location() {

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public ArrayList<Item> getItemArrayList() {
        return itemArrayList;
    }

    public void setItemArrayList(ArrayList<Item> itemArrayList) {
        this.itemArrayList = itemArrayList;
    }
}
