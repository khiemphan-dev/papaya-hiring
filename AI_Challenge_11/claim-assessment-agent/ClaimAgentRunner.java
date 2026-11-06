import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

public class ClaimAgentRunner {

    private static final String OLLAMA_URL = "http://localhost:11434/v1/chat/completions";

    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println("🚀 BẮT ĐẦU CHẠY CLAIM ASSESSMENT AGENT (3 TEST CASES)");
        System.out.println("==================================================");

        List<Map<String, String>> testCases = MockDatabase.getTestCases();

        for (Map<String, String> testCase : testCases) {
            runSingleClaimAssessment(testCase);
            System.out.println("\n--------------------------------------------------\n");
        }
    }

    private static void runSingleClaimAssessment(Map<String, String> tc) {
        String caseId = tc.get("caseId");
        String description = tc.get("description");
        String policyId = tc.get("policyId");
        String claimType = tc.get("claimType");
        double amount = Double.parseDouble(tc.get("amount"));
        String diagnosis = tc.get("diagnosis");
        String procedures = tc.get("procedures");
        String documentId = tc.get("documentId");

        System.out.println("📌 Đang xử lý: " + caseId + " - " + description);

        System.out.println("  -> [Step 1] Xác minh tài liệu...");
        String docResult = AgentTools.verifyDocument(documentId);
        System.out.println("     Output: " + docResult);

        System.out.println("  -> [Step 2] Tra cứu chính sách bảo hiểm...");
        String policyResult = AgentTools.lookupPolicy(policyId);
        System.out.println("     Output: [Policy Data Retrieved]");

        System.out.println("  -> [Step 3] Kiểm tra tính cần thiết y tế...");
        String medicalResult = AgentTools.checkMedicalNecessity(diagnosis, procedures);
        System.out.println("     Output: " + medicalResult);

        System.out.println("  -> [Step 4] Tính toán quyền lợi bảo hiểm...");
        String benefitResult = AgentTools.calculateBenefit(policyId, claimType, amount);
        System.out.println("     Output: " + benefitResult);

        String userPrompt = String.format(
            "Hãy đóng vai trò là Claim Assessment AI Agent. Dựa vào kết quả các công cụ dưới đây, hãy lập Báo cáo đánh giá gồm đúng 6 phần: " +
            "1. Document Review, 2. Policy Verification, 3. Medical Necessity, 4. Benefit Calculation, 5. Recommendation (APPROVE / REJECT / REQUEST_MORE_INFO), 6. Policy Citations.\n\n" +
            "Dữ liệu đầu vào:\n- Document Result: %s\n- Policy Result: %s\n- Medical Result: %s\n- Benefit Result: %s\n" +
            "Lưu ý quy tắc: Nếu tài liệu thiếu/incomplete, phải đề xuất REQUEST_MORE_INFO, không được từ chối.",
            docResult, policyResult, medicalResult, benefitResult
        );

        callOllamaAgent(userPrompt);
    }

    private static void callOllamaAgent(String prompt) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            
            // Xây dựng JSON payload an toàn
            String escapedPrompt = prompt.replace("\"", "\\\"").replace("\n", "\\n");
            String jsonPayload = String.format("""
                {
                  "model": "qwen2.5-coder:7b",
                  "messages": [
                    {"role": "system", "content": "You are an expert insurance claim assessment AI agent that produces structured 6-section reports with exact policy citations."},
                    {"role": "user", "content": "%s"}
                  ],
                  "stream": false,
                  "temperature": 0.1
                }
                """, escapedPrompt);

            // ==========================================================
            // 🔍 IN RA STEP CALL CHI TIẾT GỬI ĐẾN OLLAMA
            // ==========================================================
            System.out.println("\n--------------------------------------------------bytes");
            System.out.println("📤 [OLLAMA STEP CALL - REQUEST DETAILS]");
            System.out.println("🔹 Target URL : " + OLLAMA_URL);
            System.out.println("🔹 Model Name : qwen2.5-coder:7b");
            System.out.println("🔹 Payload Body (JSON):\n" + jsonPayload);
            System.out.println("--------------------------------------------------");

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(OLLAMA_URL))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                    .build();

            System.out.println("⏳ Đang gửi request tới Ollama local trên...");
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                System.out.println("\n📥 [OLLAMA RESPONSE - SUCCESS]:");
                String body = response.body();
                
                // Trích xuất nội dung 'content' trả về từ JSON của Ollama để hiển thị gọn gàng
                int contentIndex = body.indexOf("\"content\":\"");
                if (contentIndex != -1) {
                    int startIndex = contentIndex + 11;
                    // Tìm vị trí kết thúc nội dung content (trước field tiếp theo hoặc cuối chuỗi)
                    System.out.println(body.substring(startIndex, body.length() - 3)
                            .replace("\\n", "\n")
                            .replace("\\\"", "\""));
                } else {
                    System.out.println(body);
                }
            } else {
                System.out.println("❌ Lỗi gọi Ollama API, mã trạng thái: " + response.statusCode());
                System.out.println("Chi tiết lỗi: " + response.body());
            }

        } catch (Exception e) {
            System.out.println("❌ Không thể kết nối tới Ollama. Hãy đảm bảo Ollama đang chạy trên máy (http://localhost:11434).");
            e.printStackTrace();
        }
    }
}