package otp.temporal;

import java.time.Instant;
import java.util.Random;
import java.time.Duration;

public class OtpWorkflowImpl implements OtpWorkflow {
    private String phone;
    private String currentOtp;
    private Instant otpValidTill;
    private Random rand = new Random();

    public String initiateOtpLogin(String phone) {
        this.phone = phone;
        regenerateOtp();
        return this.currentOtp;
    }

    private void regenerateOtp() {
        this.currentOtp = String.format("%06d", rand.nextInt(1000000));
        this.otpValidTill = Instant.now().plus(Duration.ofMinutes(3));
    }
}
