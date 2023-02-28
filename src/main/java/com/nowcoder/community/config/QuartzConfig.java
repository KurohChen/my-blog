package com.nowcoder.community.config;

import com.nowcoder.community.quartz.AlphaJob;
import com.nowcoder.community.quartz.PostScoreRefreshJob;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.SimpleTrigger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;

//配置->数据库->调用
@Configuration
public class QuartzConfig {

    //BeanFactory:真个IoC容器的顶层接口
    //FactoryBean：可简化Bean的实例化过程
    //1.通过FactoryBean封装了Bean的实例化过程
    //2.将FactoryBean装配到Spring容器里
    //3.将FactoryBean注入给其他的Bean
    //4.该Bean得到的是FactoryBean所管理的对象实例
    //配置JobDetail
    //目前不运行，注释掉
    //@Bean
    public JobDetailFactoryBean alphaJobDetail(){
        JobDetailFactoryBean factoryBean = new JobDetailFactoryBean();
        factoryBean.setJobClass(AlphaJob.class);
        factoryBean.setName("alphaJob");
        factoryBean.setGroup("alphaJobGroup");
        //该任务长久保存
        factoryBean.setDurability(true);
        //该任务是不是可恢复的
        factoryBean.setRequestsRecovery(true);
        return factoryBean;
    }
    //配置Trigger：（SimpleTriggerFactoryBean或CronTriggerFactoryBean）
    //@Bean
    public SimpleTriggerFactoryBean alphaTrigger(JobDetail alphaJobDetail){
        SimpleTriggerFactoryBean factoryBean = new SimpleTriggerFactoryBean();
        //设置Trigger对哪个Job进行处理
        factoryBean.setJobDetail(alphaJobDetail);
        factoryBean.setName("alphaTrigger");
        factoryBean.setGroup("alphaTriggerGroup");
        //每三秒执行一次
        factoryBean.setRepeatInterval(3000);
        //trigger的底层需要存储job的状态
        factoryBean.setJobDataMap(new JobDataMap());
        return factoryBean;
    }
    //刷新帖子分数任务
    @Bean
    public JobDetailFactoryBean postScoreRefreshJobDetail(){
        JobDetailFactoryBean factoryBean = new JobDetailFactoryBean();
        factoryBean.setJobClass(PostScoreRefreshJob.class);
        factoryBean.setName("postScoreRefreshJob");
        factoryBean.setGroup("communityJobGroup");
        //该任务长久保存
        factoryBean.setDurability(true);
        //该任务是不是可恢复的
        factoryBean.setRequestsRecovery(true);
        return factoryBean;
    }
    //配置Trigger：（SimpleTriggerFactoryBean或CronTriggerFactoryBean）
    @Bean
    public SimpleTriggerFactoryBean postScoreRefreshTrigger(JobDetail postScoreRefreshJobDetail){
        SimpleTriggerFactoryBean factoryBean = new SimpleTriggerFactoryBean();
        //设置Trigger对哪个Job进行处理
        factoryBean.setJobDetail(postScoreRefreshJobDetail);
        factoryBean.setName("postScoreRefreshTrigger");
        factoryBean.setGroup("communityTriggerGroup");
        //每三秒执行一次
        factoryBean.setRepeatInterval(1000*60*5);
        //trigger的底层需要存储job的状态
        factoryBean.setJobDataMap(new JobDataMap());
        return factoryBean;
    }
}
