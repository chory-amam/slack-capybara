package models;

import com.google.common.base.Charsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ConfigReader {
    final private static Logger log = LoggerFactory.getLogger(ConfigReader.class);

    final private static ConfigReader reader = new ConfigReader();
    Map<String, Object> yaml = null;


    private static File getSettingsFile() throws FileNotFoundException {
        final String userFilePath = "conf/capybara.yaml";
        if (Files.exists(Paths.get(userFilePath))) {
            return new File(userFilePath);
        }

        final String defaultFilePath = "src/main/java/resources/capybara.yaml";
        if (Files.exists(Paths.get(defaultFilePath))) {
            return new File(defaultFilePath);
        }

        throw new FileNotFoundException();
    }

    private ConfigReader() {
        try {
            final File SettingsFile = getSettingsFile();
            log.info("FileName: " + SettingsFile.getName() + "FilePath: " + SettingsFile.getAbsolutePath());
            yaml = (Map<String, Object>) new Yaml().load(com.google.common.io.Files.newReader(SettingsFile, Charsets.UTF_8));
        } catch (final FileNotFoundException e) {
            log.warn("config file is not exist.", e);
            throw new RuntimeException();
        }
    }

    public static ConfigReader getInstance() {
        return reader;
    }

    public int getPort() {
        return (int) yaml.get("port");
    }

    public String getBasicIcon() {
        return (String) yaml.get("icon.basic");
    }

    public String getHappyIcon() {
        return (String) yaml.get("icon.happy");
    }

    public String getAngryIcon() {
        return (String) yaml.get("icon.angry");
    }

    public String getSadIcon() {
        return (String) yaml.get("icon.sad");
    }

    public List<String> getOtherIcon() {
        return (List) yaml.get("icon.other");
    }

    public String getOneOthericon() {
        final List<String> otherIcon = getOtherIcon();
        Collections.shuffle(otherIcon);
        return otherIcon.get(0);
    }

    public String getDatabaseURI() {
        return (String) yaml.get("database.uri");
    }

    public String getDatabaseId() {
        return (String) yaml.get("database.id");
    }

    public String getDatabasePassword() {
        return (String) yaml.get("database.password");
    }

    public String getSlackTeam() {
        return (String) yaml.get("slack.team");
    }

    public String getSlackUserName() {
        return (String) yaml.get("slack.username");
    }

    public String getSlackPassword() {
        return (String) yaml.get("slack.password");
    }

    public String getSlackRoom() {
        return (String) yaml.get("slack.room");
    }

    public String getSlackApiToken() {
        return (String) yaml.get("slack.api.token");
    }

    public String getGoogleMapApiKey() {
        return (String) yaml.get("google.map.api.key");
    }

    public String getGoogleMapLanguage() {
        return (String) yaml.get("google.map.language");
    }

    public String getGoogleMapSensor() {
        return (String) yaml.get("google.map.sensor");
    }

    public String getGoogleMapScale() {
        return (String) yaml.get("google.map.scale");
    }

    public String getGoogleMapZoom() {
        return (String) yaml.get("google.map.zoom");
    }

    public String getGoogleMapSizeLength() {
        return (String) yaml.get("google.map.size.length");
    }

    public String getGoogleMapSizeHeight() {
        return (String) yaml.get("google.map.size.height");
    }
}
