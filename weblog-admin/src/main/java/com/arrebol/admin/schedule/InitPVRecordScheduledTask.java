package com.arrebol.admin.schedule;

import com.arrebol.common.domain.dos.StatisticsArticlePVDO;
import com.arrebol.common.domain.mapper.StatisticsArticlePVMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Description
 *
 * @author Arrebol
 * @date 2024/3/1
 */
@Component
@Slf4j
public class InitPVRecordScheduledTask {

    @Autowired
    private StatisticsArticlePVMapper articlePVMapper;

    @Scheduled(cron = "0 0 23 * * ?") // 每天晚间 23 点执行
    public void excute() {
        LocalDate currDate = LocalDate.now();

        LocalDate tomorrowDate = currDate.plusDays(1);

        // 组装插入的记录
        StatisticsArticlePVDO articlePVDO = StatisticsArticlePVDO.builder()
                .pvDate(tomorrowDate) // 明日的文章 pv 访问量
                .pvCount(0L) // 默认阅读量为 0
                .createTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .build();

        articlePVMapper.insert(articlePVDO);
    }

}
