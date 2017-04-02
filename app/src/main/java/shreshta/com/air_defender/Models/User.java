package shreshta.com.air_defender.Models;

import java.util.ArrayList;

/**
 * Created by amrith on 4/2/17.
 */

public class User {
    public String fcmId,name,uid,phone,sex,picture,status,email;
    public boolean registered;
    public int yob;
    public ArrayList<ContactModel> contacts;
}
