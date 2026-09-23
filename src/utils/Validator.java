package src.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Validator {
    public static boolean isValidMaBenhAn(String maBenhAn) {
        if (maBenhAn == null) {
            return false;
        }
        return maBenhAn.matches("^BA-\\d{3}$");
    }

    public static boolean isValidDate(String date) {
        if (date == null || date.isEmpty()) {
            return false;
        }
        
        if (!date.matches("^\\d{1,2}/\\d{1,2}/\\d{4}$")) {
            return false;
        }
        
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        sdf.setLenient(false);
        
        try {
            sdf.parse(date);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isValidate(String date) {
        return isValidDate(date);
    }

    public static boolean isAdmissionBeforeOrEqualDischarge(String ngayNhapVien, String ngayRaVien) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            sdf.setLenient(false);
            Date d1 = sdf.parse(ngayNhapVien);
            Date d2 = sdf.parse(ngayRaVien);
            return !d2.before(d1);
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isNgayNhapVienRaVien(String ngayNhapVien, String ngayRaVien) throws ParseException {
        return isAdmissionBeforeOrEqualDischarge(ngayNhapVien, ngayRaVien);
    }

    public static boolean isValidVIPType(String loai) {
        return "VIP I".equals(loai) || "VIP II".equals(loai) || "VIP III".equals(loai);
    }

    public static boolean isValidPhiNamVien(long phiNamVien) {
        return phiNamVien >= 0;
    }

    public static boolean isValidName(String name) {
        return name != null && name.matches("^[A-Za-z ]+$");
    }
}
