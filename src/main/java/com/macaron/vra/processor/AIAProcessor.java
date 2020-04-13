package com.macaron.vra.processor;

import com.macaron.vra.service.impl.AIAFeedbackServiceImpl;
import com.macaron.vra.service.impl.AIAPsttDataUploadServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.concurrent.Future;

@Component
public class AIAProcessor {

    private static final Logger logger = LoggerFactory.getLogger(AIAProcessor.class);

    @Autowired
    @Qualifier("taskExecutors")
    private ThreadPoolTaskExecutor taskExecutors;

    @Autowired
    private AIAFeedbackServiceImpl feedbackServiceImpl;

    @Autowired
    private AIAPsttDataUploadServiceImpl psttUpload;


    public Future<Future<?>> processfeedBackToADSTask(Date dataTimeStart, Date dataTimeEnd) {

        logger.info("put processfeedBackToADSTask in taskExecPool begin dataTimeStart={},dataTimeEnd={}", dataTimeStart,
                dataTimeEnd);

        Future<?> ret = taskExecutors.submit(new FeedBackToADSTask(dataTimeStart, dataTimeEnd));

        logger.info("put processfeedBackToADSTask in taskExecPool finish dataTimeStart={},dataTimeEnd={}", dataTimeStart, dataTimeEnd);
        return new AsyncResult<Future<?>>(ret);
    }

    public Future<Future<?>> processfeedBackPQATask(Date dataTimeStart, Date dataTimeEnd) {

        logger.info("put processfeedBackPQATask in taskExecPool begin dataTimeStart={},dataTimeEnd={}", dataTimeStart,
                dataTimeEnd);

        Future<?> ret = taskExecutors.submit(new FeedBackToPQATask(dataTimeStart, dataTimeEnd));
        logger.info("put processfeedBackPQATask in taskExecPool finish dataTimeStart={},dataTimeEnd={}", dataTimeStart,
                dataTimeEnd);
        return new AsyncResult<Future<?>>(ret);
    }

    public Future<Future<?>> processUploadTask(String BatchNo) {
        logger.info("put processUploadTask in taskExecPool begin BatchNo={}", BatchNo);
        Future<?> ret = taskExecutors.submit(new UploadTask(BatchNo));
        logger.info("put processUploadTask in taskExecPool end BatchNo={}", BatchNo);
        return new AsyncResult<Future<?>>(ret);
    }

    public Future<Future<?>> processViolationTask(Date taskDate) {
        logger.info("put processViolationTask in taskExecPool begin taskDate={}", taskDate);
        Future<?> ret = taskExecutors.submit(new ViolationTask(taskDate));
        logger.info("put processViolationTask in taskExecPool end taskDate={}", taskDate);
        return new AsyncResult<Future<?>>(ret);
    }


    public Future<Future<?>> processFeedBackQaModelsToADSTask(Date dataTimeStart, Date dataTimeEnd) {
        Future<?> ret = taskExecutors.submit(() -> {
            feedbackServiceImpl.feedBackQaModelsToADSTask(dataTimeStart, dataTimeEnd);
        });
        return new AsyncResult<Future<?>>(ret);
    }


    private class FeedBackToADSTask implements Runnable {
        private Date dataTimeStart;
        private Date dataTimeEnd;

        public FeedBackToADSTask(Date dataTimeStart, Date dataTimeEnd) {
            super();
            this.dataTimeStart = dataTimeStart;
            this.dataTimeEnd = dataTimeEnd;
        }

        @Override
        public void run() {
            feedbackServiceImpl.feedBackToADS(dataTimeStart, dataTimeEnd);
        }
    }

    private class FeedBackToPQATask implements Runnable {
        private Date dataTimeStart;
        private Date dataTimeEnd;

        public FeedBackToPQATask(Date dataTimeStart, Date dataTimeEnd) {
            super();
            this.dataTimeStart = dataTimeStart;
            this.dataTimeEnd = dataTimeEnd;
        }

        @Override
        public void run() {
            feedbackServiceImpl.feedBackToPQA(dataTimeStart, dataTimeEnd);
        }
    }

    private class UploadTask implements Runnable {
        private String batchNo;

        public UploadTask(String batchNo) {
            super();
            this.batchNo = batchNo;
        }

        @Override
        public void run() {
            psttUpload.process(this.batchNo);
        }

    }

    private class DispatchOrderTask implements Runnable {

        public DispatchOrderTask() {
            super();
        }

        @Override
        public void run() {
            feedbackServiceImpl.dispatchOrder();
        }

    }

    private class ViolationTask implements Runnable {

        private Date taskDate;

        public ViolationTask(Date taskDate) {
            super();
            this.taskDate = taskDate;
        }

        @Override
        public void run() {
            feedbackServiceImpl.violationTask(taskDate);
        }

    }
}
