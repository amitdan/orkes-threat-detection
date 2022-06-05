package io.orkes.samples.workers;

        import com.amazonaws.regions.Regions;
        import com.netflix.conductor.client.worker.Worker;
        import com.netflix.conductor.common.metadata.tasks.Task;
        import com.netflix.conductor.common.metadata.tasks.TaskResult;
        import io.orkes.samples.utils.S3Utils;
        import io.orkes.samples.utils.SendMailUtil;
        import org.im4java.core.ConvertCmd;
        import org.im4java.core.IMOperation;
        import org.springframework.stereotype.Component;

        import java.io.PrintWriter;
        import java.io.StringWriter;
        import java.time.Instant;
        import java.util.UUID;

@Component
public class SendEmailWorker implements Worker {

    @Override
    public String getTaskDefName() {
        return "send_email_s3_threat_scan";
    }

    @Override
    public TaskResult execute(Task task) {

        TaskResult result = new TaskResult(task);

        try {
            String fileLocation = (String) task.getInputData().get("fileLocation");
            String senderEmail = (String) task.getInputData().get("senderEmail");
            SendMailUtil.sendEmail(senderEmail, fileLocation);
            result.setStatus(TaskResult.Status.COMPLETED);
            result.addOutputData("status", "success");
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


    private void resizeImage(String inputImage, Integer width, Integer height, String outputImage ) throws Exception {
        ConvertCmd cmd = new ConvertCmd();

        IMOperation op = new IMOperation();
        op.addImage(inputImage);
        op.resize(width,height);
        op.addImage(outputImage);

        cmd.run(op);
    }

    private void vibrant(String inputImage, Integer vibrance, String outputImage ) throws Exception {
        ConvertCmd cmd = new ConvertCmd();

        IMOperation op = new IMOperation();
        op.quiet();
        op.addImage(inputImage);
        op.colorspace("HCL");
        op.channel("g");
        if(vibrance < 0) {
            op.p_sigmoidalContrast(Double.valueOf(Math.abs(vibrance)), 0.0);
        }else {
            op.sigmoidalContrast(Double.valueOf(Math.abs(vibrance)), 0.0);
        }
        op.p_channel();
        op.colorspace("sRGB");
        op.p_repage();
        op.addImage(outputImage);

        cmd.run(op);
    }

    private void sepia(String inputImage, Double sepiaIntensityThreshold, String outputImage ) throws Exception {
        ConvertCmd cmd = new ConvertCmd();

        IMOperation op = new IMOperation();
        op.quiet();
        op.addImage(inputImage);
        op.sepiaTone(sepiaIntensityThreshold);
        op.addImage(outputImage);

        cmd.run(op);
    }
}



