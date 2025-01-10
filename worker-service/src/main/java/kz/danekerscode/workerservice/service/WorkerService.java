package kz.danekerscode.workerservice.service;

import kz.danekerscode.matrix.MatrixResult;
import kz.danekerscode.matrix.MatrixTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.stream.IntStream;

@Service
@KafkaListener(topics = "${kafka.topics.matrix-calculate}", groupId = "${kafka.group-id:worker-service}")
public class WorkerService {

    private static final Logger log = LoggerFactory.getLogger(WorkerService.class);
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${kafka.topics.matrix-calculate-result}")
    private String matrixCalculateResultTopic;

    public WorkerService(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaHandler
    public void processTask(MatrixTask task) {
        log.info("Received task: {}", task.getJobId());
        log.info("Computing result for jobId: {}", task.getJobId());

        double result = computeDotProduct(task.getRowA(), task.getColB());

        log.info("Computed result for task {}: rowIndex={}, colIndex={}, result={}",
                task.getJobId(), task.getRowIndex(), task.getColIndex(), result);

        MatrixResult resultMessage = new MatrixResult(task.getJobId(), task.getRowIndex(), task.getColIndex(), result);
        kafkaTemplate.send(matrixCalculateResultTopic, task.getJobId(), resultMessage);

        log.info("Result for task {} sent to coordinator.", task.getJobId());
    }

    private double computeDotProduct(double[] row, double[] col) {
        return IntStream.range(0, row.length)
                .mapToDouble(i -> row[i] * col[i])
                .sum();
    }
}
