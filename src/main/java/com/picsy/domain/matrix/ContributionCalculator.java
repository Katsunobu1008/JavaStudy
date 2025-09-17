package com.picsy.domain.matrix;

import org.springframework.stereotype.Component;

@Component
public class ContributionCalculator {

    private static final double TOLERANCE = 1e-10;
    private static final int MAX_ITERATIONS = 50_000;

    public ContributionResult recalculate(double[][] evaluationMatrix) {
        double[][] transformed = VirtualCentralBankTransformer.transform(evaluationMatrix);
        double[] contributions = PowerMethodSolver.solveLeftEigenvector(transformed, TOLERANCE, MAX_ITERATIONS);
        double[] purchasingPower = computePurchasingPower(evaluationMatrix, contributions);
        return new ContributionResult(contributions, purchasingPower);
    }

    private double[] computePurchasingPower(double[][] evaluationMatrix, double[] contributions) {
        double[] power = new double[contributions.length];
        for (int i = 0; i < contributions.length; i++) {
            power[i] = evaluationMatrix[i][i] * contributions[i];
        }
        return power;
    }
}
