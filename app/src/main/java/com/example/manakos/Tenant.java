package com.example.manakos;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Tenant implements Parcelable {
    private String UID;
    private String KID;
    private String RID;

    public Tenant(String UID, String KID, String RID1) {
        this.UID = UID;
        this.KID = KID;
        this.RID = RID1;
    }

    protected Tenant(Parcel in) {
        UID = in.readString();
        RID = in.readString();
        KID = in.readString();
    }

    public static final Creator<Tenant> CREATOR = new Creator<Tenant>() {
        @Override
        public Tenant createFromParcel(Parcel in) {
            return new Tenant(in);
        }

        @Override
        public Tenant[] newArray(int size) {
            return new Tenant[size];
        }
    };

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getKID() {
        return KID;
    }

    public void setKID(String KID) {
        this.KID = KID;
    }

    public String getRID() {
        return RID;
    }

    public void setRID(String RID) {
        this.RID = RID;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(UID);
        dest.writeString(RID);
        dest.writeString(KID);
    }
}
