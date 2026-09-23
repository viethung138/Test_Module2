import java.util.Scanner;

import src.exceptions.DuplicateMedicalRecordException;
import src.models.BenhAn;
import src.models.BenhAnThuong;
import src.models.BenhAnVIP;
import src.service.QuanLyBenhAn;

public class App {
    private static final Scanner scanner = new Scanner(System.in);
    private static final QuanLyBenhAn service = new QuanLyBenhAn();

    public static void main(String[] args) {
        int choice;
        do {
            displayMenu();
            choice = getIntInput("Nhap lua chon: ");
            switch (choice) {
                case 1:
                    themMoi();
                    break;
                case 2:
                    xoaBenhAn();
                    break;
                case 3:
                    xemDanhSach();
                    break;
                case 4:
                    System.out.println("Tam biet!");
                    break;
                default:
                    System.out.println("Lua chon khong hop le. Vui long nhap lai.");
            }
        } while (choice != 4);
    }

    /**
     * Display the main menu
     */
    private static void displayMenu() {
        System.out.println("\n========== QUAN LY BENH AN - BENH VIEN XYZ ==========");
        System.out.println("1. Them moi benh an");
        System.out.println("2. Xoa benh an");
        System.out.println("3. Xem danh sach benh an");
        System.out.println("4. Thoat");
        System.out.println("=====================================================");
    }

    /**
     * Feature 1: Add a new medical record
     */
    private static void themMoi() {
        System.out.println("\n--- THEM MOI BENH AN ---");

        // Choose record type
        System.out.println("Chon loai benh an:");
        System.out.println("1. Benh an thuong");
        System.out.println("2. Benh an VIP");
        int loai = getIntInput("Nhap lua chon (1 hoac 2): ");

        while (loai != 1 && loai != 2) {
            System.out.println("Lua chon khong hop le. Vui long nhap 1 hoac 2.");
            loai = getIntInput("Nhap lua chon (1 hoac 2): ");
        }

        // Auto-increment STT
        int stt = service.getNextSTT();
        System.out.println("So thu tu benh an: " + stt);

        // Input and validate Ma Benh An
        String maBenhAn;
        while (true) {
            System.out.print("Nhap Ma Benh An (BA-XXX, voi XXX la so): ");
            maBenhAn = scanner.nextLine().trim();
            if (!Validator.isValidMaBenhAn(maBenhAn)) {
                System.out.println("Loi: Ma benh an phai dung dinh dang BA-XXX, voi XXX la cac ki tu so.");
                continue;
            }
            // Check duplicate
            try {
                if (service.isDuplicate(maBenhAn)) {
                    throw new DuplicateMedicalRecordException("Benh an da ton tai.");
                }
                break;
            } catch (DuplicateMedicalRecordException e) {
                System.out.println("Loi: " + e.getMessage() + " Vui long nhap lai.");
            }
        }

        // Input Ten Benh Nhan
        System.out.print("Nhap Ten Benh Nhan: ");
        String tenBenhNhan = scanner.nextLine().trim();

        // Input and validate Ngay Nhap Vien
        String ngayNhapVien;
        while (true) {
            System.out.print("Nhap Ngay Nhap Vien (dd/MM/yyyy): ");
            ngayNhapVien = scanner.nextLine().trim();
            if (!Validator.isValidDate(ngayNhapVien)) {
                System.out.println("Loi: Ngay nhap vien phai dung dinh dang dd/MM/yyyy.");
            } else {
                break;
            }
        }

        // Input and validate Ngay Ra Vien
        String ngayRaVien;
        while (true) {
            System.out.print("Nhap Ngay Ra Vien (dd/MM/yyyy): ");
            ngayRaVien = scanner.nextLine().trim();
            if (!Validator.isValidDate(ngayRaVien)) {
                System.out.println("Loi: Ngay ra vien phai dung dinh dang dd/MM/yyyy.");
                continue;
            }
            if (!Validator.isAdmissionBeforeOrEqualDischarge(ngayNhapVien, ngayRaVien)) {
                System.out.println("Loi: Ngay nhap vien phai nho hon hoac bang ngay ra vien.");
                continue;
            }
            break;
        }

        // Input Ly Do Nhap Vien
        System.out.print("Nhap Ly Do Nhap Vien: ");
        String lyDoNhapVien = scanner.nextLine().trim();

        BenhAn record;

        if (loai == 1) {
            // Benh An Thuong - input Phi Nam Vien
            double phiNamVien;
            while (true) {
                System.out.print("Nhap Phi Nam Vien (VND): ");
                try {
                    phiNamVien = Double.parseDouble(scanner.nextLine().trim());
                    if (phiNamVien < 0) {
                        System.out.println("Loi: Phi nam vien phai lon hon hoac bang 0.");
                        continue;
                    }
                    break;
                } catch (NumberFormatException e) {
                    System.out.println("Loi: Vui long nhap so hop le.");
                }
            }
            record = new BenhAnThuong(stt, maBenhAn, tenBenhNhan,
                    ngayNhapVien, ngayRaVien, lyDoNhapVien, phiNamVien);
        } else {
            // Benh An VIP - input Loai VIP
            String loaiVIP;
            while (true) {
                System.out.println("Chon Loai VIP:");
                System.out.println("1. VIP I");
                System.out.println("2. VIP II");
                System.out.println("3. VIP III");
                System.out.print("Nhap lua chon (1, 2 hoac 3): ");
                String vipChoice = scanner.nextLine().trim();
                switch (vipChoice) {
                    case "1":
                        loaiVIP = "VIP I";
                        break;
                    case "2":
                        loaiVIP = "VIP II";
                        break;
                    case "3":
                        loaiVIP = "VIP III";
                        break;
                    default:
                        System.out.println("Loi: Vui long chon 1, 2 hoac 3.");
                        continue;
                }
                if (Validator.isValidVIPType(loaiVIP)) {
                    break;
                }
            }

            // Input and validate Thoi Han VIP
            String thoiHanVIP;
            while (true) {
                System.out.print("Nhap Thoi Han VIP (dd/MM/yyyy): ");
                thoiHanVIP = scanner.nextLine().trim();
                if (!Validator.isValidDate(thoiHanVIP)) {
                    System.out.println("Loi: Thoi han VIP phai dung dinh dang dd/MM/yyyy.");
                } else {
                    break;
                }
            }

            record = new BenhAnVIP(stt, maBenhAn, tenBenhNhan,
                    ngayNhapVien, ngayRaVien, lyDoNhapVien, loaiVIP, thoiHanVIP);
        }

        // Save to CSV
        try {
            service.addRecord(record);
            System.out.println("Them moi benh an thanh cong!");
        } catch (DuplicateMedicalRecordException e) {
            System.out.println("Loi: " + e.getMessage());
        }
    }

    /**
     * Feature 2: Delete a medical record
     */
    private static void xoaBenhAn() {
        System.out.println("\n--- XOA BENH AN ---");
        System.out.print("Nhap Ma Benh An can xoa: ");
        String maBenhAn = scanner.nextLine().trim();

        if (!service.isDuplicate(maBenhAn)) {
            System.out.println("Khong tim thay benh an co ma: " + maBenhAn);
            return;
        }

        // Confirm deletion
        String confirm;
        while (true) {
            System.out.print("Ban co chac chan muon xoa benh an " + maBenhAn + "? (Yes/No): ");
            confirm = scanner.nextLine().trim();
            if (confirm.equalsIgnoreCase("Yes") || confirm.equalsIgnoreCase("No")) {
                break;
            }
            System.out.println("Vui long nhap 'Yes' hoac 'No'.");
        }

        if (confirm.equalsIgnoreCase("Yes")) {
            boolean deleted = service.deleteRecord(maBenhAn);
            if (deleted) {
                System.out.println("Xoa benh an thanh cong!");
                System.out.println("\nDanh sach benh an sau khi xoa:");
                service.displayAll();
            }
        } else {
            System.out.println("Huy xoa. Quay ve menu chinh.");
        }
    }

    /**
     * Feature 3: View all medical records
     */
    private static void xemDanhSach() {
        System.out.println("\n--- DANH SACH BENH AN ---");
        service.displayAll();
    }

    /**
     * Helper: Get integer input from user
     */
    private static int getIntInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                int value = Integer.parseInt(scanner.nextLine().trim());
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Loi: Vui long nhap so nguyen hop le.");
            }
        }
    }
}
