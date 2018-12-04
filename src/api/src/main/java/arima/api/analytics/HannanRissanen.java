package arima.api.analytics;

import arima.api.analytics.Matrix;
import arima.api.analytics.Vector;

import java.util.Arrays;

import arima.api.analytics.ForecastUtil;
import arima.api.models.ArimaParameterModel;

/**
 * Hannan-Rissanen algorithm for estimating ARIMA parameters
 */
public final class HannanRissanen {

    private HannanRissanen() {
    }

    /**
     * Estimate ARMA(p,q) parameters, i.e. AR-parameters: \phi_1, ... , \phi_p
     *                                     MA-parameters: \theta_1, ... , \theta_q
     * Input data is assumed to be stationary, has zero-mean, aligned, and imputed
     *
     * @param data_orig original data
     * @param params ARIMA parameters
     * @param forecast_length forecast length
     * @param maxIteration maximum number of iteration
     */
    public static void estimateARMA(final double[] data_orig, final ArimaParameterModel params,
        final int forecast_length, final int maxIteration) {
        final double[] data = new double[data_orig.length];
        final int total_length = data.length;
        System.arraycopy(data_orig, 0, data, 0, total_length);
        final int r = (params.getDegreeP() > params.getDegreeQ()) ?
            1 + params.getDegreeP() : 1 + params.getDegreeQ();
        final int length = total_length - forecast_length;
        final int size = length - r;
        if (length < 2 * r) {
            throw new RuntimeException("not enough data points: length=" + length + ", r=" + r);
        }

        // step 1: apply Yule-Walker method and estimate AR(r) model on input data
        final double[] errors = new double[length];
        for (int j = 0; j < r; ++j) {
            errors[j] = 0;
        }

        // step 2: iterate Least-Square fitting until the parameters converge
        // instantiate Z-matrix
        final double[][] matrix = new double[params.getNumParamsP() + params.getNumParamsQ()][size];

        double bestRMSE = -1; // initial value
        int remainIteration = maxIteration;
        Vector bestParams = null;
        while (--remainIteration >= 0) {
            final Vector estimatedParams = iterationStep(params, data, errors, matrix, r,
                length,
                size);
            params.setParamsFromVector(estimatedParams);

            // forecast for validation data and compute RMSE
            final double[] forecasts = ArimaSolver.forecastARMA(params, data, length, data.length);
            final double anotherRMSE = ArimaSolver
                .computeRMSE(data, forecasts, length, 0, forecast_length);
            // update errors
            final double[] train_forecasts = ArimaSolver.forecastARMA(params, data, r, data.length);
            for (int j = 0; j < size; ++j) {
                errors[j + r] = data[j + r] - train_forecasts[j];
            }
            if (bestRMSE < 0 || anotherRMSE < bestRMSE) {
                bestParams = estimatedParams;
                bestRMSE = anotherRMSE;
            }
        }
        params.setParamsFromVector(bestParams);
    }

    private static Vector iterationStep(
        final ArimaParameterModel params,
        final double[] data, final double[] errors,
        final double[][] matrix, final int r, final int length, final int size) {

        int rowIdx = 0;
        // copy over shifted timeseries data into matrix
        final int[] offsetsAR = params.getOffsetsAR();
        for (int pIdx : offsetsAR) {
            System.arraycopy(data, r - pIdx, matrix[rowIdx], 0, size);
            ++rowIdx;
        }
        // copy over shifted errors into matrix
        final int[] offsetsMA = params.getOffsetsMA();
        for (int qIdx : offsetsMA) {
            System.arraycopy(errors, r - qIdx, matrix[rowIdx], 0, size);
            ++rowIdx;
        }

        // instantiate matrix to perform least squares algorithm
        final Matrix zt = new Matrix(matrix, false);

        // instantiate target vector
        final double[] vector = new double[size];
        System.arraycopy(data, r, vector, 0, size);
        final Vector x = new Vector(vector, false);

        // obtain least squares solution
        final Vector ztx = zt.timesVector(x);
        final Matrix ztz = zt.computeAAT();
        final Vector estimatedVector = ztz
            .solveSPDIntoVector(ztx, ForecastUtil.maxConditionNumber);

        return estimatedVector;
    }
    
    public static double[] fit(final double[] data, final int p) {

        int length = data.length;
        if (length == 0 || p < 1) {
            throw new RuntimeException(
                "fitYuleWalker - Invalid Parameters" + "length=" + length + ", p = " + p);
        }

        double[] r = new double[p + 1];
        for (double aData : data) {
            r[0] += Math.pow(aData, 2);
        }
        r[0] /= length;

        for (int j = 1; j < p + 1; j++) {
            for (int i = 0; i < length - j; i++) {
                r[j] += data[i] * data[i + j];
            }
            r[j] /= (length);
        }

        final Matrix toeplitz = ForecastUtil.initToeplitz(Arrays.copyOfRange(r, 0, p));
        final Vector rVector = new Vector(Arrays.copyOfRange(r, 1, p + 1), false);

        return toeplitz.solveSPDIntoVector(rVector, ForecastUtil.maxConditionNumber).deepCopy();
    }
}