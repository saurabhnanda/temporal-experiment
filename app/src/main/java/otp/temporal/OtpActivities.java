package otp.temporal;

import io.temporal.activity.ActivityInterface;

@ActivityInterface
public interface OtpActivities {
    enum CustomerStatus {
        NEW,
        ACTIVE,
        BLOCKED,
        SHADOWBANNED
    }

    CustomerStatus getCustomerStatus(String phone);
    
    void deliverOtp(String phone, String otp);
}
