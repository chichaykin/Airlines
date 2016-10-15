package com.mich.airlines.repositories;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.annotation.Unique;
import com.raizlabs.android.dbflow.structure.BaseModel;

@SuppressWarnings("FieldCanBeLocal")
@Table(database = AppDatabase.class)
public class Favorite extends BaseModel {

    @PrimaryKey
    @Unique
    String code;
    @Column
    String logoURL;
    @Column
    String site;
    @Column
    String phone;
    @Column
    String name;

    public Favorite() {
    }

    public Favorite(String code, String logoURL, String site, String phone, String name) {
        this.code = code;
        this.logoURL = logoURL;
        this.site = site;
        this.phone = phone;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getLogoURL() {
        return logoURL;
    }

    public String getSite() {
        return site;
    }

    public String getPhone() {
        return phone;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Favorite{" +
                "code='" + code + '\'' +
                ", logoURL='" + logoURL + '\'' +
                ", site='" + site + '\'' +
                ", phone='" + phone + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}


