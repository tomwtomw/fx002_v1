/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tomw.czeki.entities;

import java.util.Objects;
import org.json.simple.JSONObject;

/**
 *
 * @author tomw
 */
public class TwoSidedCheckImage {

    private static final String FRONT_KEY  = "Front";
    private static final String BACK_KEY  = "Back";
    private String frontPageLocation;
    private String backPageLocation;
   
    
    public static final String BLANK="";

    public TwoSidedCheckImage(){
        this.frontPageLocation=BLANK;
        this.backPageLocation=BLANK;
    }
    public TwoSidedCheckImage(String front,String back){
        this.frontPageLocation=front;
        this.backPageLocation=back;
    }

    public String getFrontPageLocation() {
        return frontPageLocation;
    }

    public void setFrontPageLocation(String frontPageLocation) {
        this.frontPageLocation = frontPageLocation;
    }

    public String getBackPageLocation() {
        return backPageLocation;
    }

    public void setBackPageLocation(String backPageLocation) {
        this.backPageLocation = backPageLocation;
    }
    
    public JSONObject toJson(){
        JSONObject result = new JSONObject();
        result.put(FRONT_KEY, getFrontPageLocation());
        result.put(BACK_KEY , getBackPageLocation());
        return result;
    }
    
    public static TwoSidedCheckImage fromJson(JSONObject json){
        TwoSidedCheckImage result = new TwoSidedCheckImage();
        String front = (String)json.get(FRONT_KEY);
        String back = (String)json.get(BACK_KEY);
        
        if(front!=null){
            result.setFrontPageLocation(front);
        }
        if(back!=null){
            result.setBackPageLocation(back);
        }
        return result;
    }
    
    @Override
    public String toString(){
        return toJson().toString();
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + Objects.hashCode(this.frontPageLocation);
        hash = 71 * hash + Objects.hashCode(this.backPageLocation);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final TwoSidedCheckImage other = (TwoSidedCheckImage) obj;
        if (!Objects.equals(this.frontPageLocation, other.frontPageLocation)) {
            return false;
        }
        return Objects.equals(this.backPageLocation, other.backPageLocation);
    }
    
}
