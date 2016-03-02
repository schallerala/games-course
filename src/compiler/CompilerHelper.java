package compiler;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * Created by schaller on 02/03/16.
 */
public class CompilerHelper {
    private static CompilerHelper ourInstance = new CompilerHelper();

    public static CompilerHelper getInstance() {
        return ourInstance;
    }

    public String getString (JsonNode node) {
        if (node.isArray()) {
            String returnStr = "";
            for (JsonNode jsonNode : node) {
                returnStr += jsonNode.asText() + " ";
            }

            returnStr = returnStr.trim();
            return returnStr;
        }

        return node.asText();
    }
}
