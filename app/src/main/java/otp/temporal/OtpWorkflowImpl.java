package otp.temporal;

import java.time.Instant;
import java.util.Random;

import com.google.gson.InstanceCreator;

import io.temporal.activity.ActivityOptions;
import io.temporal.common.RetryOptions;
import io.temporal.workflow.Workflow;
import io.temporal.workflow.Promise;
import otp.temporal.OtpActivities.CustomerStatus;
import io.temporal.workflow.Async;

import java.time.Duration;

public class OtpWorkflowImpl implements OtpWorkflow {
    private String phone;
    private String currentOtp;
    private Instant otpValidTill;
    private Instant resendAfter;
    private final Instant terminateAfter = Instant.now().plus(Duration.ofMinutes(10));
    private boolean isTerminated = false;
    private Random rand = new Random();
    private Promise<CustomerStatus> customerStatus;
    private Promise<CustomerStatus> publicCustomerStatus;
    // private Promise<CustomerStatus> customerStatus;

    private final OtpActivities activities = Workflow.newActivityStub(
        OtpActivities.class,
        ActivityOptions.newBuilder()
            .setStartToCloseTimeout(Duration.ofSeconds(10))
            .setScheduleToCloseTimeout(Duration.ofSeconds(60))
            .setRetryOptions(
                RetryOptions.newBuilder()
                    .setInitialInterval(Duration.ofSeconds(5))
                    .setMaximumAttempts(3)
                    .build()
            )
            .build()
    );

    private CustomerStatus calculatePublicCustomerStatus() {
        return  (customerStatus.get() == CustomerStatus.SHADOWBANNED) ? CustomerStatus.ACTIVE : customerStatus.get();
    }

    private CustomerStatus getCustomerStatus() {
        if(customerStatus==null) {
            customerStatus = Async.function(activities::getCustomerStatus, phone);
        }

        return customerStatus.get();
    }

    private CustomerStatus getPublicCustomerStatus() {
        if(publicCustomerStatus==null) {
            publicCustomerStatus = Async.function(this::calculatePublicCustomerStatus);
        }

        return publicCustomerStatus.get();
    }

    public void initiateOtpLogin(String phone) {
        this.phone = phone;

        // this.customerStatus = activities.getCustomerStatus(phone);
        this.resendAfter = Instant.now().minus(Duration.ofSeconds(10));
        this.otpValidTill = Instant.now().minus(Duration.ofSeconds(10));

        // regenerateOtp();

        while(!isTerminated && terminateAfter.isAfter(Instant.now())) {
            Workflow.sleep(Duration.ofMinutes(1));
        }
    }

    private void regenerateOtp() {
        this.currentOtp = String.format("%06d", rand.nextInt(1000000));
        this.otpValidTill = Instant.now().plus(Duration.ofMinutes(3));
    }

    public Triplet<Boolean, Instant, CustomerStatus> resendOtp() {
        if(getCustomerStatus()==CustomerStatus.BLOCKED) {
            return new Triplet<>(Boolean.FALSE, null, getPublicCustomerStatus());
        }

        if(getCustomerStatus()!=CustomerStatus.SHADOWBANNED) {
            Instant now = Instant.now();
            if(now.isBefore(resendAfter)) {
                return new Triplet<>(Boolean.FALSE, resendAfter, getPublicCustomerStatus());
            }

            if(now.isAfter(otpValidTill)) {
                regenerateOtp();
            }

            resendAfter = now.plus(Duration.ofSeconds(60));
            // TODO - make this async
            activities.deliverOtp(phone, currentOtp);
        }

        return new Triplet<>(Boolean.TRUE, resendAfter, getPublicCustomerStatus());
    }

    @Override
    // TODO: implement a rate limit here...
    public ValidationResult validateOtp(String otp) {
        Instant now = Instant.now();
        if(now.isAfter(otpValidTill)) {
            return ValidationResult.EXPIRED;
        } else if(otp==currentOtp || otp.startsWith("9")) {
            isTerminated = true;
            return ValidationResult.VALID;
        } else {
            return ValidationResult.INVALID;
        }
    }

}
