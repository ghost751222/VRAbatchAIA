package com.macaron.vra.config;

import com.macaron.vra.vo.PsaeVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
@PropertySource(value = {"classpath:application.yml"}, encoding = "utf-8")
public class PsaeConfig {

    @Value("${psae.casecontent.qualified.key:@null}")
    String qualifiedKey;


    @Value("${psae.violation.models}")
    List<String> violationModels;

    @Value("${psae.casecontent.critical.key:@null}")
    String criticalKey;

    @Value("${psae.casecontent.critical.positive.modelGroup}")
    List<String> criticalPositiveModelGroup;

    @Value("${psae.casecontent.critical.negative.modelGroup}")
    List<String> criticalNegativeModelGroup;


    @Value("${psae.casecontent.confirm.key:@null}")
    String confirmKey;


    @Value("${psae.casecontent.confirm.positive.modelGroup}")
    List<String> confirmPositiveModelGroup;

    @Value("${psae.casecontent.confirm.negative.modelGroup}")
    List<String> confirmNegativeModelGroup;

    @Value("${psae.showmodel:false}")
    boolean showModel;


    @Value("${psae.config.protocol:http}")
    String psaeConfigProtocol;

    @Autowired
    Environment env;

    @Bean
    public PsaeVo psaeVo() {
        PsaeVo vo = new PsaeVo();
        vo.setIp(env.getProperty("psae.config.ip"));
        vo.setPort(Integer.parseInt(env.getProperty("psae.config.port")));
        vo.setUser(env.getProperty("psae.config.user"));
        vo.setPassword(env.getProperty("psae.config.password"));
        vo.setUserGroup(env.getProperty("psae.config.user.group"));
        vo.setProtocol(psaeConfigProtocol);
        return vo;
    }

    public List<String> getViolationModels() {
        return violationModels;
    }

    public List<String> getConfirmPositiveModelGroup() {
        return confirmPositiveModelGroup;
    }

    public List<String> getConfirmNegativeModelGroup() {
        return confirmNegativeModelGroup;
    }

    public List<String> getCriticalPositiveModelGroup() {
        return criticalPositiveModelGroup;
    }

    public List<String> getCriticalNegativeModelGroup() {
        return criticalNegativeModelGroup;
    }

    public String getQualifiedKey(){
        return qualifiedKey;
    }

    public String getConfirmKey(){
        return  confirmKey;
    }

    public String getCriticalKey(){
        return criticalKey;
    }

    @PostConstruct
    private void showModel() {
        if(showModel){
            showModel(criticalPositiveModelGroup,"重大瑕疵正向");
            showModel(criticalNegativeModelGroup,"重大瑕疵負向");
            showModel(confirmPositiveModelGroup,"待覆核正向");
            showModel(confirmNegativeModelGroup,"待覆核負向");
            showModel(violationModels,"違規模組");
        }
    }

    private void showModel(List<String> models,String name){
        System.out.println("-------------------" + name + "-----------------------");
        models.forEach(s -> System.out.println(s));

    }
}
