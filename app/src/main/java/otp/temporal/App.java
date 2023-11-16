/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package otp.temporal;

import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import io.temporal.serviceclient.WorkflowServiceStubs;
import io.temporal.worker.Worker;
import io.temporal.worker.WorkerFactory;

public class App {
    public String getGreeting() {
        return "Hello World!";
    }


    public static void main(String[] args) {
        WorkflowServiceStubs service = WorkflowServiceStubs.newLocalServiceStubs();
        WorkflowClient client = WorkflowClient.newInstance(service);
        WorkerFactory factory = WorkerFactory.newInstance(client);
        Worker worker = factory.newWorker("default");
        worker.registerWorkflowImplementationTypes(OtpWorkflowImpl.class);
        factory.start();

        WorkflowOptions options = WorkflowOptions.newBuilder()
            // .setWorkflowId(WORKFLOW_ID)
            .setTaskQueue("default")
            .build();

        OtpWorkflow workflow = client.newWorkflowStub(OtpWorkflow.class, options);

        WorkflowClient.start(workflow::initiateOtpLogin, "+919876543210");
        // workflow.initiateOtpLogin("+919876543210");

        workflow.resendOtp();

        // System.out.println("the generated otp is " + workflow.initiateOtpLogin("+91 98765 43210"));

        System.exit(0);
    }
}
