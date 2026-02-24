<div align="center">

<!-- 🖼️ HÌNH ẢNH: Chèn logo ứng dụng tại đây -->
<!-- Gợi ý: <img src="docs/images/logo.png" alt="FlashcardApp Logo" width="180"/> -->

# 📚 Flashcard Learning Application

**Ứng dụng học từ vựng tương tác hiệu suất cao, xây dựng bằng JavaFX**

[![Java](https://img.shields.io/badge/Java-17-orange?style=flat-square&logo=openjdk)](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
[![JavaFX](https://img.shields.io/badge/JavaFX-17-blue?style=flat-square&logo=openjfx)](https://openjfx.io/)
[![SQLite](https://img.shields.io/badge/SQLite-3-lightgrey?style=flat-square&logo=sqlite)](https://www.sqlite.org/)
[![Maven](https://img.shields.io/badge/Build-Maven-red?style=flat-square&logo=apachemaven)](https://maven.apache.org/)
[![License: MIT](https://img.shields.io/badge/License-MIT-green?style=flat-square)](LICENSE)

[Tính năng](#-tính-năng) · [Cài đặt](#️-cài-đặt--chạy-ứng-dụng) · [Cấu trúc dự án](#-cấu-trúc-dự-án) · [Công nghệ](#-công-nghệ-sử-dụng) · [Database](#-database) · [Tác giả](#-tác-giả)

</div>

---

## 📖 Giới thiệu

**Flashcard Learning Application** là một ứng dụng desktop được xây dựng bằng **JavaFX**, giúp người dùng ghi nhớ từ vựng một cách hiệu quả thông qua hệ thống flashcard tương tác. Ứng dụng hỗ trợ nhiều chế độ học tập và kiểm tra, theo dõi tiến trình học hàng ngày, phù hợp cho học sinh, sinh viên và người tự học ngoại ngữ.

<!-- 🖼️ HÌNH ẢNH: Chèn ảnh chụp màn hình tổng quan ứng dụng (dashboard/màn hình chính) -->
<!-- Gợi ý: <img src="docs/images/screenshot-main.png" alt="Màn hình chính" width="800"/> -->

---

## ✨ Tính năng

### 🔐 Quản lý người dùng

- **Đăng ký & Đăng nhập** — Xác thực người dùng an toàn, lưu trữ bằng SQLite.
- **Session Manager** — Quản lý phiên làm việc hiện tại của người dùng.
- **Quên mật khẩu** — *(Đang phát triển)*

<!-- 🖼️ HÌNH ẢNH: Chèn ảnh màn hình Đăng nhập / Đăng ký -->
<!-- Gợi ý: <img src="docs/images/screenshot-login.png" alt="Màn hình đăng nhập" width="600"/> -->

---

### 📁 Quản lý nội dung

- **Chủ đề (Topics)** — Tạo, chỉnh sửa và xoá các chủ đề học tập một cách linh hoạt.
- **Từ vựng (Vocabulary)** — Quản lý toàn bộ danh sách từ vựng trong từng chủ đề: thêm, sửa, xoá.
- **Tìm kiếm** — Tìm kiếm nhanh chủ đề hoặc từ vựng theo tên.

<!-- 🖼️ HÌNH ẢNH: Chèn ảnh màn hình danh sách chủ đề & từ vựng -->
<!-- Gợi ý: <img src="docs/images/screenshot-topics.png" alt="Quản lý chủ đề" width="600"/> -->

---

### 📖 Chế độ học tập & Kiểm tra

| Chế độ | Mô tả |
|---|---|
| **Study Mode** | Xem flashcard theo chủ đề, lật thẻ để xem định nghĩa |
| **Topic Test** | Làm bài kiểm tra dựa trên một chủ đề cụ thể |
| **Random Test** | Câu hỏi tổng hợp ngẫu nhiên từ nhiều chủ đề |

**Các dạng câu hỏi được hỗ trợ:**
- 🔘 Trắc nghiệm (Multiple choice)
- ✍️ Điền từ (Fill in the blank)
- 🔤 Sắp xếp chữ cái (Scrambled words)

<!-- 🖼️ HÌNH ẢNH: Chèn ảnh màn hình học flashcard (Study Mode) -->
<!-- Gợi ý: <img src="docs/images/screenshot-study.png" alt="Chế độ học tập" width="600"/> -->

<!-- 🖼️ HÌNH ẢNH: Chèn ảnh màn hình làm bài kiểm tra (Test Mode) -->
<!-- Gợi ý: <img src="docs/images/screenshot-test.png" alt="Chế độ kiểm tra" width="600"/> -->

---

### 📊 Thống kê & Theo dõi tiến độ

- **Lịch sử kiểm tra** — Lưu lại toàn bộ kết quả và điểm số các lần làm bài.
- **Điểm trung bình** — Tổng hợp và hiển thị điểm trung bình theo từng chủ đề.
- **Streak (Chuỗi ngày học)** — Ghi nhận số ngày học liên tục, khuyến khích duy trì thói quen học tập.

<!-- 🖼️ HÌNH ẢNH: Chèn ảnh màn hình thống kê / biểu đồ tiến độ -->
<!-- Gợi ý: <img src="docs/images/screenshot-stats.png" alt="Thống kê tiến độ" width="600"/> -->

---

## 🗂 Cấu trúc dự án

```text
FlashcardApp/
├── src/
│   └── main/
│       ├── java/com/mycompany/flashcardapp/
│       │   ├── App.java                   # Entry point (Main class)
│       │   ├── controller/                # FXML Controllers (Business Logic)
│       │   ├── database/                  # SQLite DAO & Connection Manager
│       │   ├── model/                     # Data Entities (Flashcard, Topic, User, ...)
│       │   └── constant/                  # Hằng số & thông báo toàn hệ thống
│       └── resources/
│           └── view/                      # FXML Layouts (Giao diện)
├── pom.xml                                # Maven Dependencies
└── flashcard.sqlite                       # File cơ sở dữ liệu SQLite cục bộ
```

---

## 💻 Công nghệ sử dụng

| Thành phần | Công nghệ | Phiên bản |
|---|---|---|
| Ngôn ngữ | [Java](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html) | 17 |
| UI Framework | [JavaFX](https://openjfx.io/) | 17 |
| Cơ sở dữ liệu | [SQLite](https://www.sqlite.org/) + JDBC | 3.x |
| Build Tool | [Apache Maven](https://maven.apache.org/) | 3.x |

---

## ⚙️ Cài đặt & Chạy ứng dụng

### Yêu cầu hệ thống

Trước khi bắt đầu, hãy đảm bảo máy tính đã cài đặt:

- ✅ **JDK 17** trở lên — [Tải tại đây](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
- ✅ **Apache Maven** — [Tải tại đây](https://maven.apache.org/download.cgi)

Kiểm tra phiên bản đã cài:
```bash
java -version
mvn -version
```

---

### Các bước cài đặt

**Bước 1: Clone repository**
```bash
git clone https://github.com/Lanlanchuche/FlashcardApp.git
cd FlashcardApp
```

**Bước 2: Build dự án**
```bash
mvn clean install
```

**Bước 3: Chạy ứng dụng**
```bash
mvn javafx:run
```

> 💡 **Lưu ý:** File cơ sở dữ liệu `flashcard.sqlite` sẽ được tự động tạo ở thư mục gốc khi khởi chạy lần đầu.

---

## 🗄 Database

Ứng dụng sử dụng **SQLite** để lưu trữ toàn bộ dữ liệu cục bộ trên máy người dùng, không cần cấu hình server.

- **File:** `flashcard.sqlite` (tự động khởi tạo khi chạy lần đầu)
- **Kết nối:** Được quản lý qua `DatabaseConnection.java`
- **Truy cập dữ liệu:** Thông qua các lớp DAO trong thư mục `database/`

**Sơ đồ các bảng chính:**

| Bảng | Mô tả |
|---|---|
| `users` | Thông tin tài khoản người dùng |
| `topics` | Danh sách chủ đề học tập |
| `flashcards` | Từ vựng thuộc từng chủ đề |
| `test_results` | Lịch sử và kết quả các lần kiểm tra |
| `streaks` | Dữ liệu chuỗi ngày học liên tục |

<!-- 🖼️ HÌNH ẢNH: Chèn sơ đồ ERD (Entity Relationship Diagram) của database -->
<!-- Gợi ý: <img src="docs/images/erd.png" alt="Database ERD" width="700"/> -->

---

## 🗺 Lộ trình phát triển (Roadmap)

- [x] Hệ thống đăng ký & đăng nhập
- [x] Quản lý chủ đề & từ vựng
- [x] Chế độ học Flashcard
- [x] Chế độ kiểm tra (Topic & Random)
- [x] Thống kê điểm số & Streak
---

## 🤝 Đóng góp

Mọi đóng góp đều được hoan nghênh! Nếu bạn muốn cải thiện dự án:

1. Fork repository này
2. Tạo branch mới: `git checkout -b feature/ten-tinh-nang`
3. Commit thay đổi: `git commit -m 'feat: Thêm tính năng XYZ'`
4. Push lên branch: `git push origin feature/ten-tinh-nang`
5. Mở một **Pull Request**

---

## 📄 Giấy phép

Dự án này được phân phối theo giấy phép **MIT**. Xem chi tiết tại file [LICENSE](LICENSE).

---

## 👤 Tác giả

**Lan Nguyễn**

- 📧 Email: [lanlannguyen3306@gmail.com](mailto:lanlannguyen3306@gmail.com)
- 🐙 GitHub: [@Lanlanchuche](https://github.com/Lanlanchuche)

**Tạ Phương Anh**

- 📧 Email: [panh30954@gmail.com](mailto:panh30954@gmail.com)
- 🐙 GitHub: 

---

<div align="center">

Nếu dự án này hữu ích với bạn, hãy để lại một ⭐ để ủng hộ tác giả!


</div>

