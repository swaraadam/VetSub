package muhammad.shulhi.muhibush.vetsub.model;

import java.io.Serializable;
import java.util.Date;

public class Dokter implements Serializable {
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
