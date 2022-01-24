package com.example.dogeapi.batch.job;

import com.example.dogeapi.account.model.Account;
import com.example.dogeapi.mapper.AccountRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Slf4j // log 사용을 위한 lombok 어노테이션
@AllArgsConstructor
@Configuration
public class JobConfiguration {
    private final JobBuilderFactory jobBuilderFactory; // 생성자 DI 받음
    private final StepBuilderFactory stepBuilderFactory; // 생성자 DI 받음




    // 익명 tasklet
    @Bean
    public Job simpleJob() {
        return jobBuilderFactory.get("simpleJob")
                .start(simpleStep1())
                .build();
    }

    @Bean
    public Step simpleStep1() {
        return stepBuilderFactory.get("simpleStep1")
                .tasklet((contribution, chunkContext) -> {
                    log.info(">>>>> This is Step1");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }
    @Bean
    @JobScope
    public Step scopeStep1(@Value("#{jobParameters[requestDate]}") String requestDate){ //Jobparameters 사용하는 이유.
        return stepBuilderFactory.get("scopeStep1")
                .tasklet((contribution, chunkContext) -> {
                    log.info(">>>>> This is scopeStep1");
                    log.info(">>>>> requestDate = {}", requestDate);
                    return RepeatStatus.FINISHED;
                })
                .build();
    }





    //청크 지향
    final private AccountRepository accountRepository;
    private final int CHUNK_SIZE = 10;

    @Bean
    public Job completeUserJob(JobBuilderFactory jobBuilderFactory, Step completeJobStep) {
        return jobBuilderFactory.get("completeUserJob")
                .incrementer(new RunIdIncrementer())
                .start(completeJobStep)
                .build();
    }
    @Bean
    public Step completeJobStep(StepBuilderFactory stepBuilderFactory) {
        return stepBuilderFactory.get("completeUserStep")
                .allowStartIfComplete(true) // 완료된 스텝 재 실행하기. 스텝이 완료 됐더라도 다시 실행하게 함. 잡ExitStatus가 COMPLETE라면 적용 x
                .<Account, Account> chunk(CHUNK_SIZE)
                .reader(completeMemberReader())
                .processor(completeUserProcessor())
                .writer(completeUserWriter())
                .build();
    }

    @Bean
    @StepScope
    public QueueItemReader<Account> completeMemberReader() {  //reader
        List<Account> oldMembers = accountRepository.getAll();
        return new QueueItemReader<>(oldMembers);  // 전체를 다읽어옴
    }

    public ItemProcessor<Account, Account> completeUserProcessor() {  //processor
        return new ItemProcessor<Account, Account>() {
            @Override
            public Account process(Account account) {
                account.setMemberTier("정회원");
                return account;
            }
        };
    }

    public ItemWriter<Account> completeUserWriter() {  //writer
        return new ItemWriter<Account>() {
            @Override
            public void write(List<? extends Account> users) {
                // 위에 청크가 있어서 10개씩 커밋함.
                accountRepository.updateList(users);
            }
        };
    }
}