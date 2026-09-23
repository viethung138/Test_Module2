package src.service;

import java.util.ArrayList;
import java.util.List;

import src.exceptions.DuplicateMedicalRecordException;
import src.models.BenhAn;
import src.models.BenhAnThuong;
import src.models.BenhAnVIP;
import java.io.File;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class QuanLyBenhAn {
    private static final String FILE_PATH = "data/medical_records.csv";
    private static final String HEADER = "STT, MaBenhAn, TenBenhNhan, NgayNhapVien, NgayRaVien, LyDoNhapVien, LoaiBenhAn";
    
    public List<BenhAn> readAll() {
        List<BenhAn> list = new ArrayList<>();
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return list;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            boolean isHeader = true;

            while ((line = br.readLine()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue;
                }

                if (line.trim().isEmpty()) {
                    continue;
                }
                
                BenhAn record = parseCSVLine(line);
                if (record != null) {
                    list.add(record);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading CSV: " + e.getMessage());
        }
        return list;
    }
        
    private BenhAn parseCSVLine(String line) {
        String[] parts = line.split(",");
        if (parts.length < 10) {
            return null;
        }

        int stt = Integer.parseInt(parts[0].trim());
        String maBenhAn = parts[1].trim();
        String tenBenhNhan = parts[2].trim();
        String ngayNhapVien = parts[3].trim();
        String ngayRaVien = parts[4].trim();
        String lyDoNhapVien = parts[5].trim();
        String loaiBenhAn = parts[6].trim();

        if (loaiBenhAn.equalsIgnoreCase("Thuong")) {
            long phiNamVien = 0;
            if (!parts[7].trim().isEmpty()) {
                phiNamVien = Long.parseLong(parts[7].trim());
            }
            return new BenhAnThuong(stt, maBenhAn, tenBenhNhan, ngayNhapVien, ngayRaVien, lyDoNhapVien, phiNamVien);
        } else if (loaiBenhAn.equalsIgnoreCase("VIP")) {
            String loaiVip = parts.length > 7 ? parts[7].trim() : "";
            String thoiHanVip = parts.length > 8 ? parts[8].trim() : "";
            return new BenhAnVIP(stt, maBenhAn, tenBenhNhan, ngayNhapVien, ngayRaVien, lyDoNhapVien, loaiVip, thoiHanVip);
        }
        return null;
    }

    private void writeAll(List<BenhAn> list) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH))) {
            bw.write(HEADER);
            bw.newLine();
            for (BenhAn benhAn : list) {
                bw.write(benhAn.toCSV());
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error writing CSV: " + e.getMessage());
        }
    }

    public int getNextSTT() {
        List<BenhAn> list = readAll();
        if (list.isEmpty()) {
            return 1;
        }
        return list.get(list.size() - 1).getStt() + 1;
    }

    public boolean isDuplicate(String maBenhAn) {
        List<BenhAn> list = readAll();
        for (BenhAn benhAn : list) {
            if (benhAn.getMaBenhAn().equals(maBenhAn)) {
                return true;
            }
        }
        return false;
    }

    public void addBenhAn(BenhAn benhAn) throws DuplicateMedicalRecordException {
        if (isDuplicate(benhAn.getMaBenhAn())) {
            throw new DuplicateMedicalRecordException("Ma benh an da ton tai");
        }

        File dir = new File("data");
        if (!dir.exists()) {
            dir.mkdirs();
        }

        File file = new File(FILE_PATH);
        boolean writeHeader = !file.exists() || file.length() == 0;

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            if (writeHeader) {
                bw.write(HEADER);
                bw.newLine();
            }
            bw.write(benhAn.toCSV());
            bw.newLine();
        } catch (IOException e) {
            System.err.println("Error writing CSV: " + e.getMessage());
        }
    }

    public boolean deleteRecord(String maBenhAn) {
        List<BenhAn> list = readAll();
        boolean found = false;
         
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getMaBenhAn().equals(maBenhAn)) {
                list.remove(i);
                found = true;
                break;
            }
        }
        if (found) {
            writeAll(list);
        }
        return found;
    }

    public void displayAll() {
        List<BenhAn> list = readAll();
        if (list.isEmpty()) {
            System.out.println("Danh sách bệnh nhân trống.");
            return;
        }

        System.out.println("================= DANH SÁCH BỆNH NHÂN =================");
        for (BenhAn benhAn : list) {
            System.out.println(benhAn.toString());
        }
        System.out.println("=======================================================");
    }
}
