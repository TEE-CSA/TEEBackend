package com.nighthawk.spring_portfolio.mvc.car;

import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api/fibonacci")
public class FibonacciApiController {

    abstract public class FibonacciAlgorithm {
        abstract void fibonacci(int length);
        public int measureFibonacciTime(int size) {
            long startTime = System.nanoTime();
            fibonacci(size);
            long endTime = System.nanoTime();
            return (int) ((endTime - startTime));
        }
    }

    public class ForLoopFibonacci extends FibonacciAlgorithm {
        @Override
        void fibonacci(int length) {
            int a = 0, b = 1;
            for (int i = 1; i <= length; ++i) {
                int c = a + b;
                a = b;
                b = c;
            }
        }
    }

    public class WhileLoopFibonacci extends FibonacciAlgorithm {
        @Override
        void fibonacci(int length) {
            int a = 0, b = 1;
            int i = 1;
            while (i <= length) {
                int c = a + b;
                a = b;
                b = c;
                i++;
            }
        }
    }

    public class RecursionFibonacci extends FibonacciAlgorithm {
        @Override
        void fibonacci(int length) {
            finishRecursion(length);
        }

        private long finishRecursion(int n) {
            int f[]= new int[n + 2];
            int i;
 
            f[0] = 0;
            f[1] = 1;
 
            for (i = 2; i <= n; i++) {
            
            f[i] = f[i - 1] + f[i - 2];
        }
 
        return f[n];
        }
    }

    public class MatrixFibonacci extends FibonacciAlgorithm {
        @Override
        void fibonacci(int length) {
            matrixRecursive(length);
        }

        private long matrixRecursive(int n) {
            if (n == 0) {
                return 0;
            }
            long a = 0, b = 1, temp;
            for (int i = 2; i <= n; i++) {
                temp = a + b;
                a = b;
                b = temp;
            }
            return a;
        }
    }

    @GetMapping("/speeds")
    public Map<String, Integer> getAlgorithmSpeeds(@RequestParam(required = false) Integer length) {
        int size = (length != null && length > 0) ? length : 35;

        Map<String, Integer> algorithmSpeeds = new HashMap<>();

        algorithmSpeeds.put("forLoopFibonacci", new ForLoopFibonacci().measureFibonacciTime(size));
        algorithmSpeeds.put("whileLoopFibonacci", new WhileLoopFibonacci().measureFibonacciTime(size));
        algorithmSpeeds.put("recursionFibonacci", new RecursionFibonacci().measureFibonacciTime(size));
        algorithmSpeeds.put("matrixFibonacci", new MatrixFibonacci().measureFibonacciTime(size));

        return algorithmSpeeds;
    }
}