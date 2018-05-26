package me.phum.pocketigl.Model;


/**
 * User data object
 */
public class User extends DataObject {

    private String id;
    private String fullName;

    //default constructor
    public User(){}

    public User(String id, String fullName){
        this.id = id;
        this.fullName = fullName;
    }

    public String getId(){
        return this.id;
    }

    public String getFullName(){
        return this.fullName;
    }

}
