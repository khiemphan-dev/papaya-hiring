# Hướng Dẫn Chạy Chương Trình Claim Assessment AI Agent (Google Gemini API)

Tài liệu này hướng dẫn chi tiết cách thiết lập môi trường, cấu hình API Key, biên dịch và khởi chạy dự án **Claim Assessment AI Agent** sử dụng **Google Gemini API** bằng Core Java.

---

## 1. Yêu cầu hệ thống

Trước khi bắt đầu, hãy đảm bảo máy tính của bạn đã đáp ứng các yêu cầu sau:

* **Java Development Kit (JDK)**: Phiên bản 11 trở lên (khuyên dùng JDK 17 hoặc 21).
* **Kết nối Internet**: Để kết nối trực tiếp với Google Gemini API Cloud.
* **Google Gemini API Key**: Lấy khóa API miễn phí hoặc trả phí tại [Google AI Studio](https://aistudio.google.com/).

---

## 2. Cấu trúc thư mục dự án

Đảm bảo các file Java sau nằm trong cùng một thư mục làm việc:

```text
claim-assessment-agent/
├── MockDatabase.java
├── AgentTools.java
└── ClaimGeminiAgentRunner.java
```

---

## 3. Các bước thực hiện

### Bước 1: Cấu hình Gemini API Key

Mở file `ClaimGeminiAgentRunner.java` bằng bất kỳ trình soạn thảo mã nguồn nào và thay thế giá trị `GEMINI_API_KEY` bằng khóa API thật của bạn:

```java
// Trong file ClaimGeminiAgentRunner.java
private static final String AI_AGENT_API_KEY = "YOUR_ACTUAL_GEMINI_API_KEY";
```

---

### Bước 2: Biên dịch các file Java

Mở **Terminal** (hoặc Command Prompt) tại thư mục chứa dự án (/papaya-hiring/AI_Challenge_11/claim-assessment-agent) và chạy lệnh biên dịch:

```bash
javac MockDatabase.java AgentTools.java ClaimGeminiAgentRunner.java
```

> **Lưu ý**: Sau khi biên dịch thành công, hệ thống sẽ tự động sinh ra các file `.class` tương ứng mà không báo lỗi.

---

### Bước 3: Khởi chạy chương trình

Tiến hành chạy chương trình chính:

```bash
java ClaimGeminiAgentRunner
```

---

## 4. Kết quả mong đợi

Khi chương trình chạy, hệ thống sẽ lần lượt xử lý **3 Test Cases** và hiển thị chi tiết trên Terminal:

1. **Tool Execution Logs**: Nhật ký chạy 4 công cụ theo trình tự (`verifyDocument` $\rightarrow$ `lookupPolicy` $\rightarrow$ `checkMedicalNecessity` $\rightarrow$ `calculateBenefit`).
2. **Gemini API Step Call**: In chi tiết URL endpoint và JSON Payload gửi tới Google Gemini API.
3. **Structured Assessment Report (6 Sections)**: Báo cáo đánh giá chuẩn 6 phần được tổng hợp bởi Gemini API với các trích dẫn điều khoản chính xác cho từng quyết định (`APPROVE`, `REJECT`, `REQUEST_MORE_INFO`).