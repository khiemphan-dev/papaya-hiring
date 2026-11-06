import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

public class ClaimGeminiAgentRunner {

    private static final String AI_AGENT_URL = "https://generativelanguage.googleapis.com/v1beta/openai/chat/completions";
    private static final String AI_AGENT_API_KEY = "YOUR_ACTUAL_GEMINI_API_KEY";

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

        callGeminiAgent(userPrompt);
    }

    private static void callGeminiAgent(String prompt) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            
            // Xây dựng JSON payload (giữ nguyên cấu trúc chuẩn OpenAI)
            String escapedPrompt = prompt.replace("\"", "\\\"").replace("\n", "\\n");
            String jsonPayload = String.format("""
                {
                  "model": "gemini-3.8-flash",
                  "messages": [
                    {"role": "system", "content": "You are an expert insurance claim assessment AI agent that produces structured 6-section reports with exact policy citations."},
                    {"role": "user", "content": "%s"}
                  ],
                  "temperature": 0.2
                }
                """, escapedPrompt);

            System.out.println("\n--------------------------------------------------");
            System.out.println("📤 [GEMINI STEP CALL - REQUEST DETAILS]");
            System.out.println("🔹 Target URL : " + AI_AGENT_URL);
            System.out.println("🔹 Model Name : gemini-3.8-flash");
            System.out.println("--------------------------------------------------");

            // Xây dựng HttpRequest kèm Header Authorization chứa Gemini API Key
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(AI_AGENT_URL))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + AI_AGENT_API_KEY)
                    .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                    .build();

            System.out.println("⏳ Đang gửi request tới Google Gemini API...");
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                System.out.println("\n📥 [GEMINI RESPONSE - SUCCESS]:");
                String body = response.body();
                
                // Trích xuất nội dung 'content' trả về từ JSON
                int contentIndex = body.indexOf("\"content\":\"");
                if (contentIndex != -1) {
                    int startIndex = contentIndex + 11;
                    // In ra nội dung phản hồi từ Gemini
                    System.out.println(body.substring(startIndex, body.length() - 3)
                            .replace("\\n", "\n")
                            .replace("\\\"", "\""));
                } else {
                    System.out.println(body);
                }
            } else {
                System.out.println("❌ Lỗi gọi Gemini API, mã trạng thái: " + response.statusCode());
                System.out.println("Chi tiết lỗi: " + response.body());
            }

        } catch (Exception e) {
            System.out.println("❌ Không thể kết nối tới Google Gemini API.");
            e.printStackTrace();
        }
    }
}