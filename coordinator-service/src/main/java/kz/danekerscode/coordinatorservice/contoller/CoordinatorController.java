package kz.danekerscode.coordinatorservice.contoller;

import kz.danekerscode.coordinatorservice.service.MatrixService;
import kz.danekerscode.matrix.MatrixRequest;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/matrix")
public class CoordinatorController {

    private final MatrixService matrixService;

    public CoordinatorController(MatrixService matrixService) {
        this.matrixService = matrixService;
    }

    @PostMapping("/start")
    String startComputation(@RequestBody MatrixRequest request) {
        return matrixService.startComputation(request.getMatrixA(), request.getMatrixB());
    }

    @GetMapping("/result/{jobId}")
    Map<String, Object> getResult(@PathVariable String jobId) {
        double[][] result = matrixService.getResult(jobId);
        if (result == null) {
            return Map.of("error", "Job not completed yet or not found");
        }
        return Map.of("result", result);
    }
}
