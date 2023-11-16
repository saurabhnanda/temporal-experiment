package otp.temporal;

import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

@WorkflowInterface
public interface OtpWorkflow {
    @WorkflowMethod
    String initiateOtpLogin(String phone);
}
