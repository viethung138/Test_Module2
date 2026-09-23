package src.models;

public class BenhAnVIP extends BenhAn {
    private String loaiVip;
    private String thoiHanVip;

    public BenhAnVIP() {

    }

    public BenhAnVIP(int stt, String maBenhAn, String tenBenhNhan, String ngayNhapVien, String ngayRaVien, String lyDoNhapVien, String loaiVip, String thoiHanVip) {
        super(stt, maBenhAn, tenBenhNhan, ngayNhapVien, ngayRaVien, lyDoNhapVien);
        this.loaiVip = loaiVip;
        this.thoiHanVip = thoiHanVip;
    }

    public String getLoaiVip() {
        return loaiVip;
    }

    public void setLoaiVip(String loaiVip) {
        this.loaiVip = loaiVip;
    }

    public String getThoiHanVip() {
        return thoiHanVip;
    }

    public void setThoiHanVip(String thoiHanVip) {
        this.thoiHanVip = thoiHanVip;
    }

    @Override 
    public String toCSV() {
        return getStt() + "," + getMaBenhAn() + "," + getTenBenhNhan() + "," + getNgayNhapVien() + "," + getNgayRaVien() + "," + getLyDoNhapVien() + "," + getLoaiBenhAn() + "," + loaiVip + "," + thoiHanVip;
    }

    @Override
    public String getLoaiBenhAn() {
        return "VIP";
    }

    @Override 
    public String toString() {
        return super.toString() + ", " + loaiVip + ", " + thoiHanVip;
    }
}
