import java.util.*;

public class MockDatabase {

    // 1. Kho dữ liệu Chính sách bảo hiểm (Policies)
    public static Map<String, Object> getPolicy(String policyId) {
        if ("POL-001".equals(policyId)) {
            Map<String, Object> policy = new HashMap<>();
            policy.put("policyId", "POL-001");
            policy.put("holderName", "Nguyen Van A");
            policy.put("status", "ACTIVE");
            policy.put("effectiveDate", "2025-01-01");
            policy.put("expiryDate", "2026-12-31");
            
            Map<String, Map<String, Object>> benefits = new HashMap<>();
            benefits.put("Inpatient", Map.of("limit", 50000000.0, "copay", 0.1, "waitingPeriodDays", 30));
            benefits.put("Outpatient", Map.of("limit", 10000000.0, "copay", 0.2, "waitingPeriodDays", 0));
            policy.put("benefits", benefits);
            
            policy.put("exclusions", List.of("Cosmetic surgery", "Experimental treatments"));
            return policy;
        }
        return null;
    }

    // 2. Kho dữ liệu Tài liệu (Documents)
    public static Map<String, Object> getDocument(String documentId) {
        if ("DOC-101".equals(documentId)) {
            return Map.of(
                "documentId", "DOC-101",
                "type", "Medical Bill / Invoice",
                "status", "complete",
                "issues", List.of()
            );
        } else if ("DOC-102".equals(documentId)) {
            return Map.of(
                "documentId", "DOC-102",
                "type", "Discharge Summary",
                "status", "incomplete",
                "issues", List.of("Missing physician signature and official hospital stamp")
            );
        }
        return Map.of("error", "Document not found", "status", "missing");
    }

    // 3. Quy tắc y tế (Medical Necessity Rules)
    public static boolean checkMedicalRule(String diagnosis, String procedures) {
        return "Acute Appendicitis".equals(diagnosis) && "Appendectomy".equals(procedures);
    }

    // 4. Định nghĩa 3 Test Cases theo yêu cầu đề bài
    public static List<Map<String, String>> getTestCases() {
        List<Map<String, String>> cases = new ArrayList<>();
        
        // Case 1: Straightforward Approval (Phê duyệt trực tiếp)
        cases.add(Map.of(
            "caseId", "CASE-1",
            "description", "Straightforward Approval",
            "policyId", "POL-001",
            "claimType", "Inpatient",
            "amount", "15000000",
            "diagnosis", "Acute Appendicitis",
            "procedures", "Appendectomy",
            "documentId", "DOC-101",
            "expectedOutcome", "APPROVE"
        ));

        // Case 2: Rejection - Limit Exceeded (Từ chối do vượt hạn mức 50tr)
        cases.add(Map.of(
            "caseId", "CASE-2",
            "description", "Rejection - Limit Exceeded",
            "policyId", "POL-001",
            "claimType", "Inpatient",
            "amount", "80000000",
            "diagnosis", "Acute Appendicitis",
            "procedures", "Appendectomy",
            "documentId", "DOC-101",
            "expectedOutcome", "REJECT"
        ));

        // Case 3: Request More Info - Incomplete Document (Yêu cầu bổ sung do thiếu chữ ký/dấu)
        cases.add(Map.of(
            "caseId", "CASE-3",
            "description", "Request More Info due to incomplete doc",
            "policyId", "POL-001",
            "claimType", "Inpatient",
            "amount", "15000000",
            "diagnosis", "Acute Appendicitis",
            "procedures", "Appendectomy",
            "documentId", "DOC-102",
            "expectedOutcome", "REQUEST_MORE_INFO"
        ));

        return cases;
    }
}