import java.util.Map;

public class AgentTools {

    // 1. lookupPolicy(policyId) — Tra cứu điều khoản, hạn mức, loại trừ[cite: 1, 7]
    public static String lookupPolicy(String policyId) {
        Map<String, Object> policy = MockDatabase.getPolicy(policyId);
        if (policy == null) {
            return "{\"error\": \"Policy ID " + policyId + " not found.\"}";
        }
        return policy.toString();
    }

    // 2. calculateBenefit(policyId, claimType, amount) — Tính toán số tiền chi trả, copay, hạn mức[cite: 1, 7]
    public static String calculateBenefit(String policyId, String claimType, double amount) {
        Map<String, Object> policy = MockDatabase.getPolicy(policyId);
        if (policy == null) {
            return "{\"error\": \"Policy ID " + policyId + " not found.\"}";
        }

        @SuppressWarnings("unchecked")
        Map<String, Map<String, Object>> benefits = (Map<String, Map<String, Object>>) policy.get("benefits");
        Map<String, Object> benefitInfo = benefits != null ? benefits.get(claimType) : null;
        
        if (benefitInfo == null) {
            return "{\"error\": \"Claim type " + claimType + " not covered under this policy.\"}";
        }

        double limit = (double) benefitInfo.get("limit");
        double copayRate = (double) benefitInfo.get("copay");

        double coveredBeforeCopay = Math.min(amount, limit);
        double copayAmount = coveredBeforeCopay * copayRate;
        double finalPayout = coveredBeforeCopay - copayAmount;
        double remainingLimit = (amount <= limit) ? (limit - amount) : 0.0;

        return String.format(
            "{\"submittedAmount\": %.1f, \"coveredAmount\": %.1f, \"copayAmount\": %.1f, \"finalPayout\": %.1f, \"remainingLimit\": %.1f, \"exceededLimit\": %b}",
            amount, coveredBeforeCopay, copayAmount, finalPayout, remainingLimit, (amount > limit)
        );
    }

    // 3. verifyDocument(documentId) — Kiểm tra loại tài liệu, trạng thái completeness và lỗi[cite: 1, 7]
    public static String verifyDocument(String documentId) {
        Map<String, Object> doc = MockDatabase.getDocument(documentId);
        return doc.toString();
    }

    // 4. checkMedicalNecessity(diagnosis, procedures) — Kiểm tra tính phù hợp y tế[cite: 1, 7]
    public static String checkMedicalNecessity(String diagnosis, String procedures) {
        boolean isNecessary = MockDatabase.checkMedicalRule(diagnosis, procedures);
        return String.format(
            "{\"diagnosis\": \"%s\", \"procedures\": \"%s\", \"isMedicalNecessary\": %b, \"note\": \"%s\"}",
            diagnosis, procedures, isNecessary, 
            isNecessary ? "Clinically appropriate" : "Treatment does not match diagnosis or lacks clinical justification."
        );
    }
}