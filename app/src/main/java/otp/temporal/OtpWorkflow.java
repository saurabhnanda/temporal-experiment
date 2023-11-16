package otp.temporal;

import io.temporal.workflow.UpdateMethod;
import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;
import java.time.Instant;

@WorkflowInterface
public interface OtpWorkflow {
    @WorkflowMethod
    void initiateOtpLogin(String phone);

    @UpdateMethod
    Pair<Boolean, Instant> resendOtp();

    enum ValidationResult {
        VALID, INVALID, EXPIRED
    }

    @UpdateMethod
    ValidationResult validateOtp(String otp);
}
