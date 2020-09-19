package com.example.internd;

public class Constants {
    public    String STORAGE_PATH_UPLOADS = "uploads/";
    public    String DATABASE_PATH_UPLOADS = "uploads";

    public Constants(String DATABASE_PATH_UPLOADS) {
        this.DATABASE_PATH_UPLOADS = DATABASE_PATH_UPLOADS;
    }

    public  String getStoragePathUploads() {
        return STORAGE_PATH_UPLOADS;
    }

    public Constants(String STORAGE_PATH_UPLOADS, String DATABASE_PATH_UPLOADS) {
        this.STORAGE_PATH_UPLOADS = STORAGE_PATH_UPLOADS;
        this.DATABASE_PATH_UPLOADS = DATABASE_PATH_UPLOADS;
    }

    public void setDATABASE_PATH_UPLOADS(String DATABASE_PATH_UPLOADS) {
        this.DATABASE_PATH_UPLOADS = DATABASE_PATH_UPLOADS;
    }

    public String getDatabasePathUploads() {
        return DATABASE_PATH_UPLOADS;
    }
}
