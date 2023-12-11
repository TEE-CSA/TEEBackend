package com.nighthawk.spring_portfolio.mvc.car;

import org.springframework.web.bind.annotation.*;

import com.nighthawk.spring_portfolio.mvc.betting.Betting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api/fibonacci")
public class FibonacciApiController {

    abstract public class FibonacciAlgorithm {
        abstract void fibonacci(int length);
        public int measureFibonacciTime(int size) {
            long startTime = System.currentTimeMillis();
            fibonacci(size);
            long endTime = System.currentTimeMillis();
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

    @GetMapping("/bet")
    public Map<String, Object> betOnFibonacciRace(@RequestParam(required = true) int betAmount,
                                                  @RequestParam(required = true) int startingPoints) {
        Betting game = new Betting(startingPoints);
    
        Map<String, Integer> speeds = getAlgorithmSpeeds(null);
    
        // Randomly select a Fibonacci algorithm
        List<String> algorithms = new ArrayList<>(speeds.keySet());
        String selectedAlgorithm = algorithms.get(new Random().nextInt(algorithms.size()));
    
        String fastestAlgorithm = speeds.entrySet().stream()
                .min(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("none");
    
        boolean isGuessCorrect = selectedAlgorithm.equals(fastestAlgorithm);
    
        game.placeBet(betAmount, isGuessCorrect);
    
        Map<String, Object> result = new HashMap<>();
        result.put("message", game.getResultMessage());
        result.put("newScore", game.getPoints());
        result.put("selectedAlgorithm", selectedAlgorithm);
        result.put("fastestAlgorithm", fastestAlgorithm);
    
        Map<String, String> fibonacciFacts = getFibonacciFacts();
        result.put("funFact", fibonacciFacts.get(fastestAlgorithm));
    
        return result;
    }
    
    private Map<String, String> getFibonacciFacts() {
        Map<String, String> facts = new HashMap<>();
        facts.put("forLoopFibonacci", "For Loop Fibonacci is efficient for small sequences but becomes less efficient as the sequence grows due to its linear time complexity.");
        facts.put("whileLoopFibonacci", "While Loop Fibonacci is similar to the for-loop version but uses a while loop, making it slightly more versatile in certain contexts.");
        facts.put("recursionFibonacci", "Recursion Fibonacci is elegant but can be less efficient due to its exponential time complexity, especially for large sequences.");
        facts.put("matrixFibonacci", "Matrix Fibonacci uses matrix multiplication to calculate Fibonacci numbers, providing a more efficient approach for larger sequences.");
        return facts;
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