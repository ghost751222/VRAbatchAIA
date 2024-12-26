package com.macaron.vra.launcher;

import com.macaron.vra.dao.impl.ADSUsersDaoImpl;
import com.macaron.vra.processor.AIAProcessor;
import com.macaron.vra.service.impl.AIAServerCheckServiceImpl;
import com.macaron.vra.service.impl.EncryptorServiceImpl;
import com.macaron.vra.service.impl.PsaeApiServiceImpl;
import com.macaron.vra.service.impl.SmtpServiceImpl;
import com.macaron.vra.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Component
@PropertySource(value = {"classpath:application.properties"}, encoding = "utf-8")
@SpringBootApplication
@ComponentScan("com.macaron.vra")
public class AIAPsttDataUploadLauncher {

    private static final Logger logger = LoggerFactory.getLogger(AIAPsttDataUploadLauncher.class);

    @Value("${feedback.schedule.cron}")
    private String feedback_schedule_cron;

    @Value("${upload.schedule.cron}")
    private String upload_schedule_cron;

    @Value("${violation.schedule.cron}")
    private String violation_schedule_cron;

    @Value("${servercheck.schedule.fixedDelay}")
    private long servercheck_schedule_fixedDelay;


    @Value("${feedbackqamodels.schedule.cron:0 0/15 * * * ?}")
    private String feedbackqamodels_schedule_cron;

    @Value("#{'${servercheck.list}'.split(',')}")
    private List<String> serverlist;

    @Value("${spring.mail.username}")
    String mailUsername;
    @Value("${spring.ismail:false}")
    Boolean ismail;

    @Value("${spring.mailto.username}")
    List<String> mailtoUsername;

    @Autowired
    private TaskScheduler taskScheduler;

    @Autowired
    private AIAProcessor processor;

    @Autowired
    AIAServerCheckServiceImpl aiaServerCheckServiceImpl;

    @Autowired
    SmtpServiceImpl smtpServiceImpl;

    @Autowired
    AIAServerCheckServiceImpl serverCheckServiceImpl;

    @Autowired
    EncryptorServiceImpl encryptorServiceImpl;

    @Autowired
    PsaeApiServiceImpl psaeApiService;

    @Autowired
    ADSUsersDaoImpl adsUsersDao;

    public void launchSchedule() {


        Runnable feedBackToPQATask = new Runnable() {
            @Override
            public void run() {
                LocalDateTime now = LocalDateTime.now();
                int minRank = 15;
                Date s = DateUtil.toDate(now.minusMinutes(minRank));
                Date e = DateUtil.toDate(now.minusMinutes(0));
                processor.processfeedBackPQATask(s, e);
            }
        };

        Runnable feedBackToADSTask = new Runnable() {
            @Override
            public void run() {
                LocalDateTime now = LocalDateTime.now();
                int minRank = 16;
                Date s = DateUtil.toDate(now.minusMinutes(minRank));
                Date e = DateUtil.toDate(now.minusMinutes(0));
                processor.processfeedBackToADSTask(s, e);
            }
        };

        Runnable uploadTask = new Runnable() {
            @Override
            public void run() {
                processor.processUploadTask(null);
            }
        };

        Runnable processViolationTask = new Runnable() {
            @Override
            public void run() {
                processor.processViolationTask(Calendar.getInstance().getTime());
            }
        };

        Runnable serverCheckTask = new Runnable() {

            @Override
            public void run() {
                for (String s : serverlist) {
                    try {

                        String[] tmp = s.split(":");
                        if (ismail && !serverCheckServiceImpl.IsHostAvailable(tmp[0], Integer.parseInt(tmp[1]))) {
                            smtpServiceImpl.sendMail(mailUsername, mailtoUsername, String.format("Server %s is not available", s));
                        }
                    } catch (Exception e) {
                        logger.error("serverCheckTask {}", e);
                    }

                }

            }

        };


        Runnable feedBackQaModelsToADSTask = () -> {
            int minRank = 16;
            LocalDateTime now = LocalDateTime.now();
            Date s = DateUtil.toDate(now.minusMinutes(minRank));
            Date e = DateUtil.toDate(now.minusMinutes(0));
            processor.processFeedBackQaModelsToADSTask(s, e);
        };

        //prevent dataSource lock
        adsUsersDao.test();


        taskScheduler.schedule(feedBackToPQATask, new CronTrigger(feedback_schedule_cron));
        taskScheduler.schedule(feedBackToADSTask, new CronTrigger(feedback_schedule_cron));
        taskScheduler.schedule(uploadTask, new CronTrigger(upload_schedule_cron));
        taskScheduler.schedule(processViolationTask, new CronTrigger(violation_schedule_cron));
        taskScheduler.schedule(feedBackQaModelsToADSTask, new CronTrigger(feedbackqamodels_schedule_cron));
        taskScheduler.scheduleWithFixedDelay(serverCheckTask, servercheck_schedule_fixedDelay);

    }

    public void launchManual(String batchNo) throws InterruptedException, ExecutionException {
        logger.info("launchManual start batchNo={}", batchNo);
        Future<Future<?>> f = processor.processUploadTask(batchNo);
        logger.info("launchManual end batchNo={}", batchNo);

        while (!f.get().isDone()) {

        }

    }

    public void launchViolationTask(Date taskDate) throws ExecutionException, InterruptedException {
        logger.info("launchViolationTask start taskDate={}", taskDate.toString());
        Future<Future<?>> f = processor.processViolationTask(taskDate);
        logger.info("launchViolationTask end taskDate={}", taskDate.toString());

        while (!f.get().isDone()) {

        }
    }

    public void lanuchFeedBackToAds(Date startDate, Date endDate) throws ExecutionException, InterruptedException {
        logger.info("lanuchFeedBackToAds Begin startDate={} endDate={}", startDate, endDate);

        Future<Future<?>> f = processor.processfeedBackToADSTask(startDate, endDate);
        logger.info("lanuchFeedBackToAds End startDate={} endDate={}", startDate, endDate);
        while (!f.get().isDone()) {

        }
    }

    public void lanuchFeedBackToPqa(Date startDate, Date endDate) throws ExecutionException, InterruptedException {
        logger.info("lanuchFeedBackToPqa Begin startDate={} endDate={}", startDate, endDate);
        Future<Future<?>> f = processor.processfeedBackPQATask(startDate, endDate);
        logger.info("lanuchFeedBackToPqa End startDate={} endDate={}", startDate, endDate);
        while (!f.get().isDone()) {

        }
    }

    public void lanuchFeedBackQaModelsToADS(Date startDate, Date endDate) throws ExecutionException, InterruptedException {
        Future<Future<?>> f = processor.processFeedBackQaModelsToADSTask(startDate, endDate);
        while (!f.get().isDone()) {
        }
    }

    public void encrypt(String input) {
        System.out.println("Encrypt Value = " + encryptorServiceImpl.encrypt(input));
    }

    public static void main(String[] args) {
        SpringApplication.run(AIAPsttDataUploadLauncher.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return args -> {
            ((AbstractApplicationContext) ctx).registerShutdownHook();


            if (args.length > 0) {
                try {
                    AIAPsttDataUploadLauncher launcher = ctx.getBean(AIAPsttDataUploadLauncher.class);
                    String mode = args[0];
                    if ("auto".equalsIgnoreCase(mode)) {
                        launcher.launchSchedule();
                    } else if ("upload".equalsIgnoreCase(mode)) {
                        String batchNo = args[1];
                        launcher.launchManual(batchNo);
                        System.exit(0);
                    } else if ("feedBackToAds".equalsIgnoreCase(mode)) {
                        String startDate = args[1];
                        String endDate = args[2];
                        if (DateUtil.isValid_AdYMDHM(startDate) && DateUtil.isValid_AdYMDHM(endDate)) {
                            launcher.lanuchFeedBackToAds(DateUtil.StringToDate(startDate), DateUtil.StringToDate(endDate));
                            System.exit(0);
                        } else {
                            throw new Exception("InValid Date String " + startDate + "~" + endDate);
                        }

                    } else if ("feedBackToPqa".equalsIgnoreCase(mode)) {
                        String startDate = args[1];
                        String endDate = args[2];
                        if (DateUtil.isValid_AdYMDHM(startDate) && DateUtil.isValid_AdYMDHM(endDate)) {
                            launcher.lanuchFeedBackToPqa(DateUtil.StringToDate(startDate), DateUtil.StringToDate(endDate));
                            System.exit(0);
                        } else {
                            throw new Exception("InValid Date String " + startDate + "~" + endDate);
                        }
                    } else if ("feedBackQaModelsToADS".equalsIgnoreCase(mode)) {
                        String startDate = args[1];
                        String endDate = args[2];
                        if (DateUtil.isValid_AdYMDHM(startDate) && DateUtil.isValid_AdYMDHM(endDate)) {
                            launcher.lanuchFeedBackQaModelsToADS(DateUtil.StringToDate(startDate), DateUtil.StringToDate(endDate));
                            System.exit(0);
                        } else {
                            throw new Exception("InValid Date String " + startDate + "~" + endDate);
                        }
                    } else if ("violation".equalsIgnoreCase(mode)) {
                        if (DateUtil.isValid_AdYMD(args[1])) {
                            launcher.launchViolationTask(Objects.requireNonNull(DateUtil.StringToDate(args[1])));
                            System.exit(0);
                        } else {
                            throw new Exception("InValid Date String " + args[1]);
                        }


                    } else if ("encrypt".equalsIgnoreCase(mode)) {
                        launcher.encrypt(args[1]);
                        System.exit(0);
                    } else {
                        logger.info("Unknown command");
                        System.exit(0);
                    }

                } catch (Exception e) {
                    logger.error("{}", e);

                }
            } else {
                logger.info("please input args");
            }
        };
    }

}
