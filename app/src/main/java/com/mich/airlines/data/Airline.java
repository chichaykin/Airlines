package com.mich.airlines.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Describe an airline
 */
public class Airline implements Parcelable {
    private String code;
    private String defaultName;
    private String logoURL;
    private String name;
    private String phone;
    private String site;
    private String usName;
    private boolean favorite;

    public Airline(String name, String code, String logoURL, String phone, String site, boolean favorite) {
        this.code = code;
        this.logoURL = logoURL;
        this.name = name;
        this.phone = phone;
        this.site = site;
        this.favorite = favorite;
    }

    protected Airline(Parcel in) {
        code = in.readString();
        defaultName = in.readString();
        logoURL = in.readString();
        name = in.readString();
        phone = in.readString();
        site = in.readString();
        usName = in.readString();
        favorite = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(code);
        dest.writeString(defaultName);
        dest.writeString(logoURL);
        dest.writeString(name);
        dest.writeString(phone);
        dest.writeString(site);
        dest.writeString(usName);
        dest.writeByte((byte) (favorite ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Airline> CREATOR = new Creator<Airline>() {
        @Override
        public Airline createFromParcel(Parcel in) {
            return new Airline(in);
        }

        @Override
        public Airline[] newArray(int size) {
            return new Airline[size];
        }
    };

    public String getCode() {
        return code;
    }

    public String getDefaultName() {
        return defaultName;
    }

    public String getLogoURL() {
        return logoURL;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getSite() {
        return site;
    }

    public String getUsName() {
        return usName;
    }

    public void setLogoURL(String logoURL) {
        this.logoURL = logoURL;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public boolean isFavorite() {
        return favorite;
    }

    @Override
    public String toString() {
        return "Airline{" +
                "code='" + code + '\'' +
                ", defaultName='" + defaultName + '\'' +
                ", logoURL='" + logoURL + '\'' +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", site='" + site + '\'' +
                ", usName='" + usName + '\'' +
                ", favorite=" + favorite +
                '}';
    }
}
