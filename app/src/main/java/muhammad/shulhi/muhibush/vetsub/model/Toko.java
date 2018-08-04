package muhammad.shulhi.muhibush.vetsub.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.Date;

public class Toko{
    private String _id;
    private String nama;
    private String email;
    private String password;
    private String alamat;
    private String telepon;
    private String foto;
    private Date createdAt;
    private Date updateAt;
    private Jam jam;
    private Posisi posisi;
    private String search;

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public String get_id() {
        return _id;
    }

    public String getNama() {
        return nama;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getAlamat() {
        return alamat;
    }

    public String getTelepon() {
        return telepon;
    }

    public String getFoto() {
        return foto;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Date getUpdateAt() {
        return updateAt;
    }

    public Jam getJam() {
        return jam;
    }

    public Posisi getPosisi() {
        return posisi;
    }
}
