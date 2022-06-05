package io.orkes.samples.workers;

        import com.amazonaws.regions.Regions;
        import com.netflix.conductor.client.worker.Worker;
        import com.netflix.conductor.common.metadata.tasks.Task;
        import com.netflix.conductor.common.metadata.tasks.TaskResult;
        import io.orkes.samples.utils.S3Utils;
        import io.orkes.samples.utils.Util;
        import org.im4java.core.ConvertCmd;
        import org.im4java.core.IMOperation;
        import org.springframework.stereotype.Component;

        import java.io.PrintWriter;
        import java.io.StringWriter;
        import java.time.Instant;
        import java.util.UUID;

@Component
public class RaiseIncidentWorker implements Worker {

    @Override
    public String getTaskDefName() {
        return "raise_incident";
    }

    @Override
    public TaskResult execute(Task task) {

        TaskResult result = new TaskResult(task);

        try {
            String fileLocation = (String) task.getInputData().get("fileLocation");
            String scanType = (String) task.getInputData().get("scanType");
            String senderEmail = (String) task.getInputData().get("senderEmail");

            Util.raiseIncident(fileLocation);

            result.setStatus(TaskResult.Status.COMPLETED);
            result.addOutputData("status", "success");
            result.addOutputData("scanType", scanType);

        } catch (Exception e) {
            e.printStackTrace();
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


