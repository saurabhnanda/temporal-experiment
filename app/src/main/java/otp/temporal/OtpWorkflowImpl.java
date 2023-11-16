package otp.temporal;

import java.time.Instant;
import java.util.Random;

import io.temporal.activity.ActivityOptions;
import io.temporal.common.RetryOptions;
import io.temporal.workflow.Workflow;
// import otp.temporal.OtpActivities.CustomerStatus;

import java.time.Duration;

public class OtpWorkflowImpl implements OtpWorkflow {
    private String phone;
    private String currentOtp;
    private Instant otpValidTill;
    private Instant resendAfter;
    private Random rand = new Random();
    // private CustomerStatus customerStatus;

    private final OtpActivities activities = Workflow.newActivityStub(
        OtpActivities.class,
        ActivityOptions.newBuilder()
            .setRetryOptions(
                RetryOptions.newBuilder()
                    .setInitialInterval(Duration.ofSeconds(5))
                    .setMaximumAttempts(3)
                    .build()
            )
            .build()
    );

    public void initiateOtpLogin(String phone) {
        this.phone = phone;
        // this.customerStatus = activities.getCustomerStatus(phone);
        this.resendAfter = Instant.now();

        regenerateOtp();
        // return this.currentOtp;
    }

    private void regenerateOtp() {
        this.currentOtp = String.format("%06d", rand.nextInt(1000000));
        this.otpValidTill = Instant.now().plus(Duration.ofMinutes(3));
    }

    public Pair<Boolean, Instant> resendOtp() {
        Instant now = Instant.now();
        if(now.isBefore(resendAfter)) {
            return new Pair<>(Boolean.FALSE, resendAfter);
        }

        if(now.isAfter(otpValidTill)) {
            regenerateOtp();
        }

        // TODO - make this async
        activities.deliverOtp(phone, currentOtp);
        resendAfter = now.plus(Duration.ofMinutes(60));
        return new Pair<>(Boolean.TRUE, resendAfter);

        
    }
}
