package otp.temporal;

import io.temporal.workflow.UpdateMethod;
import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;
import otp.temporal.OtpActivities.CustomerStatus;

import java.time.Instant;

@WorkflowInterface
public interface OtpWorkflow {
    @WorkflowMethod
    void initiateOtpLogin(String phone);

    @UpdateMethod
    Triplet<Boolean, Instant, CustomerStatus> resendOtp();

    enum ValidationResult {
        VALID, INVALID, EXPIRED
    }

    @UpdateMethod
    ValidationResult validateOtp(String otp);
}
