package kz.danekerscode.coordinatorservice.service;

import kz.danekerscode.matrix.MatrixResult;
import kz.danekerscode.matrix.MatrixTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@KafkaListener(topics = "${kafka.topics.matrix-calculate-result}", groupId = "coordinator-service")
public class MatrixService {

    private static final Logger log = LoggerFactory.getLogger(MatrixService.class);
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final Map<String, AtomicInteger> remainingResultsMap = new ConcurrentHashMap<>();
    private final Map<String, Map<String, Double>> resultCacheMap = new ConcurrentHashMap<>();
    private final Map<String, double[][]> resultMap = new ConcurrentHashMap<>();

    @Value("${kafka.topics.matrix-calculate}")
    private String matrixCalculationTopic;

    public MatrixService(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public double[][] getResult(String jobId) {
        return resultMap.get(jobId);
    }

    public String startComputation(double[][] matrixA, double[][] matrixB) {
        int rowsA = matrixA.length;
        int colsB = matrixB[0].length;

        String jobId = UUID.randomUUID().toString();
        log.info("Starting computation for job: {}", jobId);

        remainingResultsMap.put(jobId, new AtomicInteger(rowsA * colsB));
        resultCacheMap.put(jobId, new ConcurrentHashMap<>());

        for (int i = 0; i < rowsA; i++) {
            for (int j = 0; j < colsB; j++) {
                double[] rowA = matrixA[i];
                double[] colB = getColumn(matrixB, j);
                log.info("Sending task for job: {} row: {} col: {}", jobId, i, j);
                kafkaTemplate.send(matrixCalculationTopic, new MatrixTask(
                        jobId, i, j, rowA, colB
                ));
            }
        }

        return jobId;
    }

    private double[] getColumn(double[][] matrix, int colIndex) {
        return Arrays.stream(matrix)
                .mapToDouble(row -> row[colIndex])
                .toArray();
    }

    public void aggregateResult(String jobId, int rowIndex, int colIndex, double value) {
        Map<String, Double> resultCache = resultCacheMap.get(jobId);

        if (resultCache == null) {
            log.warn("Result cache for job {} not found", jobId);
            return;
        }

        String key = rowIndex + "," + colIndex;
        resultCache.put(key, value);

        var remainingResults = remainingResultsMap.get(jobId);
        if (remainingResults != null && remainingResults.decrementAndGet() == 0) {
            double[][] resultMatrix = buildResultMatrix(jobId);
            log.info("Computation for job {} completed", jobId);
            resultMap.put(jobId, resultMatrix);
            remainingResultsMap.remove(jobId);
            resultCacheMap.remove(jobId);
        }
    }

    private double[][] buildResultMatrix(String jobId) {
        Map<String, Double> resultCache = resultCacheMap.get(jobId);

        int rows = resultCache.keySet().stream()
                .mapToInt(key -> Integer.parseInt(key.split(",")[0]))
                .max().orElse(0) + 1;

        int cols = resultCache.keySet().stream()
                .mapToInt(key -> Integer.parseInt(key.split(",")[1]))
                .max().orElse(0) + 1;

        double[][] resultMatrix = new double[rows][cols];

        for (var entry : resultCache.entrySet()) {
            String[] indices = entry.getKey().split(",");
            int row = Integer.parseInt(indices[0]);
            int col = Integer.parseInt(indices[1]);
            resultMatrix[row][col] = entry.getValue();
        }

        return resultMatrix;
    }

    @KafkaHandler
    public void consumeResult(MatrixResult result) {
        this.aggregateResult(result.getJobId(), result.getRowIndex(), result.getColIndex(), result.getValue());
    }
}
