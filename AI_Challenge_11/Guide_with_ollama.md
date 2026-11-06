# Hướng Dẫn Chạy Chương Trình Claim Assessment AI Agent

Tài liệu này hướng dẫn chi tiết cách thiết lập môi trường, biên dịch và khởi chạy dự án **Claim Assessment AI Agent** bằng Core Java trên máy tính.

---

## 1. Yêu cầu hệ thống

Trước khi bắt đầu, hãy đảm bảo máy tính của bạn đã cài đặt các công cụ sau:

- **Java Development Kit (JDK)**: Phiên bản 11 trở lên (Khuyên dùng OpenJDK 17 hoặc 21).
- **Ollama**: Đã cài đặt và đang khởi chạy cục bộ trên máy tại địa chỉ `http://localhost:11434`.
- **Mô hình AI**: Mô hình `qwen2.5-coder:7b` (hoặc tên mô hình bạn đã cấu hình trong code) đã được tải về Ollama (`ollama pull qwen2.5-coder:7b`).

---

## 2. Cấu trúc thư mục dự án

Đảm bảo cả 3 file Java sau nằm trong cùng một thư mục làm việc:

```text
claim-assessment-agent/
├── MockDatabase.java
├── AgentTools.java
└── ClaimAgentRunner.java
```

---

## 3. Các bước thực hiện

### Bước 1: Khởi động Ollama (Local LLM)
Mở Terminal và đảm bảo tiến trình Ollama đang hoạt động:
```bash
ollama run qwen2.5-coder:7b
```

### Bước 2: Biên dịch các file Java
Mở Terminal tại thư mục chứa dự án (/papaya-hiring/AI_Challenge_11/claim-assessment-agent) và chạy lệnh biên dịch tất cả các file mã nguồn Java:

```bash
javac MockDatabase.java AgentTools.java ClaimAgentRunner.java
```

> **Lưu ý**: Sau khi biên dịch thành công, hệ thống sẽ tự động tạo ra các file `.class` tương ứng.

### Bước 3: Khởi chạy chương trình
Tiến hành chạy chương trình chính (`ClaimAgentRunner`):

```bash
java ClaimAgentRunner
```

---

## 4. Kết quả mong đợi

Chương trình sẽ tự động duyệt qua **3 Test Cases** và hiển thị kết quả theo trình tự:

1. **Tool Execution Logs**: In ra chi tiết dữ liệu và kết quả phản hồi của 4 công cụ (`verifyDocument`, `lookupPolicy`, `checkMedicalNecessity`, `calculateBenefit`).
2. **Ollama Request Details**: In ra URL endpoint và toàn bộ cấu trúc JSON Payload gửi tới AI.
3. **Assessment Report (6 Sections)**: Báo cáo đánh giá chuẩn 6 phần được tổng hợp bởi AI bao gồm đầy đủ trích dẫn điều khoản chính sách cho từng quyết định (APPROVE / REJECT / REQUEST_MORE_INFO).