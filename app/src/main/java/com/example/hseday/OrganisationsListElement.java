package com.example.hseday;

import android.media.Image;

/**
 * Created by Виталий on 31.01.2017.
 */

public class OrganisationsListElement {
    String Name;
    Image Icon;

    OrganisationsListElement(String Name, Image Icon){
        this.Icon = Icon;
        this.Name = Name;
    }
}
