package com.dota2.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

/**
 * TestNG listener for test lifecycle events
 */
public class TestConfig implements ITestListener {
    private static final Logger logger = LogManager.getLogger(TestConfig.class);

    @Override
    public void onStart(ITestContext context) {
        logger.info("Starting test suite: {}", context.getName());
    }

    @Override
    public void onFinish(ITestContext context) {
        logger.info("Finishing test suite: {}", context.getName());
        logger.info("Passed tests: {}", context.getPassedTests().size());
        logger.info("Failed tests: {}", context.getFailedTests().size());
        logger.info("Skipped tests: {}", context.getSkippedTests().size());
    }

    @Override
    public void onTestStart(ITestResult result) {
        logger.info("Starting test: {}", result.getName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        logger.info("Test passed: {}", result.getName());
        logger.info("Test duration: {} ms", result.getEndMillis() - result.getStartMillis());
    }

    @Override
    public void onTestFailure(ITestResult result) {
        logger.error("Test failed: {}", result.getName());
        logger.error("Failure reason: {}", result.getThrowable().getMessage());
        logger.error("Test duration: {} ms", result.getEndMillis() - result.getStartMillis());
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        logger.warn("Test skipped: {}", result.getName());
        if (result.getThrowable() != null) {
            logger.warn("Skip reason: {}", result.getThrowable().getMessage());
        }
    }
}