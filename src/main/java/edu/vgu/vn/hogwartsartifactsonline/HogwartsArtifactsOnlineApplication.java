package edu.vgu.vn.hogwartsartifactsonline;

import edu.vgu.vn.hogwartsartifactsonline.artifact.utils.IdWorker;
import edu.vgu.vn.hogwartsartifactsonline.wizard.utils.WizardIdWorker;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class HogwartsArtifactsOnlineApplication {

    public static void main(String[] args) {
        SpringApplication.run(HogwartsArtifactsOnlineApplication.class, args);
    }

    @Bean
    public IdWorker idWorker()
    {
        return new IdWorker(1,1);
    }
    @Bean
    public WizardIdWorker wizardIdWorker(){return new WizardIdWorker();}

}
