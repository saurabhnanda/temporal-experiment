package otp.temporal;

import java.net.http.HttpClient;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import io.temporal.activity.Activity;

import java.io.IOException;
import java.net.URI;

public class OtpActivitiesImpl implements OtpActivities {

    public void deliverOtp(String phone, String otp) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest req = HttpRequest.newBuilder(URI.create("https://eox94j23vsvjpr8.m.pipedream.net?phoneNumber=" + phone + "&otp=" + otp)).build();
            client.send(req, HttpResponse.BodyHandlers.discarding());
        } catch (IOException e) {
            throw Activity.wrap(e);
        } catch (InterruptedException e) {
            throw Activity.wrap(e);
        }
    }

    public CustomerStatus getCustomerStatus(String phone) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest req = HttpRequest.newBuilder(URI.create("https://eoho4fa4rawga50.m.pipedream.net?phoneNumber=" + phone)).build();
            String body = client.send(req, HttpResponse.BodyHandlers.ofString()).body();
            if(body.equalsIgnoreCase("new")) {
                return CustomerStatus.NEW;
            } else if (body.equalsIgnoreCase("active")) {
                return CustomerStatus.ACTIVE;
            } else if (body.equalsIgnoreCase("blocked")) {
                return CustomerStatus.BLOCKED;
            } else if (body.equalsIgnoreCase("shadowbanned")) {
                return CustomerStatus.SHADOWBANNED;
            } 

            throw new RuntimeException("Unhandled customer status = " + body);
        } catch (Exception e) {
            throw Activity.wrap(e);
        }
    }
}
