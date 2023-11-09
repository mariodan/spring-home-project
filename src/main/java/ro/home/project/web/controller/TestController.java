package ro.home.project.web.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.ZoneId;

@RequestMapping("/v1/users")
@RestController
public class TestController {

    @GetMapping("/test")
    public TstObj test() {

        TstObj tst = new TstObj();
        tst.setNow(LocalDateTime.now());
        tst.setWithTimeZone(LocalDateTime.now(ZoneId.of("Europe/Bucharest")));
        return tst;
    }

    class TstObj {
        private LocalDateTime now;
        private LocalDateTime withTimeZone;

        public LocalDateTime getNow() {
            return now;
        }

        public void setNow(LocalDateTime now) {
            this.now = now;
        }

        public LocalDateTime getWithTimeZone() {
            return withTimeZone;
        }

        public void setWithTimeZone(LocalDateTime withTimeZone) {
            this.withTimeZone = withTimeZone;
        }
    }
}
