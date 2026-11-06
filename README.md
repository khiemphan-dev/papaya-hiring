# Papaya AI Engineering Assessment - Submission

Dự án này chứa phần lời giải cho bài kiểm tra tuyển dụng **AI Engineer** tại Papaya Insurtech, bao gồm phần trả lời câu hỏi tư duy logic và bài tập thực hành xây dựng **Claim Assessment AI Agent** (AI Challenge 11).

---

## 📁 Cấu Trúc Thư Mục

```text
.
├── PAPAYA-Logical_Questions.md    # Trả lời các câu hỏi tư duy/logic từ Papaya
└── AI_Challenge_11/              # Thư mục giải bài tập AI Challenge 11 (Claim Assessment Agent)
    ├── claim-assessment-agent/   # Mã nguồn Java chính (MockDatabase, AgentTools, Runners)
    ├── Guide_with_gemini.md       # Hướng dẫn chi tiết biên dịch và chạy bằng Google Gemini API
    ├── Guide_with_ollama.md       # Hướng dẫn chi tiết chạy hoàn toàn Local với Ollama
    └── Test_result.md             # Báo cáo kết quả kiểm thử trên 3 Test Cases thực tế
```

---

## 📑 Danh Mục Nội Dung

### 1. Logical Questions (`PAPAYA-Logical_Questions.md`)
* Chi tiết lời giải cho các câu hỏi logic, tư duy thuật toán và lập luận được yêu cầu trong bộ câu hỏi tuyển dụng của Papaya.
* Bạn có thể xem chi tiết câu trả lời tại file: [`PAPAYA-Logical_Questions.md`](./PAPAYA-Logical_Questions.md).

---

### 2. AI Engineering Challenge #11: Claim Assessment Agent (`/AI_Challenge_11`)
Thử thách yêu cầu xây dựng một **AI Agent** xử lý thẩm định yêu cầu bồi thường bảo hiểm (Insurance Claim Assessment) bằng **Core Java** với 4 công cụ (Tools) bắt buộc:
1. `verifyDocument(claimId)`: Xác minh tính hợp lệ của chứng từ y tế.
2. `lookupPolicy(policyNumber)`: Tra cứu quyền lợi bảo hiểm, thời gian chờ, hạn mức và tỷ lệ đồng chi trả (copay).
3. `checkMedicalNecessity(icdCode, treatment)`: Kiểm tra chỉ định y khoa phù hợp với mã chẩn đoán ICD.
4. `calculateBenefit(totalAmount, copayRate, maxLimit)`: Tính toán số tiền bồi thường thực tế.

---

## 🚀 Hướng Dẫn Nhanh Chạy Bài Tập AI Challenge 11

Thư mục `AI_Challenge_11` hỗ trợ 2 chế độ khởi chạy linh hoạt:

### Chế độ 1: Chạy Local với Ollama (Khuyên dùng - Offline & Miễn phí)
* Tận dụng sức mạnh phần cứng máy tính cá nhân (ví dụ: Apple Silicon Mac M2) để chạy các mô hình open-source như `qwen2.5-coder:7b` hoặc `gemma4:12b`.
* Hướng dẫn chi tiết: [`AI_Challenge_11/Guide_with_ollama.md`](./AI_Challenge_11/Guide_with_ollama.md).

```bash
cd AI_Challenge_11/claim-assessment-agent
javac MockDatabase.java AgentTools.java ClaimAgentRunner.java
java ClaimAgentRunner
```

### Chế độ 2: Chạy Cloud với Google Gemini API
* Tích hợp Google Gemini API Cloud cho kết quả phân tích nhanh và độ chính xác cao.
* Hướng dẫn chi tiết: [`AI_Challenge_11/Guide_with_gemini.md`](./AI_Challenge_11/Guide_with_gemini.md).

```bash
cd AI_Challenge_11/claim-assessment-agent
javac MockDatabase.java AgentTools.java ClaimGeminiAgentRunner.java
java ClaimGeminiAgentRunner
```

---

## 📊 Kết Quả Đánh Giá & Báo Cáo
Xem báo cáo chi tiết về kết quả xử lý của AI Agent trên 3 kịch bản bồi thường thực tế (`APPROVE`, `REJECT`, `REQUEST_MORE_INFO`) tại: [`AI_Challenge_11/Test_result.md`](./AI_Challenge_11/Test_result.md).