package compiler;


import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import core.Aynik;
import map.LocationDeath;

import java.io.File;
import java.io.IOException;

/**
 * Created by schaller on 05/02/16.
 */
public class AynikCompiler {

    private JsonNode jsonNode;
    private File gameData;

    public AynikCompiler(File gameData) throws IOException {
        this.init(gameData);
    }

    private void init (File gameData) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(JsonParser.Feature.ALLOW_COMMENTS);

        jsonNode = mapper.readTree(gameData).findPath("AYNIK");
    }

    public void prepareGame(Aynik core) {

        new LocationDeath()
    }
}
