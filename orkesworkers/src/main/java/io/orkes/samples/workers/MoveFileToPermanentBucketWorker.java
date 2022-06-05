package io.orkes.samples.workers;

import com.amazonaws.regions.Regions;
import com.netflix.conductor.client.worker.Worker;
import com.netflix.conductor.common.metadata.tasks.Task;
import com.netflix.conductor.common.metadata.tasks.TaskResult;
import io.orkes.samples.utils.S3Utils;
import org.springframework.stereotype.Component;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.UUID;

@Component
public class MoveFileToPermanentBucketWorker implements Worker {

    @Override
    public String getTaskDefName() {
        return "move_file_to_permanent_bucket";
    }

    @Override
    public TaskResult execute(Task task) {

        TaskResult result = new TaskResult(task);

        try {
            String fileLocation = (String) task.getInputData().get("fileLocation");
            String scanType = (String) task.getInputData().get("scanType");

            String outputFileName = "/tmp/" + UUID.randomUUID().toString();
            String s3BucketName = "permanent-bucket";
            String url = S3Utils.uploadToS3(outputFileName, Regions.US_EAST_1, s3BucketName);

            result.setStatus(TaskResult.Status.COMPLETED);
            result.addOutputData("fileLocation", url);
            result.addOutputData("status", "success");
            result.addOutputData("scanType", scanType);
        } catch (Exception e) {
            result.setStatus(TaskResult.Status.FAILED);
            final StringWriter sw = new StringWriter();
            final PrintWriter pw = new PrintWriter(sw, true);
            e.printStackTrace(pw);
            result.log(sw.getBuffer().toString());
            result.addOutputData("status", "fail");
        }
        return result;
    }
}