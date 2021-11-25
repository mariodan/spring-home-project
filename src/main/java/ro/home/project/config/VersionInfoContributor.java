package ro.home.project.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.boot.info.BuildProperties;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

/**
 * {@link InfoContributor} for adding the application build version and date and version the META-INF.
 */
@Component
public class VersionInfoContributor implements InfoContributor {

    @Autowired(required = false)
    BuildProperties buildProperties;

    public VersionInfo extractVersionInfo() {

        final var versionInfo = new VersionInfo();

        if (buildProperties != null) {
            versionInfo.setVersion(buildProperties.getVersion());
            var buildDate = LocalDateTime.ofInstant(buildProperties.getTime(), ZoneId.of("Europe/Bucharest"));
            var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            versionInfo.setBuildDate(formatter.format(buildDate));
        } else {
            versionInfo.setVersion("not defined");
            versionInfo.setBuildDate("not defined");
        }

        return versionInfo;
    }

    @Override
    public void contribute(final Info.Builder builder) {

        var versionInfo = extractVersionInfo();
        builder.withDetail("build", Map.of(
                "version", versionInfo.getVersion(),
                "date", versionInfo.getBuildDate()));

        //add core details
        final String path = this.getClass().getName().replace(".", "/") + ".class";
        var url = this.getClass().getClassLoader().getResource(path);
        if (url != null) {
            var urlStr = url.toString();
            urlStr = urlStr.substring(0, urlStr.indexOf(path));
            final Map<String, Object> data = new HashMap<>();
            String version;
            try {
                url = new URL(urlStr + JarFile.MANIFEST_NAME);
                try (final InputStream is = url.openStream()) {
                    var mf = new Manifest(is);
                    version = mf.getMainAttributes().getValue("Implementation-Version");
                    data.put("date", mf.getMainAttributes().getValue("Built-Date"));
                }
            } catch (final Exception e) {
                version = "unavailable";
            }
            data.put("version", version);
            builder.withDetail("core", data);
        }
    }

    public static class VersionInfo {

        private String version;
        private String buildDate;

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getBuildDate() {
            return buildDate;
        }

        public void setBuildDate(String buildDate) {
            this.buildDate = buildDate;
        }
    }
}
