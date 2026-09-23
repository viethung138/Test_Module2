package src.models;

public class BenhAnThuong extends BenhAn {
    private long phiNamVien;

    public BenhAnThuong() {

    }

    public BenhAnThuong(int stt, String maBenhAn, String tenBenhNhan, String ngayNhapVien, String ngayRaVien, String lyDoNhapVien, long phiNamVien) {
        super(stt, maBenhAn, tenBenhNhan, ngayNhapVien, ngayRaVien, lyDoNhapVien);
        this.phiNamVien = phiNamVien;
    }

    public long getPhiNamVien() {
        return phiNamVien;
    }

    public void setPhiNamVien(long phiNamVien) {
        this.phiNamVien = phiNamVien;
    }

    @Override 
    public String toCSV() {
        return getStt() + "," + getMaBenhAn() + "," + getTenBenhNhan() + "," + getNgayNhapVien() + "," + getNgayRaVien() + "," + getLyDoNhapVien() + "," + getLoaiBenhAn() + "," + phiNamVien;
    }

    @Override
    public String getLoaiBenhAn() {
        return "Thuong";
    }

    @Override 
    public String toString() {
        return super.toString() + ", " + phiNamVien + "VND";
    }
}
