package com.patachadmin.patachou.Model;

public class Admin {
    String adminId,adminName,adminPic,adminEmail,password;


    public Admin(String adminId, String adminName, String adminEmail, String password) {
        this.adminId = adminId;
        this.adminName = adminName;
      //  this.adminPic = adminPic;
        this.adminEmail = adminEmail;
        this.password=password;
    }

    public String getPassword() {
        return password;
    }

    public String getAdminId() {
        return adminId;
    }

    public String getAdminName() {
        return adminName;
    }

    public String getAdminPic() {
        return adminPic;
    }

    public String getAdminEmail() {
        return adminEmail;
    }
}
