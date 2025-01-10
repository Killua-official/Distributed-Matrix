package kz.danekerscode.workerservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import kz.danekerscode.matrix.MatrixTask;
import org.apache.kafka.common.serialization.Deserializer;

import java.util.Map;

public class MatrixTaskDeserializer implements Deserializer<MatrixTask> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        // No configuration required for now.
    }

    @Override
    public MatrixTask deserialize(String topic, byte[] data) {
        try {
            return objectMapper.readValue(data, MatrixTask.class);
        } catch (Exception e) {
            throw new RuntimeException("Error deserializing MatrixRequest", e);
        }
    }

    @Override
    public void close() {
        // No-op
    }
}