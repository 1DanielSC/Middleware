package application;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import annotations.GetMapping;
import annotations.PostMapping;
import annotations.RequestMapping;

@RequestMapping(name = "/buy")
public class Buy {

    
    @PostMapping(route = "/add")
    public JsonObject add(JsonObject json){
        JsonElement var = json.get("valor");
        int valor = var.getAsInt();
        valor += 5;
        
        JsonObject result = new JsonObject();
        result.addProperty("valor", valor);

        return result;
    }

    @GetMapping(route = "/sub")
    public JsonObject sub(JsonObject json){
        int valor = 5;
        
        JsonObject result = new JsonObject();
        result.addProperty("valor", valor);

        return result;
    }
}
